package common;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

/**
 * Creates streamable messages.
 */
public class MessageCreator {
    /**
     * Wraps a message type and body into a streamable format.
     *
     * @param type The type of the message.
     * @param body The body of the message.
     * @return <code>ByteBuffer</code> with the encapsulated message.
     */
    public static ByteBuffer createMsg(MsgType type, String body) {
        StringJoiner joiner = new StringJoiner(Constants.MSG_TYPE_DELIMITER);
        joiner.add(type.toString());
        joiner.add(body);

        String message = prependLengthHeader(joiner.toString());

        return ByteBuffer.wrap(message.getBytes());
    }

    private static String prependLengthHeader(String message) {
        StringJoiner joiner = new StringJoiner(Constants.MSG_LEN_DELIMITER);
        joiner.add(Integer.toString(message.length()));
        joiner.add(message);

        return joiner.toString();
    }
}
