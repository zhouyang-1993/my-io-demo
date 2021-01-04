package my.io.demo.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhouyang
 */
public class ServerClient {

    public static void main(String[] args) {
        severStart();
    }

    public static void severStart() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);

            Socket socket = serverSocket.accept();

            System.out.println(socket.getPort());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str = bufferedReader.readLine();
            System.out.println("收到了客户端的信息" + str);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("server has accepted.");
            bufferedWriter.flush();
            System.out.println("回写给客户端信息");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}

