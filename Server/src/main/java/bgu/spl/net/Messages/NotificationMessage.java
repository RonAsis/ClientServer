package bgu.spl.net.Messages;

import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class NotificationMessage extends MessagesServerToClient {
    int notificationType;
    String postingUser;
    String content;
    ConcurrentLinkedQueue<String > userSentMessageTo;

    public  NotificationMessage(int notificationType, String postingUser, ConcurrentLinkedQueue<String >userSentMessageTo, String content){
        super(9);
        this.notificationType=notificationType;
        this.postingUser=postingUser;
        this.content=content;
        this.userSentMessageTo=userSentMessageTo;
    }
    @Override

    public String getContainResult() {
        return null;
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
    public Message createMessage(byte nextByte) {
        return null;
    }
}
