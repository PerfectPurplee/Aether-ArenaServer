package main;

import main.clients.ConnectedClient;
import main.clients.spells.Spell01;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.List;

public class ServerEngine extends Thread {

    Server server;
    private final int UPS_SET = 128;

    public static final int gameMapWidth = 1920;
    public static final int gameMapHeight = 1080;

    public ServerEngine() {
        server = new Server();

        this.start();
    }


    private synchronized void update() {
//        UPDATE ALL CLIENTS PLAYER POSITIONS AND CHOOSE SPRITE FOR ANIMATION
        ConnectedClient.listOfConnectedClients.forEach(connectedClient -> {
            connectedClient.playerClass.moveController();
            connectedClient.playerClass.updatePlayerHitboxWorld();
        });

        Spell01.updateAllSpells01();


        ConnectedClient.listOfConnectedClients.forEach(connectedClient -> {
            try {
                server.serverSocket.send(PacketManager.UpdateAllPlayersPositionsPacket(connectedClient));
                List<DatagramPacket> packetList = PacketManager.updateAllPlayerSpells(connectedClient);
                for (DatagramPacket packet : packetList) {
                    server.serverSocket.send(packet);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

//ConnectedClient.listOfConnectedClients.forEach(connectedClient -> System.out.println("ID: " + connectedClient.playerMovementHandler.clientID + "PosX: " + connectedClient.playerMovementHandler.playerPosXWorld));

    }

    @Override
    public void run() {
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
//                System.out.println("UPS: " + updates);

                updates = 0;

            }
        }
    }
}
