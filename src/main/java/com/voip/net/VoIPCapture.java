package com.voip.net;

import com.voip.Constants;
import com.voip.audio.AudioCapture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.LineUnavailableException;

/**
 * The VoIPCapture class. This collects data from an AudioCapture instance, and
 * sends it to a foreign host using datagrams.
 *
 * @author Dan Jenkins
 */
public class VoIPCapture implements Runnable {

    /** The host which will be called. */
    private final InetAddress host;

    /** The datagram socket for sending and receivign audio data. */
    private final DatagramSocket socket;

    /** The capture object, for recording sound. */
    private final AudioCapture capture;

    /** The datagram packet to send. */
    private DatagramPacket packet;

    /** The audio data being read. */
    private byte[] data;

    /** A boolean which determines whether we are currently reading audio. */
    private boolean reading;

    /**
     * The default constructor. This creates a new VoIPCapture object capable of
     * sending datagram packets with captured audio to a specified host.
     *
     * @param host The host to send messages to.
     *
     * @throws LineUnavailableException If the line cannot be accessed.
     * @throws SocketException If there is an issue with the data transfer.
     */
    public VoIPCapture(InetAddress host) throws LineUnavailableException,
                                                SocketException {

        // Store the host to send messages to.
        this.host = host;

        // Create the audio capture object to read information in.
        this.capture = new AudioCapture();

        // Create the socket on which to send data.
        this.socket = new DatagramSocket();

    }

    /**
     * The run method. This reads data from the audio input and sends it through
     * UDP to the foreign client.
     */
    @Override
    public void run() {

        // Note that we are reading data
        this.reading = true;

        try {

            // Open the stream
            this.capture.open();

            // Check that we are still reading audio
            while (this.reading) {

                // Capture the audio data
                this.data = this.capture.read();

                // Encapsulate it in a packet
                this.packet = new DatagramPacket(this.data, this.data.length,
                    this.host, Constants.VOIP_PORT);

                try {

                    // And send the data
                    this.socket.send(this.packet);

                } catch (IOException ex) {

                    // Something went wrong here
                    ex.printStackTrace();

                }

            }

            // Finish capturing audio
            this.capture.stop();

        } catch (LineUnavailableException ex) {

            // Something went wrong
            ex.printStackTrace();

        }

        // And close the network streams
        this.socket.close();

    }

    /**
     * The method which stops the VoIPCapture reading from the current stream.
     */
    public void stop() {

        this.reading = false;

    }

}

