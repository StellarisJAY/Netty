import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ChatClient {
    private static final String IP = "182.92.116.152";
    private static final int PORT = 25510;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public ChatClient() {
        try{
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(IP, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws IOException {
        message = username + " : " + message;
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        socketChannel.write(buffer);
    }

    private void readMessage() throws IOException{
        if(selector.select(500) > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    System.out.println(new String(buffer.array()).trim());
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();

        new Thread(){
            @Override
            public void run() {
                while(true){
                    try {
                        chatClient.readMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String message = scanner.nextLine();
            try {
                chatClient.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
