package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

public class MessageLogout extends  Message {

    String nameUser="";
    public MessageLogout(String name){
        super(3);
        this.nameUser=name;
    }

    public MessageLogout(){
        super(3);
    }
    @Override
    public short act(SharedData sharedData){
        if(sharedData.logout(this.nameUser)){
            Message ackMessage=new AckMessage(getOpcode(),"");
            setResult(ackMessage);
            ((AckMessage) ackMessage).setNameUser("");
            return this.getOpcode();
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
    }

    @Override
    public Message createMessage(byte nextByte) {
        return this;
    }

    @Override
    public byte[] getBytes() {
        return this.shortToBytes(getOpcode());
    }

    public String getNameUser(){
        return this.nameUser;
    }
}
