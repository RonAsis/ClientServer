package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class MessageLogin extends Message {

    private String nameUser="";
    private String password="";

    /**
     * constructor
     */
    public MessageLogin(){
        super(2);
    }


    /**
     * do the action of this message
     * @param sharedData
     * @return
     */
    @Override
    public short act(SharedData sharedData,String name) {
        if(sharedData.login(this.nameUser,this.password)){
            Message ackMessage=new AckMessage(getOpcode());
            setResult(ackMessage);
            ((AckMessage) ackMessage).setNameUser(this.nameUser);
            return this.getOpcode();
        }
        else{
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
    }

    /**
     * return the name of the user that want login
     * @return
     */

    public String getNameUser() {
        return this.nameUser;
    }

    /**
     * use from create message from bytes
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
