package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UserListMessage extends  MessagesClientToServer {
    int opcode=7;
    String name;

    public  UserListMessage(String name){
        super(new Future<>());
        this.name=name;
    }
    @Override
    public void excute() {
        SharedData sharedData = SharedData.getInstance();
        ConcurrentLinkedQueue<String> userNameListRegister = sharedData.getUserNameListRegister(this.name);
        if (userNameListRegister.size() == 0)
            setResult(new ErrorMessage(this.opcode));
        else {
            {// less part of the list is successful
                String optional = "";
                String delimeter = "\0";//between all name;
                for (String key : userNameListRegister) {
                    optional = optional + delimeter;
                }
                optional = userNameListRegister.size() + " " + optional;//need check if need be space of \0**************************************************
                setResult(new AckMessage(this.opcode, optional));
            }

        }
    }
}
