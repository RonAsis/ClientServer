package bgu.spl.net.impl.newsfeed;

import bgu.spl.net.Messages.Message;
import bgu.spl.net.Messages.RegisterMessage;
import bgu.spl.net.impl.rci.RCIClient;

public class NewsFeedClientMain {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            args = new String[]{"127.0.0.1"};
        }

//        System.out.println("running clients");
        runFirstClient(args[0]);
        //runSecondClient(args[0]);
        //runThirdClient(args[0]);
    }

    private static void runFirstClient(String host) throws Exception {
        RegisterMessage rm=new RegisterMessage("Ron1","123456");
        try (RCIClient c = new RCIClient(host, 7777)) {
            c.send(rm);
            Message message = c.receive();
            System.out.println(message.toString());
        RegisterMessage rm1=new RegisterMessage("Ron2","123456");
            c.send(rm1);
             message = c.receive();
            System.out.println(message.toString());
            c.send(rm);
             message = c.receive();
            System.out.println(message.toString());
//            c.receive(); //o
//
//            c.send(new PublishNewsCommand(
//                    "headlines",
//                    "new SPL assignment is out soon!!"));
//
//            c.receive(); //ok
//
//            c.send(new PublishNewsCommand(
//                    "headlines",
//                    "THE CAKE IS A LIE!"));
//
//            c.receive(); //ok
        }

    }

//    private static void runSecondClient(String host) throws Exception {
//        try (RCIClient c = new RCIClient(host, 7777)) {
//            c.send(new FetchNewsCommand("jobs"));
//            System.out.println("second client received: " + c.receive());
//        }
//    }
//
//    private static void runThirdClient(String host) throws Exception {
//        try (RCIClient c = new RCIClient(host, 7777)) {
//            c.send(new FetchNewsCommand("headlines"));
//            System.out.println("third client received: " + c.receive());
//        }
//    }
}
