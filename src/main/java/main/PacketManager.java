package main;

import datatransferobjects.Spell01DTO;
import main.clients.ConnectedClient;
import main.clients.spells.Spell01;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static main.EnumContainer.ServerClientConnectionCopyObjects;

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

    public static List<DatagramPacket> updateAllPlayerSpells(ConnectedClient client) throws IOException {

        final int packetType = 2;
        final int maxDataLength = 1406;
        final int loopByteSize = 24;
        int byteSize = 14;

        List<DatagramPacket> packetList = new ArrayList<>();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream dataOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        try {

            dataOutputStream.writeInt(packetType);
            dataOutputStream.writeInt(client.playerClass.clientID);
//            synchronized (Spell01DTO.listOfAllSpell01DTO) {

//                Spell01DTO.listOfAllSpell01DTO = Spell01DTO.listOfAllSpell01DTO.stream().filter(spell01DTO ->
//                        spell01DTO.spellPosXWorld >= -64 && spell01DTO.spellPosYWorld >= -64 &&
//                                spell01DTO.spellPosXWorld <= ServerEngine.gameMapWidth + 64 &&
//                                spell01DTO.spellPosYWorld <= ServerEngine.gameMapHeight + 64).collect(Collectors.toList());
//            }
            for (Spell01 spell01 : Spell01.listOfActiveSpell01s) {
                if (byteSize > maxDataLength) {
                    dataOutputStream.flush();

                    byte[] data = byteArrayOutputStream.toByteArray();
                    DatagramPacket datagramPacket = new DatagramPacket(data, data.length, client.clientIPaddress, client.port);
                    packetList.add(datagramPacket);
                    byteArrayOutputStream.close();
                    dataOutputStream.close();

                    byteArrayOutputStream = new ByteArrayOutputStream();
                    dataOutputStream = new ObjectOutputStream(byteArrayOutputStream);

                    dataOutputStream.writeInt(packetType);
                    dataOutputStream.writeInt(client.playerClass.clientID);
                }
                dataOutputStream.writeInt(spell01.spellID);
                dataOutputStream.writeInt(spell01.spellCasterClientID);
                dataOutputStream.writeFloat(spell01.normalizedVectorX);
                dataOutputStream.writeFloat(spell01.normalizedVectorY);
                dataOutputStream.writeFloat(spell01.spellPosXWorld);
                dataOutputStream.writeFloat(spell01.spellPosYWorld);

                byteSize += loopByteSize;
            }
            dataOutputStream.flush();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
//        System.out.println(data.length);

        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, client.clientIPaddress, client.port);
        packetList.add(datagramPacket);


        try {
            byteArrayOutputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("packet size" + packetList.size() + " Whole data length: " + data.length);
        return packetList;
    }


}

