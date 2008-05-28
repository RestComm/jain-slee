package org.jivesoftware.smack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class DebugInputStreamReader extends InputStreamReader {

	public DebugInputStreamReader(InputStream in, String charsetName) throws UnsupportedEncodingException {
		super(in, charsetName);
	}

	public DebugInputStreamReader(InputStream in, CharsetDecoder dec) {
		super(in, dec);
	}

	public DebugInputStreamReader(InputStream in, Charset cs) {
		super(in, cs);		
	}

	public DebugInputStreamReader(InputStream in) {
		super(in);
	}
	
	public int read() throws IOException {
		int ch = super.read();
		System.out.println("DebugInputStreamReader #"+hashCode()+": "+(char)ch);
		return ch;
	}
	
	public int read(char[] cbuf) throws IOException {
		int rtn = super.read(cbuf);
		System.out.println("DebugInputStreamReader #"+hashCode()+": "+String.copyValueOf(cbuf));
		return rtn;
	}
	
	public int read(char[] cbuf, int offset, int length) throws IOException {
		int rtn = super.read(cbuf, offset, length);
		System.out.println("DebugInputStreamReader #"+hashCode()+": "+String.copyValueOf(cbuf,offset,length));
		return rtn;
	}
	
}
