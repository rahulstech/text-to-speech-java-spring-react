import React, { useCallback, useEffect, useState } from "react";
import { useCreateSpeech, useGetCreateSpeechJobResult, useGetJobStatus } from '../../hooks/tts.hooks';
import type { TTSRequest } from "../../services/api";


const MAX_ALLOWED_TEXT_CHARACTERS: number = 500;

const languages = [
  "ENGLISH",
  "BENGALI",
  "GUJARATI",
  "HINDI",
  "KANNADA",
  "KONKANI",
  "MALAYALAM",
  "MARATHI",
  "MAITHILI",
  "ODIA",
  "PUNJABI",
  "TAMIL",
  "TELUGU",
  "URDU",
  "SINDHI",
];

const styles = ["CASUAL", "DEEP", "JOLLY"];

function toTitleCase(value) {
  return value.charAt(0) + value.slice(1).toLowerCase();
}

interface TTSFormState {
  text: string
  language: string
  gender: string
  style: string
}

const DEFAULT_TTS_FORM_STATE: TTSFormState = {
    text: "",
    language: "ENGLISH",
    gender: "FEMALE",
    style: "CASUAL"
}

interface PageState {
  lastJobId: string | null,
  lastSuccessfulJobId: string | null,
}

const DEFAULT_PAGE_STATE: PageState = {
  lastJobId: null,
  lastSuccessfulJobId: null,
}

