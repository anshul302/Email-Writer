package com.email.email_writer_sb.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.SQLOutput;
import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;


    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder webClient) {
        this.webClient = WebClient.builder().build();
    }

    public String generateEmailReply(EmailRequest emailRequest){
        //Build the prompt
        String prompt=BuildPrompt(emailRequest);

        //craft a request
        Map<String , Object> requestBody=Map.of(
                "contents",new Object[] {
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );

        try {
            // Construct the full URL by appending the API key as a query parameter
            String fullUrl = geminiApiUrl + "?key=AIzaSyATfHpII88hk-5GcZNsgN1sXEihBFK362c";

            // Do request and get response
            String response = webClient.post()
                    .uri(fullUrl) // Use the constructed URL
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody) // Pass the request body
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();



            // Extract Response and return
            return extractResponseContent(response);
        } catch (Exception e) {
            return "Error generating email reply: " + e.getMessage();
        }



    }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper mapper=new ObjectMapper();
            JsonNode rootNode=mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch(Exception e){
            return "Error processing request " + e.getMessage();
        }
    }

    private String BuildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt=new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content.Please do not generate subject line ");
        if(emailRequest.getTone()!= null && !emailRequest.getTone().isEmpty()){
            prompt.append("Use a ").append(emailRequest.getTone()).append("tone.");
        }
        prompt.append("\n Original email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();

    }
}
