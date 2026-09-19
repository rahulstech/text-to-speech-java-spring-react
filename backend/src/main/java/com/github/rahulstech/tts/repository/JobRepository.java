package com.github.rahulstech.tts.repository;

import com.github.rahulstech.tts.entity.JobEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<@NonNull JobEntity, @NonNull UUID> {}
