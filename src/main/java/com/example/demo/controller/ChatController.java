package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/{userId}")
    public String sendMessage(@PathVariable String userId, @RequestBody String content) throws ExecutionException, InterruptedException {
        ChatMessage message = new ChatMessage("user", content, System.currentTimeMillis());
        // Aquí podrías llamar a la API de OpenAI/Gemini
        chatService.saveMessage(userId, message);
        return "Mensaje guardado en Firebase!";
    }

    @GetMapping("/{userId}/history")
    public List<ChatMessage> getHistory(@PathVariable String userId) throws ExecutionException, InterruptedException {
        return chatService.getChatHistory(userId);
    }
}
