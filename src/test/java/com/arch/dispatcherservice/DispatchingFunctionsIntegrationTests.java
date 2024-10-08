package com.arch.dispatcherservice;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@FunctionalSpringBootTest
class DispatchingFunctionsIntegrationTests {

  @Autowired
  private FunctionCatalog functionCatalog;

  @Test
  void packAndLabelOrder() {
    // Gets the composed function from the FunctionCatalog
    Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> packAndLabel = functionCatalog
        .lookup(Function.class, "pack|label");
    long orderId = 121;

    // Defines an OrderAcceptedMessage, which is the input to the function
    // Asserts that the output of the function is the expected OrderDispatchedMessage object
    StepVerifier.create(packAndLabel.apply(new OrderAcceptedMessage(orderId)))
        .expectNextMatches(dispatchedOrder -> dispatchedOrder.equals(new OrderDispatchedMessage(orderId)))
        .verifyComplete();
  }
}
