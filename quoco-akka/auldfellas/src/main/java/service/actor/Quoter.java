package service.actor;

import akka.actor.*;
import service.core.Quotation;
import service.core.QuotationService;
import service.message.Init;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class Quoter extends AbstractActor {
    private QuotationService service;

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                //Handles the request for quotation form broker
                .match(QuotationRequest.class,
                        msg -> {
                            Quotation quotation =
                                    service.generateQuotation(msg.getClientInfo());
                            getSender().tell(
                                    new QuotationResponse(msg.getId(), quotation), getSelf());
                        })
                //To initialize the service attribute
                .match(Init.class,
                        msg -> {
                            service = msg.getService();
                        }).build();
    }
}