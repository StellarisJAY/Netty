package nio.channel;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;

public class GatheringExample {
    public static void main(String[] args) throws IOException {
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(3);
        buffers[1] = ByteBuffer.allocate(2);

        FileInputStream inputStream = new FileInputStream("D://test.txt");
        FileChannel fileChannel = inputStream.getChannel();

        fileChannel.read(buffers);
        Arrays.asList(buffers).forEach(buffer -> {
            buffer.clear();
        });

        Arrays.asList(buffers).forEach(buffer -> {
            System.out.println(buffer);
            System.out.println(Arrays.toString(buffer.array()));
        });
    }
}
