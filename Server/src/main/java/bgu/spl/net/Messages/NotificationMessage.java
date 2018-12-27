package bgu.spl.net.Messages;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class NotificationMessage extends Message {
    char notificationType='2';
    String postingUser="";
    String content="";
    List<String > userSentMessageTo;

    public  NotificationMessage(char notificationType, String postingUser, List<String > userSentMessageTo, String content){
        super(9);
        this.notificationType=notificationType;
        this.postingUser=postingUser;
        this.content=content;
        this.userSentMessageTo=userSentMessageTo;
    }
    public List getListUserSentMessageTo(){
        return this.userSentMessageTo;
    }
    public NotificationMessage(){
        super(9);
    }
//    @Override
//
//    public String getContainResult() {
//        return this.getResult();
//    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        String convertToBytes=notificationType+this.postingUser;
        byte []result=convertToBytes.getBytes();
        result=mergeTwoArraysOfBytes(opcodeByte,result);
        result=addByteToArray(result,delimeter);
        result=mergeTwoArraysOfBytes(result,this.content.getBytes());
        result=addByteToArray(result,delimeter);
        return result;
    }

    public boolean checkIfFindInTheListOfUsers(String name){
        if(userSentMessageTo.contains(name))
            return true;
        else
            return false;
    }

    @Override
    public Message createMessage(byte nextByte) {
        if(notificationType=='2'){
            this.notificationType=(char)nextByte;
            return null;
        }
        if(nextByte!=delimeter) {
            addBytes(nextByte);
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
    public String toString(){
        return postingUser+">"+this.content;
    }
}
