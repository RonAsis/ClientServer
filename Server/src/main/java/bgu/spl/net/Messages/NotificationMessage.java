package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import java.util.List;

import static bgu.spl.net.api.MessageEncoderDecoderImpl.delimeter;

public class NotificationMessage extends Message {

    byte notificationType;
    String postingUser;
    String content;
    List<String > userSentMessageTo;

    /**
     * constructor
     * @param notificationType
     * @param postingUser
     * @param userSentMessageTo
     * @param content
     */
    public  NotificationMessage(byte notificationType, String postingUser, List<String > userSentMessageTo, String content){
        super(9);
        this.notificationType=notificationType;
        this.postingUser=postingUser;
        this.content=content;
        this.userSentMessageTo=userSentMessageTo;
    }

    /**
     * return the list of user that need get the message ,need use only after the message is ready
     * @return
     */
    public List getListUserSentMessageTo(){
        return this.userSentMessageTo;
    }

    /**
     * return bytes of this message
     * @return
     */
    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        opcodeByte=this.addByteToArray(opcodeByte,this.notificationType);
        String convertToBytes=this.postingUser;
        byte []result=convertToBytes.getBytes();
        result=mergeTwoArraysOfBytes(opcodeByte,result);
        result=addByteToArray(result,delimeter);
        result=mergeTwoArraysOfBytes(result,this.content.getBytes());
        result=addByteToArray(result,delimeter);
        return result;
    }

    @Override
    public short act(SharedData sharedData, String name) {
        return this.getOpcode();
    }

    /**
     * check if user find in the list of the people that needs get the notification
     * @param name
     * @return
     */
    public boolean checkIfFindInTheListOfUsers(String name){
            if (userSentMessageTo.contains(name))
                return true;
            return false;
    }
}
