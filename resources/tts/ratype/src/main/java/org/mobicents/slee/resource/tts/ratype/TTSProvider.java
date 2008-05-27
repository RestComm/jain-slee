package org.mobicents.slee.resource.tts.ratype;


public interface TTSProvider {
	
	public TTSSession getNewTTSSession(String fileName, String voiceName);

}
