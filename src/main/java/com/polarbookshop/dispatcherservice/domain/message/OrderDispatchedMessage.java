/*----------------------------------------------------------------------------*/
/* Source File:   ORDERDISPATCHEDMESSAGE.JAVA                                 */
/* Copyright (c), 2023 CSoftZ                                                 */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Nov.16/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.polarbookshop.dispatcherservice.domain.message;

/**
 * Holds the Order Dispatch message in a PUb/Sub Model.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record OrderDispatchedMessage(Long orderId) {
}
