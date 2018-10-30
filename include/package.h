//
// Created by yellowsea on 10/29/18.
//
#include <string>
#include "lib/cryptopp/osrng.cpp"
#include "lib/cryptopp/eccrypto.h"

using namespace std;
using namespace CryptoPP;

#ifndef CHAT_PACKAGE_H
#define CHAT_PACKAGE_H

class Package {
public:
    Package(string* message, ECDSA<ECP, SHA256>::Signer* signer);
    bool verify(ECDSA<ECP, SHA256>::Verifier* verifier);
    string getMessage();
    time_t getTimestamp();
    string getSignature();
private:
    string message;
    time_t timestamp;
    string signature;

    void sign(ECDSA<ECP, SHA256>::Signer* signer);
};

#endif //CHAT_PACKAGE_H
