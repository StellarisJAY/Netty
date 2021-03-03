package nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileChannelCopyExample {
    public static void main(String[] args) throws IOException {
        File file = new File("D://test.jpg");

        FileInputStream inputStream = new FileInputStream(file);
        FileChannel channel1 = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("D://copy.png");
        FileChannel channel2 = outputStream.getChannel();

        channel2.transferFrom(channel1, 0, channel1.size());

        inputStream.close();
        outputStream.close();
    }
}
