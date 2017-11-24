package common;

import java.util.ArrayDeque;
import java.util.Queue;

public class MessageContr {
    private StringBuilder receivedChars = new StringBuilder();
    private final Queue<Message> messagesQ = new ArrayDeque<>();

    public synchronized void addMessage(String receivedMsg) {
        receivedChars.append(receivedMsg);
        while (extractMsg());
    }

    private boolean extractMsg() {
        String nowRecChars = receivedChars.toString();
        String[] splitMsgAtHead = nowRecChars.split(Constants.MSG_LEN_DELIMITER);

        if(splitMsgAtHead.length < 2) return false; //The whole msg hasen't been sent

        int msgLen = Integer.parseInt(splitMsgAtHead[0]);
        if(!isMsgDone(msgLen, splitMsgAtHead[1])) return false; //Msg is not as long as we say it is

        String msg = splitMsgAtHead[1].substring(0, msgLen);
        messagesQ.add(new Message(msg)); //Add message to que

        receivedChars.delete(0, nowRecChars.length()); //We have extracted the message so delete it

        return true;
    }

    //Is the msg as long as we say it is?
    private boolean isMsgDone(int msgLen, String msg) {
        return msg.length() >= msgLen;
    }

    public synchronized boolean hasNext() {
        return !messagesQ.isEmpty();
    }

    public synchronized Message nextMsg() {
        return messagesQ.poll();
    }
}
