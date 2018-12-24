package bgu.spl.net.Messages;

public class ErrorMessage extends MessagesServerToClient {

    short messageOpcode;

    public ErrorMessage(int messageOpcode){
        super(11);
        this.messageOpcode=(short)messageOpcode;
    }

    @Override
    public Message createMessage(byte nextByte) {
        return null;
    }


    @Override
    public String getContainResult() {
        return "Error "+this.messageOpcode;
    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        byte[] messageOpcodeBytes=this.shortToBytes(messageOpcode);
       return mergeTwoArraysOfBytes(opcodeByte,messageOpcodeBytes);
    }
}
