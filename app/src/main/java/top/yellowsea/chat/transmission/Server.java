package top.yellowsea.chat.transmission;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final Integer port;
    private final User user;

    Server(User user, Integer port) {
        this.port = port;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            Thread thread = new ClientHandler(user, socket, inputStream, outputStream);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
