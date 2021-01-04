package my.io.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class NioClient {

    public static void main(String[] args) {
        start();
    }

    public static void start(){

        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", 8080));
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while(true){
                if(socketChannel.isConnectionPending()){
                    socketChannel.finishConnect();
                    byteBuffer.put("I'm nio client 1.".getBytes(StandardCharsets.UTF_8));
                    byteBuffer.flip();

                    socketChannel.write(byteBuffer);
                    break;
                }else{
                    System.out.println("链接未就绪");
                    TimeUnit.SECONDS.sleep(3);
                }
            }
            //
            byteBuffer.clear();
            while (true){
                int i = socketChannel.read(byteBuffer);
                if(i > 0){
                    String serverMsg = new String(byteBuffer.array(), 0, i);
                    System.out.printf("接收到服务端的返回信息:\"%s\"%n", serverMsg);
                    break;
                }else{
                    System.out.println("未获取到返回值");
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
