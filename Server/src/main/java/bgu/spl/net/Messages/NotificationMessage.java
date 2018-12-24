package bgu.spl.net.Messages;

import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class NotificationMessage extends Message {
    int notificationType=-1;
    String postingUser="";
    String content="";
    ConcurrentLinkedQueue<String > userSentMessageTo;

    public  NotificationMessage(int notificationType, String postingUser, ConcurrentLinkedQueue<String >userSentMessageTo, String content){
        super(9);
        this.notificationType=notificationType;
        this.postingUser=postingUser;
        this.content=content;
        this.userSentMessageTo=userSentMessageTo;
    }
    public NotificationMessage(){
        super(9);
    }
    @Override

    public String getContainResult() {
        return this.getResult();
    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        char c=(char)notificationType;
        String convertToBytes=notificationType+this.postingUser+delimeter+this.content+delimeter;
        byte[] stringtoBytes=convertToBytes.getBytes();
        return this.mergeTwoArraysOfBytes(opcodeByte,stringtoBytes);
    }

    @Override
    public void excute() {
        System.out.println(getContainResult());
    }

    @Override
    public Message createMessage(byte nextByte) {
        if(nextByte!=delimeter) {
            addBytes(nextByte);
            return null;
        }
        if(notificationType==-1){
            addBytes(nextByte);
            this.notificationType=Integer.parseInt(popString());
            rest();
            return null;
        }
        if(postingUser.length()==0){
            if(nextByte!=delimeter) {
                addBytes(nextByte);
                return null;
            }
            else{
                this.postingUser=popString();
                rest();
                return null;
            }
        }
        else{
            if(nextByte!=delimeter) {
                addBytes(nextByte);
                return null;
            }
            else{
                this.content=popString();
                rest();
                return this;
            }
        }
    }
}
