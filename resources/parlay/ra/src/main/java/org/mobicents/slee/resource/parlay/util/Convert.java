
package org.mobicents.slee.resource.parlay.util;

/** 
 * <code>Convert</code> is a utility that provides conversion functions for java types.
 * For example converting a byte[] to a hex string.
 */
public class Convert {

    // table to convert a nibble to a hex char.
    private static char[] hexChar =
        { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };


    /**
     * Default private constructor.
     * Cannot create a new Convert with default properties, just use its methods.
     */
    private Convert() {
        super();
    }

    /**
     * convert a byte b to 2-char hex string with possible leading zero. 
     * @param b byte to be converted
     * @return String hex equivalent of byte
     */
    public static String toHexString(final byte b) {
        return Integer.toString((b & 0xff) + 0x100, 16).substring(1);
    }

    /**
     *  Fast convert a byte array to a hex string with possible leading zero. 
     * @param b hex string rep in byte array
     * @return String
     * @pre b != null
     */
    public static String toHexString(final byte[] b) {
        
        final StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            // look up high nibble char
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);

            // look up low nibble char
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * Convert a hex string to a byte array.
     * Permits upper or lower case hex.
     *
     * @param s String must have even number of characters.
     *   and be formed only of digits 0-9 A-F or a-f. No spaces, minus or plus signs.
     * @return corresponding byte array.
     * @pre s != null
     */
    public static byte[] fromHexString(final String s) {
        final int stringLength = s.length();
        if ((stringLength & 0x1) != 0) {
            throw new IllegalArgumentException("fromHexString requires an even number of hex characters");
        }
        byte[] b = new byte[stringLength / 2];

        for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
            b[j] = (byte) ((charToNibble(s.charAt(i)) << 4) | charToNibble(s.charAt(i + 1)));
        }
        return b;
    }

    /**
     * convert a single char to corresponding nibble.
     *
     * @param c char to convert. must be 0-9 a-f A-F, no spaces, plus or minus signs.
     * @return corresponding integer
     */
    private static int charToNibble(final char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 0xa;
        } else if ('A' <= c && c <= 'F') {
            return c - 'A' + 0xa;
        } else {
            throw new IllegalArgumentException("Invalid hex character: " + c);
        }
    }
    
    /**
     * Compares two byte arrays and returns true if they are equal
     *
     * @param b1 the first byte array
     * @param b2 the second byte array
     *
     * @return a boolean indicating whether the two arrays are equal.
     */
    public static boolean assertEquals(final byte[] b1, final byte[] b2) {
        
        if ( b1 == null || b2 == null ) {
            return false;
        }
        
        if (b1.length != b2.length) {
            return false;
        }

        int i = 0;
        while (i < b1.length) {
            if (b1[i] != b2[i]) {

                return false;
            }
            i++;
        }

        return true;
    }
    
    /**
     * Creates a four byte array from an integer.
     * @param x the integer to convert
     * @return byte[]
     */
    public static byte[] toByteArray(final int x) {
        byte[] oid = new byte[4];
        oid[0] = (byte)((x >> 24) & 0xFF);
        oid[1] = (byte)((x >> 16) & 0xFF);
        oid[2] = (byte)((x >> 8) & 0xFF);
        oid[3] = (byte)(x & 0xFF);
        
        return oid;
    }

}
