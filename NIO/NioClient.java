package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socket = SocketChannel.open();
        socket.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(8000);

        if(!socket.connect(address)){
            while(!socket.finishConnect()){

            }
        }

        String str = "Hello world";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        socket.write(buffer);
        Thread.currentThread().sleep(3000);
        socket.close();
    }
}
