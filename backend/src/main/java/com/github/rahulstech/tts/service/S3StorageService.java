package com.github.rahulstech.tts.service;

import com.github.rahulstech.tts.error.HttpException;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;

@Service
public class S3StorageService {

    private static final String STORAGE_PUBLIC_KEY_PREFIX = "text-to-speech";

    private final Logger log = LoggerFactory.getLogger(S3StorageService.class);

    private final Region awsS3Region;

    private final String awsS3Bucket;

    private final String awsCDNBaseUrl;

    private final S3Client s3Client;

    public S3StorageService(
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.bucket}") String bucket,
            @Value("${aws.cdn.base_url}") String baseUrl
    ) {
        this.awsS3Region = Region.of(region);
        this.awsS3Bucket = bucket;
        this.awsCDNBaseUrl = baseUrl;

        this.s3Client = S3Client.builder()
                .region(awsS3Region)
                .build();
    }

    @PreDestroy
    public void onPreDestroy() {
        S3Client client = this.s3Client;
        if (null != client) {
            try {
                client.close();
            }
            catch (Exception ignore) {}
        }
    }


    public String uploadFile(InputStream source, String filename, MimeType mimeType, long size) {
        String contentType = mimeType.toString();
        String key = createPublicKey(filename);
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(awsS3Bucket)
                .contentType(contentType)
                .key(key)
                .build();

        try {
            s3Client.putObject(req, RequestBody.fromInputStream(source, size));
            log.info("uploadFile successful; filename='{}' content-type='{}' size={}", filename, contentType, size);

            return createCDNURL(key);
        }
        catch (Exception e) {
            log.error(
                    "uploadFile failed; filename='{}' content-type='{}' size={}",
                    filename,
                    contentType,
                    size,
                    e
            );

            throw HttpException.internalServerError("");
        }
    }

    private String createPublicKey(String suffix) {
        return STORAGE_PUBLIC_KEY_PREFIX+"/"+suffix;
    }

    private String createCDNURL(String key) {
        return awsCDNBaseUrl+"/"+key;
    }
}
