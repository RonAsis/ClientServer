package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PmMessage extends  MessagesClientToServer {
        int opcode=6;
        String name;
        ConcurrentLinkedQueue<String> listUserS;
        String msg;

    public PmMessage(String name, ConcurrentLinkedQueue<String> list,String msg){
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
