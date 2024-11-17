package com.jiang.rabbitmq;

import com.jiang.Utils.JsonUtils;
import com.jiang.entity.po.UserMessage;
import com.jiang.rabbitmq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MqProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendReminder(UserMessage userMessage){
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_MESSAGE_EXCHANGE,RabbitMQConfig.USER_MESSAGE_ROUTING_KEY, JsonUtils.convertObj2Json(userMessage));
    }
}
