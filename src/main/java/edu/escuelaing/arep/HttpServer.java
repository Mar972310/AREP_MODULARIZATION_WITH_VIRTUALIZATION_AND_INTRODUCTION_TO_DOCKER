package edu.escuelaing.arep;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import edu.escuelaing.arep.FrameWork.FrameWorkSetting;
import edu.escuelaing.arep.RequestHandler.Impl.HttpRequestHandlerImpl;

/**
 *
 * @author Maria Valentina Torres Monsalve
 */

public class HttpServer {
    private static final int PORT = 35000;
    private boolean running = true;
    private ServerSocket serverSocket;
    private static String ruta = "src/main/java/edu/escuelaing/arep/resources";

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer server = new HttpServer();
        server.startServer();
    }

    /**
     * Starts the HTTP server, listens for incoming connections, 
     * and processes requests using HttpRequestHandler.
     * 
     * @throws IOException If an error occurs while starting the server.
     * @throws URISyntaxException If an error occurs while loading components.
     */
    public void startServer() throws IOException, URISyntaxException {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Failed to start server on port: " + PORT);
            throw e;
        }

        while (running) {
            try {
                FrameWorkSetting.loadComponents();
                System.out.println("Ready to receive on port: " + PORT +" ...");
                Socket clientSocket = serverSocket.accept();
                HttpRequestHandlerImpl requestHandler = new HttpRequestHandlerImpl(clientSocket,ruta);

                requestHandler.handlerRequest();
            } catch (IOException e) {
                if (!running) {
                    System.out.println("Server stopped.");
                    break;
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the HTTP server by closing the server socket 
     * and setting the running flag to false.
     */
    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server stopped successfully.");
            } catch (IOException e) {
                System.err.println("Error closing server: " + e.getMessage());
            }
        }
    }
}
