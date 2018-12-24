package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class PmMessage extends  MessagesClientToServer {
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
        public void excute() {
            SharedData sharedData=SharedData.getInstance();
            if(sharedData.sendMessagePM(name,userSentMessageTo,msg)) {
                ConcurrentLinkedQueue list=new ConcurrentLinkedQueue();
                list.add(userSentMessageTo);
                this.setResult(new NotificationMessage(typeMessage, name, list, msg));
            }
            else{
                this.setResult(new ErrorMessage(6));
            }
        }

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
}
