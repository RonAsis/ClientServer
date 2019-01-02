package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class RegisterMessage extends Message{

    private String nameUser="";
    private String password="";

    /**
     * constructor
     */
    public RegisterMessage(){
        super(1);
    }

    /**
     * do the action of this message
     * @param sharedData
     * @return
     */
    public short act(SharedData sharedData,String name){
        if(sharedData.register(this.nameUser,this.password)){
            setResult(new AckMessage(getOpcode()));
            return this.getOpcode();
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
    }

    /**
     * use for create message from bytes
     * @param nextByte
     * @return
     */
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
}
