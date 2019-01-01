package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.accessories.SharedData;
import bgu.spl.net.api.MessageEncoderDecoderlmpl;
import bgu.spl.net.api.bidi.BGSProtocol;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    /**
     * for run the Reactor server
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port=0;
        int noOfThreads=0;

        if(args.length>1){
            port=Integer.parseInt(args[0]);
            noOfThreads=Integer.parseInt(args[1]);
        }
        else{
            throw  new Exception("Needs enter 2 arguments");
        }
        SharedData sharedData=new SharedData();
        Server.reactor(
                noOfThreads
                ,port, //port
                () -> new BGSProtocol(sharedData), //protocol factory
                MessageEncoderDecoderlmpl::new //message encoder decoder factory
        ).serve();

    }
}
