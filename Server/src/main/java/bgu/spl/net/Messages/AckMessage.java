package bgu.spl.net.Messages;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class AckMessage extends MessagesServerToClient {

    short messageOpcode;
    String optional;

    public AckMessage(int messageOpcode, String optional ){
        super(10);
        this.messageOpcode=(short)messageOpcode;
        this.optional=optional;
    }

    @Override
    public String getContainResult() {
        String result="ACK "+getOpcode()+" "+this.messageOpcode;
        if(this.optional.length()>0)
            result=result+" "+this.optional;
        return result;
    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        byte[] messageOpcodeBytes=this.shortToBytes(messageOpcode);
        byte [] c=mergeTwoArraysOfBytes(opcodeByte,messageOpcodeBytes);
        return  mergeTwoArraysOfBytes(c,optional.getBytes());
    }

    @Override
    public Message createMessage(byte nextByte) {
        return null;
    }
}
