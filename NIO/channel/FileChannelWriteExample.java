package nio.channel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelWriteExample {
    public static void main(String[] args) throws IOException {
        String str = "Hello world";
        File file1 = new File("D://test.txt");

        FileOutputStream outputStream = new FileOutputStream(file1);

        // 实际类型是FileChannelImpl
        FileChannel fileChannel = outputStream.getChannel();
        // 创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 将string写入buffer
        buffer.put(str.getBytes());
        // 写入完成后反转buffer
        buffer.flip();

        // 将buffer的数据写入channel
        fileChannel.write(buffer);

        outputStream.close();
    }
}
