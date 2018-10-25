package top.yellowsea.chat.transmission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

public class Client extends Thread {
    private final User user;
    private final String address;
    private final Integer port;

    Client(User user, String address, Integer port) {
        this.user = user;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(address, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(user.getKeyPair().getPublic());
            PublicKey publicKey = (PublicKey) inputStream.readObject();
            SecretKey secretKey = ECDH.generateSecretKey(user.getKeyPair().getPrivate(), publicKey);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            PrivateKey privateKey = user.getKeyPair().getPrivate();
            System.out.println("Connection with " + address + ":" + port + " is built!");
            System.out.println("--------------------------------");
            while (true) {
                String message = bufferedReader.readLine();
                System.out.println("--------------------------------");
                byte[] data = ECDH.encryptString(secretKey, message.getBytes("UTF-8"));
                InformationPackage informationPackage = new InformationPackage(data, privateKey);
                outputStream.writeObject(informationPackage);
                outputStream.flush();
                if (message.equals("exit")) {
                    socket.close();
                    System.out.println("Connection is broken: " + address + ":" + port);
                    System.out.println("--------------------------------");
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ignored) {
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
