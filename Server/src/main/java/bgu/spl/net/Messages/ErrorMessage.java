package bgu.spl.net.Messages;

public class ErrorMessage implements  MessagesServerToClient {
    int opcode=11;
    int messageOpcode;
    public ErrorMessage(int messageOpcode){
        this.messageOpcode=messageOpcode;
    }


    @Override
    public String getContainResult() {
        return "Error "+this.messageOpcode;
    }
}
