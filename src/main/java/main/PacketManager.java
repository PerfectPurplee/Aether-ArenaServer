package main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public abstract class PacketManager {


    public static DatagramPacket LoginAnswerPacket(ConnectedClient client) {
        final int packetType = 0;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeInt(packetType);
            dataOutputStream.writeFloat(client.playerMovementHandler.playerPosXWorld);
            dataOutputStream.writeFloat(client.playerMovementHandler.playerPosYWorld);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, client.clientIPaddress, client.port);

        try {
            byteArrayOutputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return datagramPacket;
    }

}

