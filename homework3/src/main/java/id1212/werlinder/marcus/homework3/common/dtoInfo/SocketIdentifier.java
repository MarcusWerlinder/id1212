package id1212.werlinder.marcus.homework3.common.dtoInfo;

import java.io.Serializable;

public class SocketIdentifier implements Serializable {

    private long userId;

    public SocketIdentifier(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
