package com.freely.backend.mail;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailService {
    @Value("${mail.sender}")
    private String mailSender;

    @Value("${sendgrid.apikey}")
    private String sendGridApiKey;

    public void sendMail(String recipientMail, String subject, String body) {
        Email from = new Email(mailSender);
        Email to = new Email(recipientMail);
        Content content = new Content("text/html", body);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            response.getStatusCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