export default function Home() {

  const [ttsFormState, setTTSFormState] = useState<TTSFormState>(DEFAULT_TTS_FORM_STATE);
  const [pageState, setPageState] = useState<PageState>(DEFAULT_PAGE_STATE);

  const { mutate, isPending: isCreateSpeechPending } = useCreateSpeech();
  const { data: jobStatusData } = useGetJobStatus(pageState.lastJobId);
  const { data: jobResultData, isError: isJobResultError } = useGetCreateSpeechJobResult(pageState.lastSuccessfulJobId);

  const characterCount = ttsFormState.text.length;
  const counterColor = characterCount < MAX_ALLOWED_TEXT_CHARACTERS ? "text-counter-safe" : "text-danger";

  const handleTTSFormSubmit = useCallback((e: React.SubmitEvent) => {
    e.preventDefault();

    const { text, language, gender, style } = ttsFormState;

    const request: TTSRequest = {
      text, language, gender, voice_style: style
    };

    mutate(request,{ 
      onSuccess(res) {
        setPageState((prev)=> ({
          ...prev,
          lastJobId: res.job_id})
        );
      }
    })
  },[ttsFormState, mutate, pageState]);


  const handleFormDataChange = useCallback((e: React.ChangeEvent<{name: string, value: string}>)=> {
    const { name, value } = e.target;

    setTTSFormState((prev)=> ({
      ...prev,
      [name]: value
    }))
  }, [setTTSFormState]);

  useEffect(()=>{
    if (jobStatusData?.is_finished) {
      setPageState(prev=> ({
        ...prev,
        lastJobId: null,
        lastSuccessfulJobId: jobStatusData.job_id
      }));
    }
  },[jobStatusData, setPageState]);

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Fixed App Bar */}
      <header className="fixed inset-x-0 top-0 z-50 h-16 border-b border-gray-200 bg-white">
        <div className="flex h-full items-center px-6">
          <h1 className="text-xl font-semibold text-gray-900">
            Text-to-Seepch
          </h1>
        </div>
      </header>

      {/* Main Content */}
      <main className="mx-auto max-w-[900px] px-6 pb-12 pt-24">
        <div className="grid grid-cols-[45%_55%] gap-8">
          {/* Left Column */}
          <div className="space-y-5">
            <form onSubmit={handleTTSFormSubmit}>
                {/* Text Area */}
                <div className="mb-3">
                  <div className="relative ">
                      <textarea
                        name="text"
                        value={ttsFormState.text}
                        onChange={handleFormDataChange}
                        maxLength={500}
                        placeholder="Write your text..."
                        className="h-40 w-full resize-none rounded-lg border border-gray-300 bg-white p-4 pb-9 text-sm text-gray-900 outline-none transition placeholder:text-gray-400 focus:border-primary focus:ring-1 focus:ring-primary"
                      />

                      <span
                        className={`absolute bottom-3 right-3 text-xs font-medium ${counterColor}`}
                      >
                        {characterCount}/{MAX_ALLOWED_TEXT_CHARACTERS}
                      </span>
                  </div>
                </div>

                {/* Language */}
                <div className="mb-2">
                  <label
                      htmlFor="language"
                      className="mb-2 block text-sm font-medium text-gray-700"
                  >
                      Language
                  </label>

                  <select
                      name="language"
                      value={ttsFormState.language}
                      onChange={handleFormDataChange}
                      className="w-full rounded-lg border border-gray-300 bg-white px-3 py-2.5 text-sm text-gray-900 outline-none focus:border-primary focus:ring-1 focus:ring-primary"
                  >
                      {languages.map((item) => (
                      <option key={item} value={item}>
                          {toTitleCase(item)}
                      </option>
                      ))}
                  </select>
                </div>

                {/* Gender */}
                <fieldset className="mb-3">
                  <legend className="mb-2 text-sm font-medium text-gray-700">
                      Gender
                  </legend>

                  <div className="flex gap-6">
                      <label className="flex cursor-pointer items-center gap-2 text-sm text-gray-700">
                      <input
                          type="radio"
                          name="gender"
                          value="MALE"
                          checked={ttsFormState.gender === "MALE"}
                          onChange={handleFormDataChange}
                          className="h-4 w-4 accent-primary"
                      />
                      Male
                      </label>

                      <label className="flex cursor-pointer items-center gap-2 text-sm text-gray-700">
                      <input
                          type="radio"
                          name="gender"
                          value="FEMALE"
                          checked={ttsFormState.gender === "FEMALE"}
                          onChange={handleFormDataChange}
                          className="h-4 w-4 accent-primary"
                      />
                      Female
                      </label>
                  </div>
                </fieldset>

                {/* Style */}
                <div className="mb-3">
                  <label
                      htmlFor="style"
                      className="mb-2 block text-sm font-medium text-gray-700"
                  >
                      Style
                  </label>

                  <select
                      name="style"
                      value={ttsFormState.style}
                      onChange={handleFormDataChange}
                      className="w-full rounded-lg border border-gray-300 bg-white px-3 py-2.5 text-sm text-gray-900 outline-none focus:border-primary focus:ring-1 focus:ring-primary"
                  >
                      {styles.map((item) => (
                      <option key={item} value={item}>
                          {toTitleCase(item)}
                      </option>
                      ))}
                  </select>
                </div>

                {/* Convert Button */}
                <button type="submit" disabled={isCreateSpeechPending}
                  className="w-full rounded-lg bg-primary mt-5 px-4 py-2.5 text-sm font-medium text-white transition hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
                >
                Convert into Speech
                </button>
            </form>
            
          </div>

          {/* Right Column */}
          <div className="flex min-h-40 items-center justify-center">
            { pageState.lastJobId && !jobStatusData?.is_finished ? (
              <div className="flex flex-col items-center gap-3">
                <div className="h-10 w-10 animate-spin rounded-full border-4 border-gray-200 border-t-primary" />
                <span className="text-sm text-gray-500">
                  Converting your text into speech...
                </span>
              </div>
            ) : isJobResultError ? (
              <div className="w-full rounded-lg border border-danger bg-danger/10 p-5 text-center">
                <p className="text-sm font-medium text-danger">
                  Failed to retrieve the generated audio.
                </p>
              </div>
            ) : jobResultData ? (
              <div className="w-full space-y-4 rounded-lg border border-gray-200 bg-white p-5">
                <audio
                  controls
                  src={jobResultData.uri}
                  className="w-full"
                />

                <a
                  href={jobResultData.uri}
                  download
                  className="block w-full rounded-lg bg-primary px-4 py-2.5 text-center text-sm font-medium text-white transition hover:opacity-90"
                >
                  Download Audio
                </a>
              </div>
            ) : null}
          </div>
        </div>
      </main>
    </div>
  );
}