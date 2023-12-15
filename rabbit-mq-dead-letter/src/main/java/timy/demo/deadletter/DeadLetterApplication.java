package timy.demo.deadletter;

import static timy.demo.deadletter.RabbitMqConst.WORK_EXCHANGE;
import static timy.demo.deadletter.RabbitMqConst.WORK_KEY;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeadLetterApplication implements CommandLineRunner {

  private final RabbitTemplate rabbitTemplate;

  public DeadLetterApplication(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public static void main(String[] args) {
    SpringApplication.run(DeadLetterApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    for (int i = 0; i < 5; i++) {
      rabbitTemplate.convertAndSend(WORK_EXCHANGE, WORK_KEY, "content" + i, message -> {
        message.getMessageProperties().getHeaders().put("retry.count", 1);
        return message;
      });
    }

  }
}
