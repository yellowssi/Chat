//
// Created by yellowsea on 10/29/18.
//
#include <include/package.h>

Package::Package(string* message, ECDSA<ECP, SHA256>::Signer* signer) {
    this->message = *message;
    this->timestamp = time(nullptr);
    this->sign(signer);
}

void Package::sign(ECDSA<ECP, SHA256>::Signer* signer) {
    AutoSeededRandomPool rng;
    StringSource s(message, true, new SignerFilter(rng, *signer, new StringSink(signature)));
}

bool Package::verify(ECDSA<ECP, SHA256>::Verifier* verifier) {
    bool result = false;
    StringSource s(signature + message, true,
            new SignatureVerificationFilter(*verifier, new ArraySink((byte*)&result, sizeof(result))));
    return result;
}

string Package::getMessage() {
    return message;
}

time_t Package::getTimestamp() {
    return timestamp;
}

string Package::getSignature() {
    return signature;
}

