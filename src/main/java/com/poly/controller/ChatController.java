package com.poly.controller;

import com.poly.model.Message;
import com.poly.service.ChatJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller  // ❌ Loại bỏ @RestController để có thể trả về trang HTML
@RequestMapping("/api")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatJsonService chatJsonService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatJsonService chatJsonService) {
        this.messagingTemplate = messagingTemplate;
        this.chatJsonService = chatJsonService;
    }

    // ✅ Xử lý tin nhắn gửi đi
    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, Message message) {
        if (message.getSenderId() == null) {
            System.out.println("Error: Sender ID is missing.");
            return;
        }

        message.setReceiverId(Integer.parseInt(to));
        chatJsonService.saveMessage(message);

        // ✅ Gửi tin nhắn đến user cụ thể
        messagingTemplate.convertAndSendToUser(to, "/queue/messages", message);
    }

    // ✅ API lấy tin nhắn của user
    @GetMapping("/getMessages/{userId}")
    @ResponseBody
    public List<Message> getMessages(@PathVariable Integer userId) {
        return chatJsonService.getMessagesForUser(userId);
    }

    // ✅ Hiển thị trang chat
    @GetMapping("/chat")
    public String chatPage() {
        return "chat";  // Trả về file chat.html trong thư mục templates
    }
}
