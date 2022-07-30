package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class User {
    private Socket socket;
    private DataInputStream is;
    private DataOutputStream out;
    private String name;
    private UUID uuid;

    public User(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.is = new DataInputStream(socket.getInputStream());
        this.uuid = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public DataInputStream getIs() {
        return is;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public static void whoIsOnline(ArrayList<User> users) {

        for (User userInUsers: users) {
            String whoIsOnLine = "";
            for (User userOnLine: users) {
                if (!(userInUsers.getUuid().toString().equals(userOnLine.getUuid().toString()))) {
                    whoIsOnLine += userOnLine.getName() + "\n";
                }
            }
            try {
                userInUsers.getOut().writeUTF(whoIsOnLine + "# is online");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
/*
 * Решить какие геттеры и сеттеры нужны классу User
 * Запретить отправку сообщения отправителю
 *
 */
/*
* Решить какие геттеры и сеттеры нужны классу User
* Запретить отправку сообщения отправителю
*
 */