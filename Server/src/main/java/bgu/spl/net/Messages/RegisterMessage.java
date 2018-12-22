package bgu.spl.net.Messages;

import bgu.spl.net.Future;
import bgu.spl.net.accessories.SharedData;

public class RegisterMessage extends MessagesClientToServer{

    private int opcode=1;
    private String name;
    private String password;

    public RegisterMessage(String name ,String password){
        super(new Future<>());
        this.name=name;
        this.password=password;
    }
    @Override
    public void excute() {
        SharedData sharedData=SharedData.getInstance();
        if(sharedData.register(this.name,this.password)){
            setResult(new AckMessage(this.opcode,""));
        }
        else{
            setResult(new ErrorMessage(this.opcode));
        }
    }

}
