
package org.alcatel.jsce.statevent;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Vector;

final class CharBuffer {
   char[] buf;
   int length = 0;

   public CharBuffer(int length) {
      buf = new char[length + 1];
   }

   public boolean isEmpty() {
      return (length == 0);
   }

   public int fill(Reader in) throws IOException {
      int d;
      for (; length < buf.length; length++) {
         d = in.read();
         if (d == -1) {
            break;
         }
         else {
            buf[length] = (char) d;
         }
      }
      return length;
   }

   public void drop(int n) {
      for (int i = n; i < buf.length; i++) {
         buf[i - n] = buf[i];
      }
      length -= n;
   }

   public char pop() {
      char ret = buf[0];
      drop(1);
      return ret;
   }

   public boolean startsWith(char[] match) {
      for (int i = 0; i <= length && i < match.length; i++) {
         if (buf[i] != match[i]) {
            return false;
         }
      }
      return true;
   }
}

/**
 * Multiple string search and replace utility.
 * This class accepts any number of search+replace string pairs to be added.
 * It can then operate on either an input String or stream and replace all occurances
 * of any search pattern with its corresponding replace string.
 * <p>
 * Example that does html > and < encoding:
 * <pre>
 * Replacer htmlEscaper = new Replacer();
 * htmlEscaper.add("<", "&amp;lt;");
 * htmlEscaper.add(">", "&amp;gt;");
 * String escapedString = replacer.replace(aHtmlString);
 * </pre>
 */
public class Replacer {

   private Vector search = new Vector();
   private Vector replace = new Vector();

   CharBuffer buffer;

   public Replacer() {
   }

   public void add(String search1, String replace1) {
      this.search.add(search1.toCharArray());
      this.replace.add(replace1);
   }

   private void initBuffer() {
      int maxSize = 0;
      // Figure out the longest search string
      for (int i = 0; i < search.size(); i++) {
         int length = ( (char[]) search.get(i)).length;
         if (length > maxSize) {
            maxSize = length;
         }
      }
      buffer = new CharBuffer(maxSize);
   }

   public void processStreams(Reader in2, Writer out2) throws IOException {
      initBuffer();
      char[][] find = new char[search.size()][];
      for (int i = 0; i < find.length; i++) {
         find[i] = (char[]) search.get(i);

         // Wrap around buffered alternatives to increase speed
         //BufferedReader in2 = new BufferedReader(in);
         //BufferedWriter out2 = new BufferedWriter(out);

      }
      while (buffer.fill(in2) > 0) {
         int indexOfLargestMatch = -1;
         int lengthOfLargestMatch = 0;
         for (int i = 0; i < find.length; i++) {
            // Check for match on first character first to speed things up
            if (buffer.buf[0] == find[i][0] && buffer.startsWith(find[i]) &&
                find[i].length > lengthOfLargestMatch) {
               lengthOfLargestMatch = find[i].length;
               indexOfLargestMatch = i;
            }
         }
         if (indexOfLargestMatch != -1) {
            buffer.drop(lengthOfLargestMatch);
            out2.write( (String) replace.get(indexOfLargestMatch));
         }
         else { // No match
            out2.write(buffer.pop());
         }
      }
      out2.flush();
   }

   public String replace(String s) {
      //if (true) return s;
      StringReader reader = new StringReader(s);
      StringWriter writer = new StringWriter();
      try {
         processStreams(reader, writer);
         return writer.toString();
      }
      catch (IOException ex) {
         throw new RuntimeException(ex.toString());
      }
   }

   public static void main(String[] args) {
      Replacer replacer = new Replacer();
      replacer.add("$style", "[This is an exanded style variable]");
      replacer.add("$styleFile", "[This is an exanded styleFile variable]");
      String s = "This is a simple text with a $style and a $styleFile variable. Here comes a $style variable again.";
      System.out.println(replacer.replace(s));
   }
}

