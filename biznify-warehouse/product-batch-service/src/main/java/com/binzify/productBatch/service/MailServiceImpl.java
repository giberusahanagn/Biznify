package com.binzify.productBatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.binzify.productBatch.dto.PartnerDTO;
import com.binzify.productBatch.entity.ProductBatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendPartnerBatchStoredEmail(ProductBatch batch, PartnerDTO partner) {
        if (partner.getEmail() == null || partner.getEmail().isEmpty()) {
            log.warn("Partner email is null or empty. Skipping email sending.");
            return;
        }

        String subject = "Warehouse: Product Batch Stored";
        String body = "Dear " + partner.getName() + ",\n\n"
                + "Your product batch has been successfully stored in the warehouse.\n\n"
                + "Invoice Number : " + batch.getInvoiceNumber() + "\n"
                + "BatchCode     : " + batch.getBatchCode() + "\n"
                + "SKU Code       : " + batch.getSkuCode() + "\n"
                + "Quantity       : " + batch.getQuantity() + "\n"
                + "Warehouse ID   : " + batch.getWarehouseId() + "\n\n"
                + "Regards,\nWarehouse System";

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(partner.getEmail());
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email sent to partner: {}", partner.getEmail());
        } catch (Exception e) {
            log.error("Failed to send email to partner {}: {}", partner.getEmail(), e.getMessage());
        }
    }
}
