package my.io.demo.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SelectorServer {

    public static void main(String[] args) {
        start();
    }



    private static Selector selector;

    public static void start(){
        try {

            selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while(true){
                System.out.println("进入selector阻塞");
                selector.select();
                System.out.println("有事件进入");

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while(keyIterator.hasNext()){
                    SelectionKey selectionKey = keyIterator.next();
                    keyIterator.remove();
                    if(selectionKey.isAcceptable()){
                        handlerAccept(selectionKey);
                    }else if(selectionKey.isReadable()){
                        handlerRead(selectionKey);
                    }
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handlerRead(SelectionKey selectionKey) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handlerAccept(SelectionKey selectionKey) throws IOException {
       ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

       SocketChannel socketChannel = serverSocketChannel.accept();
       ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int i = socketChannel.read(buffer);
            String msg = new String(buffer.array(), 0, i);
            System.out.println("接收到client信息：" + msg);
            socketChannel.write(ByteBuffer.wrap(("I've accepted your msg : " + msg).getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
