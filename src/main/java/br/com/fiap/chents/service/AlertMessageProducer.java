package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.dto.AlertMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlertMessageProducer {

    private static final Logger LOGGER =  LoggerFactory.getLogger(AlertMessageProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public AlertMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOcorrenciaMessage(AlertMessage message) {
        LOGGER.info(String.format("Sending alert message -> %s", message));
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
