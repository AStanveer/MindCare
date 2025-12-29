package com.teamspring.MindCare.dto.gemini;

import java.util.Collections;
import java.util.List;

public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String text) {
        this.contents = Collections.singletonList(new Content("user", text));
    }

    public List<Content> getContents() {
        return contents;
    }

    // Inner Helper Classes
    public static class Content {
        private String role;
        private List<Part> parts;

        public Content(String role, String text) {
            this.role = role;
            this.parts = Collections.singletonList(new Part(text));
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public List<Part> getParts() { return parts; }
        public void setParts(List<Part> parts) { this.parts = parts; }
    }

    public static class Part {
        private String text;
        public Part(String text) { this.text = text; }
        public String getText() { return text; }
    }
}
