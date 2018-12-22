package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

public class StatMessage extends  MessagesClientToServer {
    int opcode=8;
    String name;
    String nameUserStat;

    public  StatMessage(String name, String nameUserStat){
        super(new Future<>());
        this.name=name;
        this.nameUserStat=nameUserStat;
    }

    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        String statUser=sharedData.getStatUser(this.name,this.nameUserStat);
        if(statUser.length()==0)
            setResult(new ErrorMessage(this.opcode));
        else{
            setResult(new AckMessage(this.opcode,statUser));
        }
    }
}
