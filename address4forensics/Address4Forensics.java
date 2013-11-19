package org.forensics;

import java.util.Arrays;
import java.util.Hashtable;

import org.apache.commons.lang3.ArrayUtils;

public class Address4Forensics {

	/**
	 * @param args
	 */
	static Hashtable<String, String> instructions     = new Hashtable<String, String>();
	public static void main(String[] args) {
		instructions.put("-p", "This specifies the known physical address for calculating either a cluster address or a logical address. Format -p address");
		instructions.put("--physical-known", "This specifies the known physical address for calculating either a cluster address or a logical address.  Format --physical-known=address");
		instructions.put("-c", "This specifies the known cluster address for calculating either a physical address or a logical address -k ,-r,-t, and-f must be provided with this option Format -c address");
		instructions.put("-l", "This specifies the known logical address for calculating either a cluster address or a physical address. Format  -l address");
		instructions.put("--logical-known", "This specifies the known logical address for calculating either a cluster address or a physical address. Format --logical-known=address"   );
		instructions.put("-s", "When the -B option is used, this allows for a specification of bytes per sector other than the default 512 Format  -s bytes ");
		instructions.put("--sector-size", "When the -B option is used, this allows for a specification of bytes per sector other than the default 512 Format  --sector-size=bytes ");
		instructions.put("-B", "Instead of returning sector values for the conversion, this returns the byte address of the calculated value, which is the number of sectors multiplied by the number of bytes per sector Format  -B");
		instructions.put("--byte-address","Instead of returning sector values for the conversion, this returns the byte address of the calculated value, which is the number of sectors multiplied by the number of bytes per sector Format  --byte-address");
		instructions.put("-b","This specifies the physical address (sector number) of the start of the partition, and defaults to 0 for ease in working with images of a single partition Format  -b offset");
		instructions.put("--partition-start","This specifies the physical address (sector number) of the start of the partition, and defaults to 0 for ease in working with images of a single partition Format  --partition-start=offset");
		instructions.put("-k","This specifies the number of sectors per cluster  Format -k sectors ");
		instructions.put("--cluster-size", "This specifies the number of sectors per cluster  Format --cluster-size= sectors ");
		instructions.put("-r","This specifies the number of sectors per cluster  Format -r sectors ");
		instructions.put("--reserved", "This specifies the number of sectors per cluster  Format --reserved= sectors ");
		instructions.put("-t","This specifies the number of fat tables  Format -t tables ");
		instructions.put("--fat-tables", "This specifies the number of fat tables  Format --fat-tables= tables ");
		instructions.put("-f", "This specifies length of each FAT table in sectors   Format -f= length ");
		instructions.put("--fat-length", "This specifies length of each FAT table in sectors   Format --fat-length= length ");
		instructions.put("-P", "Calculate the logical address from either the cluster address or the physical address. Format  -P");
		instructions.put("-L", "Calculate the logical address from either the cluster address or the physical address. Format  -L");
		instructions.put("-C", "Calculate the logical address from either the cluster address or the physical address. Format  -C");
		instructions.put("--help", "help");
		instructions.put("-h","help");
		
		long temp;
		long physicalAddress = ((temp = getValueOf(args, "-p")) < 0) ? getValueOf(
				args, "--physical-known=") : temp;
		long logicalAddress = ((temp = getValueOf(args, "-l")) < 0) ? getValueOf(
				args, "--logical-known=") : temp;
		long clusterAddress = ((temp = getValueOf(args, "-c")) < 0) ? getValueOf(
				args, "--cluster-known=") : temp;
		long no_of_sectors = ((temp = getValueOf(args, "-k")) < 0) ? getValueOf(
				args, "--cluster-size=") : temp;
		
		long rsrv_sectors = ((temp = getValueOf(args, "-r")) < 0) ? getValueOf(
				args, "--reserved=") : temp;
		long fat_tables = ((temp = getValueOf(args, "-t")) < 0) ? getValueOf(
				args, "--fat-tables=") : temp;
				fat_tables = fat_tables < 0 ? 2 : fat_tables;
		
		long fat_length = ((temp = getValueOf(args, "-f")) < 0) ? getValueOf(
				args, "--fat-length=") : temp;
		long startAddress = ((temp = getValueOf(args, "-b")) < 0) ? getValueOf(
				args, "--partition-start=") : temp;
		startAddress = (startAddress < 0) ? 0 : startAddress;
		long bytes_per_sec = ((temp = getValueOf(args, "-s")) < 0) ? getValueOf(
				args, "--sector-size=") : temp;
		boolean isBytes = (ArrayUtils.indexOf(args, "-B") > 0) ? true : false;
		bytes_per_sec = (bytes_per_sec < 0) ? 512 : bytes_per_sec;
		if (ArrayUtils.indexOf(args, "-L") == 0) {

			if (ArrayUtils.indexOf(args, "-C") >= 0
					|| ArrayUtils.indexOf(args, "-P") >= 0) {
				System.out.println("Improper commands");
				help();
				System.exit(0);
			}

			// startAddress = ( startAddress > 0) ? startAddress : 0;
			if (physicalAddress < 0 && clusterAddress < 0 && logicalAddress < 0) {
				System.out
						.println("Please enter atleast one of -p or -c options");
			} else if (physicalAddress > 0)
				System.out
						.println("Converting physical address to logical address:"
								+ physicalToLogical(physicalAddress,
										startAddress, isBytes, bytes_per_sec));
			else if (clusterAddress > 0 && rsrv_sectors > 0 && fat_length > 0
					&& no_of_sectors > 0) {
				System.out
						.println("Converted cluster address to physical address:"
								+ clusterToLogical(clusterAddress,
										no_of_sectors, rsrv_sectors,
										fat_tables, fat_length, isBytes,
										bytes_per_sec));
			} else if (logicalAddress > 0) {
				System.out.println("Logical Address :" + logicalAddress);
			}

		} else if (ArrayUtils.indexOf(args, "-C") == 0) {
			if (ArrayUtils.indexOf(args, "-L") >= 0
					|| ArrayUtils.indexOf(args, "-P") >= 0) {
				System.out.println("Improper commands");
				help();
				System.exit(0);
			}

			
			if (physicalAddress < 0 && logicalAddress < 0 && clusterAddress < 0) {
				System.out
						.println("Please enter atleast one of -l or -c options");
			} else if (physicalAddress > 0 && rsrv_sectors > 0 && fat_length > 0
					&& no_of_sectors > 0)
				System.out
						.println("Converting physical address to cluster address:"+ physicalToCluster(startAddress, physicalAddress, no_of_sectors, rsrv_sectors, fat_tables,fat_length ));
			else if (clusterAddress > 0 ) {
				System.out.println("cluster  address:" + clusterAddress);
			} else if (logicalAddress > 0 &&  rsrv_sectors > 0 && fat_length > 0
					&& no_of_sectors > 0) {
				System.out
						.println("Converting logical address to cluster address:"+ logicalToCluster(logicalAddress, no_of_sectors, rsrv_sectors, fat_tables, fat_length));
			}
			else
			{
				System.out.println("Invalid arguments\n");
				help();
				System.exit(0);
			}
		}

		else if (ArrayUtils.indexOf(args, "-P") == 0) {
			if (ArrayUtils.indexOf(args, "-L") >= 0
					|| ArrayUtils.indexOf(args, "-C") >= 0) {
				System.out.println("Improper commands");
				help();
				System.exit(0);
			}
			if (physicalAddress < 0 && logicalAddress < 0 && clusterAddress < 0) {
				System.out
						.println("Please enter atleast one of -l or -c options");
			} else if (physicalAddress > 0)
				System.out.println("Physical Address:" + physicalAddress);
			else if (clusterAddress > 0) {
				System.out.println("cluster  address to physical address:"
						+ clusterToPhysical(clusterAddress, no_of_sectors,
								rsrv_sectors, fat_tables, fat_length,
								startAddress, isBytes, bytes_per_sec));
			} else if (logicalAddress > 0) {
				System.out
						.println("Converting logical address to cluster address:"
								+ logicalToPhysical(logicalAddress,
										startAddress, isBytes, bytes_per_sec));
			}
		}
		else 
		{
			System.out.println("Improper usage");
			help();
		}

	}

