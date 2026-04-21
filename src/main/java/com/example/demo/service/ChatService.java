package com.example.demo.service;

import com.example.demo.model.ChatMessage;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ChatService {

    public String saveMessage(String userId, ChatMessage message) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<com.google.cloud.firestore.WriteResult> future = db.collection("chats")
                .document(userId)
                .collection("messages")
                .document(String.valueOf(message.getTimestamp()))
                .set(message);
        
        return future.get().getUpdateTime().toString();
    }

    public List<ChatMessage> getChatHistory(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        List<ChatMessage> history = new ArrayList<>();
        
        db.collection("chats")
                .document(userId)
                .collection("messages")
                .orderBy("timestamp")
                .get()
                .get()
                .getDocuments()
                .forEach(doc -> history.add(doc.toObject(ChatMessage.class)));
        
        return history;
    }
}
