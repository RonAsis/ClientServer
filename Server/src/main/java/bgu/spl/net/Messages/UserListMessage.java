package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UserListMessage extends  Message {
    String name;

    public  UserListMessage(String name){
        super(7);
        this.name=name;
    }
    @Override
    public void excute() {
        SharedData sharedData = SharedData.getInstance();
        ConcurrentLinkedQueue<String> userNameListRegister = sharedData.getUserNameListRegister(this.name);
        if (userNameListRegister.size() == 0)
            setResult(new ErrorMessage(getOpcode()));
        else {
            {// less part of the list is successful
                String optional = "";
                String delimeter = "\0";//between all name;
                for (String key : userNameListRegister) {
                    optional = optional + delimeter;
                }
                optional = userNameListRegister.size() + " " + optional;//need check if need be space of \0**************************************************
                setResult(new AckMessage(getOpcode(), optional));
            }

        }
    }
//
//    @Override
//    public String getContainResult() {
//        return null;
//    }

    @Override
    public Message createMessage(byte nextByte) {
        return this;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
