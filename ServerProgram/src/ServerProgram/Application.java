package ServerProgram;

import ServerProgram.Client.Client;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Application {
    public static void main(String[] args) {
        Log.info("Program Started");

        try {
            InetAddress serverAddr = InetAddress.getByName("192.168.1.3");
            ServerSocket serverSocket = new ServerSocket(6678,50,serverAddr);
            Log.info("Socket Created");

            ArrayList<Client> clientList = new ArrayList<>();

            while (true) {
                Socket client = serverSocket.accept();
                Log.info("Connection Accepted From Client at " + client.getInetAddress().getHostAddress());
                Client newClient = new Client(client, clientList);
                clientList.add(newClient);
                newClient.start();

            }
        } catch (Exception e) {
            Log.error(e.toString());
        }
    }
}