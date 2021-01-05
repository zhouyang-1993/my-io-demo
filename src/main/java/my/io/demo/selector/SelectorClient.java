package my.io.demo.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SelectorClient {


    public static void main(String[] args) {
        start();
    }


    private static Selector selector;

    public static void start(){

        try {
            selector = Selector.open();

            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", 8080));

            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while(true){
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while(keyIterator.hasNext()){
                    SelectionKey selectionKey = keyIterator.next();
                    keyIterator.remove();
                    if(selectionKey.isConnectable()){
                        handlerConnect(selectionKey);
                    }else if(selectionKey.isReadable()){
                        handlerRead(selectionKey);
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void handlerRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int i = socketChannel1.read(byteBuffer);
        String msg = new String(byteBuffer.array(), 0, i);
        System.out.printf("接收到服务端返回的信息%s%n", msg);
    }

    private static void handlerConnect(SelectionKey selectionKey) throws IOException, InterruptedException {
        SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();

        socketChannel1.finishConnect();

        System.out.println("connect and sleep.");
        TimeUnit.SECONDS.sleep(5);
        socketChannel1.write(ByteBuffer.wrap("I'm client xxx1".getBytes(StandardCharsets.UTF_8)));
        System.out.println("write over");
        socketChannel1.register(selector, SelectionKey.OP_READ);
    }
}
