This project has 2 tasks.

Task 1: Conversion Utility
==========================
Tool A: Address Conversion:
--------------------------
This tool needs to perform different address conversions. The program has to execute the related conversion module bases on the command line inputs provided by the users. Functionalities and formulae: 
1. Logical To Physical Address: Logical Address + Offset 
2. Physical To Logical Address: Physical Address - Offset 
3. Physical To Cluster Address: (((physical address - start) - rsrv_sectors - (no_of_fat_tables * sec_per_fat)) / sec_per_cluster) + 2; 
4. Logical To Cluster Address: (( (logical address ) - rsrv_sectors - (no_of_fat_tables * sec_per_fat)) / sec_per_cluster) + 2; 
5. Cluster To Physical Address: startoffset+ rsrv_sectors + (no_of_fat) * sec_per_fat + (address - 2)* sec_per_cluster 
6. Cluster To Logical Address: rsrv_sectors + (no_of_fat) * sec_per_fat + (address - 2)* sec_per_cluster 
	
Also program checks the different input arguments and if the valid arguments are not provided it displays the error message with missing parameters. 
	
Tool B: MAC Conversion:
-----------------------
This tool performs the hex value to date or time based on the options given as arguments by the user. If the user selects the file option then programs reads the hex value from the file and converts it to the valid date or time. or user may also provide a hex value in the command line which has to be converted accordingly.

Implementation of conversion: Read the hex and convert it to binary string and read the correct offset based on option for time or date. Convert them to decimal and print the output to the user.

Task 2: Acquisition, Authentication, and Analysis
=================================================
This tool has to request user for the file and it must read the file to display the VBR and MBR data. First program read the file in read only mode using RandomAccessFile and FileInputStream for reading MBR and VBR, and calculate hash respectively. Program uses java.security API for calculating hash values for the Image file. For reading the MBR and VBR program uses RandomAccessFile class as it will be help full to jump the file pointer from one offset to the other easily rather than going sequentially . Output is printed to output file. This uses above utilities when ever required. 


