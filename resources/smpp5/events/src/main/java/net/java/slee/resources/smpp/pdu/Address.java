package net.java.slee.resources.smpp.pdu;

public interface Address {

	public static final byte TON_UNKNOWN = 0;
	public static final byte TON_INTERNATIONAL = 1;
	public static final byte TON_NATIONAL = 2;
	public static final byte TON_NETWORK_SPECIFIC = 3;
	public static final byte TON_SUBSCRIBER_NUMBER = 4;
	public static final byte TON_ALPHANUMERIC = 5;
	public static final byte TON_ABBREVIATED = 6;

	public static final byte NPI_UNKNOWN = 0;
	public static final byte NPI_ISDN = 1;
	public static final byte NPI_DATA = 3;
	public static final byte NPI_TELEX = 4;
	public static final byte NPI_LAND_MOBILE = 5;
	public static final byte NPI_NATIONAL = 8;
	public static final byte NPI_PRIAVTE = 9;
	public static final byte NPI_ERMES = 10;
	public static final byte NPI_INTERNET = 14;
	public static final byte NPI_WAP = 18;

	public String getAddress();
	public String setAddress(String address);

	public int getAddressNpi();
	public void setAddressNpi(int addNpi);

	public int getAddressTon();
	public void setAddressTon(int addTon);
}
