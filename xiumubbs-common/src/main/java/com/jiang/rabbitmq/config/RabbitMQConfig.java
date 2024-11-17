package com.jiang.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /*
    * topic模式
    * */
    public static final String USER_MESSAGE_QUEUE = "user.queue";
    public static final String USER_MESSAGE_EXCHANGE = "user.exchange";
    public static final String USER_MESSAGE_ROUTING_KEY = "user.key";

    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(USER_MESSAGE_QUEUE, true);
    }

    /*
    * 声明交换机
    * */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(USER_MESSAGE_EXCHANGE);
    }

    /**
     * 声明绑定关系
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(USER_MESSAGE_ROUTING_KEY);
    }

}
