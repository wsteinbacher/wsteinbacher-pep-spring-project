package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Message;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if(message.getMessageText().length() > 0 && message.getMessageText().length() <= 255 && accountRepository.findById(message.getPostedBy()).isPresent()) {
            return messageRepository.save(message);
        }
        return null;
    }

    public List<Message> retrieveAllMessages() {
        return messageRepository.findAll();
    }

    public Message findById(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()) {
            return optionalMessage.get();
        }
        return null;
    }

    public Integer deleteMessage(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()) {
            messageRepository.delete(optionalMessage.get());
            return 1;
        }
        return null;
    }

    public Integer updateMessage(Integer messageId, String messageText) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent() && messageText.length() > 0 && messageText.length() <= 255) {
            Message message = optionalMessage.get();
            message.setMessageText(messageText);
            messageRepository.save(message);
            return 1;
        }
        return 0;
        
    }

    public List<Message> findAllByUser(Integer postedBy) {
        return messageRepository.findAllByPostedBy(postedBy);
    }
}