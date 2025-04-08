package com.example.controller;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.service.*;
import com.example.entity.*;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Account newAccount) {
        if(accountService.getByName(newAccount) != null) {
            return ResponseEntity.status(409).body("Username already taken.");
        }
        Account addedAccount = accountService.addAccount(newAccount);
        if(addedAccount != null) {
            return ResponseEntity.status(200).body(addedAccount);
        }
        else {
            return ResponseEntity.status(400).body("Username must not be blank and password must be at least 4 characters long.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account) {
        Account logAccount = accountService.login(account);
        if(logAccount != null) {
            return ResponseEntity.status(200).body(logAccount);
        }
        else {
            return ResponseEntity.status(401).body("Unauthorized.");
        }
    }

    @PostMapping("/messages")
    public ResponseEntity createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage != null) {
            return ResponseEntity.status(200).body(createdMessage);
        }
        else {
            return ResponseEntity.status(400).body("Messages must have text, be no longer than 255 characters, and be posted by an existing account.");
        }
    }

    @GetMapping("/messages")
    public ResponseEntity getAllMessages() {
        return ResponseEntity.status(200).body(messageService.retrieveAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable Integer messageId) {
        return ResponseEntity.status(200).body(messageService.findById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessage(@PathVariable Integer messageId) {
        return ResponseEntity.status(200).body(messageService.deleteMessage(messageId));
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessage(@PathVariable Integer messageId, @RequestBody String messageText) {
        JSONObject json = new JSONObject(messageText);
        String text = json.getString("messageText");
        Integer update = messageService.updateMessage(messageId, text);
        if(update.equals(1)) {
            return ResponseEntity.status(200).body(1);
        }
        else {
            return ResponseEntity.status(400).body("The message to be updated must exist and the updated text must be between 1 and 255 characters, inclusive.");
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getAllMessagesByAccount(@PathVariable Integer accountId) {
        return ResponseEntity.status(200).body(messageService.findAllByUser(accountId));
    }
}