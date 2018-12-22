package bgu.spl.net.Messages;

public class AckMessage implements  MessagesServerToClient {

    int opcode=10;
    int messageOpcode;
    String optional;

    public AckMessage(int messageOpcode, String optional ){
        this.messageOpcode=messageOpcode;
        this.optional=optional;
    }

    @Override
    public String getContainResult() {
        String result="ACK "+this.opcode+" "+this.messageOpcode;
        if(this.optional.length()>0)
            result=result+" "+this.optional;
        return result;
    }
}
