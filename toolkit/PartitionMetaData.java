package org.toolkit;

public class PartitionMetaData {
	long startAddress;
	long length;
	String type;
	int number;
	String desc;
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public PartitionMetaData(long startAddress, long length, String type, int num, String desc) {
		super();
		this.startAddress = startAddress;
		this.length = length;
		this.type = type;
		this.number = num;
		this.desc = desc;
	}
	public long getStartAddress() {
		return startAddress;
	}
	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress;
	}
	public long getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
