package bgu.spl.net.api;

public class MessageEncoderDecoderlmpl implements MessageEncoderDecoder {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int numberParts=1;
    @Override
    public Object decodeNextByte(byte nextByte) {
        return null;
    }

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }
}
