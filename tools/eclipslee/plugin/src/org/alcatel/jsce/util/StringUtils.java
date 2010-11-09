
/**
 *   Copyright 2006 Alcatel, OSP.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.alcatel.jsce.util;

import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

/**
 *
 * <p>Title: JAIN Slee for the OSP</p>
 * <p>Description: JAIN Slee implementation on top of the Alcatel OSP platform</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Alcatel Namur</p>
 * @author Dominique Gallot
 * @version 1.0
 *
 * Some usefull string utils
 *
 */

public class StringUtils
{
    public static final char DEFAULT_SEPARATOR = ';';
    public static final String DEFAULT_SEPARATOR_STR = String.valueOf(DEFAULT_SEPARATOR);

    /**
     *  private constuctor
     */
    private StringUtils()
    {}

    /**
     * Create a string of the same char
     *
     * @param  a_len  The len of the string
     * @param  c      The char to use
     * @return        The string created
     */
    public static String getStringOfChars(int a_len, char c)
    {
        char chars[] = new char[a_len];
        for (int i = 0; i < a_len; i++)
        {
            chars[i] = c;
        }
        return new String(chars);
    }

    /**
     * Check is an string is empty
     *
     * @param  str  The string to test
     * @return      true/false
     */
    public static boolean isEmptyStr(String str)
    {
        return isEmptyStr(str, false);
    }

    /**
     *  Check is an string is empty
     *
     * @param  str   The string to test
     * @param  trim  trim the string before the test
     * @return       true/false
     */
    public static boolean isEmptyStr(String str, boolean trim)
    {
        if (str == null)
        {
            return true;
        }
        return trim ? str.trim().equals("") : str.equals("");
    }

    /**
     * Parse a string retrun each tocken separated by DEFAULT_SEPARATOR_STR
     *
     * @param  values  The string to parse
     * @return         The array of string parsed
     * @see DEFAULT_SEPARATOR_STR
     */
    public static String[] getStrings(String values)
    {
        return getStrings(values, DEFAULT_SEPARATOR_STR);
    }

    /**
     * Parse a string retrun each tocken separated by separator
     *
     * @param  values     The string to parse
     * @param  separator  The separator to use
     * @return            The array of string parsed
     */
    public static String[] getStrings(String values, String separator)
    {
        if (values == null)
        {
            return new String[0];
        }
        StringTokenizer st = new StringTokenizer(values, separator);
        Vector ve = new Vector();
        while (st.hasMoreTokens())
        {
            ve.add(st.nextElement());
        }
        String result[] = new String[0];
        result = (String[]) ve.toArray(result);
        return result;
    }

    /**
     * Parse a string retrun each tocken separated by separator
     *
     * @param  values     The string to parse
     * @param  separator  The separator to use
     * @return            The vector of string
     */
    public static Vector getStringsV(String values, String separator)
    {
        if (values == null)
        {
            return new Vector();
        }
        StringTokenizer st = new StringTokenizer(values, separator);
        Vector ve = new Vector();
        while (st.hasMoreTokens())
        {
            ve.add(st.nextElement());
        }
        return ve;
    }

