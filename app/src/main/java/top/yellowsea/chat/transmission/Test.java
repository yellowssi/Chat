package top.yellowsea.chat.transmission;

public class Test {
    public static void main(String args[]) {
        User user = new User("yellowsea");
        Server server = new Server(user, 55555);
        server.start();
    }
}
