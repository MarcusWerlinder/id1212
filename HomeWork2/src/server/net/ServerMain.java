package server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.InvalidParameterException;

public class ServerMain {

    private static final int LINGER_TIME = 5000;

    private ServerSocketChannel listeningSocketChannel;
    private static final int portNo = 8080;
    private Selector selector;

    void addPendingMsg(SelectionKey channelKey) {
        if (channelKey == null) throw new InvalidParameterException("The channel key is not defined");

        if(!channelKey.isValid() || !(channelKey.channel() instanceof SocketChannel)) throw new InvalidParameterException("Your channel key is bad");

        channelKey.interestOps(SelectionKey.OP_WRITE);
    }

    public static void start() {
        ServerMain server = new ServerMain();
        try {
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serve() throws IOException {
        initSelector();
        initListeningSocketChannel();

        while (true) {
            selector.select();
            for (SelectionKey key : selector.selectedKeys()){
                selector.selectedKeys().remove(key);
                if(!key.isValid()) {
                    continue;
                }
                if(key.isAcceptable()){
                    startHandler(key);
                } else if (key.isWritable()) {
                    sendToClient(key);
                } else if (key.isReadable()) {
                    recvFromClient(key);
                }
            }
        }
        
    }

    private void sendToClient(SelectionKey key) throws IOException{
        ClientHandler clientHandler = (ClientHandler) key.attachment();

        clientHandler.sendMessage();
        key.interestOps(SelectionKey.OP_READ);
    }

    private void recvFromClient(SelectionKey key) throws IOException {
        ClientHandler client = (ClientHandler) key.attachment();
        try {
            client.recvMsg();
        } catch (IOException clientClosedConn) {
            System.out.println("The client seems to have closed their connection");
            removeClient(key);
        }
    }

    private void startHandler(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        ClientHandler handler = new ClientHandler(this, clientChannel);
        handler.registerKey(clientChannel.register(selector, SelectionKey.OP_READ, handler));
        clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
    }

    private void initListeningSocketChannel() throws IOException {
        listeningSocketChannel = ServerSocketChannel.open();
        listeningSocketChannel.configureBlocking(false);
        listeningSocketChannel.bind(new InetSocketAddress(portNo));
        listeningSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

    private void initSelector() throws IOException {
        selector = Selector.open();
    }

    private void removeClient(SelectionKey key) throws IOException {
        ClientHandler clientHandler = (ClientHandler) key.attachment();
        clientHandler.disconnectClient();
        key.cancel();
    }

    void wakeup() {
        selector.wakeup();
    }
}
