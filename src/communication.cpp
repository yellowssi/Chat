//
// Created by yellowsea on 10/29/18.
//

#include "include/communication.h"

Communication::Communication(User* user, char* address) {
    cli_socket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(sockaddr_in));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = inet_addr(address);
    server_addr.sin_port = htons(55555);
    if (connect(cli_socket, (sockaddr*)&server_addr, sizeof(sockaddr)) < 0) {
        throw "Socket connect error!";
    }
}

void Communication::generateSharedKey(User user, SecByteBlock *publicKey) {
    ECDH<ECP>::Domain domain = *user.getECDHDomain();
    *sharedKey = SecByteBlock(domain.AgreedValueLength());
    domain.Agree(*sharedKey, *user.getECDHPrivateKey(), *publicKey);
}

void Communication::generateSigner(User user) {
    *signer = ECDSA<ECP, SHA256>::Signer(*user.getECDSAPrivateKey());
}

void Communication::generateVerifier(ECDSA<ECP, SHA256>::PublicKey* publicKey) {
    *verifier = ECDSA<ECP, SHA256>::Verifier(*publicKey);
}
