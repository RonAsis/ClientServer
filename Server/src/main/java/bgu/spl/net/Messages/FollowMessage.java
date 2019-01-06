package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import java.util.ArrayList;
import java.util.List;

import static bgu.spl.net.api.MessageEncoderDecoderImpl.delimeter;

public class FollowMessage extends  Message{

    private String name;
    private byte follow=-1;
    private short numOfUsers=-1;
    private List<String> listUsers;

    /**
     * constructor
     * @param
     */
    public FollowMessage(){
        super(4);
        listUsers=new ArrayList<>();

    }

    /**
     * do the action of this message
     * @param sharedData
     * @return
     */
    public short act(SharedData sharedData,String name) {
        this.name=name;
        if(name!=null) {
            if (this.follow == 0) {
                follow(sharedData);
                return this.getOpcode();
            } else if (this.follow == 1) {
                unfollow(sharedData);
                return this.getOpcode();
            }
        }
      //  else {
            setResult(new ErrorMessage(getOpcode()));//if the nubmer of follow is not 1 or 0
            return -1;
       // }
    }


    /**
     * helper for follow
     * @param sharedData
     */
    private void follow(SharedData sharedData){
        decideResult(this.listUsers=sharedData.followUser(this.listUsers,this.name));
    }

    /**
     * help for unfollow
     * @param sharedData
     */
    private void unfollow(SharedData sharedData) {
        decideResult(this.listUsers=sharedData.unFollowUser(this.listUsers, this.name));
    }

    /**
     * decide if this is Error or ACK
     * @param listSucceful
     */
    private void decideResult(List<String> listSucceful){
        if (listSucceful.size() == 0)
            setResult(new ErrorMessage(getOpcode()));
        else {// less part of the list is successful
           this.numOfUsers=(short)this.listUsers.size();
            setResult(new AckMessage(getOpcode(),this.numOfUsers,this.listUsers));
        }
        }

    /**
     * use for create message from bytes
     * @param nextByte
     * @return
     */
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
}