	private static void help() {
		// TODO Auto-generated method stub
		System.out.println("\t Usage: address4forensics -L | -P | -C [-b offset][-B [-s bytes]][-l address][-p address ] [-c address -k sectors -r sectors -t tables -f sectors] -L, --logical");
		for(String s : instructions.keySet())
			{
				System.out.println("\t["+s+"]\t"+instructions.get(s) );
			}
	}

	public static long getValueOf(String args[], String value) {
		try {
			int i = 0;
			long result=0;
			if (value.length() == 2) {
				if ((i = ArrayUtils.indexOf(args, value)) > 0) {
					if(i<args.length-1)
					result = Long.parseLong(args[i + 1]);
					else
					{
						System.out.println("Please enter a valid parameter for "+value+"option\n");
						help();
					}
					return result;

				}
			} else {
				String temp = Arrays.toString(args);
				String arr[] = temp.split(value);
				if (arr.length == 2) {
					String arr2[] = arr[1].split(",");

					result = Long.parseLong(arr2[0].split("]")[0]);
					return result;
				}
			}
		} catch (NumberFormatException nfe) {
			System.out.println("Please enter valid parameter for "+value+"option\n");
			help();
			System.exit(0);
		}
		return -1;
	}

	public static long physicalToLogical(long address, long start,
			boolean isBytes, long bytes_per_sec) {
		if (isBytes) {
			return (address - start) * bytes_per_sec;
		} else
			return address - start;
	}

