/*----------------------------------------------------------------------------*/
/* Source File:   DISPATCHINGFUNCTIONS.JAVA                                   */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.16/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.dispatcherservice.function;

import com.polarbookshop.dispatcherservice.domain.message.OrderAcceptedMessage;
import com.polarbookshop.dispatcherservice.domain.message.OrderDispatchedMessage;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

/**
 * Defines functions to be used in a Pub/Sub model.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Configuration
public class DispatchingFunctions {

    private static final Logger log = LoggerFactory.getLogger(DispatchingFunctions.class);

    /**
     * Takes the identifier of an accepted order as input, packs the order, and returns the order identifier as output, ready to be labeled.
     *
     * @return The order identifier that were accepted.
     */
    @Bean
    public Function<OrderAcceptedMessage, Long> pack() {
        return orderAcceptedMessage -> {
            log.info("The order with id {} is packed.", orderAcceptedMessage.orderId());
            return orderAcceptedMessage.orderId();
        };
    }

    /**
     * Takes the identifier of the 'packed' order as input, labels the order, and returns the order identifier as output which completes the dispatch process.
     *
     * @return The order identifier that was accepted/labeled.
     */
    @Bean
    public Function<Flux<Long>, Flux<OrderDispatchedMessage>> label() {
        return orderFlux -> orderFlux.map(orderId -> {
            log.info("The order with id {} is labeled.", orderId);
            return new OrderDispatchedMessage(orderId);
        });
    }

}
