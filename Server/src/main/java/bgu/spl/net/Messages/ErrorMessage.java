package bgu.spl.net.Messages;

public class ErrorMessage extends Message {

    short messageOpcode;

    public ErrorMessage(int messageOpcode){
        super(11);
        this.messageOpcode=(short)messageOpcode;
    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        byte[] messageOpcodeBytes=this.shortToBytes(messageOpcode);
       return mergeTwoArraysOfBytes(opcodeByte,messageOpcodeBytes);
    }


}
