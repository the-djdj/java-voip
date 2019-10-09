package com.voip.audio;

import com.voip.Constants;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import static javax.sound.sampled.DataLine.Info;

/**
 * The AudioCapture class. This caputures audio from an audio line and creates an
 * audio buffer which can be sent over a network.
 *
 * @author Dan Jenkins
 */
public class AudioCapture {

    /** The data line for audio. */
    private final TargetDataLine line;

    /** The format for the audio. */
    private final AudioFormat format;

    /** The data line info for the audio. */
    private final Info info;

    /** The byte array for data being read in. */
    private byte[] data;

    /**
     * The default constructor. This creates a new AudioCapture instance and sets
     * up all of the line connections.
     */
    public AudioCapture() throws LineUnavailableException {

        // Create the audio format option
        this.format = new AudioFormat(Constants.AUDIO_SAMPLE_RATE,
                                      Constants.AUDIO_SAMPLE_SIZE,
                                      Constants.AUDIO_CHANNELS,
                                      Constants.AUDIO_SIGNED,
                                      Constants.AUDIO_BIG_ENDIAN);

        // Create the dataline info object
        this.info = new Info(TargetDataLine.class, this.format);

        // Check that we can actually use this dataline
        if (!AudioSystem.isLineSupported(this.info)) {

            // Do something

        }

        // Get the dataline
        this.line = (TargetDataLine) AudioSystem.getLine(this.info);

        // Create a variable to store data to be written to the buffer
        this.data = new byte[(int) this.format.getSampleRate()];

    }

    /**
     * The method which opens the data line so that audio can be read in.
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
    public byte[] read() {

        // Read in the data
        this.line.read(data, 0, data.length);

        // And return
        return data;

    }

    /**
     * The method which stops the AudioSender reading from the current stream.
     */
    public void stop() {

        // Make sure no audio is still in the line
        this.line.drain();
        this.line.stop();
        this.line.close();

    }

}

