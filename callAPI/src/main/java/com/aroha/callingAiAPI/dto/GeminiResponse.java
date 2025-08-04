package com.aroha.callingAiAPI.dto;

import java.util.List;

public class GeminiResponse {
    private List<Candidate> candidates;

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public static class Candidate {
        private Content content;

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }
    }

    public static class Content {
        private List<GeminiRequest.Part> parts;

        public List<GeminiRequest.Part> getParts() {
            return parts;
        }

        public void setParts(List<GeminiRequest.Part> parts) {
            this.parts = parts;
        }
    }
}
