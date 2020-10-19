package com.fastis.datahandlers;

import com.fastis.data.Board;
import com.fastis.data.User;
import com.fastis.security.AccessVerifier;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class InviteByEmail {

    private final String host = "teamezraacademy@gmail.com";
    //private final String password = "plyelmdyxdjjgztn";
    private final String password = "plyelmdyxdjjgztn";

    private Properties prop;
    private Session session;

    public InviteByEmail(AccessVerifier accessVerifier) {
        setProperties();
        setSession();
    }

    private void setSession() {
        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(host, password);
                    }
                });
    }


    private void setProperties() {
        prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
    }


    public void sendInvite(Board board, String sendTo, User user) {

        String boardLink = "<a href='http://localhost:8080/boardHome/" + board.getId() + "'>" + " " + board.getName() + " <a>";
        String inviteFrom = user.getFirstname(); // or email


        try {

            Message message = new MimeMessage(session);
            message.setHeader("Content-type", "text/html; charset=UTF-8");
            message.setFrom(new InternetAddress(host));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(sendTo)
            );

            message.setSubject("Invitation to Ezra Organizer");
            message.setContent(
                    "Hello,"
                            + "<br> " + inviteFrom + " has invited you to follow " + board.getName() + "." +
                            "<br> Click the following link to see their board:" + boardLink, "text/html"
                            );

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
