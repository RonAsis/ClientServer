package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class RegisterMessage extends MessagesClientToServer{

    private String nameUser="";
    private String password="";

    public RegisterMessage(){
        super(1);
    }
    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        if(sharedData.register(this.nameUser,this.password)){
            setResult(new AckMessage(getOpcode(),""));
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Message createMessage(byte nextByte) {
         if(nextByte!=delimeter) {
             addBytes(nextByte);
             return null;
        }
        else if (nameUser.length()==0 && nextByte!=delimeter){
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

}
