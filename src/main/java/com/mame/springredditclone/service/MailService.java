package com.mame.springredditclone.service;

import com.mame.springredditclone.exceptions.SpringRedditException;
import com.mame.springredditclone.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor @Slf4j
public class MailService {//to send out email

    private final MailContentBuilder mailContentBuilder;//to build body of email
    private final JavaMailSender mailSender;
@Async//to speed up the works by doing many jobs at a time
    public void sendMail(NotificationEmail notificationEmail){ //contains details of email(recipient,subject & body)
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("anyUrl@email.com");//but in real world real email address
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());//mailContentBuilder.build will return message in html format
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } catch (MailException e) {
            throw new SpringRedditException("Exception occured when sending mail to " + notificationEmail + " " + e.toString() );
//            throw new SpringRedditException("Exception occured when sending mail to " + notificationEmail.getRecipient() );
        }
    }
}
