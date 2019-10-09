package com.voip.audio;

import com.voip.Constants;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * The AudioPlayback class. This reads captured audio from an audio line and
 * reads in from the audio buffer.
 *
 * @author Dan Jenkins
 */
public class AudioPlayback {

    /** The data line for audio. */
    private final SourceDataLine line;

    /** The format for the audio. */
    private final AudioFormat format;

    /**
     * The default constructor. This creates a new AudioPlayback instance and
     * sets up all of the line connections.
     */
    public AudioPlayback() throws LineUnavailableException {

        // Create the audio format option
        this.format = new AudioFormat(Constants.AUDIO_SAMPLE_RATE,
                                      Constants.AUDIO_SAMPLE_SIZE,
                                      Constants.AUDIO_CHANNELS,
                                      Constants.AUDIO_SIGNED,
                                      Constants.AUDIO_BIG_ENDIAN);

        // Get the dataline
        this.line = AudioSystem.getSourceDataLine(this.format);

    }

    /**
     * The method which opens the data line so that audio can be read out.
     *
     * @throws LineUnavailableException If the line is unavailable.
     */
    public void open() throws LineUnavailableException {

        // Open the line
        this.line.open(this.format);

        // And start capturing audio
        this.line.start();

    }

    /**
     * The method which reads audio in from the audio line. This writes all
     * audio input into this class's output stream, allowing it to be fetched by
     * another thread elsewhere.
     */
    public void write(byte[] data) {

        // Read the next chunk of data, and write it to the output stream
        this.line.write(data, 0, data.length);

    }

    /**
     * The method which stops the AudioPlayback reading from the current stream.
     */
    public void stop() {

        // Make sure no audio is still in the line
        this.line.drain();
        this.line.stop();
        this.line.close();

    }

    /**
     * The method which returns the audio format object used in this class.
     *
     * @return The audio format.
     */
    public AudioFormat getFormat() {

        return this.format;

    }

}
