package org.example.Server;

import org.example.API.controllers.AuthController;
import org.example.API.controllers.CarController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private final int port; //5000 - 7000
    private final AuthController authController;
    private final CarController carController;
    private ServerSocket serverSocket; // keep a reference

    public SocketServer(int port, AuthController authController, CarController carController) {
        this.port = port;
        this.authController = authController;
        this.carController = carController;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Socket server started on port " + port);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, authController, carController)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close server socket to release port
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    System.out.println("Server socket closed.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server stopped.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}