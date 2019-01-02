package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;
import java.util.ArrayList;
import java.util.List;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class PostMessage extends  Message {

    String name;
    List<String> listUserS;
    String content="";
    char typeMessage='0';

    /**
     * constructor
     * @param
     */
    public PostMessage(){
        super(5);
        this.listUserS=new ArrayList<>();
    }
    /**
     * do the action of this message
     * @param sharedData
     * @return
     */
    @Override
    public short act(SharedData sharedData,String name) {
        this.name=name;
        List list=sharedData.sendMessagePost(name,listUserS,content);
        if(list==null){
            this.setResult(new ErrorMessage(5));
            return -1;
        }
        else{
            Message notificationMessage=new NotificationMessage(typeMessage, name, list, content);
            this.setResult(notificationMessage);
            sharedData.addNotifactionToMessagesPost(notificationMessage);
            return this.getOpcode();
        }
    }


    /**
     * use for create message from bytes
     * @param nextByte
     * @return
     */
    public Message createMessage(byte nextByte) {
         if(nextByte!=delimeter) {
            addBytes(nextByte);
            return null;
        }
        else{
            content=popString();
            String[] spilt=content.split(" ");
            int i=0;
            for(;i<spilt.length;i++){
                if(spilt[i].contains("@"))
                    this.listUserS.add(spilt[i].substring(1));
            }
            return this;
         }
    }

}
