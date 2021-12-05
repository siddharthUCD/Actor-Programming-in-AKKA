package service.actor;

import akka.actor.AbstractActor;
import service.Main;
import service.messages.ApplicationResponse;

public class Client extends AbstractActor {
    Main clientInfo = new Main();

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                //Handles the reply from broker with all the quotations
                .match(ApplicationResponse.class,
                        msg -> {
                            clientInfo.printApplicationResponse(msg);
                        }).build();
    }
}