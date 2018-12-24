package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    public void excute() {
        if(this.follow==0)
            follow();
        else if(this.follow==1)
            unfollow();
        else
            setResult(new ErrorMessage(getOpcode()));//if the nubmer of follow is not 1 or 0
    }

//    @Override
//    public String getContainResult() {
//        return this.getResult();
//    }

    private void follow(){
        SharedData sharedData=SharedData.getInstance();
        decideResult(sharedData.followUser(this.listUsers,this.name));
    }
    private void unfollow() {
        SharedData sharedData = SharedData.getInstance();
        decideResult(sharedData.unFollowUser(this.listUsers, this.name));
    }
    private void decideResult(List<String> listSucceful){
        if (listSucceful.size() == 0)
            setResult(new ErrorMessage(getOpcode()));
        else {// less part of the list is successful
            String optional = "";
            String delimeter = "\0";//between all name;
            for (String key : listUsers) {
                optional = optional + delimeter;
            }
            optional = this.numOfUsers + " " + optional;//need check if need be space of \0**************************************************
            setResult(new AckMessage(getOpcode(), optional));
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
