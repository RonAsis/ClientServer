package bgu.spl.net.Messages;

import bgu.spl.net.Future;

import java.nio.charset.StandardCharsets;

public abstract class MessagesClientToServer extends Message {

    private Future<MessagesServerToClient> result;
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = -1;

    public MessagesClientToServer(int opcode){
        super(opcode);
        this.result=new Future<>();
    }

    public abstract void  excute();

    public String getResult(){
        return this.result.get().getContainResult();
    }

    public void setResult(MessagesServerToClient result) {
        this.result.resolve(result);
    }

    public String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
    public void addBytes(Byte nextByte){
        len++;
        this.bytes[len]=nextByte;
    }
    public void rest(){
        this.len=-1;
    }
    public void biggerBytes(int size){
        bytes=new byte[size];
    }
    public int getLen(){
        return this.len;
    }
}
