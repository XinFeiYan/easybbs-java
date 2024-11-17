package com.jiang.rabbitmq;

import com.jiang.Utils.JsonUtils;
import com.jiang.Utils.StringTools;
import com.jiang.entity.po.UserMessage;
import com.jiang.rabbitmq.config.RabbitMQConfig;
import com.jiang.service.UserMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MqConsumer {
    private final static Logger logger = LoggerFactory.getLogger(MqConsumer.class);

    @Resource
    private UserMessageService userMessageService;

    @RabbitListener(queues = RabbitMQConfig.USER_MESSAGE_QUEUE)
    public void postMessage(String message){

        UserMessage userMessage = JsonUtils.convertJson2Obj(message,UserMessage.class);

        userMessageService.add(userMessage);
    }

}
