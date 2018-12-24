package bgu.spl.net.Messages;

import java.util.Arrays;

public abstract class MessagesServerToClient extends Message {

    MessagesServerToClient(int opcode){
        super(opcode);
    }

    public abstract  String  getContainResult();

    public abstract  byte[] getBytes();

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
    public byte[] mergeTwoArraysOfBytes(byte[] a,byte[] b){
        int lenA = a.length;
        int lenB = b.length;
        byte[] c = Arrays.copyOf(a, lenA + lenB);
        System.arraycopy(b, 0, c, lenA, lenB);
        return c;
    }


}
