package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.accessories.SharedData;
import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BGSProtocol;
import bgu.spl.net.srv.Server;

public class NewsFeedServerMain {

    public static void main(String[] args) {
        NewsFeed feed = new NewsFeed(); //one shared object

// //you can use any server...
//        Server.threadPerClient(
//                7777, //port
//                () -> new RemoteCommandInvocationProtocol<>(feed), //protocol factory
//                ObjectEncoderDecoder::new //message encoder decoder factory
//        ).serve();

//        Server.reactor(
//                Runtime.getRuntime().availableProcessors(),
//                7777, //port
//                () ->  new RemoteCommandInvocationProtocol<>(feed), //protocol factory
//                ObjectEncoderDecoder::new //message encoder decoder factory
//        ).serve();
//
//        Server.threadPerClient(
//                7777, //port
//                () -> new EchoProtocol(), //protocol factory
//                LineMessageEncoderDecoder::new //message encoder decoder factory
//        ).serve();
        SharedData sharedData=new SharedData() ;

        Server.threadPerClient(
                7777, //port
                () -> new BGSProtocol(sharedData), //protocol factory
                MessageEncoderDecoderImpl::new //message encoder decoder factory
        ).serve();
   }
}
