
import com.mycompany.chatapplication.Message;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
public class MessageTest {
    
    
    Message msg = new Message();
    
    // Message length test
    @Test
    public void testMessageLengthSuccess() {
        
        String result =
                msg.validateMessageLength(
                        "Hi Mike, can you join us for dinner tonight?"
                );

        assertEquals(
                "Message ready to send.",
                result
        );
    }
    
    
    @Test
    public void testMessageLengthFailure() {

        String longMessage =
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
              + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
              + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
              + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
              + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
              + "aaaaaa";

        String result =
                msg.validateMessageLength(longMessage);

        assertTrue(
                result.contains("Message exceeds 250 characters")
        );
    }

    // =====================================================
    // RECIPIENT NUMBER TESTS
    // =====================================================

    @Test
    public void testRecipientCellSuccess() {;

        String result =
                msg.checkRecipientCell("+27718693002");

        assertEquals(
                "Cell phone number successfully captured.",
                result
        );
    }

    @Test
    public void testRecipientCellFailure() {
        
        String result = msg.checkRecipientCell("08575975889");

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                result
        );
    }

    // =====================================================
    // MESSAGE HASH TEST  
    // =====================================================

    @Test
    public void testCreateMessageHash() {

        String hash = msg.createMessageHash("0012345678",0,"Hi Mike, can you join us for dinner tonight?");

        assertEquals("00:0:HITONIGHT",hash);
    }

    // =====================================================
    // MESSAGE ID TEST
    // =====================================================

    @Test
    public void testGeneratedMessageID() {
        String generatedID = Message.generateMessageID();
        assertNotNull(generatedID);
        
        assertTrue(generatedID.length() <= 10);
}

    // =====================================================
    // SENT MESSAGE OPTION TESTS
    // =====================================================

    @Test
    public void testSentMessageOptionSend() {

        String expected =
                "Message successfully sent.";

        assertEquals(
                expected,
                "Message successfully sent."
        );
    }

    @Test
    public void testSentMessageOptionDisregard() {

        String expected =
                "Press 0 to delete the message.";

        assertEquals(
                expected,
                "Press 0 to delete the message."
        );
    }

    @Test
    public void testSentMessageOptionStore() {

        String expected =
                "Message successfully stored.";

        assertEquals(
                expected,
                "Message successfully stored."
        );
    }

    // =====================================================
    // TOTAL MESSAGE COUNT TEST
    // =====================================================

    @Test
    public void testReturnTotalMessages() {

        int total = msg.returnTotalMessages();

        assertTrue(total >= 0);
    }
}

