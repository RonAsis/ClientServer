package bgu.spl.net.impl.echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static bgu.spl.net.api.MessageEncoderDecoderImpl.delimeter;

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
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {

            short s=1;
            String register=s+"ronasis"+delimeter+"123456"+delimeter;
            out.write(register);
            out.newLine();
            out.flush();

            System.out.println("awaiting response");
            String line = in.readLine();
//            System.out.println("message from server: " + line);
//            System.out.println("-----------------------------------");
//            System.out.println("sending message to server");
//            out.write("2"+"How Are you ?");
//            out.newLine();
//            out.flush();
//            System.out.println("awaiting response");
//            line = in.readLine();
//            System.out.println("message from server: " + line);
//            System.out.println("-----------------------------------");
//            System.out.println("sending message to server");
//            out.write("3"+"I do Spl3 now");
//            out.newLine();
//            out.flush();
//            System.out.println("awaiting response");
//            line = in.readLine();
//            System.out.println("message from server: " + line);


//            while ((line = in.readLine()) != null) {
//                System.out.println("echo: " + line);
//                out.write(line);
//                out.newLine();
//                out.flush();
//            }

        }
    }
}
