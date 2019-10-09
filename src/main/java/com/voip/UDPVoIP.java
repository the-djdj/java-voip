package com.voip;

import com.voip.net.VoIPCapture;
import com.voip.net.VoIPPlayback;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.LineUnavailableException;

/**
 * The UDPVoIP class. This collects data from the input line, sends it via UDP
 * to a foreign client, receives data from that client, and plays that audio
 * back to our local user.
 *
 * @author Dan Jenkins
 */
public class UDPVoIP {

    /** The host which to call. */
    private final InetAddress host;

    /** Our own address, for filtering our own packets. */
    private final InetAddress address;

    /** The object for receiving audio. */
    private final VoIPPlayback voipPlayback;

    /** The object for sending audio. */
    private final VoIPCapture voipCapture;

    /** The thread for sending audio. */
    private final Thread capture;

    /** The thread for receiving audio. */
    private final Thread playback;

    /**
     * The default constructor. This creates a new UDPVoIP object which controls
     * all of the data going in and out of the voip system, to a specified host.
     *
     * @param host The host to which we should connect.
     * @param address Our address, for ignoring packets
     *
     * @throws UnknownHostException If the host cannot be reached.
     * @throws SocketException If there is an issue with the data transfer.
     * @throws LineUnavailableException If the audio lines cannot be used.
     * @throws IOException If there is an issue with the data transfer.
     */
    public UDPVoIP(String host, String address) throws UnknownHostException,
                                       SocketException,
                                       LineUnavailableException,
                                       IOException {

        // Store the host to send messages to.
        this.host = InetAddress.getByName(host);
        this.address = InetAddress.getByName(address);

        // Create the audio senders and receivers
        this.voipPlayback = new VoIPPlayback(this.host, this.address);
        this.voipCapture = new VoIPCapture(this.host);

        // And create the respective threads
        this.playback = new Thread(this.voipPlayback);
        this.capture = new Thread(this.voipCapture);

    }

    /**
     * The method to start a VoIP call. This starts the capture and playbacks
     * using the current streams.
     */
    public void start() {

        this.playback.start();
        this.capture.start();

    }

    /**
     * The method which stops the the capture and playbacks from using the
     * current streams.
     */
    public void stop() {

        this.voipPlayback.stop();
        this.voipCapture.stop();

    }

}
