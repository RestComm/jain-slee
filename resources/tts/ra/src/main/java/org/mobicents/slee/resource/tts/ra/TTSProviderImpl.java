package org.mobicents.slee.resource.tts.ra;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.tts.ratype.TTSProvider;
import org.mobicents.slee.resource.tts.ratype.TTSSession;

public class TTSProviderImpl implements TTSProvider {

	private static Logger logger = Logger.getLogger(TTSProviderImpl.class);

	private TTSResourceAdaptor ra;

	public TTSProviderImpl(TTSResourceAdaptor ra) {
		logger
				.debug("Constructor TTSProviderImpl(TTSResourceAdaptor ra) called.");
		this.ra = ra;
	}

	public TTSSession getNewTTSSession(String fileName, String voiceName) {
		TTSSession ttsSessionActivity = new TTSSessionImpl(fileName, voiceName);

		TTSActivityHandle handle = new TTSActivityHandle(ttsSessionActivity
				.getSessionId());
		return ttsSessionActivity;
	}

}
