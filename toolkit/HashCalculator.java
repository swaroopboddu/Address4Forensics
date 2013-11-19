package org.toolkit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

public class HashCalculator {

    public static String calculateHash(MessageDigest algorithm, MessageDigest algo2,
    		FileInputStream fis) throws IOException{

        //FileInputStream     fis = new FileInputStream(fileName);
        //BufferedInputStream bis = new BufferedInputStream(fis);
        //DigestInputStream   dis = new DigestInputStream(bis, algorithm);
        byte[] dataBytes = new byte[1024];
        
        int nread = 0; 
     
        while ((nread = fis.read(dataBytes)) != -1) {
          algorithm.update(dataBytes, 0, nread);
          algo2.update(dataBytes, 0, nread);
        };
        // read the file and update the hash calculation
       // while (dis.read() != -1);

        // get the hash value as byte array
       // byte[] hash = algorithm.digest();
        String result = byteArray2Hex(algorithm.digest())+":"+byteArray2Hex(algo2.digest());
        return result;
    }

    public static String byteArray2Hex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static void Calculate(FileInputStream fis, String fn) {
        //String fileName = "TestImage1.img";
        /*Scanner sc = new Scanner(System.in);
        fileName = sc.nextLine();
*/
        MessageDigest sha1 = null;
        MessageDigest md5 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unknown Exception occured program terminated");
		}

        try {
        	File MD5_f = new File("MD5-"+fn+".txt");
        	File SHA1_f = new File("SHA1-"+fn+".txt");
        	FileOutputStream MD5_fos = new FileOutputStream(MD5_f);
        	FileOutputStream SHA1_fos = new FileOutputStream(SHA1_f);
        	String hash = calculateHash(md5,sha1, fis);
        	String arr_has[] = hash.split(":"); 
        	//String SHA1_out = calculateHash(, fis);
        	MD5_fos.write(arr_has[0].getBytes());
        	SHA1_fos.write(arr_has[1].getBytes());
			System.out.println(arr_has[0]);
		    System.out.println(arr_has[1]);
        }  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Unable to read from file");
		}
    }
}
