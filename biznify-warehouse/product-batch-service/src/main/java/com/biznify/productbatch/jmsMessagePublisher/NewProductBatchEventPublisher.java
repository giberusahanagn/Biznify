package com.biznify.productbatch.jmsMessagePublisher;

import com.biznify.shared_events.dto.NewProductBatchInfoToPartner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component

public class NewProductBatchEventPublisher {



    private final JmsTemplate jmsTemplate;

    public NewProductBatchEventPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendBatchEvent(NewProductBatchInfoToPartner event) {
        jmsTemplate.convertAndSend("new-product-batch-email-to-partner", event);
    }

}