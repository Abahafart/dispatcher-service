package com.arch.dispatcherservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)// Configures the test binder
class FunctionsStreamIntegrationTests {

  @Autowired
  private InputDestination inputDestination;// represents the input binding packlabel-in-0
  @Autowired
  private OutputDestination outputDestination;// Represents the output binding packlabel-out-0
  @Autowired
  private ObjectMapper objectMapper;// Uses jackson to deserialize JSON message payloads to Java objects

  @Test
  void whenOrderAcceptedThenDispatched() throws IOException {
    long orderId = 121L;
    Message<OrderAcceptedMessage> inputMessage = MessageBuilder.withPayload(new OrderAcceptedMessage(orderId)).build();
    Message<OrderDispatchedMessage> expectedOutputMessage = MessageBuilder.withPayload(new OrderDispatchedMessage(orderId)).build();

    // send the message to the input channel
    this.inputDestination.send(inputMessage);

    // receives and asserts a message from the output channel
    assertThat(objectMapper.readValue(outputDestination.receive().getPayload(), OrderDispatchedMessage.class))
        .isEqualTo(expectedOutputMessage.getPayload());
  }
}
