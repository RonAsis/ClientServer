package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

public class MessageLogout extends  Message {

    String nameUser;

    /**
     * constructor
     * @param
     */
    public MessageLogout(){
        super(3);
    }

    /**
     * do the action of this message
     * @param sharedData
     * @return
     */
    @Override
    public short act(SharedData sharedData,String name){
        this.nameUser=name;
        if(sharedData.logout(this.nameUser)){
            Message ackMessage=new AckMessage(getOpcode());
            setResult(ackMessage);
            ((AckMessage) ackMessage).setNameUser("");
            return this.getOpcode();
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
    }

    /**
     * create the message from bytes
     * @param nextByte
     * @return
     */
    @Override
    public Message createMessage(byte nextByte) {
        return this;
    }

    /**
     * return the name of the user that want logout
     * @return
     */
    public String getNameUser(){
        return this.nameUser;
    }
}
