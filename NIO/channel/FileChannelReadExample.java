package nio.channel;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelReadExample {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("D://test.txt");

        FileChannel fileChannel = inputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        fileChannel.read(buffer);

        buffer.flip();
        System.out.println(new String(buffer.array(), "UTF-8"));
        inputStream.close();
    }
}
