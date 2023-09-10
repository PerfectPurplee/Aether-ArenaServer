package main;

import java.io.*;
import java.net.*;
import java.util.Optional;

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

            System.out.println("Server  recived packet TYPE:" + packetType);


//           PACKET TYPE 0 IS LOGIN PACKET FOR NEW CLIENT
            if (packetType == 0) {
                ConnectedClient client = new ConnectedClient(packet.getAddress(), packet.getPort());
                serverSocket.send(PacketManager.LoginAnswerPacket(client));
            }
//           PACKET WITH INPUTS FOR CHARACTER MOVEMENT
            if(packetType == 1) {
                int connectedClinetID = dataInputStream.readInt();

                Optional<ConnectedClient> connectedClient;
                synchronized (ConnectedClient.listOfConnectedClients) {
                     connectedClient = ConnectedClient.listOfConnectedClients.stream()
                            .filter(element -> element.playerMovementHandler.clientID == connectedClinetID).findFirst();
                }
                if (connectedClient.isPresent()) {

                connectedClient.get().playerMovementHandler.mouseClickXPos = dataInputStream.readInt();
                connectedClient.get().playerMovementHandler.mouseClickYPos = dataInputStream.readInt();

                connectedClient.get().playerMovementHandler.playerMovementStartingPosX = connectedClient.get().playerMovementHandler.playerPosXWorld;
                connectedClient.get().playerMovementHandler.playerMovementStartingPosY = connectedClient.get().playerMovementHandler.playerPosYWorld;

                connectedClient.get().playerMovementHandler.setVectorForPlayerMovement();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    TO BEDZIE MOVEMENT PACKET
//    private void sendDataToClient() {
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
//
//        try {
//            dataOutputStream.writeFloat(playerPosXWorld);
//            dataOutputStream.writeFloat(playerPosYWorld);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        byte[] data = byteArrayOutputStream.toByteArray();
//        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, clientIpAddress, port);
//
//        try {
//            serverSocket.send(datagramPacket);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }


}
