package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

public class MessageLogout extends  MessagesClientToServer {

    String nameUser;
    public MessageLogout(String name){
        super(3);
        this.nameUser=name;
    }
    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        if(sharedData.logout(this.nameUser)){
            setResult(new AckMessage(getOpcode(),""));
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
        }
    }

    @Override
    public Message createMessage(byte nextByte) {
        return this;
    }
}
