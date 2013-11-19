package org.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class ToolkitMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		String fileName = null;
		System.out.println("Please enter the complete path of the file: ");
		Scanner sc = new Scanner(System.in);
        fileName = sc.nextLine();
        FileInputStream fis = null;
        RandomAccessFile raf = null;
	System.out.println("\nPlease wait calculating data\n");
	File f = new File("PartionsDetails.txt");
	File f1 = new File(fileName);
        if(f.exists())
        	f.delete();
		try {
			fis = new FileInputStream(fileName);
			raf = new RandomAccessFile(fileName, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("File not found");
		}
        HashCalculator.Calculate(fis, f1.getName());
        
        HDAnalysis.calculateMBR(raf);
        HDAnalysis.calculateVBR(raf);
	}
	

}
