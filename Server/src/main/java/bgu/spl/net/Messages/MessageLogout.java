package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

public class MessageLogout extends  Message {

    String nameUser;
    public MessageLogout(String name){
        super(3);
        this.nameUser=name;
    }

    public MessageLogout(){
        super(3);
    }
    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        if(sharedData.logout(this.nameUser)){
            setResult(new AckMessage(getOpcode(),""));
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
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
}
