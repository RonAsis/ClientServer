package bgu.spl.net.impl.rci;

import bgu.spl.net.Messages.Message;
import bgu.spl.net.Messages.MessagesServerToClient;
import bgu.spl.net.api.MessageEncoderDecoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class RCIClient implements Closeable {

    private final MessageEncoderDecoder encdec;
    private final Socket sock;
    private final BufferedInputStream in;
    private final BufferedOutputStream out;

    public RCIClient(String host, int port) throws IOException {
        sock = new Socket(host, port);
        encdec = new ObjectEncoderDecoder();
        in = new BufferedInputStream(sock.getInputStream());
        out = new BufferedOutputStream(sock.getOutputStream());
    }

    public void send(String message) throws IOException {
        out.write(encdec.encode((Object)message));
        out.flush();
    }

    public MessagesServerToClient receive() throws IOException {
        int read;
        while ((read = in.read()) >= 0) {
            Object msg = encdec.decodeNextByte((byte) read);
            if (msg != null) {
                return (MessagesServerToClient)msg;
            }
        }

        throw new IOException("disconnected before complete reading message");
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        sock.close();
    }

}
