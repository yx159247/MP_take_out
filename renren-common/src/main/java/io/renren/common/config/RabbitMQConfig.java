package io.renren.common.config;

import io.renren.common.constant.Constant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/3/31
 **/
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue orderQueue() {
        return new Queue(Constant.ORDER_QUEUE, true);
    }

    @Bean
    public FanoutExchange orderExchange() {
        return new FanoutExchange(Constant.ORDER_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingOrderDirect() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange());
    }

}
