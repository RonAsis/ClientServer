package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class RegisterMessage extends Message{

    private String nameUser="";
    private String password="";

    public RegisterMessage(){
        super(1);
    }

    public RegisterMessage(String nameUser,String password){
        super(1);
        this.nameUser=nameUser;
        this.password=password;
    }
    public short act(SharedData sharedData){
        if(sharedData.register(this.nameUser,this.password)){
            setResult(new AckMessage(getOpcode()));
            return this.getOpcode();
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
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
             return null;
         }
         else{
             this.password=this.popString();
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
