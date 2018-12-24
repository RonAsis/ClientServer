package bgu.spl.net.Messages;

import bgu.spl.net.Future;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class Message<T> {

        private short opcode;
        private Future<Message> result;
        private byte[] bytes = new byte[1 << 10]; //start with 1k
        private int len = -1;

        public Message(int opcode){
                this.opcode=(short)opcode;
                this.result=new Future<>();
        }

        public short getOpcode(){
                return this.opcode;
        }

        public abstract Message  createMessage(byte nextByte);

        public abstract byte[] getBytes();

        public abstract void  excute();

        public abstract  String  getContainResult();

        public String getResult(){
                return this.result.get().getContainResult();
        }

        public void setResult(Message result) {
                this.result.resolve(result);
        }

        public String popString() {
                //notice that we explicitly requesting that the string will be decoded from UTF-8
                //this is not actually required as it is the default encoding in java.
                String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
                len = 0;
                return result;
        }

        public void addBytes(Byte nextByte){
                len++;
                this.bytes[len]=nextByte;
        }

        public void rest(){
                this.len=-1;
        }

        public void biggerBytes(int size){
                bytes=new byte[size];
        }

        public int getLen(){
                return this.len;
        }

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
    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

}
