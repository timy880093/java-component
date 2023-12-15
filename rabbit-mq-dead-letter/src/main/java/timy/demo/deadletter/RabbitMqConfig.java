package timy.demo.deadletter;

import static timy.demo.deadletter.RabbitMqConst.DLX_EXCHANGE;
import static timy.demo.deadletter.RabbitMqConst.DLX_KEY;
import static timy.demo.deadletter.RabbitMqConst.DLX_QUEUE;
import static timy.demo.deadletter.RabbitMqConst.WORK_EXCHANGE;
import static timy.demo.deadletter.RabbitMqConst.WORK_KEY;
import static timy.demo.deadletter.RabbitMqConst.WORK_QUEUE;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  @Bean(WORK_EXCHANGE)
  Exchange workExchange() {
    return ExchangeBuilder.directExchange(WORK_EXCHANGE).durable(true).build();
  }

  @Bean(WORK_QUEUE)
  Queue workQueue() {
    return QueueBuilder.durable(WORK_QUEUE).ttl(3000).deadLetterExchange(DLX_EXCHANGE)
        .deadLetterRoutingKey(DLX_KEY).build();
  }

  @Bean
  Binding workBinding(@Qualifier(WORK_EXCHANGE) Exchange workExchange,
      @Qualifier(WORK_QUEUE) Queue workQueue) {
    return BindingBuilder.bind(workQueue).to(workExchange).with(WORK_KEY).noargs();
  }

  @Bean(DLX_EXCHANGE)
  Exchange dlxExchange() {
    return ExchangeBuilder.directExchange(DLX_EXCHANGE).durable(true).build();
  }

  @Bean(DLX_QUEUE)
  Queue dlxQueue() {
    return QueueBuilder.durable(DLX_QUEUE).ttl(5000).deadLetterExchange(WORK_EXCHANGE)
        .deadLetterRoutingKey(WORK_KEY).build();
  }

  @Bean
  Binding dlxBinding(@Qualifier(DLX_EXCHANGE) Exchange exchange,
      @Qualifier(DLX_QUEUE) Queue queue) {
    return BindingBuilder.bind(queue).to(exchange).with(DLX_KEY).noargs();
  }

}
