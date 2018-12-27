package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class StatMessage extends  Message {
    String userName;

    public  StatMessage(String name){
        super(8);
        this.userName=name;
    }

    @Override
    public short act(SharedData sharedData) {
        short [] statUser=sharedData.getStatUser(this.userName);
        if(statUser==null) {
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
        else{
            setResult(new AckMessage(this.getOpcode(),statUser[0],statUser[1],statUser[2]));
            return  this.getOpcode();
        }
    }


    @Override
    public Message createMessage(byte nextByte) {
        if(nextByte!=delimeter) {
            addBytes(nextByte);
            return null;
        }
        else{
            this.userName=popString();
            this.rest();
            return this;
        }
    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        byte[] result=mergeTwoArraysOfBytes(opcodeByte,this.userName.getBytes());
        result=addByteToArray(result,delimeter);
        return result;
    }
}
