package com.poly.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.model.Message;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatJsonService {
    private static final String FILE_PATH = "src/main/resources/data/chat_messages.json"; // Sửa đường dẫn file
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Lưu tin nhắn vào file JSON
     */
    public synchronized void saveMessage(Message message) {
        List<Message> messages = getAllMessages();
        messages.add(message);

        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // Đảm bảo thư mục tồn tại
            objectMapper.writeValue(file, messages);
            System.out.println("✅ Tin nhắn đã được lưu: " + message.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy tin nhắn của người dùng (dựa vào senderId hoặc receiverId)
     */
    public List<Message> getMessagesForUser(Integer userId) {
        return getAllMessages().stream()
                .filter(msg -> msg.getSenderId().equals(userId) || msg.getReceiverId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Lấy tất cả tin nhắn từ JSON
     */
    private List<Message> getAllMessages() {
        try {
            File file = Paths.get(FILE_PATH).toFile();
            if (!file.exists()) {
                System.out.println("⚠️ File JSON chưa tồn tại, tạo danh sách rỗng.");
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Message>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
