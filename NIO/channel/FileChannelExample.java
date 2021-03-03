package nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelExample {
    public static void main(String[] args) throws IOException {
        File file1 = new File("D://result.txt");
        File file2 = new File("D://2.txt");

        FileInputStream inputStream = new FileInputStream(file1);
        FileChannel fileChannel1 = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream(file2);
        FileChannel fileChannel2 = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while(fileChannel1.read(buffer) != -1){
            buffer.flip();
            fileChannel2.write(buffer);
            buffer.clear();
        }
    }
}
