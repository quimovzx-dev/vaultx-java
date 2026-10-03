import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.Arrays;

public class VaultX {
    static final byte[] MAGIC={'V','X','1'};
    static final int ITER=210000, KEY_BITS=256;
    static byte[] random(int n){ byte[] b=new byte[n]; new SecureRandom().nextBytes(b); return b; }
    static SecretKey key(char[] p, byte[] salt) throws Exception {
        PBEKeySpec s=new PBEKeySpec(p,salt,ITER,KEY_BITS);
        byte[] raw=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(s).getEncoded();
        s.clearPassword(); return new SecretKeySpec(raw,"AES");
    }
    static void encrypt(Path in,Path out,char[] p)throws Exception{
        byte[] salt=random(16),iv=random(12),plain=Files.readAllBytes(in);
        Cipher c=Cipher.getInstance("AES/GCM/NoPadding");
        c.init(Cipher.ENCRYPT_MODE,key(p,salt),new GCMParameterSpec(128,iv));
        try(DataOutputStream d=new DataOutputStream(Files.newOutputStream(out))){
            d.write(MAGIC); d.write(salt); d.write(iv); d.write(c.doFinal(plain));
        }
        System.out.println("Encrypted -> "+out);
    }
    static void decrypt(Path in,Path out,char[] p)throws Exception{
        try(DataInputStream d=new DataInputStream(Files.newInputStream(in))){
            if(!Arrays.equals(d.readNBytes(3),MAGIC)) throw new IOException("Not a VAULTX file");
            byte[] salt=d.readNBytes(16),iv=d.readNBytes(12),enc=d.readAllBytes();
            Cipher c=Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE,key(p,salt),new GCMParameterSpec(128,iv));
            Files.write(out,c.doFinal(enc));
        }
        System.out.println("Decrypted -> "+out);
    }
    public static void main(String[] a)throws Exception{
        if(a.length!=4){System.out.println("encrypt <in> <out> <password> OR decrypt <in> <out> <password>");return;}
        char[] p=a[3].toCharArray();
        try{if(a[0].equals("encrypt"))encrypt(Paths.get(a[1]),Paths.get(a[2]),p);else if(a[0].equals("decrypt"))decrypt(Paths.get(a[1]),Paths.get(a[2]),p);}
        finally{Arrays.fill(p,'\0');}
    }
}
