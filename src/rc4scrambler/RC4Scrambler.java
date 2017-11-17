/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc4scrambler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;
import java.lang.StringBuilder;

/**
 *
 * @author Hazira Fakhrurrozi A
 */
public class RC4Scrambler {

    private char[] key;
    private int[] sbox;
    private static final int SBOX_LENGTH = 2560;
    private static final int KEY_MIN_LENGTH = 5;
    
    
    public String Scambler(String Data) {
        int number=0;
//        System.out.println("Enter the String you want to encrypt ");
//        BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        try {
            String salt = String.valueOf(System.currentTimeMillis());
            String text = new StringBuilder(salt).reverse().toString();
//            String input = rd.readLine();
            String s[] = Data.split("\\r?\\n");
            
            RC4Scrambler rc4 = new RC4Scrambler(salt);
            char[] result = rc4.encrypt(text.toCharArray());
//            System.out.println("encrypted string:\n" + new String(result));
            byte[] bytes = new String (result).getBytes();
            byte[] a = new String(result).getBytes();
//            System.out.println( Hex.encodeHexString(a) );
//            System.out.println("decrypted string:\n"
//                    + new String(rc4.decrypt(result)));
//            System.out.println("----------" + Arrays.toString(rc4.sbox));
            int []sboxed = rc4.sbox;
//            System.out.println(sboxed[1]);
            for(int i=0;i<SBOX_LENGTH;i++){
                
                if(rc4.sbox[i]<(s.length)){
                    
                        number = number+1;      
                        sb.append(number+". "+s[sboxed[i]]).append("\n");
                    
                    
                    
                }
                else{
                    sb.append("");
                }
            }
//            String Scrambled =sb.toString();
        } catch (InvalidKeyException e) {

            System.err.println(e.getMessage());

        }
        
        return sb.toString();
        

    }

    public RC4Scrambler(String key) throws InvalidKeyException {

        setKey(key);

    }

    public RC4Scrambler() {

    }

    public char[] decrypt(final char[] msg) {

        return encrypt(msg);

    }

    public char[] encrypt(final char[] msg) {

        sbox = initSBox(key);
        char[] code = new char[msg.length];
        int i = 0;
        int j = 0;
        for (int n = 0; n < msg.length; n++) {

            i = (i + 1) % SBOX_LENGTH;
            j = (j + sbox[i]) % SBOX_LENGTH;
            swap(i, j, sbox);
            int rand = sbox[(sbox[i] + sbox[j]) % SBOX_LENGTH];
            code[n] = (char) (rand ^ (int) msg[n]);

        }
        return code;

    }

    private int[] initSBox(char[] key) {

        int[] sbox = new int[SBOX_LENGTH];
        int j = 0;

        for (int i = 0; i < SBOX_LENGTH; i++) {

            sbox[i] = i;

        }

        for (int i = 0; i < SBOX_LENGTH; i++) {

            j = (j + sbox[i] + key[i % key.length]) % SBOX_LENGTH;
            swap(i, j, sbox);

        }
        return sbox;

    }

    private void swap(int i, int j, int[] sbox) {

        int temp = sbox[i];
        sbox[i] = sbox[j];
        sbox[j] = temp;

    }

    public void setKey(String key) throws InvalidKeyException {

        if (!(key.length() >= KEY_MIN_LENGTH && key.length() < SBOX_LENGTH)) {

            throw new InvalidKeyException("Key length has to be between "
                    + KEY_MIN_LENGTH + " and " + (SBOX_LENGTH - 1));

        }

        this.key = key.toCharArray();

    }
    
}
