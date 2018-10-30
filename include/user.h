//
// Created by yellowsea on 10/29/18.
//
#include <string>
#include "lib/cryptopp/oids.h"
#include "lib/cryptopp/osrng.h"
#include "lib/cryptopp/eccrypto.h"

using namespace std;
using namespace CryptoPP;

#ifndef CHAT_USER_H
#define CHAT_USER_H

class User {
public:
    User(string username) {
        this->username = username;
        this->generateECDSAKeyPair();
        this->generateECDHKeyPair();
    }
    string getUserName();
    ECDSA<ECP, SHA256>::PrivateKey* getECDSAPrivateKey();
    ECDSA<ECP, SHA256>::PublicKey* getECDSAPublicKey();
    ECDH<ECP>::Domain* getECDHDomain();
    SecByteBlock* getECDHPrivateKey();
    SecByteBlock* getECDHPublicKey();
private:
    string username;
    ECDSA<ECP, SHA256>::PrivateKey* ECDSAPrivateKey;
    ECDSA<ECP, SHA256>::PublicKey* ECDSAPublicKey;
    ECDH<ECP>::Domain* ECDHDomain;
    SecByteBlock* ECDHPrivateKey;
    SecByteBlock* ECDHPublicKey;

    void generateECDSAKeyPair();
    void generateECDHKeyPair();
};

#endif //CHAT_USER_H
