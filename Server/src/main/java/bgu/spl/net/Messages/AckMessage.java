package bgu.spl.net.Messages;

import com.sun.org.apache.regexp.internal.RE;

import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class AckMessage extends Message {

    private short messageOpcode=-1;//also use for stat opcode
    private String optional;
    private int numOfUsers=-1;
    private ConcurrentLinkedQueue<String> userNameList=new ConcurrentLinkedQueue<>();
    private int numPosts=-1;
    private int numFollowers=-1;
    private int numFollowing=-1;
    private String nameUser;

    public AckMessage(int messageOpcode, String optional ){
        super(10);
        this.messageOpcode=(short)messageOpcode;
        this.optional=optional;
    }
    public AckMessage(int messageOpcode ){
        super(10);
        this.messageOpcode=(short)messageOpcode;
    }
    public AckMessage( ){
        super(10);
    }
//    @Override
//    public String getContainResult() {
//        String result="ACK "+getOpcode()+" "+this.messageOpcode;
//        if(this.optional.length()>0)
//            result=result+" "+this.optional;
//        return result;
//    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        byte[] messageOpcodeBytes=this.shortToBytes(messageOpcode);
        byte [] c=mergeTwoArraysOfBytes(opcodeByte,messageOpcodeBytes);
        return c;
        //  return  mergeTwoArraysOfBytes(c,optional.getBytes());
    }


    @Override
    public Message createMessage(byte nextByte) {
        if(messageOpcode==-1 && getLen()<1){
            addBytes(nextByte);
            return null;
        }
        if(messageOpcode==-1){
            this.messageOpcode=this.bytesToShort();
            this.rest();
            if(this.messageOpcode!=4 && this.messageOpcode!=7 && this.messageOpcode!=8)
                return  this;
            return null;
        }
        if (messageOpcode==4){
            return createMessageOpcode4And7(nextByte);
        }
        if(messageOpcode==7){
            return createMessageOpcode4And7(nextByte);
        }
        if(messageOpcode==8){
            return  createMessageOpcode8(nextByte);
        }
        return null;
    }
    private Message createMessageOpcode4And7(byte nextByte){
        this.numOfUsers=createNumber2Bytes( nextByte,this.numOfUsers);
        if(this.numOfUsers==-1)
            return null;
        if(numOfUsers>this.userNameList.size()+1){
            if(nextByte!=delimeter){
                addBytes(nextByte);
                return null;
            }
            else{
                this.userNameList.add(popString());
                rest();
                return null;
            }
        }
        else{
            if(nextByte!=delimeter){
                addBytes(nextByte);
                return null;
            }
            else{
                this.userNameList.add(popString());
                rest();
                return this;
            }
        }
    }

    private int createNumber2Bytes(byte nextByte,int number){
        if(number==-1 && getLen()<1){
            addBytes(nextByte);
            return -1;
        }
        if(number==-1){
            addBytes(nextByte);
            int result=Integer.parseInt(popString());
            rest();
            return result;
        }
        return -1;
    }


    private Message createMessageOpcode8(byte nextByte){
        if(numPosts==-1) {
            this.numPosts = createNumber2Bytes(nextByte, this.numPosts);
                return null;
        }
        if(this.numFollowers==-1){
        this.numFollowers=createNumber2Bytes(nextByte,this.numFollowers);
            return null;
    }
        if(this.numFollowing==-1 && getLen()<1) {
            this.numFollowing = createNumber2Bytes(nextByte, this.numFollowers);
            return null;
        }
            else{
            this.numFollowing = createNumber2Bytes(nextByte, this.numFollowers);
            return this;
            }
    }
    public short getMessageOpcode(){
        return  this.messageOpcode;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getNameUser() {
        return nameUser;
    }
}
