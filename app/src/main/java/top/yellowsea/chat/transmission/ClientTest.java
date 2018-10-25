package top.yellowsea.chat.transmission;

public class ClientTest {
    public static void main(String args[]) {
        User user = new User("yellowsea");
        Thread server = new Server(user, 55554);
        server.start();
        Thread client = new Client(user, "127.0.0.1", 55555);
        client.start();
    }
}
