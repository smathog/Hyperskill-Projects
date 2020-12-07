package advisor;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.*;

public class Server {
    //Singleton instance
    private static Server instance = null;

    //regex for extracting code
    Pattern codePattern = Pattern.compile("(?<=code=).+");

    //Instance variables
    private HttpServer httpServer;
    private String code = null;
    private boolean hasResponse = false;

    public static Server getServer() {
        if (instance == null)
            instance = new Server();
        return instance;
    }

    public String getCode() {
        return code;
    }

    public boolean getResponse() {
        return hasResponse;
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    private Server() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(8080), 0);
            httpServer.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String query = exchange.getRequestURI().getQuery();
                    if (query == null) {
                        String reject = "Authorization code not found. Try again.";
                        exchange.sendResponseHeaders(200, reject.length());
                        exchange.getResponseBody().write(reject.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }
                    hasResponse = true;
                    Matcher m = codePattern.matcher(query);
                    if (m.find())
                    {
                        code = m.group();
                        String hello = "Got the code. Return back to your program.";
                        exchange.sendResponseHeaders(200, hello.length());
                        exchange.getResponseBody().write(hello.getBytes());
                        exchange.getResponseBody().close();
                    } else {
                        String reject = "Authorization code not found. Try again.";
                        exchange.sendResponseHeaders(200, reject.length());
                        exchange.getResponseBody().write(reject.getBytes());
                        exchange.getResponseBody().close();
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Server init failed!");
            System.out.println(e.getMessage() + " " + e.getStackTrace());
        }
    }
}
