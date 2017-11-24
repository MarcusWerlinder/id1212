package common;

public class Message {
    private String[] splitMsg;

    Message(String unparsedMsg) {
        parse(unparsedMsg);
    }

    private void parse(String unparsedMsg) {
        try {
            splitMsg = unparsedMsg.split(Constants.MSG_TYPE_DELIMITER);
        } catch (Exception e) {
            System.err.println("Unable to parse the following message: " + unparsedMsg);
        }
    }

    public MsgType getType() {
        return MsgType.valueOf(splitMsg[Constants.MSG_TYPE_INDEX].toUpperCase());
    }

    public String getBody() {
        return splitMsg[Constants.MSG_BODY_INDEX];
    }
}
