package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class StatMessage extends  Message {

    String userName;
    String userStat;
    /**
     * constructor
     * @param
     */
    public  StatMessage(){
        super(8);
    }

    /**
     * do the the action of this message
     * @param sharedData
     * @return
     */
    @Override
    public short act(SharedData sharedData,String name) {
        this.userName=name;
        short [] statUser=sharedData.getStatUser(this.userName,this.userStat);
        if(statUser==null) {
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
        else{
            setResult(new AckMessage(this.getOpcode(),statUser[0],statUser[1],statUser[2]));
            return  this.getOpcode();
        }
    }

    /**
     * use for decode this message from bytes to object
     * @param nextByte
     * @return
     */
    @Override
    public Message createMessage(byte nextByte) {
        if(nextByte!=delimeter) {
            addBytes(nextByte);
            return null;
        }
        else{
            this.userStat=popString();
            this.rest();
            return this;
        }
    }
}
