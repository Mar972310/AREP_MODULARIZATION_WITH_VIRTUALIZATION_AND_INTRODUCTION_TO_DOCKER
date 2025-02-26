package WebServerTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import edu.escuelaing.arep.HttpServer;

/**
 *
 * @author Maria Valentina Torres Monsalve
 */

public class WebServerTest {

    
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



    @Test
    public void shouldLoadStaticFileHtml()  {

        String file = "index.html";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticFileHtml()  {
        String file = "web.html";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticFileCss()  {
   
        String file = "style.css";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticFileCss()  {
    
        String file = "styles.css";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticFileJs()  {
        String file = "script.js";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticFileJs()  {
        String file = "prueba.js";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticImagePNG()  {
        String file = "imagen1.png";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadStaticImageJPG()  {
        
        String file = "imagen2.jpg";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticImagePNG()  {
        String file = "imagen8.png";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadStaticImageJPG()  {
        String file = "imagen20.jpg";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(404, responseCode);
            request.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadGreetingControllerWithQuery()  {
        String file = "app/greeting?name=maria";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Hello maria !\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadGreetingControllerWithoutQuery()  {
        String file = "app/greeting";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Hello world !\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadGreetingControllerWithQuery()  {
        String file = "app/greeting?name=maria";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"Hello juan !\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //voy aqui
    @Test
    public void shouldLoadMathControllerPIWithQuery()  {
        String file = "app/pi?decimals=5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"3,14159\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerPIWithoutQuery()  {
        String file = "app/pi";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"3,14\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerPIWithQuery()  {
        String file = "app/pi?decimals=5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"3,141\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerSumWithQuery()  {
        String file = "app/sum?number=3,2,5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerSumWithoutQuery()  {
        String file = "app/sum";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No numbers were entered\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerSumWithQuery()  {
        String file = "app/sum?number=5,7";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerRestWithQuery()  {
        String file = "app/rest?number=3,2,5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"-4\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerRestWithoutQuery()  {
        String file = "app/rest";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No numbers were entered\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerRestWithQuery()  {
        String file = "app/rest?number=5,7";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Test
    public void shouldLoadMathControllerMulWithQuery()  {
        String file = "app/mul?number=3,2,5";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"30\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerMulWithoutQuery()  {
        String file = "app/mul";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No numbers were entered\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerMul1WithoutQuery()  {
        String file = "app/mul?number=1";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Missing numbers for multiplication\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerMulWithQuery()  {
        String file = "app/mul?number=5,7";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    @Test
    public void shouldLoadMathControllerDivWithQuery()  {
        String file = "app/div?number=4,2";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"2.0\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerDiv2WithQuery()  {
        String file = "app/div?number=4,0";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Cannot divide by 0\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerDiv1WithoutQuery()  {
        String file = "app/div?number=1";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"Missing numbers for division\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldLoadMathControllerDivWithoutQuery()  {
        String file = "app/div";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertEquals("{\"response\":\"No numbers were entered\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notShouldLoadMathControllerDivWithQuery()  {
        String file = "app/mul?number=5,7";
        try {
            URL requestUrl = new URL(URL + file);
            HttpURLConnection request = (HttpURLConnection) requestUrl.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();
            assertEquals(200, responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String response = in.readLine();
            in.close();
            assertNotEquals("{\"response\":\"10\"}", response);
            request.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @AfterClass
    public static void tearDown(){
        try {
            server.stopServer();
            serverThread.join(); 
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}