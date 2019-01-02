package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class PmMessage extends  Message {

        String userName;
        ConcurrentLinkedQueue<String> listUserS;
        String userSentMessageTo="";
        String content;
        byte typeMessage=0;

    /**
     * constructor
     * @param
     */
    public PmMessage(){
            super(6);
            this.listUserS=new ConcurrentLinkedQueue<>();
        }

    /**
     * for do the action of this message
     * @param sharedData
     * @return
     */
    @Override
        public short act(SharedData sharedData,String name) {
        this.userName=name;
            if(sharedData.sendMessagePM(userName,userSentMessageTo,content)) {
                List list=new ArrayList();
                list.add(userSentMessageTo);
                NotificationMessage notificationMessage=new NotificationMessage(typeMessage, userName, list, content);
                this.setResult(notificationMessage);
                sharedData.addNotifactionToMessages(notificationMessage);
                return  this.getOpcode();
            }
            else{
                this.setResult(new ErrorMessage(6));
                return  -1;
            }
        }

    /**
     * for create mesasge from bytes
     * @param nextByte
     * @return
     */
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
            this.content=popString();
            this.rest();
            return this;
        }
    }
}
