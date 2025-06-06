package com.bullzen.user.service;

import com.bullzen.user.dto.UserCreatedDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Value("${spring.kafka.topic}")
    private String TOPIC;

    @Autowired
    private KafkaTemplate<String, UserCreatedDto> kafkaTemplate;

    public void sendMessage(UserCreatedDto dto) {
        kafkaTemplate.send(TOPIC, dto);
        System.out.println("Message sent: " + dto.toString());
    }
}
