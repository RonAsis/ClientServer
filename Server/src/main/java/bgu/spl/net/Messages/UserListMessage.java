package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import java.util.List;

public class UserListMessage extends  Message {
    String userName;

    /**
     *constructor
     * @param
     */
    public  UserListMessage(){
        super(7);
    }

    /**
     * do the action of this message
     * @param sharedData
     * @return
     */
    @Override
    public short act(SharedData sharedData,String name) {
        this.userName=name;
        List<String> userNameListRegister = sharedData.getUserNameListRegister(this.userName);
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

    /**
     * for decode the mesage that get from the client
     * @param nextByte
     * @return
     */
    @Override
    public Message createMessage(byte nextByte) {
        return this;
    }

}
