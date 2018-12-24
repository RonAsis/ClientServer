package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class PostMessage extends  Message {
    String name;
    ConcurrentLinkedQueue<String> listUserS;
    String msg="";
    int typeMessage=0;
    public PostMessage(String name){
        super(5);
        this.name=name;
        this.listUserS=new ConcurrentLinkedQueue<>();
    }

    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        ConcurrentLinkedQueue list=sharedData.sendMessagePost(name,listUserS,msg);
        if(list==null){
            this.setResult(new ErrorMessage(5));
        }
        else{
            this.setResult(new NotificationMessage(typeMessage, name, list, msg));
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
            for(;i<spilt.length-1;i++){
                if(spilt[i].contains("@"))
                    this.listUserS.add(spilt[i].substring(1));
                else{
                    msg=spilt[i]+" ";
                }
            }
            this.msg=this.msg+spilt[i];
            this.rest();
            return this;
         }
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }
}
