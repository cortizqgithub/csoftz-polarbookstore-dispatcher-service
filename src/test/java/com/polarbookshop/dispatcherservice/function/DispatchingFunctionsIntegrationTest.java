/*----------------------------------------------------------------------------*/
/* Source File:   DISPATCHINGFUNCTIONSINTEGRATIONTEST.JAVA                    */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.17/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.dispatcherservice.function;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbookshop.dispatcherservice.domain.message.OrderAcceptedMessage;
import com.polarbookshop.dispatcherservice.domain.message.OrderDispatchedMessage;
import java.util.function.Function;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Defines functions to be used in a Pub/Sub model (Integration Tests, only Functions context).
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@FunctionalSpringBootTest
@Disabled("These tests are only necessary when using the functions alone (no bindings)")
class DispatchingFunctionsIntegrationTest {
    private static final long ORDER_ID = 121L;

    private static final String PACK_FN = "pack";
    private static final String LABEL_FN = "label";
    private static final String PACK_LABEL_FN = "pack|label";

    @Autowired
    private FunctionCatalog catalog;

    @Test
    void packOrder() {
        Function<OrderAcceptedMessage, Long> pack = catalog.lookup(Function.class, PACK_FN);
        long orderId = ORDER_ID;

        assertThat(pack.apply(new OrderAcceptedMessage(orderId))).isEqualTo(orderId);
    }

    @Test
    void labelOrder() {
        Function<Flux<Long>, Flux<OrderDispatchedMessage>> label = catalog.lookup(Function.class, LABEL_FN);
        Flux<Long> orderId = Flux.just(ORDER_ID);

        StepVerifier.create(label.apply(orderId))
            .expectNextMatches(dispatchedOrder -> dispatchedOrder.equals(new OrderDispatchedMessage(ORDER_ID)))
            .verifyComplete();
    }

    @Test
    void packAndLabelOrder() {
        Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> packAndLabel = catalog.lookup(Function.class, PACK_LABEL_FN);
        long orderId = ORDER_ID;

        StepVerifier.create(packAndLabel.apply(new OrderAcceptedMessage(orderId)))
            .expectNextMatches(dispatchedOrder -> dispatchedOrder.equals(new OrderDispatchedMessage(orderId)))
            .verifyComplete();
    }
}
