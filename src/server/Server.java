package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9445);
            System.out.println("������ �������");
            serverSocket.accept();
            System.out.println("������ �����������");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}