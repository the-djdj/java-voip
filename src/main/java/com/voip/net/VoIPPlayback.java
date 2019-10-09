package com.voip.net;

import com.voip.Constants;
import com.voip.audio.AudioPlayback;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javax.sound.sampled.LineUnavailableException;

/**
 * The VoIPPlayback class. This collects data from a datagram stream, and plays
 * it to the client.
 *
 * @author Dan Jenkins
 */
public class VoIPPlayback implements Runnable {

    /** The playback object, for playing sound. */
    private final AudioPlayback playback;

    /** The multicast socket for receiving audio data. */
    private final MulticastSocket socket;

    /** The multicast group to receive messages from. */
    private final InetAddress group;

    /** Our own address, so that we can ignore our own packets. */
    private final InetAddress host;

    /** A string version of the packet address. */
    private String packetAddress;

    /** A string version of our own address. */
    private String hostAddress;

    /** The datagram packet to receive. */
    private DatagramPacket packet;

    /** The audio data being read. */
    private byte[] data;

    /** A boolean which determines whether we are currently reading audio. */
    private boolean reading;

    /**
     * The default constructor. This creates a new VoIPPlayback object capable
     * of receiving datagram packets with captured audio from a foreign host.
     *
     * @param multicast The multicast address to connect to.
     * @param host Our own address, allowing us to ignore our own packets.
     *
     * @throws LineUnavailableException If the line cannot be accessed.
     * @throws IOException If there is an issue with the data transfer.
     */
    public VoIPPlayback(InetAddress multicast, InetAddress host) throws
                                                    LineUnavailableException,
                                                    IOException {

        // Create the audio playback object to write information to.
        this.playback = new AudioPlayback();

        // Store the group to read messages from
        this.group = multicast;

        // Store our own hostname so that we can ignore packets from it.
        this.host = host;

        // Create the string address
        this.hostAddress  = this.host.toString();

        // Remove all the leading details
        this.hostAddress  = this.hostAddress.substring(
            this.hostAddress.indexOf('/') + 1);

        // Create the socket on which to receive data.
        this.socket = new MulticastSocket(Constants.VOIP_PORT);
        this.socket.setReuseAddress(true);
        this.socket.joinGroup(this.group);

        // Create the data buffer
        this.data = new byte[(int) this.playback.getFormat().getSampleRate()];

    }

    @Override
    public void run() {

        // Note that we are reading data
        this.reading = true;

        try {

            // Open the stream
            this.playback.open();

            // Check that we are still reading audio
            while (this.reading) {

                try {

                    // Prepare the packet
                    this.packet = new DatagramPacket(this.data,
                                                     this.data.length);

                    // Receive the packet
                    this.socket.receive(this.packet);

                    // Get the packet address
                    this.packetAddress = this.packet.getAddress().toString();

                    // Remove all the leading details
                    this.packetAddress = this.packetAddress.substring(
                                this.packetAddress.indexOf('/') + 1);

                    // And play the audio
                    if (!this.packetAddress.equals(this.hostAddress)) {

                        this.playback.write(data);

                    }

                } catch (IOException ex) {

                    ex.printStackTrace();

                }

            }

            // Finish reading audio
            this.playback.stop();

            // And stop the data transfer
            this.socket.leaveGroup(this.group);
            this.socket.close();

        } catch (LineUnavailableException | IOException ex) {

            // Something went wrong
            ex.printStackTrace();

        }

    }

    /**
     * The method which stops the VoIPPlayback reading from the current stream.
     */
    public void stop() {

        this.reading = false;

    }

}

