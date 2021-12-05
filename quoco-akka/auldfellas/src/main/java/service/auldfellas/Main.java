package service.auldfellas;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Quoter;
import service.message.Init;

public class Main {
    public static void main(String[] args){
        //Creates an Actor System
        ActorSystem system = ActorSystem.create();

        //Creates an Actor Reference
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "auldfellas");
        ref.tell(new Init(new AFQService()), null);

        //Actors call for registration
        ActorSelection selection =
                system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        selection.tell("register", ref);
    }
}
