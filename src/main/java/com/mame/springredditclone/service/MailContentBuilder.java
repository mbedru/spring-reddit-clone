package com.mame.springredditclone.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {  ///mailContentBuilder.build will return message in html format
    private final TemplateEngine templateEngine;
    //takes the email message we want to send to the user as input
    String build(String message){
        Context context = new Context();
        context.setVariable("message",message);
        return templateEngine.process("mailTemplate", context);
        //so finally tymeleaf will automatically add the email message to our html-template
    }
}
