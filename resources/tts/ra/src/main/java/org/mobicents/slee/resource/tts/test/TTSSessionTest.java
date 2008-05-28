package org.mobicents.slee.resource.tts.test;

import org.mobicents.slee.resource.tts.ra.TTSSessionImpl;
import org.mobicents.slee.resource.tts.ratype.TTSSession;

public class TTSSessionTest {
	public static void main(String args[]){
		System.out.println("Started....");
		
		TTSSession session = new TTSSessionImpl("/NotBackedUp/tmp/test.wav","kevin16");
		boolean ok = session.textToAudioFile("Your account balance is $52");
		
		System.out.println("End. Result = "+ok);
	}

}
