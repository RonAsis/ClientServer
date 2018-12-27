package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserListMessage extends  Message {
    String name;

    public  UserListMessage(String name){
        super(7);
        this.name=name;
    }
    @Override
    public short act(SharedData sharedData) {
        List<String> userNameListRegister = sharedData.getUserNameListRegister(this.name);
        if (userNameListRegister.size() == 0) {
            setResult(new ErrorMessage(getOpcode()));
            return -1;
        }
        else {
            {// less part of the list is successful
                setResult(new AckMessage(getOpcode(),(short)userNameListRegister.size(),userNameListRegister));
                return this.getOpcode();
            }

        }
    }
//
//    @Override
//    public String getContainResult() {
//        return null;
//    }

    @Override
    public Message createMessage(byte nextByte) {
        return this;
    }

    @Override
    public byte[] getBytes() {
        return this.shortToBytes(this.getOpcode());
    }
}
