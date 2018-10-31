//
// Created by yellowsea on 10/29/18.
//

#ifndef CHAT_COMMUNICATION_H
#define CHAT_COMMUNICATION_H


#include <string>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include "lib/cryptopp/eccrypto.h"
#include "lib/cryptopp/osrng.h"
#include "lib/cryptopp/oids.h"
#include "include/user.h"

using namespace std;
using namespace CryptoPP;

class Communication {
public:
    Communication(User* user, char* address);
    ~Communication();
private:
    int cli_socket;
    ECDSA<ECP, SHA256>::Signer* signer;
    ECDSA<ECP, SHA256>::Verifier* verifier;
    SecByteBlock* sharedKey;

    void generateSharedKey(User user, SecByteBlock* publicKey);
    void generateSigner(User user);
    void generateVerifier(ECDSA<ECP, SHA256>::PublicKey* publicKey);
};


#endif //CHAT_COMMUNICATION_H
