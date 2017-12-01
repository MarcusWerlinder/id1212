package id1212.werlinder.marcus.homework3.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileHandler {
    private static final int bufferSize = 1024;

    /**
     * Handels incoming files from the TCP socket
     */
    public static void recievingFile(SocketChannel channel, Path path, long size) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE))) {

            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

            long recievedBytes = 0;

            while (recievedBytes < size) {
                long readChannel = channel.read(buffer);
                if(readChannel <= 0) break;
                recievedBytes += readChannel;

                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
        }
    }

    /**
     * For the server/client when we need to send files on the TCP socket
     */
    public static void sendFile(SocketChannel channel, Path path) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(path)) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
            }
        }
    }
}
