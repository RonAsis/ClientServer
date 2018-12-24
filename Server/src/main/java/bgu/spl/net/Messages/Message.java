package bgu.spl.net.Messages;

public abstract class Message<T> {
        private short opcode;

        public Message(int opcode){
                this.opcode=(short)opcode;
        }
        public short getOpcode(){
                return this.opcode;
        }
        public abstract Message  createMessage(byte nextByte);

}
