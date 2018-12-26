package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class PmMessage extends  Message {
        String name;
        ConcurrentLinkedQueue<String> listUserS;
        String userSentMessageTo="";
        String msg;
        int typeMessage=1;


    public PmMessage(String name){
            super(6);
            this.name=name;
            this.listUserS=new ConcurrentLinkedQueue<>();
        }

        @Override
        public short act(SharedData sharedData) {
            if(sharedData.sendMessagePM(name,userSentMessageTo,msg)) {
                List list=new ArrayList();
                list.add(userSentMessageTo);
                this.setResult(new NotificationMessage(typeMessage, name, list, msg));
                return  this.getOpcode();
            }
            else{
                this.setResult(new ErrorMessage(6));
                return  -1;
            }
        }

//    @Override
//    public String getContainResult() {
//        return null;
//    }

    @Override
    public Message createMessage(byte nextByte) {
        if(nextByte!=delimeter) {
            addBytes(nextByte);
            return null;
        }
        if(userSentMessageTo.length()==0){
            this.userSentMessageTo=popString();
            this.rest();
            return null;
        }
        else{
            this.msg=popString();
            this.rest();
            return this;
        }
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
