package main;

import datatransferobjects.Spell01DTO;
import main.clients.ConnectedClient;

import java.io.*;
import java.net.DatagramPacket;
import java.util.stream.Collectors;

import static main.EnumContainer.*;

public abstract class PacketManager {


    public static DatagramPacket LoginAnswerPacket(ConnectedClient client) throws IOException {
        final int packetType = 0;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream dataOutputStream = new ObjectOutputStream(byteArrayOutputStream);


        try {
            dataOutputStream.writeInt(packetType);
            dataOutputStream.writeInt(client.playerClass.clientID);
            dataOutputStream.writeFloat(client.playerClass.playerPosXWorld);
            dataOutputStream.writeFloat(client.playerClass.playerPosYWorld);
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


        ServerClientConnectionCopyObjects.PLayer_Champion_Shared = client.playerClass.PlayerChampion;

        try {

            objectOutputStream.writeInt(packetType);
            objectOutputStream.writeInt(howManyPlayersToUpdate);


            for (int i = 0; i < ConnectedClient.listOfConnectedClients.size(); i++) {
                ServerClientConnectionCopyObjects.Current_Player_State_Shared = ConnectedClient.listOfConnectedClients.get(i).playerClass.Current_Player_State;
                ServerClientConnectionCopyObjects.PLayer_Champion_Shared = ConnectedClient.listOfConnectedClients.get(i).playerClass.PlayerChampion;
                objectOutputStream.writeInt(ConnectedClient.listOfConnectedClients.get(i).playerClass.clientID);
                objectOutputStream.writeObject(ServerClientConnectionCopyObjects.Current_Player_State_Shared);
                objectOutputStream.writeObject(ServerClientConnectionCopyObjects.PLayer_Champion_Shared);
                objectOutputStream.writeFloat(ConnectedClient.listOfConnectedClients.get(i).playerClass.playerPosXWorld);
                objectOutputStream.writeFloat(ConnectedClient.listOfConnectedClients.get(i).playerClass.playerPosYWorld);


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

    public static DatagramPacket updateAllPlayerSpells(ConnectedClient client) throws IOException {

        final int packetType = 2;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        try {

            objectOutputStream.writeInt(packetType);
            objectOutputStream.writeInt(client.playerClass.clientID);
            synchronized (Spell01DTO.listOfAllSpell01DTO) {

                    Spell01DTO.listOfAllSpell01DTO = Spell01DTO.listOfAllSpell01DTO.stream().filter(spell01DTO ->
                            spell01DTO.spellPosXWorld >= -64 && spell01DTO.spellPosYWorld >= -64 &&
                                    spell01DTO.spellPosXWorld <= ServerEngine.gameMapWidth + 64 &&
                                    spell01DTO.spellPosYWorld <= ServerEngine.gameMapHeight + 64).collect(Collectors.toList());
                objectOutputStream.writeObject(Spell01DTO.listOfAllSpell01DTO);
            }
            objectOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        System.out.println(data.length);
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

