package bgu.spl.net.impl.echo;

import bgu.spl.net.impl.newsfeed.PublishNewsCommand;
import bgu.spl.net.impl.rci.RCIClient;

import java.io.*;
import java.net.Socket;

public class EchoClient {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            args = new String[]{"localhost", "hello"};
        }

        if (args.length < 2) {
            System.out.println("you must supply two arguments: host, message");
            System.exit(1);
        }

        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        try (Socket sock = new Socket(args[0], 7777);
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()),true)) {

            System.out.println("sending message to server");
            //out.write(args[1]);
            out.println(args[1]);
            //out.newLine();
            //out.flush();

            System.out.println("awaiting response");
            String line = in.readLine();
            System.out.println("message from server: " + line);
        }
    }
}
