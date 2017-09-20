/**
 * Course: UMUC, SDEV 425: Mitigating Software Vulnerabilities
 * Professor: Gary Harris
 * Semester: Fall 2017, OL1
 * 
 * Author: Greg Holden
 * Assignment #2: Apply Low-Impact Security Controls
 * Date: September 16, 2017
 * 
 */
package sdev425hw2;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

////////////////////////////////////////////////////////////////////////////////
// IA-2(1): IDENTIFICATION AND AUTHENTICATION (MULTI-FACTOR AUTHENTICATION) ////
////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @author Greg Holden
 */
public class JavaMailer {
    // Mail Authenticator Method
    public static void mailAuthenticator(String from, String to,
            String password, String subject, String token) {
        // Set properties of JavaMail session
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        // Get session instance
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(from, password);
                        }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText("Authentication Code (case sensitive): " + token);
            Transport.send(message);
        } catch (MessagingException e) {
                throw new RuntimeException(e);
        }
    }
}
