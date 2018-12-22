package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

public class MessageLogout extends  MessagesClientToServer {

    int opcode=3;
    String name;
    public MessageLogout(String name){
        super(new Future<>());
        this.name=name;
    }
    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        if(sharedData.logout(this.name)){
            setResult(new AckMessage(this.opcode,""));
        }
        else{
            setResult(new ErrorMessage(this.opcode));
        }
    }
}
