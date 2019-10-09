package com.voip;

/**
 * The constants class. This defines all of the variables that the voip
 * application needs to run properly.
 */
public class Constants {

    /** The port used for VoIP communication. */
    public final static int VOIP_PORT = 6363;

    /** The number of samples taken per second. */
    public final static int AUDIO_SAMPLE_RATE = 44100;

    /** The number of bits in each sample. */
    public final static int AUDIO_SAMPLE_SIZE = 16;

    /** The number of channels in the audio stream (1 = mono, 2 = stereo). */
    public final static int AUDIO_CHANNELS = 1;

    /** Whether or not the audio is signed. */
    public final static boolean AUDIO_SIGNED = true;

    /** Whether or not the audio is big-endian. */
    public final static boolean AUDIO_BIG_ENDIAN = false;

}

