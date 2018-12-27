package bgu.spl.net.Messages;

import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class AckMessage extends Message {

    private short messageOpcode=-1;//also use for stat opcode
    private short numOfUsers=-1;
    private List<String> userNameList=new ArrayList<>();
    private short numPosts=-1;
    private short numFollowers=-1;
    private short numFollowing=-1;
    private String nameUser;

//    public AckMessage(short messageOpcode, String optional ){
//        super(10);
//        this.messageOpcode=messageOpcode;
//        this.optional=optional;
//    }
    public AckMessage(int messageOpcode ){
        super(10);
        this.messageOpcode=(short)messageOpcode;
    }
    public AckMessage(short messageOpcode,short numOfUsers,List<String> userNameList){
        super(10);
        this.messageOpcode=messageOpcode;
        this.numOfUsers=numOfUsers;
        this.userNameList=userNameList;
    }
    public AckMessage(short messageOpcode,short numPosts,short numFollowers, short numFollowing){
        super(10);
        this.messageOpcode=messageOpcode;
        this.numPosts=numPosts;
        this.numFollowers=numFollowers;
        this.numFollowing=numFollowing;
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
         if (messageOpcode==4){
            return mergeTwoArraysOfBytes(c,opcodeOprtionalBytes7And4());
        }
        else if(messageOpcode==7){
            return mergeTwoArraysOfBytes(c,opcodeOprtionalBytes7And4());
        }
        else if(messageOpcode==8)
            return  mergeTwoArraysOfBytes(c,opcodeOprtionalBytes8());
        else {
            return c;
        }
        //  return  mergeTwoArraysOfBytes(c,optional.getBytes());
    }
    private byte[] opcodeOprtionalBytes8(){
        return  mergeTwoArraysOfBytes(mergeTwoArraysOfBytes(this.shortToBytes(this.numOfUsers),this.shortToBytes(this.numFollowers)),this.shortToBytes(this.numFollowing));
    }

        private byte[] opcodeOprtionalBytes7And4(){
            byte []numOfUsersByte=this.shortToBytes(this.numOfUsers);
            return mergeTwoArraysOfBytes(numOfUsersByte,createBytesFromList());
        }
        private byte[] createBytesFromList(){
        byte []result=new byte[0];
        for(String key:this.userNameList){
            result=mergeTwoArraysOfBytes(result,key.getBytes());
            result=addByteToArray(result,delimeter);
        }
        return  result;
        }
    @Override
    public Message createMessage(byte nextByte) {
        if(messageOpcode==-1 && getLen()<1){
            addBytes(nextByte);
            return null;
        }
        if(messageOpcode==-1){
            addBytes(nextByte);
            this.messageOpcode=this.bytesToShort();
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
        if(numOfUsers==-1 &&  getLen()<1){
            addBytes(nextByte);
            return null;
        }
        else if(numOfUsers==-1){
            addBytes(nextByte);
            this.numOfUsers=this.bytesToShort();
            return null;
        }
        if(numOfUsers>this.userNameList.size()) {
            if (nextByte != delimeter) {
                addBytes(nextByte);
                return null;
            } else {
                this.userNameList.add(popString());
                if (numOfUsers == this.userNameList.size())
                    return this;
                return null;
            }
        }
        else{
            return null;
            }

    }


    private Message createMessageOpcode8(byte nextByte){
        if(numPosts==-1 &&  getLen()<1){
            addBytes(nextByte);
            return null;
        }
        else if(numPosts==-1){
            this.numPosts=this.bytesToShort();
            return null;
        }
        else if(numFollowers==-1 &&  getLen()<1){
            addBytes(nextByte);
            return null;
        }
        else if(numFollowers==-1){
            this.numFollowers=this.bytesToShort();
            return null;
        }
        else if(numFollowing==-1 &&  getLen()<1){
            addBytes(nextByte);
            return null;
        }
        else {
            this.numFollowing=this.bytesToShort();
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
