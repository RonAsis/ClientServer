package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FollowMessage extends  MessagesClientToServer{

    private int opcode=4;
    private String name;
    private int follow;
    private int numOfUsers;
    private ConcurrentLinkedQueue<String> listUsers;

    public FollowMessage(String name, int follow,int numOfUsers,ConcurrentLinkedQueue<String> list){
        super(new Future<>());
        this.name=name;
        this.follow=follow;
        this.numOfUsers=numOfUsers;
        this.listUsers=list;

    }
    public void excute() {
        if(this.follow==0)
            follow();
        else if(this.follow==1)
            unfollow();
        else
            setResult(new ErrorMessage(this.opcode));//if the nubmer of follow is not 1 or 0
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
            setResult(new ErrorMessage(this.opcode));
        else {// less part of the list is successful
            String optional = "";
            String delimeter = "\0";//between all name;
            for (String key : listUsers) {
                optional = optional + delimeter;
            }
            optional = this.numOfUsers + " " + optional;//need check if need be space of \0**************************************************
            setResult(new AckMessage(this.opcode, optional));
        }
        }
}
