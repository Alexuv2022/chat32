package server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(9445);
            System.out.println("Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                User currentUser = new User(socket);
                users.add(currentUser);
                System.out.println("Клиент подключился");
                System.out.println(currentUser.getUuid());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            currentUser.getOut().writeUTF("{\"msg\":\"Введите имя:\"}");
                            currentUser.setName(currentUser.getIs().readUTF());
                            sendOnlineUsers();
                            while (true) {
                                String message = currentUser.getIs().readUTF();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("msg", currentUser.getName() + ": " + message);
                                for (User user : users) {
                                    if (!(currentUser.getUuid().toString().equals(user.getUuid().toString()))) {
                                        user.getOut().writeUTF(jsonObject.toJSONString());
                                    }
                                }
                                System.out.println(currentUser.getUuid() + ": " + message);
                            }
                        } catch (IOException e) {
                            users.remove(currentUser);
                            System.out.println(currentUser.getUuid() + " отключился");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("msg", currentUser.getName() + ": " + " указанный пользователь отключился");
                            for (User user1 : users) {
                                try {
                                    user1.getOut().writeUTF(jsonObject.toJSONString());
                                    sendOnlineUsers();
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

    static void sendOnlineUsers() throws IOException {
        JSONArray onlineUsersJSON = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (User user : users) {
            onlineUsersJSON.add(user.getName());
        }
        System.out.println(onlineUsersJSON.toString());
        jsonObject.put("users", onlineUsersJSON);
        for (User user : users) {
            user.getOut().writeUTF(jsonObject.toJSONString());
        }
    }
}
