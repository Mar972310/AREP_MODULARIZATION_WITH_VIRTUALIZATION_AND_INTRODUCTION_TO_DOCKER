package edu.escuelaing.arep;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.escuelaing.arep.FrameWork.FrameWorkSetting;
import edu.escuelaing.arep.RequestHandler.Impl.HttpRequestHandlerImpl;

/**
 *
 * @author Maria Valentina Torres Monsalve
 */

public class HttpServer {
    private static int PORT = 35000 ;
    private boolean running = true;
    private ServerSocket serverSocket;
    private static String ruta = "target/classes/edu/escuelaing/arep/resources";
    private ExecutorService threadPool;

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer server = new HttpServer();
        server.settingServer();
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
            threadPool = Executors.newFixedThreadPool(10);
            serverSocket = new ServerSocket(PORT);
            System.out.println("Ready to receive on port: " + PORT +" ...");
        } catch (IOException e) {
            System.err.println("Failed to start server on port: " + PORT);
            throw e;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Apagando el servidor...");
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }
            stopServer();
        }));

        while (running) {
            try {
                FrameWorkSetting.loadComponents();
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new HttpRequestHandlerImpl(clientSocket,ruta));
            } catch (IOException e) {
                if (!running) {
                    System.out.println("Server stopped.");
                    break;
                }
                e.printStackTrace();
            }
        }
    }

    public void settingServer(){
        String port = System.getenv("PORT");
        String route = System.getenv("STATIC");
        if (port != null){
            PORT = Integer.parseInt(port);
        }
        if (route != null){
            ruta = route;
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
