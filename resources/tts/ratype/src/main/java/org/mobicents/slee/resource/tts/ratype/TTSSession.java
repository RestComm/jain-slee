package org.mobicents.slee.resource.tts.ratype;

import java.io.InputStream;

public interface TTSSession {
	public String getSessionId();

	public boolean textToAudioFile(String text);

	public boolean streamToAudioFile(InputStream is);

	public boolean urltoAudioFile(String urlPath);
	
	public boolean filetoAudioFile(String filePath);	

}