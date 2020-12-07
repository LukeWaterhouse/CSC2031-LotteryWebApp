import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;

public class Methods {

    public String decryptData(byte[] data, KeyPair pair) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE,pair.getPrivate());
            String decipheredText = new String(cipher.doFinal(data));
            return decipheredText;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public byte[] bytesFileReader(String filename){
        try{
            System.out.println("FILENAME BEFORE filereader: "+filename);
            return Files.readAllBytes(Paths.get("UserFiles/"+filename));
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void bytesFileWriter(String filename, byte[] data){
        try {
            File dir = new File("UserFiles");

            if (!dir.exists()){
                dir.mkdir();
            }
            File myFile = new File(dir,filename);
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            os.flush();
            os.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void clearFile(String filename){
        try {
            OutputStream os = new FileOutputStream(filename);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] encryptData(String data,KeyPair pair){
        try {
            //create keypair generator object using ASA alg.
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");;
            //Initializing a Cipher Object
            cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
            return cipher.doFinal(data.getBytes());
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex){
            ex.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    static String bytesToStringHex(byte[] bytes){
        char [] hexChars = new char[bytes.length*2];
        for (int j=0;j<bytes.length;j++){
            int v = bytes[j] & 0xFF;
            hexChars[j*2] = HEX_ARRAY[v >>> 4];
            hexChars[j*2+1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}