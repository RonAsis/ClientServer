package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

public class MessageLogin extends MessagesClientToServer {

    private int opcode=2;
    private String name;
    private String password;

    public MessageLogin(String name,String password){
        super(new Future<>());
        this.name=name;
        this.password=password;
    }
    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        if(sharedData.login(this.name,this.password)){
            setResult(new AckMessage(this.opcode,""));
        }
        else{
            setResult(new ErrorMessage(this.opcode));
        }
    }
}
