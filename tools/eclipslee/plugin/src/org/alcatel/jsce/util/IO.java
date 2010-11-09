
/**
 *   Copyright 2005 Alcatel, OSP.
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.alcatel.jsce.statevent.Replacer;
import org.alcatel.jsce.statevent.StringCodec;

/**
 * Utility class to simplify common file related operations
 * This class is LGPL licensed. Just keep this notice intact
 * @author David Ekholm
 */
public class IO {

   /**
    * Read a text file into a String. Autodetects UTF-8 and UTF-16 encodings
    */
   public static String readTextFile(File file, String encoding) throws
       IOException {
      byte[] buf = readBytes(file);
      StringCodec codec = new StringCodec();
      return codec.decode(buf, encoding);
   }
   
   public static String readFromInputStream(InputStream stream) throws IOException{
	   BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String strLine, fileContent="";
		while ((strLine = reader.readLine()) != null)   {
		    fileContent = fileContent+ strLine+"\n";
		 }
		return fileContent;
   }

   public static String readTextFile(File file) throws IOException {
      return readTextFile(file, System.getProperty("file.encoding"));
   }

   public static String readTextFile(String fileName) throws IOException {
      return readTextFile(new File(fileName));
   }

   public static String readTextUrl(URL textUrl) throws IOException {
      URLConnection conn = textUrl.openConnection();
      int length = conn.getContentLength();
      if (length == -1) throw new IOException("Couldn't connect to " + textUrl);
      byte[] buf = new byte[length];
      InputStream in = conn.getInputStream();
      in.read(buf);
      in.close();
      StringCodec codec = new StringCodec();
      return codec.decode(buf, System.getProperty("file.encoding"));
   }

   public static byte[] readBytes(File f) throws IOException {
      byte[] buf = new byte[ (int) f.length()];
      FileInputStream in = new FileInputStream(f);
      in.read(buf);
      in.close();
      return buf;
   }

   public static void writeBytes(byte[] buf, File f) throws IOException {
      FileOutputStream out = new FileOutputStream(f);
      out.write(buf);
      out.close();
   }

   public static void writeTextFile(String content, File file) throws
       IOException {
      writeTextFile(content, file, System.getProperty("file.encoding"));
   }

