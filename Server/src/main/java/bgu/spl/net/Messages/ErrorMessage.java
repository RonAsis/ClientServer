package bgu.spl.net.Messages;

public class ErrorMessage extends Message {

    short messageOpcode=-1;

    public ErrorMessage(int messageOpcode){
        super(11);
        this.messageOpcode=(short)messageOpcode;
    }

    public ErrorMessage(){
        super(11);
    }
    @SuppressWarnings("Duplicates")
    @Override
    public Message createMessage(byte nextByte) {
        if(messageOpcode==-1 && getLen()<1){
            addBytes(nextByte);
            return null;
        }
        if(messageOpcode==-1){
            this.messageOpcode=this.bytesToShort();
            this.rest();
            return this;
        }
        return null;
    }


//    @Override
//    public String getContainResult() {
//        return "Error "+this.messageOpcode;
//    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        byte[] messageOpcodeBytes=this.shortToBytes(messageOpcode);
       return mergeTwoArraysOfBytes(opcodeByte,messageOpcodeBytes);
    }


}
