package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PostMessage extends  MessagesClientToServer {
    int opcode=5;
    String name;
    ConcurrentLinkedQueue<String> listUserS;
    String msg;

    public PostMessage(String name, ConcurrentLinkedQueue<String> list,String msg){
        super(new Future<>());
        this.name=name;
        this.listUserS=list;
        this.msg=msg;
    }

    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();

    }
}
