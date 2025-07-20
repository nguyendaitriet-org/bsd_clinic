package com.bsdclinic;

import software.amazon.awssdk.services.ses.model.SendEmailResponse;
import software.amazon.awssdk.services.ses.model.*;

import java.util.Map;


public interface EmailService {
    SendEmailResponse sendSimpleEmail(String fromMail, String toEmail, String subject, String body);
    SendEmailResponse sendHtmlEmail(String fromMail, String toEmail, String subject, String htmlBody);
    SendEmailResponse sendTemplatedEmail(String fromMail, String toEmail, String subject,
                                                String templateName, Map<String, Object> templateVariables);
    GetSendStatisticsResponse getSendingStatistics();
    VerifyEmailIdentityResponse verifyEmailAddress(String email);
}