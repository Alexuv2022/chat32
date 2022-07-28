package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(9445);
            System.out.println("������ �������");
            while (true) {
                Socket socket = serverSocket.accept();
                User user = new User(socket);
                users.add(user);
                System.out.println("������ �����������");
                System.out.println(user.getUuid());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            user.getOut().writeUTF("������� ���");
                            user.setName(user.getIs().readUTF());
                            while (true) {
                                String message = user.getIs().readUTF();
                                for (User user1 : users) {
                                    if (!(user.getUuid().equals(user1.getUuid()))) {
                                        user1.getOut().writeUTF(user.getName() + ": " + message);
                                    }
                                }
                                System.out.println(user.getUuid() + ": " + message);
                            }
                        } catch (IOException e) {
                            users.remove(user);
                            System.out.println(user.getUuid() + " ����������");
                            for (User user1 : users) {
                                try {
                                    user1.getOut().writeUTF(user.getName() + " ����������");
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
