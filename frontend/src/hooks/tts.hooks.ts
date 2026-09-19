import { useMutation, useQuery } from "@tanstack/react-query";
import { createSpeech, getCreateSpeechJobResult, getJobStatus, type CreateSpeechJobResultResponse, type JobStatusResponse, type TTSRequest } from "../services/api";

function useCreateSpeech() {
    return useMutation<JobStatusResponse,Error,TTSRequest>({
        mutationFn: (body: TTSRequest)=> createSpeech(body),
    });
}

function useGetJobStatus(jobId: string) {
    return useQuery<JobStatusResponse,Error>({
        queryKey: ['job-status', jobId],
        enabled: !!jobId,
        queryFn: ()=> getJobStatus(jobId),
        refetchInterval: (query)=> {
            return query.state.data?.is_finished ? false : 2500
        },
        refetchIntervalInBackground: true
    })
}

function useGetCreateSpeechJobResult(jobId: string) {
    return useQuery<CreateSpeechJobResultResponse | null,Error>({
        queryKey: ['job-result', jobId],
        enabled: !!jobId,
        queryFn: ()=> getCreateSpeechJobResult(jobId),
        refetchIntervalInBackground: true
    })
}

export { 
    useCreateSpeech, useGetJobStatus, useGetCreateSpeechJobResult
}