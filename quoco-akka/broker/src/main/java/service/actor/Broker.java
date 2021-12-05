package service.actor;

import akka.actor.*;
import scala.concurrent.duration.Duration;
import service.auldfellas.AFQService;
import service.message.Init;
import service.messages.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Broker extends AbstractActor {
    //Creates an Actor System
    ActorSystem system = ActorSystem.create();

    //Unique identifier for the client requests
    private static int SEED_ID = 100;

    //Maintains the list of available quotations services
    List<ActorRef> actorRefs = new LinkedList<>();

    //To map the SEED_ID to the appropriate client info and quotations
    HashMap<Integer, ApplicationResponse> requestsMap = new HashMap<Integer, ApplicationResponse>();

    ActorRef clientRef = new ActorRef() {
        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public ActorPath path() {
            return null;
        }
    };

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                //handles client requests for quotations
                .match(ApplicationRequest.class,
                        msg -> {
                            clientRef = getSender();
                            requestsMap.put(SEED_ID, new ApplicationResponse(msg.getClientInfo()));

                            for (ActorRef ref : actorRefs) {
                                ref.tell(
                                        new QuotationRequest(SEED_ID, msg.getClientInfo()), getSelf()
                                );
                            }

                            getContext().system().scheduler().scheduleOnce(
                                    Duration.create(3, TimeUnit.SECONDS),
                                    getSelf(),
                                    new RequestDeadline(SEED_ID++),
                                    getContext().dispatcher(), null);
                        })
                //handles quotation services messages for registration
                .match(String.class,
                        msg -> {
                            if (!msg.equals("register")) return;
                            actorRefs.add(getSender());
                            System.out.println("Registered " + getSender().toString() + " successfully!:" + actorRefs.size());
                        })
                //handles the quotations sent by the services
                .match(QuotationResponse.class,
                        msg -> {
                            requestsMap.get(msg.getId()).addQuotations(msg.getQuotation());
                        })
                //handles the timeout call to send the received quotations
                .match(RequestDeadline.class,
                        msg -> {
                            clientRef.tell(
                                    requestsMap.get(msg.getSEED_ID()), null
                            );
                        }).build();
    }

    public ActorRef CreateRef(String userName){
        //Creates an Actor Reference
        ActorRef ref = system.actorOf(Props.create(Quoter.class), userName);
        ref.tell(new Init(new AFQService()), null);

        //Actors call for registration
        ActorSelection selection =
                system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        selection.tell("register", ref);
    }
}