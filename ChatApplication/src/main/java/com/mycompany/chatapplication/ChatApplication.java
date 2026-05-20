/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatApplication {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        Message msgHandler = new Message();
        
        // Registration and Login 
        System.out.println("---QuickChat Registration---");
        System.out.print("First name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Username (underscore, max 5 chars): ");
        String username = scanner.nextLine();
        System.out.print("Password (min 8 chars, 1 capital, 1 number, 1 special): ");
        String password = scanner.nextLine();
        System.out.print("Cell phone (+27...): ");
        String cell = scanner.nextLine();
        
        String regMsg = login.registerUser(username, password, cell, firstName, lastName);
        System.out.println(regMsg);
        if (!regMsg.equals("User successfully registered.")) {
            System.out.println("Registration failed. Exiting.");
            return;
        }
        
        System.out.println("\n---Please Login---");
        System.out.print("Username: ");
        String loginUser = scanner.nextLine();
        System.out.print("Password: ");
        String loginPass = scanner.nextLine();
        String loginStatus = login.returnLoginStatus(loginUser, loginPass);
        System.out.println(loginStatus);
        if (!loginStatus.startsWith("Welcome")) {
            System.out.println("Login failed. Exiting.");
            return;
        }
        
        // --- Messaging ---
        System.out.println("\nWelcome to QuickChat");
        System.out.print("How many messages do you wish to send today? ");
        int totalMessagesToSend = scanner.nextInt();
        scanner.nextLine();
        
        List<Message> sentMessages = new ArrayList<>();
        
        int messagesCreated = 0;
        boolean running = true;
        
        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Send a message");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Show total messages sent");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            
            int option = scanner.nextInt();
            scanner.nextLine();
            
            switch (option) {
                case 1:

// Check if user already reached message limit
                    if (messagesCreated >= totalMessagesToSend) {
                        System.out.println("You have already sent all "
                        + totalMessagesToSend + " messages.");
                        break;
                    }
                    
                    Message newMsg = msgHandler.createNewMessage(scanner, messagesCreated);
                    
                    if (newMsg != null) {
                        sentMessages.add(newMsg);
                        
                        System.out.println("\n--- Message Details ---");
                        System.out.println("Message ID: " + newMsg.getMessageID());
                        System.out.println("Message Hash: " + newMsg.getMessageHash()); 
                        System.out.println("Recipient: " + newMsg.getRecipient());
                        System.out.println("Message: " + newMsg.getMessageText()); 
                        
                        messagesCreated++;
                    }
                    break;
                
                case 2:
                    // Show sent messages
                    System.out.println(msgHandler.printMessages(sentMessages));
                    
                    break; 
                case 3:
                    // Show total messages sent
                    System.out.println("Total messages sent: "
                    + msgHandler.returnTotalMessages()); 
                    
                    break;
                
                case 4:
                    // Exit program
                    System.out.println("Exiting QuickChat...");
                    running = false;
                    
                    break;
                
                default:
                    System.out.println("Invalid option. Please try again.");
            
            }
        }
        
        scanner.close();
    
    }
        
}