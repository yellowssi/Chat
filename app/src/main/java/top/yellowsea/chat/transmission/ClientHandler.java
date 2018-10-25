package top.yellowsea.chat.transmission;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.crypto.SecretKey;

public class ClientHandler extends Thread {
    private final User user;
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final SimpleDateFormat dateFormat;

    ClientHandler(User user, Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.user = user;
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    }

    @Override
    public void run() {
        try {
            PublicKey publicKey = (PublicKey) inputStream.readObject();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            PrivateKey privateKey = user.getKeyPair().getPrivate();
            SecretKey secretKey = ECDH.generateSecretKey(privateKey, publicKey);
            outputStream.writeObject(user.getKeyPair().getPublic());
            outputStream.flush();
            Thread client = new Client(user, socket.getInetAddress().getHostAddress(), 55554);
            client.start();
            while (true) {
                InformationPackage informationPackage = (InformationPackage) inputStream.readObject();
                Boolean verify = informationPackage.verifySignature(publicKey);
                String message = new String(ECDH.decryptString(secretKey, informationPackage.getData()));
                System.out.println("Time: " + dateFormat.format(informationPackage.getTimestamp()));
                System.out.println("Verify: " + verify);
                System.out.println("Message: " + message);
                System.out.println("--------------------------------");
                if (message.equals("exit") && verify && informationPackage.getTimestamp().after(timestamp)) {
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    System.out.println("Connection is broken: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                    System.out.println("--------------------------------");
                    break;
                }
            }
        } catch (ClassNotFoundException | IOException ignored) {
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
