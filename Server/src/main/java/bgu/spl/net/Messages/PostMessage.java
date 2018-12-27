package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class PostMessage extends  Message {
    String name;
    List<String> listUserS;
    String msg="";
    char typeMessage='0';
    String content;// for test the server
    public PostMessage(String name){
        super(5);
        this.name=name;
        this.listUserS=new ArrayList<>();
    }
    public PostMessage(String name,String content){
        super(5);
        this.name=name;
        this.content=content;
    }
    @Override
    public short act(SharedData sharedData) {
        List list=sharedData.sendMessagePost(name,listUserS,msg);
        if(list==null){
            this.setResult(new ErrorMessage(5));
            return -1;
        }
        else{
            Message notificationMessage=new NotificationMessage(typeMessage, name, list, msg);
            this.setResult(notificationMessage);
            sharedData.addNotifactionToMessagesPost(notificationMessage);
            return this.getOpcode();
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
        else{
            String content=popString();
            String[] spilt=content.split(" ");
            int i=0;
            for(;i<spilt.length;i++){
                if(spilt[i].contains("@"))
                    this.listUserS.add(spilt[i].substring(1));
                else{
                    msg=msg+spilt[i];
                    if(i+1<spilt.length && !spilt[i].contains("@"))
                        msg=msg+" ";
                }
            }
            this.rest();
            return this;
         }
    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        byte[] result=mergeTwoArraysOfBytes(opcodeByte,this.content.getBytes());
        result=addByteToArray(result,delimeter);
        return result;
    }
}
