package timy.demo.deadletter;

import static timy.demo.deadletter.RabbitMqConst.WORK_QUEUE;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class WorkConsumer {

  private static final Logger log = LoggerFactory.getLogger(WorkConsumer.class);

  @RabbitListener(queues = WORK_QUEUE)
  void consume(@Payload String payload, Channel channel,
      @Header(AmqpHeaders.DELIVERY_TAG) Long tag,
      @Header("retry.count") Integer retryCount) throws IOException {
    log.info("{} : {}", payload, tag);
    channel.basicReject(tag, false);
  }
}
