package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.api.MessageEncoderDecoderlmpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocollmpl;
import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
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

        Server.threadPerClient(
                7777, //port
                () -> new BidiMessagingProtocollmpl(), //protocol factory
                MessageEncoderDecoderlmpl::new //message encoder decoder factory
        ).serve();
   }
}
