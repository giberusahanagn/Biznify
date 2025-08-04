package com.aroha.callingAiAPI.dto;

import java.util.List;

import lombok.Data;

//AiApiResponce.java
@Data
public class AiApiResponce {
 private List<Choice> choices;

 @Data
 public static class Choice {
     private Message message;

     @Data
     public static class Message {
         private String role;
         private String content;
     }
 }
}
