package main;

import main.clients.ConnectedClient;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Optional;

import static main.EnumContainer.*;

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
                synchronized (ConnectedClient.listOfConnectedClients) {
                    connectedClient = ConnectedClient.listOfConnectedClients.stream()
                            .filter(element -> element.playerClass.clientID == connectedClientID).findFirst();
                }
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
                synchronized (ConnectedClient.listOfConnectedClients) {
                    connectedClient = ConnectedClient.listOfConnectedClients.stream()
                            .filter(element -> element.playerClass.clientID == connectedClientID).findFirst();

                    if (connectedClient.isPresent()) {
                        ServerClientConnectionCopyObjects.ArrayOfPlayerCreateSpellRequests = (Boolean[]) dataInputStream.readObject();
                        ServerClientConnectionCopyObjects.currentMousePosition = (Point) dataInputStream.readObject();
                        System.out.println("success");
                        connectedClient.get().playerClass.spellCastController();

                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
