package WebServerTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.escuelaing.arep.HttpServer;

public class HttpServerTest {

    private static String URL = "http://localhost:35000/";
    private static HttpServer server;
    private static Thread serverThread;

    @BeforeClass
    public static void setUp() {
        try {
            server = new HttpServer();
            serverThread = new Thread(() -> {
                try {
                    server.startServer();
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            });
            serverThread.start();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            server.stopServer();
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        int numRequests = 20;
        Thread[] threads = new Thread[numRequests];

        for (int i = 0; i < numRequests; i++) {
            threads[i] = new Thread(() -> {
                try {
                    URL url = new URL(URL + "index.html");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    int responseCode = connection.getResponseCode();

                    assertEquals("Incorrect response", 200, responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {}
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numRequests; i++) {
            threads[i].join();
        }
    }

    @Test
    public void testConcurrencyWithNonExistentFile() throws InterruptedException {
        int numRequests = 5;
        Thread[] threads = new Thread[numRequests];

        for (int i = 0; i < numRequests; i++) {
            threads[i] = new Thread(() -> {
                try {
                    URL url = new URL(URL + "myPage.html");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    int responseCode = connection.getResponseCode();

                    assertEquals("Incorrect response for nonexistent file", 404, responseCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numRequests; i++) {
            threads[i].join();
        }
    }
}
