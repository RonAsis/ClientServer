package bgu.spl.net.api;

import bgu.spl.net.Messages.*;


public class MessageEncoderDecoderImpl implements MessageEncoderDecoder {

    private byte[] bytesOpcode = new byte[2];
    private short fOpcode=-1;
    private int lenOpcode=0;
    private Message message;
    public final static byte delimeter='\0';

    /**
     * decode the bytes to message
     * @param nextByte the next byte to consider for the currently decoded
     * message
     * @return
     */
    @Override
    public Object decodeNextByte(byte nextByte) {
        if (lenOpcode<1) {
            bytesOpcode[lenOpcode] = nextByte;
            lenOpcode++;
        }
        else if(lenOpcode==1){
            bytesOpcode[lenOpcode] = nextByte;
            fOpcode=bytesToShort(this.bytesOpcode);
            lenOpcode++;
            createMessageAccordingOpcode();
            if(fOpcode==3 || fOpcode==7) {
                Message result=this.message.createMessage(nextByte);// not use in nextByte in this case.
                rest();
                return result;
            }
        }
        else  {
            Message result;
            if(this.message!=null) {
                if ((result = this.message.createMessage(nextByte)) != null)
                    rest();
                return result;
            }
        }

        return null;
    }

    /**
     * encode message to bytes
     * @param message the message to encode
     * @return
     */
    @Override
    public byte[] encode(Object message) {
        Message msc=(Message) message;
        return msc.getBytes();
    }

    /**
     * rest he EncoderDecoder
     */
    private void rest(){
        this.lenOpcode=0;
        this.message=null;

    }

    /**
     * helper for createMessage
     */
    private void createMessageAccordingOpcode(){
        switch (fOpcode){
            case 1:this.message=new RegisterMessage();
                   break;
            case 2:this.message=new MessageLogin();
                   break;
            case 3:this.message=new MessageLogout();
                   break;
            case 4:this.message=new FollowMessage();
                   break;
            case 5:this.message=new PostMessage();
                   break;
            case 6:this.message=new PmMessage();
                   break;
            case 7: this.message=new UserListMessage();
                    break;
            case 8:this.message=new StatMessage();
                    break;
        }
    }

    /**
     * covert bytes to short
     * @param byteArr
     * @return
     */
    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }



}
