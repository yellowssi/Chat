package top.yellowsea.chat.transmission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.sql.Timestamp;

import lombok.Getter;

@Getter
public class InformationPackage implements Serializable {
    private byte[] data;
    private Timestamp timestamp;
    private byte[] signature;

    private byte[] getCombinedData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        objectOutput.writeObject(this.timestamp);
        objectOutput.flush();
        byte[] timestampBytes = outputStream.toByteArray();
        byte[] combined = new byte[this.data.length + timestampBytes.length];
        System.arraycopy(this.data, 0, combined, 0, this.data.length);
        System.arraycopy(timestampBytes, 0, combined, 0, timestampBytes.length);
        return combined;
    }

    private void generateSignature(PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256WithECDSA");
            signature.initSign(privateKey);
            signature.update(this.getCombinedData());
            this.signature = signature.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean verifySignature(PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256WithECDSA");
            signature.initVerify(publicKey);
            signature.update(this.getCombinedData());
            return signature.verify(this.signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    InformationPackage(byte[] data, PrivateKey privateKey) {
        this.data = data;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.generateSignature(privateKey);
    }
}
