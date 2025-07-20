package com.bsdclinic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final SesClient sesClient;
    private final TemplateEngine templateEngine;

    private static final String CHARSET = "UTF-8";

    public SendEmailResponse sendSimpleEmail(String fromMail, String toEmail, String subject, String body) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromMail)
                    .destination(dest -> dest.toAddresses(toEmail))
                    .message(msg -> msg
                            .subject(subj -> subj.data(subject).charset(CHARSET))
                            .body(bodyBuilder -> bodyBuilder
                                    .text(text -> text.data(body).charset(CHARSET))))
                    .build();

            return sesClient.sendEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public SendEmailResponse sendHtmlEmail(String fromMail, String toEmail, String subject, String htmlBody) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromMail)
                    .destination(dest -> dest.toAddresses(toEmail))
                    .message(msg -> msg
                            .subject(subj -> subj.data(subject).charset(CHARSET))
                            .body(bodyBuilder -> bodyBuilder
                                    .html(html -> html.data(htmlBody).charset(CHARSET))))
                    .build();

            return sesClient.sendEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    public SendEmailResponse sendTemplatedEmail(
            String fromMail,
            String toEmail,
            String subject,
            String templateName,
            Map<String, Object> templateVariables
    ) {
        if (templateEngine == null) {
            throw new RuntimeException("Template engine not available");
        }

        try {
            Context context = new Context();
            context.setVariables(templateVariables);

            String htmlBody = templateEngine.process(templateName, context);

            return sendHtmlEmail(fromMail, toEmail, subject, htmlBody);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send templated email", e);
        }
    }

    public GetSendStatisticsResponse getSendingStatistics() {
        try {
            GetSendStatisticsRequest request = GetSendStatisticsRequest.builder().build();
            return sesClient.getSendStatistics(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get sending statistics", e);
        }
    }

    public VerifyEmailIdentityResponse verifyEmailAddress(String email) {
        try {
            VerifyEmailIdentityRequest request = VerifyEmailIdentityRequest.builder()
                    .emailAddress(email)
                    .build();
            return sesClient.verifyEmailIdentity(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify email address", e);
        }
    }
}
