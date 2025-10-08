package org.example;

import org.example.API.controllers.AuthController;
import org.example.API.controllers.CarController;
import org.example.API.controllers.MaintenanceController;
import org.example.Domain.models.Car;
import org.example.Domain.models.Maintenance;
import org.example.Domain.models.MaintenanceType;
import org.example.Domain.models.User;
import org.example.DataAccess.services.AuthService;
import org.example.DataAccess.services.CarService;

import org.example.DataAccess.services.MaintenanceService;
import org.example.DataAccess.HibernateUtil;
import org.example.Server.SocketServer;

import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        var sessionFactory = HibernateUtil.getSessionFactory();
        AuthService authService = new AuthService(sessionFactory);
        AuthController authController = new AuthController(authService);

        CarService carService = new CarService(sessionFactory);
        CarController carController = new CarController(carService);

        int port = 7000;
        SocketServer server = new SocketServer(port, authController, carController);

        // Add shutdown hook to stop the server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down server...");
            server.stop();
        }));

        // Start server (blocking call)
        server.start();
        System.out.println("Socket server started on port " + port);

    }
}
