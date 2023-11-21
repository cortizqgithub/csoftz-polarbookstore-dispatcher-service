/*----------------------------------------------------------------------------*/
/* Source File:   FUNCTIONSSTREAMINTEGRATIONTESTS.JAVA                        */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.18/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.dispatcherservice.stream;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarbookshop.dispatcherservice.domain.message.OrderAcceptedMessage;
import com.polarbookshop.dispatcherservice.domain.message.OrderDispatchedMessage;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.integration.support.MessageBuilder;

/**
 * Defines functions to be used in a Pub/Sub model (Integration Tests, Cloud Stream context only).
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@SpringBootTest
class FunctionsStreamIntegrationTests {
    @Autowired
    private InputDestination input;

    @Autowired
    private OutputDestination output;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenOrderAcceptedThenDispatched() throws IOException {
        var orderId = 121L;
        var inputMessage = MessageBuilder.withPayload(new OrderAcceptedMessage(orderId)).build();
        var expectedOutputMessage = MessageBuilder.withPayload(new OrderDispatchedMessage(orderId)).build();

        this.input.send(inputMessage);

        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderDispatchedMessage.class))
            .isEqualTo(expectedOutputMessage.getPayload());
    }
}
