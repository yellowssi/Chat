//
// Created by yellowsea on 10/29/18.
//

#include <include/user.h>

string User::getUserName() {
    return username;
}

ECDSA<ECP, SHA256>::PrivateKey* User::getECDSAPrivateKey() {
    return this->ECDSAPrivateKey;
}

ECDSA<ECP, SHA256>::PublicKey* User::getECDSAPublicKey() {
    return this->ECDSAPublicKey;
}

ECDH<ECP>::Domain* User::getECDHDomain() {
    return this->ECDHDomain;
}

SecByteBlock* User::getECDHPrivateKey() {
    return this->ECDHPrivateKey;
}

SecByteBlock* User::getECDHPublicKey() {
    return this->ECDHPublicKey;
}

void User::generateECDSAKeyPair() {
    AutoSeededRandomPool rng;
    ECDSAPrivateKey->Initialize(rng, ASN1::secp256r1());
    bool result = ECDSAPrivateKey->Validate(rng, 3);
    if (!result) {
        throw "Private key generated failed!";
    }
    ECDSAPrivateKey->MakePublicKey(*ECDSAPublicKey);
    result = ECDSAPublicKey->Validate(rng, 3);
    if (!result) {
        throw "Public key generated failed!";
    }
}

void User::generateECDHKeyPair() {
    AutoSeededRandomPool rng;
    OID curve = ASN1::secp256r1();
    *ECDHDomain = ECDH<ECP>::Domain(curve);
    *ECDHPrivateKey = SecByteBlock(ECDHDomain->PrivateKeyLength());
    *ECDHPublicKey = SecByteBlock(ECDHDomain->PublicKeyLength());
    ECDHDomain->GenerateKeyPair(rng, *ECDHPrivateKey, *ECDHPublicKey);
}
