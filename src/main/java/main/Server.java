package main;

import java.io.*;
import java.net.*;

public class Server extends Thread {


    private DatagramSocket serverSocket;
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
            System.out.println("Server  recived packet");

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

            int packetType = dataInputStream.readInt();

//           PACKET TYPE 0 IS LOGIN PACKET FOR NEW CLIENT
            if (packetType == 0) {
                ConnectedClient client = new ConnectedClient(packet.getAddress(), packet.getPort());
                serverSocket.send(PacketManager.LoginAnswerPacket(client));
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
