package main.clients;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectedClient {

    public static CopyOnWriteArrayList<ConnectedClient> listOfConnectedClients = new CopyOnWriteArrayList<>();


    public InetAddress clientIPaddress;
    public int port;
    public PlayerClass playerClass;

    public ConnectedClient(InetAddress clientIPaddress, int port) {
        playerClass = new PlayerClass();

        this.clientIPaddress = clientIPaddress;
        this.port = port;


        listOfConnectedClients.add(this);

    }
}
