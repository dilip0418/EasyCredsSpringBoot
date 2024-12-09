package com.dilip.ccms.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@Service
@RequiredArgsConstructor
@Async
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(
            String to,
            String name,
            EmailTemplate emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {

        logger.info("Attempting to send email to: {}", to);

        String templateName = (emailTemplate == null) ? "confirmation-email" : emailTemplate.name();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MULTIPART_MODE_MIXED_RELATED,
                UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", name);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(templateEngine.process(templateName, context), true);

        logger.info("Email content prepared. Attempting to send...");
        try {
            mailSender.send(mimeMessage);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", to, e);  // Improved error logging
        }
    }
}
