/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Lenovo
 */

public class Message {
    
    private static int messageCounter = 0; // auto-incremented number of messages sent
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private boolean sent = false; // tracks if message was sent (not just stored)
    
    public String getMessageID() {
        return messageID;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }
    
    // Random 10-digit number as string
    public static String generateMessageID() {
        Random random = new Random();
        long id = 1_000_000_000L + (long)(random.nextDouble() * 9_000_000_000L);
        return String.valueOf(id);
    }

    // Validate message ID length (≤10 characters)
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }
    
    
    public String validateMessageLength(String message) {
        if (message.length() <= 250) {
            
            return "Message ready to send.";
        
        } else {
            int excess = message.length() - 250;
            
            return "Message exceeds 250 characters by "
                + excess
                + ", please reduce the size.";
        }
    }
    
    
    public String checkRecipientCell(String cell) {
        if (cell == null || !cell.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        return "Cell phone number successfully captured.";
    }
    

    // Create message hash: first two digits of ID : messageCounter : firstWordLastWord (uppercase)
    public String createMessageHash(String msgID, int count, String message) {
        String firstTwo = msgID.length() >= 2 ? msgID.substring(0, 2) : msgID;
        // Extract first and last words
        String[] words = message.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 0 ? words[words.length - 1] : "";
        String combined = (firstWord + lastWord).replaceAll("[^a-zA-Z]", "").toUpperCase();
        return firstTwo + ":" + count + ":" + combined;
    }

    // User interaction: send, store, or disregard
    public String sentMessage(Scanner scanner, String msgID, String recipient, String msgText, int count) {
        System.out.println("\nChoose an option:");
        System.out.println("1 - Send Message");
        System.out.println("2 - Store Message (to send later, saved in JSON)");
        System.out.println("0 - Disregard Message (delete)");
        System.out.print("Your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                sent = true;
                messageCounter++;
                return "Message successfully sent";
            case 2:
                // Store in JSON file (even if not sent now)
                storeMessage(msgID, recipient, msgText, count, false);
                return "Message successfully stored";
            case 0:
                return "Press 0 to delete the message - Message disregarded.";
            default:
                return "Invalid option. Message not processed.";
        }
    }

    // Store a single message as JSON object inside a file "messages.json"
    public void storeMessage(String msgID, String recipient, String msgText, int count, boolean isSent) {
        try {
            Path path = Paths.get("messages.json");
            JSONArray messagesArray = new JSONArray();
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                if (!content.trim().isEmpty()) {
                    messagesArray = new JSONArray(content);
                }
            }
            JSONObject msgObj = new JSONObject();
            msgObj.put("messageID", msgID);
            msgObj.put("recipient", recipient);
            msgObj.put("message", msgText);
            msgObj.put("messageCounter", count);
            msgObj.put("sent", isSent);
            msgObj.put("timestamp", System.currentTimeMillis());
            messagesArray.put(msgObj);
            Files.write(path, messagesArray.toString(2).getBytes());
        } catch (Exception e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    // Print all messages sent during this session (in-memory)
    public String printMessages(List<Message> messageList) {
        if (messageList.isEmpty()) return "Coming soon!";
        StringBuilder sb = new StringBuilder("\n--- Sent Messages ---\n");
        for (Message m : messageList) {
            sb.append("Message ID: ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipient).append("\n");
            sb.append("Message: ").append(m.messageText).append("\n");
            sb.append("------------------------\n");
        }
        return sb.toString();
    }

    // Return total number of messages sent (static counter)
    public int returnTotalMessages() {
        return messageCounter;
    }

    // Method to create a new message (used by the main app)
    public Message createNewMessage(Scanner scanner, int messageNumber) {
        System.out.println("\n--- New Message #" + (messageNumber + 1) + " ---");
        
        // Recipient validation
        String recipient;
        while (true) {
            System.out.print("Recipient cell number (+27 followed by 9 digits, e.g., +27831234567): ");
            recipient = scanner.nextLine();
            String validation = checkRecipientCell(recipient);
            if (validation.equals("Valid")) break;
            System.out.println(validation);
        }
        
        // Message text (≤250 chars)
        String text;
        while (true) {
            System.out.print("Message (max 250 characters): ");
            text = scanner.nextLine();
            if (text.length() <= 250) break;
            System.out.println("Please enter a message of less than 250 characters.");
        }
        
        // Generate ID and hash
        String msgID = generateMessageID();
        while (!checkMessageID(msgID)) {
            msgID = generateMessageID(); // ensure ≤10 digits (always true here)
        }
        // Use current messageCounter value for hash (before increment)
        int currentCount = messageCounter;
        String hash = createMessageHash(msgID, currentCount, text);
        
        // Ask user to send, store, or disregard
        String actionResult = sentMessage(scanner, msgID, recipient, text, currentCount);
        
        // If sent, create a full Message object and return it
        if (actionResult.equals("Message successfully sent")) {
            Message newMessage = new Message();
            newMessage.messageID = msgID;
            newMessage.recipient = recipient;
            newMessage.messageText = text;
            newMessage.messageHash = hash;
            newMessage.sent = true;
            
            storeMessage(msgID, recipient, text, currentCount, true);
            System.out.println(actionResult);
            
            return newMessage;
        } else {
            System.out.println(actionResult);
            return null; // not sent
        }
    }

}
    