	public static long physicalToCluster(long start, long address,
			long sec_per_cluster, long rsrv_sectors, long no_of_fat,
			long sec_per_fat) {

		return ((((address - start)) - rsrv_sectors - (no_of_fat * sec_per_fat)) / sec_per_cluster) + 2;
	}

	public static long logicalToCluster(long address, long sec_per_cluster,
			long rsrv_sectors, long no_of_fat, long sec_per_fat) {

		return ((((address)) - rsrv_sectors - (no_of_fat * sec_per_fat)) / sec_per_cluster) + 2;
	}

	public static long logicalToPhysical(long address, long start,
			boolean isBytes, long bytes_per_sec) {
		if (isBytes) {
			return (address + start) * bytes_per_sec;
		} else
			return address + start;
	}

	public static long clusterToLogical(long address, long sec_per_cluster,
			long rsrv_sectors, long no_of_fat, long sec_per_fat,
			boolean isBytes, long bytes_per_sec) {
		if (isBytes)
			return (rsrv_sectors + (no_of_fat) * sec_per_fat + (address - 2)
					* sec_per_cluster)
					* bytes_per_sec;
		else
			return rsrv_sectors + (no_of_fat) * sec_per_fat + (address - 2)
					* sec_per_cluster;
	}

	public static long clusterToPhysical(long address, long sec_per_cluster,
			long rsrv_sectors, long no_of_fat, long sec_per_fat, long start,
			boolean isBytes, long bytes_per_sec) {
		if (isBytes)
			return start
					* bytes_per_sec
					+ clusterToLogical(address, sec_per_cluster, rsrv_sectors,
							no_of_fat, sec_per_fat, isBytes, bytes_per_sec);
		else
			return start
					+ clusterToLogical(address, sec_per_cluster, rsrv_sectors,
							no_of_fat, sec_per_fat, isBytes, bytes_per_sec);
	}
}