   public static void writeTextFile(String content, File file, String encoding) throws
       IOException {
//      OutputStreamWriter writer = new SmartOutputStreamWriter(new
      OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(
          file), encoding);
      writer.write(content);
      writer.close();
   }

   /**
    * Write text to text file if this means a change to the file content
    * @param content Text to write
    * @param file File to be written. Will make a comparison before writing if file already exists
    * @param encoding Text encoding to use
    * @return boolean true if a change was actually made
    * @since v4.5
    */
   public static boolean writeChangedTextFile(String content, File file, String encoding) throws
       IOException {
      if (!file.exists()) {
         writeTextFile(content, file, encoding);
         return true;
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
      writer.write(content);
      writer.close();
      byte[] outBytes = out.toByteArray();
      byte[] inBytes = readBytes(file);
      if (!Arrays.equals(outBytes, inBytes)) {
         writeBytes(outBytes, file);
         return true;
      }
      else return false;
   }

   /**
    * Read an equal sign separated list of name=value pairs into a Property object.
    * Does not handle names with spaces and is limited to ISO-8859-1. See readMapFile()
    */
   public static Properties readPropertyFile(File file) throws IOException {
      FileInputStream fis = null;
      try {
         Properties props = new Properties();
         fis = new FileInputStream(file);
         props.load(fis);
         return props;
      }
      finally {
         if (fis != null) fis.close();
      }
   }

   /**
    * Read an equal sign separated list of name=value pairs into a Map object.
    * Handles names containing spaces.
    * Respects current system encoding and auto-detects UTF-8 and UTF-16 encoding.
    * Lines beginning with # are considered comment lines
    * Values ending in backslash is considered to be a multi-line value
    * Backslashes in values are expressed as double backslashes
    */
   public static Map readMapFile(File file) throws IOException {
      Replacer backslashDecoder = new Replacer();
      backslashDecoder.add("\\\\", "\\");

      String s = readTextFile(file);
      BufferedReader in = new BufferedReader(new StringReader(s));
      try {
         Map theMap = new HashMap();
         String line;
         while ( (line = in.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("#"))continue;
            int equalsIndex = line.indexOf('=');
            if (equalsIndex != -1) {
               String key = line.substring(0, equalsIndex).trim();
               String value = line.substring(equalsIndex + 1).trim();
               while (continueLine(line)) { // Line ends in a backslash, continue on next line
                  value = value.substring(0, value.length() - 1);
                  line = in.readLine();
                  if (line == null)break;
                  value += "\n" + line;
               }
               value = backslashDecoder.replace(value);
               theMap.put(key, value);
            }
         }
         return theMap;
      }
      finally {
         if (in != null) {
            in.close();
         }
      }
   }

   /**
    * Write a Map to file.
    * Tries to write using system encoding. It that doesn't work, uses UTF-8
    * Multi-line values result in lines ending in backslashes
    * Backslashes in values generate double backslashes
    * @throws IOException
    */
   public static void writeMapFile(Map map, File f) throws IOException {
      Replacer backslashEncoder = new Replacer();
      backslashEncoder.add("\\", "\\\\");
      backslashEncoder.add("\n", "\\" + System.getProperty("line.separator"));

      StringWriter writer = new StringWriter();
      PrintWriter out = new PrintWriter(writer);

      Iterator it = map.entrySet().iterator();
      while (it.hasNext()) {
         Map.Entry e = (Map.Entry) it.next();
         out.println(e.getKey().toString() + "=" +
                     backslashEncoder.replace(e.getValue().toString()));
      }
      out.close();
      String s = writer.getBuffer().toString();
      StringCodec codec = new StringCodec();
      byte[] bytes = codec.encode(s);
      if (!s.equals(codec.decode(bytes))) bytes = codec.encode(s, "UTF-8");
      writeBytes(bytes, f);
   }

   /*
    * Returns true if the given line is a line that must
    * be appended to the next line (from the Property object of SDK1.4)
    */
   private static boolean continueLine(String line) {
      int slashCount = 0;
      int index = line.length() - 1;
      while ( (index >= 0) && (line.charAt(index--) == '\\'))
         slashCount++;
      return (slashCount % 2 == 1);
   }

   /**
    * Copy file or directory to destination. Preserves file modification date
    * @param File to copy
    * @param dest Destination. May point either to a file or a directory
    */
   public static void copyFile(File src, File dest) throws IOException {
      copyFile(src, dest, true);
   }

   /**
    * Copy file or directory to destination. Preserves file modification date
    * @param name Name of file to copy
    * @param dest Destination. May point either to a file or a directory
    */
   public static void copyFile(String name, File dest) throws IOException {
      copyFile(name, dest, true);
   }

   /**
    * Copy file or directory to destination. Preserves file modification date
    * @param name Name of file to copy
    * @param dest Destination. May point either to a file or a directory
    * @param forceCopy Always copy, even if destination file already exist
    */
   public static void copyFile(String name, File dest, boolean forceCopy) throws
       IOException {
      copyFile(new File(name), dest, forceCopy);
   }

   /**
    * Copy file or directory to destination. Preserves file modification date
    * @param src File to copy
    * @param dest Destination. May point either to a file or a directory
    * @param forceCopy Always copy, even if destination file already exist
    */
   public static void copyFile(File src, File dest, boolean forceCopy) throws
       IOException {
      if (src.isDirectory()) { // v3.5
         if (!dest.isDirectory()) {
            throw new IOException(
                "copyFile: Cannot copy directory onto single file");
         }
         File[] files = src.listFiles();
         for (int i = 0; i < files.length; i++) {
            copyFile(files[i], dest, forceCopy);
         }
      }
      else {
         if (dest.isDirectory()) {
            dest = new File(dest, src.getName());
         }
         if (!forceCopy && dest.exists())return;
         if (dest.exists() && !dest.canWrite())return;

         // Preserve file modification date
         long lastModified = src.lastModified();
         // Start copying
         BufferedInputStream in = new BufferedInputStream(new FileInputStream(
             src));
         BufferedOutputStream out = new BufferedOutputStream(new
             FileOutputStream(
             dest));
         try {
            byte[] buffer = new byte[65536];
            while (true) {
               int bytesRead = in.read(buffer);
               if (bytesRead == -1) {
                  break;
               }
               out.write(buffer, 0, bytesRead);
            }
         }
         finally {
            in.close();
            out.close();
            dest.setLastModified(lastModified);
         }
      }
   }

   /**
    * Copy a directory (including its subdirectories) to destination directory.
    * @param srcPath Path to source directory
    * @param dest Destination directory. Create one if not existing already
    * @param forceCopy Always copy, even if destination files already exists
    */
   public static void copyDirectoryContent(String srcPath, File dest,
                                           boolean forceCopy) throws
       IOException {
      copyDirectoryContent(new File(srcPath), dest, forceCopy);
   }

   public static void copyDirectoryContent(File srcDir, File dest,
                                           boolean forceCopy) throws
       IOException {
      dest.mkdir(); // create if nonexistent
      if (!srcDir.isDirectory()) {
         throw new IOException("Missing directory " + srcDir.getAbsolutePath());
      }
      File[] files = srcDir.listFiles();
      for (int i = 0; i < files.length; i++) {
         if (files[i].isDirectory()) {
            copyDirectoryContent(files[i], new File(dest, files[i].getName()),
                                 forceCopy); // Recurse
         }
         else if (files[i].isFile()) {
            copyFile(files[i].getAbsolutePath(), dest, forceCopy);
         }
      }
   }

   /**
    * Strip file extension from file name (if extension exists)
    */
   public static String baseName(String fullName) {
      int dotIndex = fullName.lastIndexOf('.');
      return (dotIndex != -1) ? fullName.substring(0, dotIndex) : fullName;
   }

   /**
    * Return a path to file that is relative to rel
    */
   public static String relativePath(File file, File rel) {
      //if (!rel.isDirectory()) rel = rel.getParentFile();
      String fileString = file.getAbsolutePath();
      String relString = rel.getAbsolutePath();
      char[] filePath = fileString.toCharArray();
      char[] relPath = relString.toCharArray();
      StringBuffer result = new StringBuffer();
      char separatorChar = File.separatorChar;
     // String separator = File.separator;

      // Step forward until the two paths differ
      int i;
      for (i = 0; i < filePath.length && i < relPath.length; i++) {
         if (filePath[i] != relPath[i]) {
            break;
         }
      }
      // Directory names might start equal so reverse to start of current path element unless file is a subdirectory of rel
      if (i < relPath.length && i < filePath.length) {
         while (i > 0 && relPath[i - 1] != separatorChar) {
            i--;

         }
      }
      if (i != 0) { // Not different discs, add parent travel code: ../..
         StringTokenizer tokens = new StringTokenizer(relString.substring(i),
             File.separator);
         while (tokens.hasMoreTokens()) {
            tokens.nextToken();
            result.append("../");
         }
      }
      // Append remaining path to file
      StringTokenizer tokens = new StringTokenizer(fileString.substring(i),
          File.separator);
      if (tokens.hasMoreTokens()) {
         result.append(tokens.nextToken());
      }
      else if (result.length() > 0 && result.charAt(result.length() - 1) == '/') {
         result.deleteCharAt(result.length() - 1); // Remove extra ending /
      }
      while (tokens.hasMoreTokens()) {
         result.append("/" + tokens.nextToken());
      }
      // Fix for empty relative path
      String res = result.toString();
      return res.equals("") ? "." : res;
   }

   /**
    * Provides a method to encode any string into a URL-safe
    * form.
    * Non-ASCII characters are first encoded as sequences of
    * two or three bytes, using the UTF-8 algorithm, before being
    * encoded as %HH escapes.
    */
   /*
     final static String[] hex = {
       "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
       "%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
       "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
       "%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
       "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
       "%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
       "%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
       "%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
       "%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
       "%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
       "%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
       "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
       "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
       "%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
       "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
       "%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
       "%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
       "%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
       "%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
       "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
       "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
       "%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
       "%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
       "%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
       "%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
       "%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
       "%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
       "%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
       "%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
       "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
       "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
       "%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
     };
    */
   /**
    * Encode a string to the "x-www-form-urlencoded" form, enhanced
    * with the UTF-8-in-URL proposal. This is what happens:
    *
    * <ul>
    * <li><p>The ASCII characters 'a' through 'z', 'A' through 'Z',
    *        and '0' through '9' remain the same.
    *
    * <li><p>The unreserved characters - _ . ! ~ * ' ( ) remain the same.
    *
    * <li><p>The space character ' ' is converted into a plus sign '+'.
    *
    * <li><p>All other ASCII characters are converted into the
    *        3-character string "%xy", where xy is
    *        the two-digit hexadecimal representation of the character
    *        code
    *
    * <li><p>All non-ASCII characters are encoded in two steps: first
    *        to a sequence of 2 or 3 bytes, using the UTF-8 algorithm;
    *        secondly each of these bytes is encoded as "%xx".
    * </ul>
    *
    * @param s The string to be encoded
    * @return The encoded string
    */

   /*
     public static String urlEncode(String s)
     {
       StringBuffer sbuf = new StringBuffer();
       int len = s.length();
       for (int i = 0; i < len; i++) {
         int ch = s.charAt(i);
         if ('A' <= ch && ch <= 'Z') {		// 'A'..'Z'
    sbuf.append((char)ch);
         } else if ('a' <= ch && ch <= 'z') {	// 'a'..'z'
    sbuf.append((char)ch);
         } else if ('0' <= ch && ch <= '9') {	// '0'..'9'
    sbuf.append((char)ch);
         } else if (ch == ' ') {			// space
    sbuf.append('+');
         } else if (ch == '-' || ch == '_'		// unreserved
             || ch == '.' || ch == '!'
             || ch == '~' || ch == '*'
             || ch == '\'' || ch == '('
             || ch == ')') {
    sbuf.append((char)ch);
         } else if (ch <= 0x007f) {		// other ASCII
    sbuf.append(hex[ch]);
         } else if (ch <= 0x07FF) {		// non-ASCII <= 0x7FF
    sbuf.append(hex[0xc0 | (ch >> 6)]);
    sbuf.append(hex[0x80 | (ch & 0x3F)]);
         } else {					// 0x7FF < ch <= 0xFFFF
    sbuf.append(hex[0xe0 | (ch >> 12)]);
    sbuf.append(hex[0x80 | ((ch >> 6) & 0x3F)]);
    sbuf.append(hex[0x80 | (ch & 0x3F)]);
         }
       }
       return sbuf.toString();
     }
    */

   private static char[] hexDigits = {
       '0', '1', '2', '3', '4', '5', '6', '7',
       '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

 

   /**
    * Combine two path parts so that there is one and only one path separator between
    * nomatter how each part looks.
    * @return String combined path
    */
   public static String combinePaths(String part1, String part2, char separator) {
      if (part1.length() == 0) return part2;
      if (part2.length() == 0) return part1;

      int end, start;
      for (end=part1.length(); end>0 && part1.charAt(end-1) == separator; end--);
      if (part1.endsWith("" + separator+separator)) end++; // Respect double separators, eg http://
      for (start = 0; start < part2.length() && part2.charAt(start) == separator; start++);
      return part1.substring(0, end) + separator + part2.substring(start, part2.length());
   }

   /**
    * Return number of bytes a serialized version of the passed object would occupy
    * @param o Object, must implement serializable
    */
   public static int sizeof(Object o) {
      try {
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(ba);
         oos.writeObject(o);
         return ba.toByteArray().length;
      }
      catch (IOException ex) {
         throw new RuntimeException(ex.toString());
      }
   }

   /**
    * TESTING
    */
   public static void main(String[] args) throws IOException {
      String empty = "";
      Integer i = new Integer(42);
      String david = "David Ekholm";
      HashMap hm = new HashMap();
      hm.put(empty, i);
      hm.put(david, i);
      System.out.println(sizeof(empty));
      System.out.println(sizeof(i));
      System.out.println(sizeof(david));
      System.out.println(sizeof(hm));

/*
      System.out.println(combinePaths("aaaa", "bbbb", '/'));
      System.out.println(combinePaths("aaaa/", "/bbbb", '/'));
      System.out.println(combinePaths("aaaa/", "", '/'));
      System.out.println(combinePaths("", "", '/'));
*/

/*
      File f = new File("C:/testmap.properties");

      Map map = new HashMap();
      map.put("akey", "avalue");
      map.put("another key", "another value\na second row and some Russian: \u0417\u0430\u0445\u043E\u0442\u0435\u043B\u043E\u0441\u044C \u0442\u0435\u043F\u043B\u043E\u0433\u043E \u043C\u043E\u0440\u044F");
      map.put("a little Swedish row", "���");
      IO.writeMapFile(map, f);
      System.out.println("Console output: " + "Some Russian: \u0417\u0430\u0445\u043E\u0442\u0435\u043B\u043E\u0441\u044C \u0442\u0435\u043F\u043B\u043E\u0433\u043E \u043C\u043E\u0440\u044F");

      Map newMap = IO.readMapFile(f);
      System.out.println(newMap);
*/
/*
             File to = new File("C:/a/dir/file");
             File from = new File("C:/b/dir");
             System.out.println("To go from " + from + " to " + to + ": " +
                         relativePath(to, from));
             to = new File("D:/foo/bar");
             System.out.println("To go from " + from + " to " + to + ": " +
                         relativePath(to, from));
             to = from;
             System.out.println("To go from " + from + " to " + to + ": " +
                         relativePath(to, from));
             to = new File("C:/b/dir/subdir");
             System.out.println("To go from " + from + " to " + to + ": " +
                         relativePath(to, from));
             to = to = new File("C:/ba/dir/file");
             System.out.println("To go from " + from + " to " + to + ": " +
                         relativePath(to, from));
             to = new File("C:/a/dir/file");
             from = new File("C:/ab/dir");
             System.out.println("To go from " + from + " to " + to + ": " +
                         relativePath(to, from));
             to = new File("C:/ab/");
             System.out.println("To go from " + from + " to " + to + ": " +
                         relativePath(to, from));
 */

   }

}


