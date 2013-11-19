package org.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;

import org.toolkit.HashCalculator;

public class HDAnalysis {

	/**
	 * @param args
	 */
	private static ArrayList<PartitionMetaData> vol_data = new ArrayList<PartitionMetaData>(); 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void calculateMBR(RandomAccessFile raf)
	{
		byte[] b = new byte[16];
		int off = 447;
		int len = 16;
		
		try {
			raf.seek(446);
			do{
			raf.read(b, 0, len);
			//System.out.println(":"+b);
			readPartition(b);
			off = off+len;
			}
			while(off<=510);
			byte  magic[] = new byte[2];
			raf.read(magic);
			
			if(HashCalculator.byteArray2Hex(magic).equalsIgnoreCase("55AA"))
			{
				System.out.println("Proper magic byte");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static int number=0;
	private static void readPartition(byte[] b) {
		FileOutputStream fos = null;	
		File f = new File("PartionsDetails.txt");
		try {
			 fos = new FileOutputStream(f,true);
			
			String hexPart = String.format("%02X", b[4]);
			//System.out.println("HexPartiotion"+hexPart);
			fos.write(hexPart.getBytes());
			//mr code
			
			String startAddress = new BigInteger(String.format("%02X", b[11])+String.format("%02X", b[10])+String.format("%02X", b[9])+String.format("%02X", b[8]),16).toString(10);
			//System.out.println("StartAddress:"+startAddress);
			
			//vol_start.add(Long.parseLong(startAddress));
			String sizeOfPart = new BigInteger(String.format("%02X", b[15])+String.format("%02X", b[14])+String.format("%02X", b[13])+String.format("%02X", b[12]),16).toString(10);
			//System.out.println("SizeOfPart:"+sizeOfPart);
			fos.write(("("+hexPart+")"+"\t").getBytes());
				String s = hexPart;
				String a = null;
				if(s.equalsIgnoreCase("01")) {
				a = "DOS 12-bit FAT";
				}
				else if(s.equalsIgnoreCase("04")) {
				a = "DOS 16-bit FAT for partitions smaller than 32 MB";
				}
				else if(s.equalsIgnoreCase("05")) {
				a = "Extended partition";
				}
				else if(s.equalsIgnoreCase("06")) {
				a = "DOS 16-bit FAT for partitions larger than 32 MB";
				}
				else if(s.equalsIgnoreCase("07")) {
				a = "NTFS";
				}
				else if(s.equalsIgnoreCase("08")) {
				a = "AIX bootable partition";
				}
				else if(s.equalsIgnoreCase("09")) {
				a = "AIX data partition";
				}
				else if(s.equalsIgnoreCase("0B")) {
				a = "DOS 32-bit FAT";
				}
				else if(s.equalsIgnoreCase("0C")) {
				a = "DOS 32-bit FAT for interrupt 13 support";
				}
				else if(s.equalsIgnoreCase("17")) {
				a = "Hidden NTFS partition (XP and earlier";
				}
				else if(s.equalsIgnoreCase("1B")) {
				a = "Hidden FAT32 partition";
				}
				else if(s.equalsIgnoreCase("1E")) {
				a = "Hidden VFAT partition";
				}
				else if(s.equalsIgnoreCase("3C")) {
				a = "Partition Magic recovery partition";
				}
				else if(s.equalsIgnoreCase("66") || s.equalsIgnoreCase("67") || s.equalsIgnoreCase("68") || s.equalsIgnoreCase("69")) {
				a = "Novell partitions";
				}
				else if(s.equalsIgnoreCase("81")) {
				a = "Linux";
				}
				else if(s.equalsIgnoreCase("82")) {
				a = "Linux swap partition (can also be associated with Solaris partitions";
				}
				else if(s.equalsIgnoreCase("83")) {
				a = "Linux native file systems (Ext2, Ext3, Reiser, xiafs";
				}
				else if(s.equalsIgnoreCase("86")) {
				a = "FAT16 volume/stripe (Windows NT)";
				}
				else if(s.equalsIgnoreCase("87")) {
				a = "High Performance File System (HPFS) fault-tolerant mirrored partition or NTFS volume/stripe set";
				}
				else if(s.equalsIgnoreCase("A5")) {
				a = "FreeBSD and BSD/386";
				}
				else if(s.equalsIgnoreCase("A6")) {
				a = "OpenBSD";
				}
				else if(s.equalsIgnoreCase("A9")) {
				a = "NetBSD";
				}
				else if(s.equalsIgnoreCase("C7")) {
				a = "Typical of a corrupted NTFS volume/stripe set";
				}
				else if(s.equalsIgnoreCase("EB")) {
				a = "BeOS";
				}
				fos.write(a.getBytes());
				fos.write(",".getBytes());
				fos.write(startAddress.getBytes());
				fos.write(",".getBytes());
				fos.write(sizeOfPart.getBytes());
				fos.write("\n".getBytes());
				
			
			PartitionMetaData pmd =new PartitionMetaData(Long.parseLong(startAddress), Long.parseLong(sizeOfPart), hexPart, number, a);
			number++;
			vol_data.add(pmd);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public static void calculateVBR(RandomAccessFile raf)
	{
		 byte b[]= new byte[512];
		for(PartitionMetaData start: vol_data)
		{
			try {
				//System.out.println(":"+((start.getStartAddress())*512));
				raf.seek(((start.getStartAddress())*512));
				raf.read(b, 0, b.length);
				/*for(byte b1: b)
				{
					System.out.println(String.format("%02X", b1));
				}*/
				//System.out.println(":"+b);
				readVBR(b,start);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static void readVBR(byte[] b, PartitionMetaData part) {
		// TODO Auto-generated method stub
		if(part.getType().equals("0B")||part.getType().equals("06")||part.getType().equals("04") || part.getType().equals("01") ){
			//System.out.println(String.format("%02X", b[15])+String.format("%02X", b[14]));
		String rsrv_sectors = new BigInteger((String.format("%02X", b[15])+String.format("%02X", b[14])),16).toString(10);
		String sec_per_clus = new BigInteger(String.format("%02X", b[13]),16).toString(10);
		String no_fats = new BigInteger(String.format("%02X", b[16]),16).toString(10);
		String fat_length="0";
		if(part.getType().equalsIgnoreCase("06"))
		  fat_length = new BigInteger((String.format("%02X", b[23])+String.format("%02X", b[22])),16).toString(10);
		if(part.getType().equalsIgnoreCase("0B"))
			fat_length = new BigInteger((String.format("%02X", b[39])+String.format("%02X", b[38])+String.format("%02X", b[37])+String.format("%02X", b[36])),16).toString(10);
		long fat_area = Long.parseLong(no_fats)*Long.parseLong(fat_length);
		long start_fat= Long.parseLong(rsrv_sectors);
		long end_fat = start_fat+fat_area-1;
		long start_rsrv = 0;
		long end_rsrv = Long.parseLong(rsrv_sectors)-1;
		long cluster2_start = 0;
		if(part.getType().equalsIgnoreCase("0B"))
			cluster2_start = part.getStartAddress() + fat_area + Long.parseLong(rsrv_sectors);
		else if(part.getType().equalsIgnoreCase("06") || part.getType().equalsIgnoreCase("04") || part.getType().equalsIgnoreCase("01"))
		{
			String max_files = new BigInteger((String.format("%02X", b[18])+String.format("%02X", b[17])),16).toString(10);
			long fdt = ((Long.parseLong(max_files)*32)+512-1)/512;
			cluster2_start = part.getStartAddress() + fat_area + Long.parseLong(rsrv_sectors)+fdt;
		}
		try {
			FileOutputStream fis = new FileOutputStream("PartionsDetails.txt", true);
			fis.write("====================================================================================================================\n".getBytes());
			fis.write(("Partition "+part.getNumber()+" ("+part.getDesc()+"):\n").getBytes());
			fis.write(("Reserved Area: Start Sector: "+start_rsrv+" Ending sector: "+ end_rsrv +"\tSize:" + rsrv_sectors+" Sectors\n").getBytes());
			fis.write(("Sectors per cluster:" +sec_per_clus +" sectors\n").getBytes() );
			fis.write(("FAT area:  Start sector: " +rsrv_sectors +" Ending sectors: "+end_fat+"\n").getBytes() );
			fis.write(("# of FATs: " +no_fats + "\n").getBytes() );
			fis.write(("The size of each FAT: " +fat_length + " sectors \n").getBytes() );
			fis.write(("The first sector of cluster 2: " +cluster2_start + " sectors \n").getBytes() );
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
	}
	private static String littleToBigendian(String address)
	{
		return address.substring(2, 4) + address.substring(0, 2);
	}

}