    /**
     * find a string in an array of string.
     *
     * @param  value       The string to find
     * @param  values      The strings list to search
     * @param  ignoreCase  true/false
     * @return             The index of the string. -1 if not found.
     */
    public static int indexOf(String value, String[] values, boolean ignoreCase)
    {
        for (int i = 0; i < values.length; i++)
        {
            if (safeEquals(value, values[i], ignoreCase))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * A equals implemenetation which support null value.
     *
     * @param  s1          The first value
     * @param  s2          The second value
     * @param  ignoreCase  true/false
     * @return             equals or not
     */
    public static boolean safeEquals(String s1, String s2, boolean ignoreCase)
    {
        if (s1 == s2)
        {
            return true;
        }
        if (s1 == null)
        {
            return false;
        }
        if (s2 == null)
        {
            return false;
        }
        if (ignoreCase)
        {
            return s1.equalsIgnoreCase(s2);
        }
        else
        {
            return s1.equals(s2);
        }
    }

    public static String[] removePrefix(String[] cols, String subject)
    {
        if (subject == null)
        {
            return cols;
        }

        String result[] = new String[cols.length];
        for (int i = 0; i < cols.length; i++)
        {
            if (cols[i].startsWith(subject))
            {
                result[i] = cols[i].substring(subject.length());
            }
        }
        return result;
    }

    /**
     *  Convert an enumeration to an array of string
     *
     * @param  e  The enumearation
     * @return    The array of string
     */
    public static String[] enumerationToStrings(Enumeration e)
    {
        Vector result = new Vector();
        while (e.hasMoreElements())
        {
            result.add(e.nextElement().toString());
        }
        return (String[]) result.toArray(new String[0]);
    }

    /**
     * Create an array of obejct to an string, will use the DEFAULT_SEPARATOR
     *
     * @param  o  The array of object
     * @return    the string
     */
    public static String arrToStr(Object[] o)
    {
        return arrToStr(o, DEFAULT_SEPARATOR);
    }

    /**
     * Create an array of obejct to an string, will use the sep
     *
     * @param  o    The array of object
     * @param  sep  The separator
     * @return      The string
     */
    public static String arrToStr(Object[] o, char sep)
    {
        return arrToStr(o, sep, true);
    }

    /**
     * Create an array of obejct to an string, will use the sep
     *
     * @param  o    The array of object
     * @param  sep  The separator
     * @return      The string
     */
    public static String arrToStr(Object[] o, String sep)
    {
        return arrToStr(o, sep, true);
    }

    /**
     * Create an array of obejct to an string, will use the sep
     *
     * @param  o    The array of object
     * @param  sep  The separator
     * @return      The string
     */
    public static String arrToStr(Object[] o, char sep, boolean putNullValue)
    {
        if (o == null)
        {
            return null;
        }
        String result = "";
        for (int i = 0; i < o.length; i++)
        {
            if (!putNullValue)
            {
                if (o[i] == null)
                {
                    continue;
                }
            }

            if (i == o.length - 1)
            {
                result = result + String.valueOf(o[i]);
            }
            else
            {
                result = result + String.valueOf(o[i]) + sep;
            }
        }
        return result;
    }

    /**
     * Create an array of obejct to an string, will use the sep. Allow to strip the null value.
     *
     * @param  o    The array of object
     * @param  sep  The separator
     * @param  putNullValue Remove the null value ?
     * @return      The string
     */
    public static String arrToStr(Object[] o, String sep, boolean putNullValue)
    {
        if (o == null)
        {
            return null;
        }
        String result = "";
        for (int i = 0; i < o.length; i++)
        {
            if (!putNullValue)
            {
                if (o[i] == null)
                {
                    continue;
                }
            }

            if (i == o.length - 1)
            {
                result = result + String.valueOf(o[i]);
            }
            else
            {
                result = result + String.valueOf(o[i]) + sep;
            }
        }
        return result;
    }

    /**
     * Create an array of obejct to an string, will use the DEFAULT_SEPARATOR
     *
     * @param  o    The array of object
     * @return      The string
     */
    public static String arrToStr(Object o)
    {
        return arrToStr(o, DEFAULT_SEPARATOR);
    }

    /**
     * Create an array of obejct to an string, will use the sep
     *
     * @param  o    The array of object
     * @param  sep  The separator
     * @return      The string
     */
    public static String arrToStr(Object o, char sep)
    {
        if (o == null || !o.getClass().isArray())
        {
            return null;
        }

        StringBuffer result = new StringBuffer();
        int length = Array.getLength(o);
        for (int i = 0; i < length; i++)
        {
            result.append(Array.get(o, i));
            if (i != length - 1)
            {
                result.append(sep);
            }
        }
        return result.toString();
    }

    public static int getLength(Object a_string)
    {
        if (a_string == null)
            return 0;
        return a_string.toString().length();
    }

    public static int getLength(String a_string)
    {
        if (a_string == null)
            return 0;
        return a_string.length();
    }

    /**
     * Convert the tab char to a string. adv version.
     *
     * @param  aString  The string to convert
     * @return          The result
     */
    public static String removeTab(String aString)
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < aString.length(); i++)
        {
            int line = 0;
            int col = 0;
            char c = aString.charAt(i);
            switch (c)
            {
                case '\r':
                    break;
                case '\n':
                    col = 0;
                    line++;
                    break;
                case '\t':
                    int count = 8 - (col % 8);
                    result.append(getStringOfChars(count, ' '));
                    col += count;
                    break;
                default:
                    col++;
                    result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Capitalise the first char of a String
     *
     * @param  str  The string to capitalise
     * @return      The result
     */
    public static String firstCapitalize(String str)
    {
        if (str.length() < 1)
        {
            return str;
        }
        StringBuffer sb = new StringBuffer(str);
        char c = sb.charAt(0);
        c = String.valueOf(c).toUpperCase().charAt(0);
        sb.setCharAt(0, c);
        return sb.toString();
    }

    /**
     * Uncapitalise the first char of a String
     *
     * @param  str  The string to capitalise
     * @return      The result
     */
    public static String firstUncapitalize(String str)
    {
        if (str.length() < 1)
        {
            return str;
        }
        StringBuffer sb = new StringBuffer(str);
        char c = sb.charAt(0);
        c = String.valueOf(c).toLowerCase().charAt(0);
        sb.setCharAt(0, c);
        return sb.toString();
    }

    /**
     * Capitalise the first char of a String
     *
     * @param  a_string    The original text
     * @param  a_searchString    The text to search
     * @param  a_replaceString    The text to replace
     * @return             The string with the text replaced
     */

    public static String searchReplace(String a_string, String a_searchString, String a_replaceString)
    {
        int index = a_string.indexOf(a_searchString);
        while (index != -1)
        {
            a_string = a_string.substring(0, index) + a_replaceString + a_string.substring(index + a_searchString.length(), a_string.length());
            index = a_string.indexOf(a_searchString);
        }
        return a_string;
    }

    // dumpAsciiChars


    /**
     * Dump an the content of an array of byte as a hex dump
     *
     * @param  data  The array of byte
     * @return       The dump
     */
    public static String dumpHex(byte[] data)
    {
        StringBuffer result = new StringBuffer();
        ByteArrayInputStream buf = new ByteArrayInputStream(data);
        int lineBytes = 0;
        int i;
        byte[] b = new byte[16];
        String s;

        while (buf.available() > 0)
        {
            b[lineBytes] = (byte) buf.read();
            int v = b[lineBytes];
            v = v < 0 ? 256 + v : v;
            s = Integer.toHexString(v);
            if (s.length() < 2)
            {
                s = "0" + s;
            }

            result.append(s.toUpperCase() + " ");

            lineBytes++;
            if (lineBytes == b.length)
            {
                dumpAsciiChars(b, b.length);
                lineBytes = 0;
            }
            // if
        }
        // while

        if (lineBytes > 1)
        {
            // unfilled line?

            for (i = lineBytes; i < b.length; i++)
            {
                // pad out.
                result.append("   ");
            }
            dumpAsciiChars(b, lineBytes);
            // dump chars.
        }
        // if

        result.append('\n');
        return result.toString();
    }

    /**
     * Dump a stack trace to a string
     *
     * @param  aException  The exception to dump
     * @return             The string with the content
     */
    public static String printStacktrace(Throwable aException)
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(bout);
        aException.printStackTrace(pw);
        pw.flush();
        return new String(bout.toByteArray());
    }

    /**
     * Print a dump hex of the byte of array
     *
     * @param  b     The byte of array
     * @param  blen  The number of byte to dump
     */
    private static void dumpAsciiChars(byte[] b, int blen)
    {
        int lineBytes;

        System.out.print("            ");
        for (lineBytes = 0; lineBytes < blen; lineBytes++)
        {
            if ( (b[lineBytes] >= 32) && (b[lineBytes] <= 126))
            {
                System.out.print( (char) b[lineBytes]);
            }
            else
            {
                System.out.print(".");
            }
        }
        // for

        System.out.println();
    }

    // dumpHex

    public static void main(String[] args)
    {
        System.err.println(searchReplace("Dominique TITI-TOTO-HELLO", "TITI", "test"));
        System.err.println(searchReplace("Dominique TITI-TOTO-HELLO", "TOTO", "test"));
        System.err.println(searchReplace("Dominique TITI-TOTO-HELLO", "HELLO", "test"));
    }
}
