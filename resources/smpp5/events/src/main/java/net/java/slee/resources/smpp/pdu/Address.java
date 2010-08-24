package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface Address {

	/**
	 * Define the Type Of Number (TON). See Section 4.7.1 addr_ton, source_addr_ton, dest_addr_ton, esme_addr_ton of
	 * SMPP Version 5.0 Specs
	 */
	public static final byte TON_UNKNOWN = 0;
	public static final byte TON_INTERNATIONAL = 1;
	public static final byte TON_NATIONAL = 2;
	public static final byte TON_NETWORK_SPECIFIC = 3;
	public static final byte TON_SUBSCRIBER_NUMBER = 4;
	public static final byte TON_ALPHANUMERIC = 5;
	public static final byte TON_ABBREVIATED = 6;

	/**
	 * Define the Numeric Plan Indicator (NPI). See Section 4.7.2 addr_npi, source_addr_npi, dest_addr_npi,
	 * esme_addr_npi of SMPP Version 5.0 Specs
	 */
	public static final byte NPI_UNKNOWN = 0;
	public static final byte NPI_ISDN = 1;
	public static final byte NPI_DATA = 3;
	public static final byte NPI_TELEX = 4;
	public static final byte NPI_LAND_MOBILE = 6;
	public static final byte NPI_NATIONAL = 8;
	public static final byte NPI_PRIAVTE = 9;
	public static final byte NPI_ERMES = 10;
	public static final byte NPI_INTERNET = 14;
	public static final byte NPI_WAP = 18;

	public String getAddress();

	public int getAddressNpi();

	public int getAddressTon();

}
