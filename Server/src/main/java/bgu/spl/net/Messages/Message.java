package bgu.spl.net.Messages;

import bgu.spl.net.accessories.SharedData;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class Message<T> {

        private short opcode;
        private Message result;
        private byte[] bytes = new byte[1 << 13]; //start with 8k
        private int len = 0;

    /**
     * constructor
     * @param opcode
     */
        public Message(int opcode){
                this.opcode=(short)opcode;
        }

    /**
     * return the opcode of the message
     * @return
     */
        public short getOpcode(){
                return this.opcode;
        }

    /**
     * create message -all message from the client to the server needs implements this
     * @param nextByte
     * @return
     */
        public  Message  createMessage(byte nextByte){return null; }

    /**
     * @return bytes of this object all message from the server to client needs implements this
     */
        public  byte[] getBytes(){
            return null;
        }

    /**
     * do act -all messages of the client to server need implement this
     * @param sharedData
     * @return
     */
        public  abstract short  act(SharedData sharedData,String name);

    /**
     * return the result
     * @return
     */
        public Message  getContainResult(){
                return this.result;
        }

    /**
     * set the result of the act
     * @param result
     */
        public void setResult(Message result) {
                this.result=result;
        }

    /**
     * convert bytes to string
     * @return
     */
        public String popString() {
                //notice that we explicitly requesting that the string will be decoded from UTF-8
                //this is not actually required as it is the default encoding in java.
                String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
                len = 0;
                return result;
        }

    /**
     * add byte to the array of bytes
     * @param nextByte
     */
        public void addBytes(Byte nextByte){
            if (len >= bytes.length) {
                bytes = Arrays.copyOf(bytes, len * 2);
            }

            bytes[len++] = nextByte;
        }

    /**
     * do that len be on zero
     */
    public void rest(){
                this.len=0;
        }

    /**
     * @return len
     */
        public int getLen(){
                return this.len;
        }

    /**
     * convert short to bytes
     * @param num
     * @return
     */
        public byte[] shortToBytes(short num)
        {
                byte[] bytesArr = new byte[2];
                bytesArr[0] = (byte)((num >> 8) & 0xFF);
                bytesArr[1] = (byte)(num & 0xFF);
                return bytesArr;
        }

    /**
     * merge two arrays of bytes
     * @param a
     * @param b
     * @return
     */
        public byte[] mergeTwoArraysOfBytes(byte[] a,byte[] b){
                int lenA = a.length;
                int lenB = b.length;
                byte[] c = Arrays.copyOf(a, lenA + lenB);
                System.arraycopy(b, 0, c, lenA, lenB);
                return c;
        }

    /**
     * convert bytes to short
      * @return
     */
    public short bytesToShort()
    {
        byte[] byteArr=this.bytes;
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        rest();
        return result;
    }

    /**
     * add one bytes to full array of byte
     * @param a
     * @param b
     * @return
     */
    public byte[]addByteToArray(byte[] a,byte b){
            byte[] c = Arrays.copyOf(a, a.length+1);
            c[a.length]=b;
            return c;
    }
}
