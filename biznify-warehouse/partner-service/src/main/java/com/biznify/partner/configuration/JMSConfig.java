package com.biznify.partner.configuration;

import com.biznify.shared_events.dto.NewProductBatchInfoToPartner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.jms.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
public class JMSConfig {

    @Bean
    public MessageConverter customJacksonMessageConverter() {
        return new MessageConverter() {

            // Configure ObjectMapper with JavaTimeModule
            private final ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            @Override
            public Message toMessage(Object object, Session session) throws JMSException {
                try {
                    String json = objectMapper.writeValueAsString(object);
                    return session.createTextMessage(json);
                } catch (Exception e) {
                    throw new MessageConversionException("Could not convert object to JSON", e);
                }
            }

            @Override
            public Object fromMessage(Message message) throws JMSException {
                if (message instanceof TextMessage textMessage) {
                    String json = textMessage.getText();
                    System.out.println("🟢 RECEIVED RAW JMS JSON: " + json);
                    try {
                        return objectMapper.readValue(json, NewProductBatchInfoToPartner.class);
                    } catch (Exception e) {
                        System.err.println("❌ JSON PARSE ERROR: " + e.getMessage());
                        e.printStackTrace();
                        throw new MessageConversionException("Failed to convert JSON to NewProductBatchInfoToPartner", e);
                    }
                }
                throw new MessageConversionException("Expected TextMessage but got: " + message.getClass());
            }
        };
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                          MessageConverter customJacksonMessageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(customJacksonMessageConverter);
        factory.setErrorHandler(t -> {
            System.err.println("❌ JMS Listener Error: " + t.getMessage());
            t.printStackTrace();
        });
        return factory;
    }
}
