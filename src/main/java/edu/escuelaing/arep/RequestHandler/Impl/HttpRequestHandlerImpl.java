package edu.escuelaing.arep.RequestHandler.Impl;
import java.net.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import edu.escuelaing.arep.FrameWork.FrameWorkSetting;
import edu.escuelaing.arep.annotation.RequestParam;

/**
 * The HttpRequestHandler class is responsible for handling HTTP requests from clients.
 * It processes the request, determines the appropriate response, and sends it back to the client.
 */
public class HttpRequestHandlerImpl {

    private final Socket clientSocket;
    private String ruta;
    PrintWriter out ;
    BufferedReader in ;
    BufferedOutputStream bodyOut ;

    /**
     * Constructs an HttpRequestHandler with the specified client socket and base directory path.
     *
     * @param clientSocket The socket connected to the client.
     * @param ruta The base directory path for serving static files.
     */
    public HttpRequestHandlerImpl(Socket clientSocket, String ruta){
        this.clientSocket = clientSocket;
        this.ruta = ruta;
    }
    
    /**
     * Handles the incoming HTTP request by reading the request, processing it, and sending a response.
     *
     * @throws IOException If an I/O error occurs while handling the request.
     * @throws URISyntaxException If the request URI is malformed.
     */

    
    public void handlerRequest() throws IOException, URISyntaxException{
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        bodyOut = new BufferedOutputStream(clientSocket.getOutputStream());
        
        String inputLine;
        boolean isFirstLine = true;
        String file = "";
        String method = "";
        while ((inputLine = in.readLine()) != null) {
            if (isFirstLine) {
                file = inputLine.split(" ")[1];
                method = inputLine.split(" ")[0];
                isFirstLine = false; 
            }
            if (!in.ready()) {
                break;
            }
        }
        System.out.println("Received request: " + method + " " + file);
        rediretMethod(method,file);
        out.close();
        bodyOut.close();
        in.close();
        clientSocket.close();  
    }  

    /**
     * Redirects the request to the appropriate handler based on the HTTP method and file path.
     *
     * @param method The HTTP method (e.g., GET, POST).
     * @param file The requested file path.
     * @throws IOException If an I/O error occurs while handling the request.
     * @throws URISyntaxException If the request URI is malformed.
     */
    public void rediretMethod(String method, String file) throws IOException, URISyntaxException{
        URI requestFile = new URI(file);
        String fileRequest = requestFile.getPath();
        String queryRequest = Optional.ofNullable(requestFile.getQuery()).orElse("");
        String contentType = getContentType(fileRequest);
        if(fileRequest.startsWith("/app")){
            handlerRequestApp(method,fileRequest,queryRequest);
        }else{
            requestStaticHandler(ruta + file, contentType); 
        }
    }
    
    /**
     * Handles requests for application endpoints by invoking the appropriate service method.
     *
     * @param method The HTTP method (e.g., GET, POST).
     * @param fileRequest The requested file path.
     * @param queryRequest The query string from the request.
     */
    public void handlerRequestApp(String method, String fileRequest, String queryRequest){
        String endpoint = fileRequest.substring(4);
        Method service = null;
        String code = "404";
        String outputLine = " ";
        if(method.equals("GET")){
            service = FrameWorkSetting.getGetService(endpoint);
            code = "200";
        }else if(method.equals("POST")){
            service = FrameWorkSetting.getPostService(endpoint);
            code = "201";
        }
        if(service != null){
            outputLine = invokeHandler(service,queryRequest);
            outputLine = "{\"response\":\"" + outputLine + "\"}";                
        }else{
            outputLine = "{\"response\":Method not supported}";
        }
        String responseHeader = requestHeader("text/json", outputLine.length(), code);
        out.println(responseHeader);
        out.println(outputLine);  
        out.flush();
    }

