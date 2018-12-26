package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class MessageLogin extends Message {

    private String nameUser="";
    private String password="";

    public MessageLogin(){
        super(2);
    }

    public MessageLogin(String name,String password)
    {
        super(2);
        this.nameUser=name;
        this.password=password;
    }
    @Override
    public short act(SharedData sharedData) {
        if(sharedData.login(this.nameUser,this.password)){
            Message ackMessage=new AckMessage(getOpcode(),"");
            setResult(ackMessage);
            ((AckMessage) ackMessage).setNameUser(this.nameUser);
            return this.getOpcode();
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
    }

//    @Override
//    public String getContainResult() {
//        return this.getResult();
//    }

    public String getNameUser() {
        return this.nameUser;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Message createMessage(byte nextByte) {
        if(nextByte!=delimeter) {
            addBytes(nextByte);
            return null;
        }
        else if (nameUser.length()==0 && nextByte==delimeter){
            this.nameUser=this.popString();
            this.rest();
            return null;
        }
        else{
            this.password=this.popString();
            this.rest();
            return this;
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        byte[] result=mergeTwoArraysOfBytes(opcodeByte,this.nameUser.getBytes());
        result=addByteToArray(result,delimeter);
        result=mergeTwoArraysOfBytes(result,password.getBytes());
        result=addByteToArray(result,delimeter);
        return result;
    }
}
