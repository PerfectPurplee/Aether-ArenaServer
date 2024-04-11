package main;

import main.clients.ConnectedClient;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.Optional;

import static main.EnumContainer.AllPlayableChampions;
import static main.EnumContainer.ServerClientConnectionCopyObjects;

public class Server extends Thread {


    public DatagramSocket serverSocket;
    private InetAddress ipAddress;


    public Server() {

        try {
            this.serverSocket = new DatagramSocket(1337);
            this.ipAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
            System.out.println(ipAddress.getHostAddress());
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.start();

    }

    @Override
    public void run() {

        while (true) {
            receiveDataFromClient();

        }
    }

    private void receiveDataFromClient() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            serverSocket.receive(packet);


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            ObjectInputStream dataInputStream = new ObjectInputStream(byteArrayInputStream);

            int packetType = dataInputStream.readInt();

//            System.out.println("Server  recived packet TYPE:" + packetType);


//           PACKET TYPE 0 IS LOGIN PACKET FOR NEW CLIENT
            if (packetType == 0) {
                ServerClientConnectionCopyObjects.PLayer_Champion_Shared = (AllPlayableChampions) dataInputStream.readObject();
                ConnectedClient client = new ConnectedClient(packet.getAddress(), packet.getPort());

                serverSocket.send(PacketManager.LoginAnswerPacket(client));
            }
//           PACKET WITH INPUTS FOR CHARACTER MOVEMENT
            if (packetType == 1) {
                int connectedClientID = dataInputStream.readInt();

                Optional<ConnectedClient> connectedClient;

                connectedClient = ConnectedClient.listOfConnectedClients.stream()
                        .filter(element -> element.playerClass.clientID == connectedClientID).findFirst();

                if (connectedClient.isPresent()) {

                    connectedClient.get().playerClass.mouseClickXPos = dataInputStream.readInt();
                    connectedClient.get().playerClass.mouseClickYPos = dataInputStream.readInt();

                    connectedClient.get().playerClass.setVectorForPlayerMovement();
                }
            }
//            SPELL CASTING PACKET
            if (packetType == 2) {

                int connectedClientID = dataInputStream.readInt();

                Optional<ConnectedClient> connectedClient;

                connectedClient = ConnectedClient.listOfConnectedClients.stream()
                        .filter(element -> element.playerClass.clientID == connectedClientID).findFirst();

                if (connectedClient.isPresent()) {
                    boolean shouldCreateSpellQ = false;
                    boolean shouldCreateSpellW = false;
                    boolean shouldCreateSpellE = false;
                    boolean shouldCreateSpellR = false;
                    boolean shouldDash = false;
                    char spellType = dataInputStream.readChar();
                    int spellID = dataInputStream.readInt();
                    double spriteAngle = dataInputStream.readDouble();
                    if (spellType == 'Q') {
                        shouldCreateSpellQ = true;
                    }
                    if (spellType == 'W') {
                        shouldCreateSpellW = true;
                    }
                    if (spellType == 'E') {
                        shouldCreateSpellE = true;
                    }
                    if (spellType == 'R') {
                        shouldCreateSpellR = true;
                    }
                    if (spellType == 'D') {
                        shouldDash = true;
                    }
                    ServerClientConnectionCopyObjects.currentMousePosition = (Point) dataInputStream.readObject();
                    connectedClient.get().playerClass.spellCastController(shouldCreateSpellQ, shouldCreateSpellW, shouldCreateSpellE, shouldCreateSpellR, shouldDash, spellID, spriteAngle);
                }

            }
//            PLAYER CHANGE HERO PACKET
            if (packetType == 3) {
                int clientIDofPlayerChangingHero = dataInputStream.readInt();
                ServerClientConnectionCopyObjects.PLayer_Champion_Shared = (AllPlayableChampions) dataInputStream.readObject();
                Optional<ConnectedClient> connectedClient = ConnectedClient.listOfConnectedClients.stream().filter(
                        client -> client.playerClass.clientID ==
                                clientIDofPlayerChangingHero).findFirst();
                connectedClient.ifPresent(client -> client.playerClass.PlayerChampion = ServerClientConnectionCopyObjects.PLayer_Champion_Shared);

//                here we send information to all the players about player changing hero;

                ConnectedClient.listOfConnectedClients.stream().filter(client -> client.playerClass.clientID != clientIDofPlayerChangingHero)
                        .forEach(client -> {
                            try {
                                serverSocket.send(PacketManager.playerChangedChampionInformationPacket(client, connectedClient));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

            }
//            PLAYER DISCONNECTED PACKET
            if (packetType == 4) {
                int disconnectedClientID = dataInputStream.readInt();

//                here we send information to everyplayer other than disconnectedClient information about disconnect;
                ConnectedClient.listOfConnectedClients.stream().filter(connectedClient -> connectedClient.playerClass.clientID != disconnectedClientID)
                                .forEach(connectedClient -> {
                                    try {
                                        serverSocket.send(PacketManager.playerDisconnectedInformationPacket(connectedClient, disconnectedClientID));
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });

                ConnectedClient.listOfConnectedClients.removeIf(client -> client.playerClass.clientID == disconnectedClientID);

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



}
