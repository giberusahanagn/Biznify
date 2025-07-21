package com.biznify.partner.JmsListner;

import com.biznify.partner.service.EmailService;
import com.biznify.partner.service.PartnerService;
import com.biznify.shared_events.dto.NewProductBatchInfoToPartner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewProductBatchListener {

    private final EmailService emailService;
    private final PartnerService partnerService;

    @Autowired
    public NewProductBatchListener(EmailService emailService, PartnerService partnerService) {
        this.emailService = emailService;
        this.partnerService = partnerService;
    }

    @JmsListener(destination = "new-product-batch-email-to-partner", containerFactory = "jmsListenerContainerFactory")
    public void receiveBatch(NewProductBatchInfoToPartner event) {
        System.out.println("✅ RECEIVED NewProductBatchInfoToPartner: " +event);

        log.info("Received JMS event for batch: {} and partnerId: {}", event.getBatchCode(), event.getPartnerId());

        try {
            String email = partnerService.getEmailByPartnerId(event.getPartnerId());

            if (email == null || email.isBlank()) {
                log.warn("No email found for partnerId: {}", event.getPartnerId());
                return;
            }

            String subject = "New Product Batch: " + event.getBatchCode();
            String body = String.format("""
                Hello Partner,

                A new product batch was stored:

                Batch Code: %s
                Invoice: %s
                SKU: %s
                Quantity: %.2f
                Warehouse: %d
                Date: %s

                Regards,
                Warehouse System
                """,
                    event.getBatchCode(),
                    event.getInvoiceNumber(),
                    event.getSkuCode(),
                    event.getQuantity(),
                    event.getWarehouseId(),
                    event.getTimestamp()
            );

            emailService.sendEmail(email, subject, body);
            log.info("Email sent to partner: {}", email);

        } catch (Exception e) {
            log.error("Failed to process JMS message for batch: {}", event.getBatchCode(), e);
        }
    }
}
