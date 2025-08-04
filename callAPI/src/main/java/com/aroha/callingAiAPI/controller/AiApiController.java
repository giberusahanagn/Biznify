package com.aroha.callingAiAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aroha.callingAiAPI.service.AiService;
import com.aroha.callingAiAPI.service.PdfExtractor;

@RestController
@RequestMapping("/api")
public class AiApiController {

    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private PdfExtractor pdfExtractor;

    @GetMapping("/ask")
    public ResponseEntity<String> askAi(@RequestParam String prompt,
                                        @RequestParam String provider) {
        try {
            AiService aiService = (AiService) context.getBean(provider);
            return ResponseEntity.ok(aiService.askQuestion(prompt));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Provider error: " + e.getMessage());
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(
            @RequestParam("file") MultipartFile file,
            @RequestParam("prompt") String prompt
    ) {	
        try {
            String extractedText = pdfExtractor.extractText(file);

            // Combine prompt + PDF content
            String finalPrompt = prompt + "\n\n-----\n\n" + extractedText;

            // Send to Gemini
            String result = aiService.askQuestion(finalPrompt);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


}
