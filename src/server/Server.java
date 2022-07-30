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
            System.out.println("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                User currentUser = new User(socket);
                users.add(currentUser);
                System.out.println("Клиент подключился");
                System.out.println(currentUser.getUuid());
                User.whoIsOnline(users);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            currentUser.getOut().writeUTF("Введите имя");
                            currentUser.setName(currentUser.getIs().readUTF());
                            User.whoIsOnline(users);
                            while (true) {
                                String message = currentUser.getIs().readUTF();
                                for (User user1 : users) {
                                    if (!(currentUser.getUuid().toString().equals(user1.getUuid().toString()))) {
                                        user1.getOut().writeUTF(currentUser.getName() + ": " + message);

                                    }
                                }
                                System.out.println(currentUser.getUuid() + ": " + message);
                            }
                        } catch (IOException e) {
                            users.remove(currentUser);
                            System.out.println(currentUser.getUuid() + " отключился");
                            User.whoIsOnline(users);
                            for (User user1 : users) {
                                try {
                                    user1.getOut().writeUTF(currentUser.getName() + " отключился");
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
