package edu.escuelaing.arep.RequestHandler;

import java.io.IOException;
import java.net.URISyntaxException;

public interface HttpRquestHandler {
    
    void handlerRequest() throws IOException, URISyntaxException;
    
} 
