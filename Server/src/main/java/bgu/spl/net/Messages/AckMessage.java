package bgu.spl.net.Messages;
import bgu.spl.net.accessories.SharedData;

import java.util.ArrayList;
import java.util.List;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class AckMessage  extends Message {

    private short messageOpcode=-1;//also use for stat opcode
    private short numOfUsers=-1;
    private List<String> userNameList=new ArrayList<>();
    private short numPosts=-1;
    private short numFollowers=-1;
    private short numFollowing=-1;
    private String nameUser;

    /**
    constructor
     */
    public AckMessage(int messageOpcode ){
        super(10);
        this.messageOpcode=(short)messageOpcode;
    }

    /**
     * constructor
     * @param messageOpcode
     * @param numOfUsers
     * @param userNameList
     */
    public AckMessage(short messageOpcode,short numOfUsers,List<String> userNameList){
        super(10);
        this.messageOpcode=messageOpcode;
        this.numOfUsers=numOfUsers;
        this.userNameList=userNameList;
    }

    /**
     * constructor
     * @param messageOpcode
     * @param numPosts
     * @param numFollowers
     * @param numFollowing
     */
    public AckMessage(short messageOpcode,short numPosts,short numFollowers, short numFollowing){
        super(10);
        this.messageOpcode=messageOpcode;
        this.numPosts=numPosts;
        this.numFollowers=numFollowers;
        this.numFollowing=numFollowing;
    }

    /**
     * return the bytes of the object
     * @return
     */
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

    @Override
    public short act(SharedData sharedData, String name) {
        return this.getOpcode();
    }


    /**
     * method help for get bytes opcode 8
     * @return
     */
    private byte[] opcodeOprtionalBytes8(){
        return  mergeTwoArraysOfBytes(mergeTwoArraysOfBytes(this.shortToBytes(this.numOfUsers),this.shortToBytes(this.numFollowers)),this.shortToBytes(this.numFollowing));
    }


    /**
     * method help for get bytes opcode 7 and 4
     * @return
     */
    private byte[] opcodeOprtionalBytes7And4(){
            byte []numOfUsersByte=this.shortToBytes(this.numOfUsers);
            return mergeTwoArraysOfBytes(numOfUsersByte,createBytesFromList());
        }


    /**
     * method help for get bytes from list
     * @return
     */
     private byte[] createBytesFromList(){
        byte []result=new byte[0];
        for(String key:this.userNameList){
            result=mergeTwoArraysOfBytes(result,key.getBytes());
            result=addByteToArray(result,delimeter);
        }
        return  result;
        }

    /**
     * get the opcode of the message
      * @return
     */
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
