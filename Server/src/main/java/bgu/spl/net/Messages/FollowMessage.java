package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

import static bgu.spl.net.api.MessageEncoderDecoderlmpl.delimeter;

public class FollowMessage extends  Message{

    private String name="";
    private int follow=-1;
    private int numOfUsers=-1;
    private ConcurrentLinkedQueue<String> listUsers;

    public FollowMessage(String name){
        super(4);
        this.name=name;
        listUsers=new ConcurrentLinkedQueue<>();

    }
    public void excute() {
        if(this.follow==0)
            follow();
        else if(this.follow==1)
            unfollow();
        else
            setResult(new ErrorMessage(getOpcode()));//if the nubmer of follow is not 1 or 0
    }

    @Override
    public String getContainResult() {
        return this.getResult();
    }

    private void follow(){
        SharedData sharedData=SharedData.getInstance();
        dicideResult(sharedData.followUser(this.listUsers,this.name));
    }
    private void unfollow() {
        SharedData sharedData = SharedData.getInstance();
        dicideResult(sharedData.unFollowUser(this.listUsers, this.name));
    }
    private void dicideResult(ConcurrentLinkedQueue<String> listSucceful){
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
        if(follow==-1){
            addBytes(nextByte);
            this.follow=Integer.parseInt(this.popString());
            this.rest();
            return null;
        }
        else if (getLen()==0 && numOfUsers==-1){
            addBytes(nextByte);
            return null;}
         else if (getLen()==1 && numOfUsers==-1){
             addBytes(nextByte);
             this.numOfUsers=Integer.parseInt(popString());
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

        return new byte[0];
    }
}
