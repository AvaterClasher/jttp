package jttp;

import com.sun.net.httpserver.HttpServer;
import jttp.events.Action;
import jttp.events.HttpRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Jttp {

    protected final ConcurrentHashMap<String, ArrayList<JttpHandler>> MIDDLEWARE = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, ArrayList<JttpHandler>> REQUEST = new ConcurrentHashMap<>();

    protected HttpServer httpServer;

    public Jttp() {
    }

    public void use(HttpRequest request) {
        addToRouteMap(MIDDLEWARE, "*", "*", request);
    }

    public void use(String context, HttpRequest request) {
        addToRouteMap(MIDDLEWARE, "*", context, request);
    }

    public void use(String method, String context, HttpRequest request) {
        addToRouteMap(MIDDLEWARE, method.toUpperCase(), context, request);
    }

    public void on(String method, String context, HttpRequest request) {
        addToRouteMap(REQUEST, method.toUpperCase(), context, request);
    }

    public void all(String context, HttpRequest request) {
        addToRouteMap(REQUEST, "*", context, request);
    }

    public void get(String context, HttpRequest request) {
        addToRouteMap(REQUEST, "GET", context, request);
    }

    public void post(String context, HttpRequest request) {
        addToRouteMap(REQUEST, "POST", context, request);
    }

    public void put(String context, HttpRequest request) {
        addToRouteMap(REQUEST, "PUT", context, request);
    }

    public void delete(String context, HttpRequest request) {
        addToRouteMap(REQUEST, "DELETE", context, request);
    }

    public void patch(String context, HttpRequest request) {
        addToRouteMap(REQUEST, "PATCH", context, request);
    }

    private void addToRouteMap(ConcurrentHashMap<String, ArrayList<JttpHandler>> routemap, String key, String context, HttpRequest request) {

        if (!routemap.containsKey(key))
            routemap.put(key, new ArrayList<>());

        routemap.get(key).add(new JttpHandler(context, request));
    }

    public void listen() throws IOException {
        launch(null, 80);
    }


    public void listen(int port) throws IOException {
        launch(null, port);
    }

    public void listen(Action action) throws IOException {
        launch(action, 80);
    }

    public void listen(int port, Action action) throws IOException {
        launch(action, port);
    }

    private void launch(Action action, int port) throws IOException {
        new Thread(() -> {
            try {
                httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
                httpServer.setExecutor(null);

                httpServer.createContext("/", new JttpContext(this));
                httpServer.start();
                action.action();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static HttpRequest statics(String path) {
        return (req, res) -> {
            String PATH = req.getURI().getPath();
            System.out.println(PATH);
            System.out.println(path);
            File reqFile = new File(PATH.equals("/") ? (path + "\\index.html") : path );
            System.out.println(reqFile);

            if (reqFile.exists()) {
                String extension = reqFile.getAbsolutePath().replaceAll("^(.*\\.|.*\\\\|.+$)", "");
                String contentType = JttpUtils.getContentType(extension);
                res.send(reqFile, contentType);
            }
        };
    }

}
