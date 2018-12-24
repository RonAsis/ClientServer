package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class StatMessage extends  MessagesClientToServer {
    String name;
    String nameUserStat;

    public  StatMessage(String name, String nameUserStat){
        super(8);
        this.name=name;
        this.nameUserStat=nameUserStat;
    }

    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        String statUser=sharedData.getStatUser(this.name,this.nameUserStat);
        if(statUser.length()==0)
            setResult(new ErrorMessage(getOpcode()));
        else{
            setResult(new AckMessage(getOpcode(),statUser));
        }
    }

    @Override
    public Message createMessage(byte nextByte) {
        if(nextByte!=delimeter) {
            addBytes(nextByte);
            return null;
        }
        else{
            this.nameUserStat=popString();
            this.rest();
            return this;
        }
    }
}
