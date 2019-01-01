package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.accessories.SharedData;
import bgu.spl.net.api.MessageEncoderDecoderlmpl;
import bgu.spl.net.api.bidi.BGSProtocol;
import bgu.spl.net.srv.Server;

public class TPCMain {

    /**
     * for run the server per client
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)throws Exception  {
        int port;
        if(args.length>0){
            port=Integer.parseInt(args[0]);
        }
        else{
            throw  new Exception("Needs enter 1 argument");
        }
        SharedData sharedData=new SharedData();
        Server.threadPerClient(
                port, //port
                () -> new BGSProtocol(sharedData), //protocol factory
                MessageEncoderDecoderlmpl::new //message encoder decoder factory
        ).serve();
    }
}
