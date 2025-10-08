package org.example.Server;

import com.google.gson.Gson;
import org.example.API.controllers.AuthController;
import org.example.API.controllers.CarController;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final AuthController authController;
    private final CarController carController;
    private final Gson gson = new Gson();

    public ClientHandler(Socket clientSocket,
                         AuthController authController,
                         CarController carController) {
        this.clientSocket = clientSocket;
        this.authController = authController;
        this.carController = carController;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputJson;

            while ((inputJson = in.readLine()) != null) {
                RequestDto request = gson.fromJson(inputJson, RequestDto.class);
                ResponseDto response;

                // Routing by controller
                switch (request.getController()) {
                    case "Auth":
                        response = authController.route(request);
                        break;
                    case "Cars":
                        response = carController.route(request);
                        break;
                    default:
                        response = new ResponseDto(false, "Unknown controller", null);
                }

                // Simular procesamiento
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                out.println(gson.toJson(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (IOException ignore) {}
        }
    }
}