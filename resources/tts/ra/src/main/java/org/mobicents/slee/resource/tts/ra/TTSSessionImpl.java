package org.mobicents.slee.resource.tts.ra;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.server.UID;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import org.jboss.logging.Logger;
import org.mobicents.slee.resource.tts.ratype.TTSSession;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.RawFileAudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

public class TTSSessionImpl implements TTSSession {
	private Logger log = Logger.getLogger(this.getClass());

	private String sessionID = null;

	private String voiceName = null;

	private Voice voice;

	private String fileName = null;

	private AudioPlayer audioPlayer = null;

	/**
	 * Constructor for TTSSessionImpl
	 * 
	 * @param fileName
	 *            This is the path of the file with fileName where you want to
	 *            store the audio output. For example /NotBackedUp/tmp/test.wav.
	 *            
	 * 
	 * @param voiceName Used by VoiceManager to get a Voice for given voiceName 
	 */
	public TTSSessionImpl(String fileName, String voiceName) {

		log.debug("TTSSessionImpl() called. voiceName = " + voiceName
				+ " File Name = " + fileName);

		this.sessionID = (new UID()).toString();
		this.fileName = fileName;
		this.voiceName = voiceName;

		init();

	}

	private void init() {
		VoiceManager voiceManager = VoiceManager.getInstance();
		this.voice = voiceManager.getVoice(this.getVoiceName());
		this.voice.allocate();

		AudioFileFormat.Type type = getAudioType(fileName);

		if (type != null) {

			this.audioPlayer = new SingleFileAudioPlayer(getBasename(fileName),
					type);

		} else {
			try {
				this.audioPlayer = new RawFileAudioPlayer(fileName);
			} catch (IOException ioe) {
				log.error("Can't open " + fileName + " " + ioe, ioe);
			}
		}

		if (audioPlayer == null) {
			log
					.warn("AudioPlayer is null. Trying to instantiate default AudioPlayer");
			/*
			 * We are using the DefaultAudioPlayer. The return value will be
			 * non-null only if the DEFAULT_AUDIO_PLAYER system property has
			 * been set to the name of an AudioPlayer class, and that class is
			 * able to be instantiated via a no arg constructor.
			 */
			try {
				audioPlayer = voice.getDefaultAudioPlayer();
			} catch (InstantiationException e) {
				log
						.error(
								"Error in creating instance of DefualtAudioPlayer",
								e);
			}
		}

		this.voice.setAudioPlayer(audioPlayer);

	}

	public String getSessionId() {
		return sessionID;
	}

	public boolean textToAudioFile(String text) {
		boolean ok = batchTextToSpeech(text);
		dispose();
		return ok;

	}

	public boolean streamToAudioFile(InputStream is) {
		return streamToSpeech(is);

	}

	public boolean urltoAudioFile(String urlPath) {
		boolean ok = false;
		try {
			URL url = new URL(urlPath);
			InputStream is = url.openStream();
			ok = streamToSpeech(is);
		} catch (IOException ioe) {
			log.error("Can't read data from " + urlPath, ioe);
		}
		return ok;

	}

	public boolean filetoAudioFile(String filePath) {
		boolean ok = false;
		try {
			InputStream is = new FileInputStream(filePath);
			ok = streamToSpeech(is);
		} catch (IOException ioe) {
			log.error("Can't read data from " + filePath);
		}
		return ok;
	}

	private String getVoiceName() {
		if (this.voiceName == null) {
			return "kevin16"; // Hardcoded to kevin16
		}

		return voiceName;
	}

	/**
	 * Returns the audio type based upon the extension of the given file
	 * 
	 * @param file
	 *            the file of interest
	 * 
	 * @return the audio type of the file or null if it is a non-supported type
	 */
	private static AudioFileFormat.Type getAudioType(String file) {
		AudioFileFormat.Type[] types = AudioSystem.getAudioFileTypes();
		String extension = getExtension(file);

		for (int i = 0; i < types.length; i++) {
			if (types[i].getExtension().equals(extension)) {
				return types[i];
			}
		}
		return null;
	}

	/**
	 * Given a filename returns the extension for the file
	 * 
	 * @param path
	 *            the path to extract the extension from
	 * 
	 * @return the extension or <code>null</code> if none
	 */
	private static String getExtension(String path) {
		int index = path.lastIndexOf(".");
		if (index == -1) {
			return null;
		} else {
			return path.substring(index + 1);
		}
	}

	/**
	 * Given a filename returns the basename for the file
	 * 
	 * @param path
	 *            the path to extract the basename from
	 * 
	 * @return the basename of the file
	 */
	private static String getBasename(String path) {
		int index = path.lastIndexOf(".");
		if (index == -1) {
			return path;
		} else {
			return path.substring(0, index);
		}
	}

	/**
	 * Converts the given text to speech
	 * 
	 * @param text
	 *            the text to speak
	 * 
	 * @return true if the utterance was played properly
	 */
	public boolean textToSpeech(String text) {
		return voice.speak(text);
	}

	/**
	 * Converts the given text to speech *
	 * 
	 * @param text
	 *            the text to speak
	 * 
	 * @return true if the utterance was played properly
	 */
	private boolean batchTextToSpeech(String text) {
		boolean ok;
		voice.startBatch();
		ok = textToSpeech(text);
		voice.endBatch();
		return ok;
	}

	/**
	 * Converts the text contained in the given stream to speech.
	 * 
	 * @param is
	 *            the stream containing the text to speak
	 */
	public boolean streamToSpeech(InputStream is) {
		boolean ok;
		voice.startBatch();
		ok = voice.speak(is);
		voice.endBatch();
		return ok;
	}

	/**
	 * Shuts down this TTSSession synthesizer by closing the AudioPlayer and
	 * voice.
	 */
	public void dispose() {
		if (audioPlayer != null)
			audioPlayer.close();
		if (voice != null)
			voice.deallocate();
	}

}
