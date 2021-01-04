package my.io.demo.bio;

import java.io.*;
import java.net.Socket;

/**
 * @author zhouyang
 */
public class Client {

    public static void main(String[] args) {
        clientDemo();
    }


    public static void clientDemo(){
        Socket socket;
        try {
            socket = new Socket("localhost", 8080);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("I'm clint xxx.\n");
            bufferedWriter.flush();
            System.out.println("发送给服务端信息");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str = bufferedReader.readLine();
            System.out.println("收到了服务端的信息" + str);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
