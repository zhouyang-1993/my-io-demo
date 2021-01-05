package my.io.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class NioServer {


    public static void main(String[] args) {
        start();
    }


    private static Selector selector;
    /**
     *  channel, buffer, selector
     */
    public static void start(){
        try {

            selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);



            while(true){

                SocketChannel socketChannel = serverSocketChannel.accept();
                if(null != socketChannel){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int i = socketChannel.read(byteBuffer);
                    System.out.printf("服务端接受到：\"%s\"%n", new String(byteBuffer.array(), 0, i));

                    byteBuffer.clear();
                    byteBuffer.put("服务端接受到了".getBytes(StandardCharsets.UTF_8));
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);

                }else{
                    System.out.println("链接未就绪");
                    TimeUnit.SECONDS.sleep(3);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
