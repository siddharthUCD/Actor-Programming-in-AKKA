package service;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Client;
import service.core.ClientInfo;
import service.core.Quotation;
import service.messages.ApplicationRequest;
import service.messages.ApplicationResponse;
import service.messages.QuotationRequest;

import java.text.NumberFormat;

public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(Client.class), "client");

        for(ClientInfo clientInfo:clients){
            ref.tell(new ApplicationRequest(), null);
            ActorSelection selection =
                    system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
            selection.tell(new ApplicationRequest(clientInfo), ref);
        }
    }

    public void printApplicationResponse(ApplicationResponse applicationResponse){
        displayProfile(applicationResponse.getClientInfo());

        // Retrieve quotations from the broker and display them...
        for(Quotation quotation : applicationResponse.getQuotations()) {
            displayQuotation(quotation);
        }

        // Print a couple of lines between each client
        System.out.println("\n");
    }

    /**
     * Display the client info nicely.
     *
     * @param info
     */
    public static void displayProfile(ClientInfo info) {
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.name) +
                        " | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
                        " | Age: " + String.format("%1$-30s", info.age)+" |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.licenseNumber) +
                        " | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     *
     * @param quotation
     */
    public static void displayQuotation(Quotation quotation) {
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.getCompany()) +
                        " | Reference: " + String.format("%1$-24s", quotation.getReference()) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice()))+" |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
    };
}
