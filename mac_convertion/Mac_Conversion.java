package org.forensics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Hashtable;

import org.apache.commons.lang3.ArrayUtils;

public class Mac_Conversion {

	/**
	 * @param args
	 */
	static Hashtable<String, String> instructions = new Hashtable<String, String>();

	public static void main(String[] args) {
		instructions.put("-T",
				"Use time conversion module. Either -f or -h must be given.");
		instructions.put("-D",
				"Use date conversion module. Either -f or -h must be given.");
		instructions
				.put("-f",
						"This specifies the path to a filename that includes a hex value of time or date. Format: -f filename");
		instructions
				.put("-h",
						"This specifies the path to a filename that includes a hex value of time or date. Format: -h 0x1234");

		String hexValue = getValueOf(args, "-h");
		if (hexValue != null && !((hexValue.trim()).equals("")) && hexValue.contains("x"))
			hexValue = hexValue.split("x")[1];
		else
			hexValue="";
		String filename = getValueOf(args, "-f");
		if(filename == null && hexValue ==null || (((hexValue.trim()).equals("")) && ((filename.trim()).equals(""))))
		{
			System.out.println("Error: Please enter -h or -f");
			help();
			System.exit(0);
		}
		if(((!(hexValue.trim()).equals("")) && !((filename.trim()).equals(""))))
		{
			System.out.println("Error: Please enter any one of the options -h or -f");
			help();
			System.exit(0);
		}
		if (ArrayUtils.indexOf(args, "-D") == 0 && !(hexValue.isEmpty())
				&& hexValue.length() == 4) {
			System.out.println("After conversion:" + hexToDate(hexValue));
			System.exit(0);
		} else if (ArrayUtils.indexOf(args, "-T") == 0 && !(hexValue.isEmpty())
				&& hexValue.length() == 4) {
			System.out.println("After conversion:" + hexToTime(hexValue));
			System.exit(0);
		}
		
		/*
		 * boolean optionTime = true; if (ArrayUtils.indexOf(args, "-D") == 0) {
		 * optionTime = false; } else if (!(ArrayUtils.indexOf(args, "-T") ==
		 * 0)) { System.out.print("Error"); help(); System.exit(0); }
		 */

		if (!(filename.isEmpty())) {
			File f = new File(filename);
			if (f.getName().matches(".*.txt$")) {

				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(f));
					String line;String hex;
					if ((line = br.readLine()) != null) {
						// process the line.
						if(line!=null && !((line.trim()).equals("")) && line.contains("x"))
						hex=line.split("x")[1].trim();
						else
						hex="";
			//			System.out.println("::"+line);
						if (ArrayUtils.indexOf(args, "-D") == 0 && !hex.equals("") && hex.length() == 4)
							System.out.println("After conversion:"
									+ hexToDate(hex));
						else if ((ArrayUtils.indexOf(args, "-T") == 0) && !hex.equals("") && hex.length() == 4)
							System.out.println("After conversion:"
									+ hexToTime(hex));
						else {
							System.out
									.print("Error : Please enter proper arguments");

							help();
							System.exit(0);
						}
					}
					br.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out
							.println("File not found \n Please provide a valid file name\n");
					help();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println("Error while reading the file\n");
					help();
				}

			}

		}

		// System.out.println("After conversion:" + hexToTime("f653"));

	}

	public static String getValueOf(String args[], String value) {

		int i = 0;

		if (((i = ArrayUtils.indexOf(args, value)) > 0) && i+1<args.length) {
			return (args[i + 1]);
		} else {
			return "";
		}

	}

	static String hexToDate(String hex) {
		String month[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };
		String recHex = hex.substring(2, 4) + hex.substring(0, 2);
		String s = hexToBin(recHex);
		//System.out.println("" + s);

		if (s.length() < 16) {
			int length = s.length();
			for (int i = 0; i < (16 - length); i++) {
				s = "0" + s;
			}
		}
		//System.out.println(s);
		String bin_Year = s.substring(0, 7);
		String bin_mon = s.substring(7, 11);
		String bin_date = s.substring(11, 16);
		int dec_year = Integer.parseInt(bin_Year, 2);
		int dec_mon = Integer.parseInt(bin_mon, 2);
		int dec_date = Integer.parseInt(bin_date, 2);
		if(1>dec_mon  || dec_mon>12 || 1>dec_date  || dec_date>31)
		{
			System.out.println("Improper date please enter a valid hex");
			help();
			System.exit(0);
		}
		return month[dec_mon - 1] + " " + dec_date + ", " + (1980 + dec_year);
	}

	static String hexToTime(String hex) {
		String recHex = hex.substring(2, 4) + hex.substring(0, 2);
		String s = hexToBin(recHex);
		if (s.length() < 16) {
			int length = s.length();
			for (int i = 0; i < (16 - length); i++) {
				s = "0" + s;
			}
		}
		//System.out.println(s);
		String bin_Hour = s.substring(0, 5);
		String bin_Min = s.substring(5, 11);
		String bin_Sec = s.substring(11, 16);
		int dec_hour = Integer.parseInt(bin_Hour, 2);
		boolean flag = false;
		if (dec_hour >= 12) {
			dec_hour = dec_hour - 12;
			flag = true;
		}
		int dec_min = Integer.parseInt(bin_Min, 2);
		int dec_sec = Integer.parseInt(bin_Sec, 2);
		if(1>dec_sec  || dec_sec>60 || 1>dec_min  || dec_min>60 || dec_hour<1 || dec_hour>12)
		{
			System.out.println("Improper date please enter a valid hex");
			help();
			System.exit(0);
		}
		String result = (dec_hour) + ":" + dec_min + ":" + dec_sec * 2 + " "
				+ ((flag == true) ? "P.M" : "A.M");
		return result;
	}

	static String hexToBin(String s) {
		return new BigInteger(s, 16).toString(2);
	}

	private static void help() {
		// TODO Auto-generated method stub
		System.out
				.println("\t Usage: mac_conversion -T|-D [-f filename |-h hex value]");
		for (String s : instructions.keySet()) {
			System.out.println("\t[" + s + "]\t" + instructions.get(s));
		}
	}

}
