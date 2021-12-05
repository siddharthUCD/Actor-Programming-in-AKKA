package service.messages;

import service.core.ClientInfo;

public class ApplicationRequest implements MySerializable{
    public ApplicationRequest(){}

    public ApplicationRequest(ClientInfo clientInfo){
        this.clientInfo = clientInfo;
    }

    private ClientInfo clientInfo;

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}
