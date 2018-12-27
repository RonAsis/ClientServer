package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.Messages.*;
import bgu.spl.net.impl.rci.RCIClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NewsFeedClientMain { public  static RCIClient c;
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            args = new String[]{"127.0.0.1"};
        }

//        System.out.println("running clients");
        runFirstClient(args[0]);
        //runThirdClient(args[0]);
    }

    private static void runFirstClient(String host) throws Exception {
        try (RCIClient c = new RCIClient(host, 7777)) {
            ///////////register1//////////////////////
            RegisterMessage rm=new RegisterMessage("Ron1","123456");
            c.send(rm);
            Message message = c.receive();
            System.out.println("client 1:"+message.toString());

            ///////////register2//////////////////////
        RegisterMessage rm1=new RegisterMessage("Ron2","123456");
            c.send(rm1);
             message = c.receive();
            System.out.println("client 1:"+message.toString());

            ///////////register3//////////////////////
            RegisterMessage rm3=new RegisterMessage("Ron3","123456");
            c.send(rm3);
            message = c.receive();
            System.out.println("client 1:"+message.toString());

            ///////////register1//////////////////////
            c.send(rm);
             message = c.receive();
            System.out.println("client 1:"+message.toString());

//            ///////////login ::Ron1 ,123456////////////////////////
//            MessageLogin ml=new MessageLogin("Ron1","123456");
//            c.send(ml);
//             message = c.receive();
//            System.out.println("client 1:"+message.toString());
//
//            ///////////logout :Ron1 ,123456/////////////////////
//            MessageLogout mOut=new MessageLogout();
//            c.send(mOut);
//            message = c.receive();
//            System.out.println("client 1:"+message.toString());
//
//            ///////////logout :Ron1 ,123456/////////////////////
//            c.send(mOut);
//            message = c.receive();
//            System.out.println("client 1:"+message.toString());

            //////////login ::Ron1 ,123456////////////////////////
                     MessageLogin ml=new MessageLogin("Ron1","123456");

            c.send(ml);
            message = c.receive();
            System.out.println("client 1:"+message.toString());

            //////////FOLLOW : Ron2,Ron3//////////////////////
            List<String> list1=new ArrayList<>();
            list1.add("Ron2");
            list1.add("Ron3");
            FollowMessage fm=new FollowMessage((byte)0, (short)list1.size(),list1);
            c.send(fm);
            message = c.receive();
            System.out.println("client 1:"+message.toString());

                runSecondClient("127.0.0.1");
                message = c.receive();
                System.out.println("client 1:" + message.toString());
            message = c.receive();
            System.out.println("client 1:" + message.toString());
            message = c.receive();
            System.out.println("client 1:" + message.toString());
            message = c.receive();
            System.out.println("client 1:" + message.toString());

            }



    }

    private static void runSecondClient(String host ) throws Exception {
        try (RCIClient c1 = new RCIClient(host, 7777)) {
            ///////////register5//////////////////////
            RegisterMessage rm1=new RegisterMessage("Ron5","123456");
            c1.send(rm1);
            Message message = c1.receive();
            System.out.println("client 2:"+message.toString());

            //////////login ::Ron5, 1234567/////////////////
            MessageLogin ml=new MessageLogin("Ron5","123456");
            c1.send(ml);
            message = c1.receive();
            System.out.println("client 2:"+message.toString());

            //////////FOLLOW : Ron1//////////////////////
            List<String> list1=new ArrayList<>();
            list1.add("Ron1");
            FollowMessage fm=new FollowMessage((byte)0, (short)list1.size(),list1);
            c1.send(fm);
            message = c1.receive();
            System.out.println("client 2:"+message.toString());

            /////////Post Message "I am run!!!"///////////////

//            Message message1=c1.receive();
//            System.out.println("client 2:"+message.toString());
            String []msg1={"I","am","want","done"};
            PostMessage postMessage=new PostMessage("Ron5",msg1[0]);
            c1.send(postMessage);
            int i=1;
            while (i<msg1.length) {
                 postMessage=new PostMessage("Ron5",msg1[i]);
                c1.send(postMessage);
                i++;
            }
        }
    }
//
//    private static void runThirdClient(String host) throws Exception {
//        try (RCIClient c = new RCIClient(host, 7777)) {
//            c.send(new FetchNewsCommand("headlines"));
//            System.out.println("third client received: " + c.receive());
//        }
//    }
}
