const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

export interface TTSRequest {
  text: string;
  language: string;
  gender: string;
  voice_style: string;
}

export interface JobStatusResponse {
  job_id: string;
  status: string;
  is_finished: boolean;
}

export interface CreateSpeechJobResultResponse {
  job_id: string;
  uri: string
}

async function createSpeech(request: TTSRequest): Promise<JobStatusResponse> {
  const response = await fetch(`${API_BASE_URL}/tts`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    const errorText = await response.text();

    throw new Error(
      errorText || `TTS request failed with status ${response.status}`,
    );
  }

  return response.json();
}

async function getJobStatus(jobId: string): Promise<JobStatusResponse> {
  const response = await fetch(
    `${API_BASE_URL}/jobs/${encodeURIComponent(jobId)}/status`,
    {
      method: "GET",
    },
  );

  if (!response.ok) {
    const errorText = await response.text();

    throw new Error(
      errorText || `job status request failed with status ${response.status}`,
    );
  }

  return response.json();
}

async function getCreateSpeechJobResult(jobId: string): Promise<CreateSpeechJobResultResponse | null> {
  const response = await fetch(
    `${API_BASE_URL}/tts/job/${encodeURIComponent(jobId)}/result`,
    {
      method: "GET",
    },
  );

  if (!response.ok) {
    const errorText = await response.text();

    throw new Error(
      errorText || `job result request failed with status ${response.status}`,
    );
  }

  return response.json();
}

export {
    createSpeech, getJobStatus, getCreateSpeechJobResult
}