package service.messages;

import service.core.ClientInfo;
import service.core.Quotation;

import java.util.LinkedList;
import java.util.List;

public class ApplicationResponse implements MySerializable{
    public ApplicationResponse(){}

    public ApplicationResponse(ClientInfo clientInfo){
        this.clientInfo = clientInfo;
        quotations = new LinkedList<>();
    }

    private ClientInfo clientInfo;
    private List<Quotation> quotations;

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void addQuotations(Quotation quotation) {
        this.quotations.add(quotation);
    }
}
