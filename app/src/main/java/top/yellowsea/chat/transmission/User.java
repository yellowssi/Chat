package top.yellowsea.chat.transmission;

import java.security.KeyPair;

import lombok.Getter;

@Getter
public class User {
    private KeyPair keyPair;
    private String username;

    User(String username) {
        this.username = username;
        this.keyPair = ECDH.generateKeyPair();
    }
}
