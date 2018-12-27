package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import java.util.ArrayList;
import java.util.List;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class FollowMessage extends  Message{

    private String name="";
    private byte follow=-1;
    private short numOfUsers=-1;
    private List<String> listUsers;

    public FollowMessage(String name){
        super(4);
        this.name=name;
        listUsers=new ArrayList<>();

    }
    public FollowMessage(byte follow,short numOfUsers,List<String> list){
        super(4);
        this.listUsers=list;
        this.follow=follow;
        this.numOfUsers=numOfUsers;
    }

    public short act(SharedData sharedData) {
        if(this.follow==0) {
            follow(sharedData);
            return this.getOpcode();
        }
        else if(this.follow==1){
            unfollow(sharedData);
            return this.getOpcode();
        }
        else {
            setResult(new ErrorMessage(getOpcode()));//if the nubmer of follow is not 1 or 0
            return -1;
        }
    }

//    @Override
//    public String getContainResult() {
//        return this.getResult();
//    }

    private void follow(SharedData sharedData){
        decideResult(this.listUsers=sharedData.followUser(this.listUsers,this.name));
    }
    private void unfollow(SharedData sharedData) {
        decideResult(this.listUsers=sharedData.unFollowUser(this.listUsers, this.name));
    }
    private void decideResult(List<String> listSucceful){
        if (listSucceful.size() == 0)
            setResult(new ErrorMessage(getOpcode()));
        else {// less part of the list is successful
            setResult(new AckMessage(getOpcode(),this.numOfUsers,this.listUsers));
        }
        }


    @Override
    public Message createMessage(byte nextByte) {
        if(this.follow==-1){
            this.follow=nextByte;
            return null;
        }
        else if (getLen()==0 && numOfUsers==-1){
            addBytes(nextByte);
            return null;}
         else if (getLen()==1 && numOfUsers==-1){
             addBytes(nextByte);
             this.numOfUsers=this.bytesToShort();
             rest();
             return null;
            }
         else if (this.listUsers.size()+1<numOfUsers){
             if (nextByte!=delimeter){
                 addBytes(nextByte);
                 return null;
             }
             this.listUsers.add(popString());
             rest();
            return null;
        }
        else{
            if (nextByte!=delimeter){
                addBytes(nextByte);
                return null;
            }
            this.listUsers.add(popString());
            rest();
            return  this;
        }

    }

    @Override
    public byte[] getBytes() {
        byte[] opcodeByte=this.shortToBytes(this.getOpcode());
        opcodeByte=this.addByteToArray(opcodeByte,this.follow);
        byte[] numOfUsersBytes=shortToBytes(this.numOfUsers);
        byte[] result=mergeTwoArraysOfBytes(opcodeByte,numOfUsersBytes);
        for(int i=0;i<listUsers.size();i++){
            result=mergeTwoArraysOfBytes(result,this.listUsers.get(i).getBytes());
            result=addByteToArray(result,delimeter);
        }
        return result;
    }
}
