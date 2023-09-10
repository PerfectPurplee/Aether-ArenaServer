package main;

import java.io.*;
import java.net.DatagramPacket;

public abstract class PacketManager {


    public static DatagramPacket LoginAnswerPacket(ConnectedClient client) throws IOException {
        final int packetType = 0;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream dataOutputStream = new ObjectOutputStream(byteArrayOutputStream);


        try {
            dataOutputStream.writeInt(packetType);
            dataOutputStream.writeInt(client.playerMovementHandler.clientID);
            dataOutputStream.writeFloat(client.playerMovementHandler.playerPosXWorld);
            dataOutputStream.writeFloat(client.playerMovementHandler.playerPosYWorld);
            dataOutputStream.flush();
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

    public static DatagramPacket UpdateAllPlayersPositionsPacket(ConnectedClient client) throws IOException {

        final int packetType = 1;
        final int howManyPlayersToUpdate = ConnectedClient.listOfConnectedClients.size();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        PlayerState.Current_Player_State_Shared = client.playerMovementHandler.Current_Player_State;

        try {

            objectOutputStream.writeInt(packetType);
            objectOutputStream.writeInt(howManyPlayersToUpdate);


            for (int i = 0; i < ConnectedClient.listOfConnectedClients.size(); i++) {
                PlayerState.Current_Player_State_Shared = ConnectedClient.listOfConnectedClients.get(i).playerMovementHandler.Current_Player_State;
                objectOutputStream.writeInt(ConnectedClient.listOfConnectedClients.get(i).playerMovementHandler.clientID);
                objectOutputStream.writeObject(PlayerState.Current_Player_State_Shared);
                objectOutputStream.writeFloat(ConnectedClient.listOfConnectedClients.get(i).playerMovementHandler.playerPosXWorld);
                objectOutputStream.writeFloat(ConnectedClient.listOfConnectedClients.get(i).playerMovementHandler.playerPosYWorld);

            }
            objectOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, client.clientIPaddress, client.port);

        try {
            byteArrayOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return datagramPacket;
    }

}