    /**
     * Invokes the appropriate service method with the provided query parameters.
     *
     * @param service The service method to invoke.
     * @param query The query string containing the parameters.
     * @return The response from the service method as a String.
     */
    public String invokeHandler(Method service, String query){
        String response = "";
        try {
            Map<String, String> queryParams = queryParams(query);
            Object[] parameters = new Object[service.getParameterCount()];
            Class<?>[] parameterTypes = service.getParameterTypes();
            Annotation[][] annotations = service.getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof RequestParam) {
                        RequestParam requestParam = (RequestParam) annotation;
                        String paramName = requestParam.value();
                        String paramValue = queryParams.get(paramName);
                        if (paramValue == null || paramValue.isEmpty()) {
                            paramValue = requestParam.defaultValue();
                        }
                        if (paramValue != null) {
                            if (parameterTypes[i] == int.class) {
                                parameters[i] = Integer.parseInt(paramValue);
                            } else if (parameterTypes[i] == double.class) {
                                parameters[i] = Double.parseDouble(paramValue);
                            } else {
                                parameters[i] = paramValue;
                            }
                        } else {
                            parameters[i] = null;
                        }
                    }
                }
            }
            Object instance = service.getDeclaringClass().getDeclaredConstructor().newInstance();
            response = (String) service.invoke(instance, parameters);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error executing service method: " + e.getMessage());
        }
        return response;
    }
    

    /**
     * Parses the query string into a map of key-value pairs.
     *
     * @param query The query string to parse.
     * @return A map containing the query parameters.
     */
    public Map<String, String> queryParams(String query){
        Map<String, String> queryParams = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return queryParams;
        }
        String[] values = query.split("&");
        for(String s: values){
            String[] valueM = s.split("=",2);
            String key = valueM[0].trim();
            String value = valueM.length > 1 ? valueM[1].trim() : "";
            queryParams.put(key, value);
        }
        return queryParams;
    }

    /**
     * Handles requests for static files by reading the file and sending it back to the client.
     *
     * @param file The path to the static file.
     * @param contentType The content type of the file.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public void requestStaticHandler(String file, String contentType) throws IOException{
        if(fileExists(file)){
            byte[] requestfile = readFileData(file);
            String requestHeader = requestHeader(contentType,requestfile.length,"200");
            out.println(requestHeader);
            out.flush();
            bodyOut.write(requestfile);
            bodyOut.flush();
        }else{
            out.println(notFound());
        } 
    }

    /**
     * Reads the data from the specified file and returns it as a byte array.
     *
     * @param requestFile The path to the file to read.
     * @return The file data as a byte array.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public byte[] readFileData(String requestFile) throws IOException {
        File file = new File(requestFile);

        if (file.isDirectory()) {
            throw new FileNotFoundException("La ruta solicitada es un directorio, no un archivo: " + requestFile);
        }
        if (!fileExists(requestFile)) {
            throw new FileNotFoundException("Archivo no encontrado: " + requestFile);
        }
        
        int fileLength = (int) file.length();
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }
        return fileData;
    }
    

    /**
     * Checks if a file exists at the specified path.
     *
     * @param filePath The path to the file.
     * @return True if the file exists, false otherwise.
     */
    public static boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }
    
    /**
     * Determines the content type of a file based on its extension.
     *
     * @param requestFile The file path.
     * @return The content type of the file.
     */
    public String getContentType(String requestFile){
        String contentType = " ";
        if (requestFile.endsWith(".html")){
            contentType = "text/html";
        }else if (requestFile.endsWith(".css")){
            contentType = "text/css";
        }else if (requestFile.endsWith(".js")){
            contentType = "application/javascript";
        }else if (requestFile.endsWith(".png")){
            contentType = "image/png";
        }else if (requestFile.endsWith(".jpg") || requestFile.endsWith(".jpeg")){
            contentType = "image/jpeg";
        }else{
            contentType = "text/plain";
        }
        return contentType;  
    }
    
    /**
     * Generates an HTTP response header with the specified content type, content length, and status code.
     *
     * @param contentType The content type of the response.
     * @param contentLength The length of the response content.
     * @param code The HTTP status code.
     * @return The generated HTTP response header.
     */
    public String requestHeader(String contentType, int contentLength, String code){
        String outHeader = "HTTP/1.1 " + code + " OK\r\n"
                    + "Content-Type: " + contentType + "\r\n"
                    + "Content-Length: " + contentLength + "\r\n";     
        return outHeader;
    }
    
    /**
     * Generates a 404 Not Found HTTP response with an HTML error page.
     *
     * @return The 404 Not Found HTTP response.
     */
    public static String notFound(){
        String outputLine = "HTTP/1.1. 404 Not Found\r\n"
                        +"Content-type: text/html\r\n"
                        +"\r\n"
                        +"<!DOCTYPE html>\n"
                        + "<html lang=\"en\">\n"
                        + "<head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    <title>404 - File Not Found</title>\n"
                        + "    <style>\n"
                        + "        body {\n"
                        + "            font-family: Arial, sans-serif;\n"
                        + "            background-color: #ded5fa;\n"
                        + "            display: flex;\n"
                        + "            justify-content: center;\n"
                        + "            align-items: center;\n"
                        + "            height: 100vh;\n"
                        + "            margin: 0;\n"
                        + "        }\n"
                        + "        .container {\n"
                        + "            text-align: center;\n"
                        + "            background-color: #c2aaeb;\n"
                        + "            padding: 50px;\n"
                        + "            border-radius: 8px;\n"
                        + "            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\n"
                        + "        }\n"
                        + "        h1 {\n"
                        + "            font-size: 5em;\n"
                        + "            color: #f7755b;\n"
                        + "        }\n"
                        + "        p {\n"
                        + "            font-size: 1.2em;\n"
                        + "            color: #555;\n"
                        + "        }\n"
                        + "    </style>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "    <div class=\"container\">\n"
                        + "        <h1>404</h1>\n"
                        + "        <p>Oops! The file you're looking for cannot be found.</p>\n"
                        + "    </div>\n"
                        + "</body>\n"
                        + "</html>";
        return outputLine;
    }    
}