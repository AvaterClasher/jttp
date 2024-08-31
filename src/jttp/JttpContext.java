package jttp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class JttpContext implements HttpHandler {

    private final Jttp JTTP;

    public JttpContext(Jttp jttp) {
        this.JTTP = jttp;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        new Thread(new JttpContextThread(httpExchange, JTTP)).start();
    }
}