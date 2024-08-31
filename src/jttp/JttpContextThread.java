package jttp;

import com.sun.net.httpserver.HttpExchange;
import jttp.http.Request;
import jttp.http.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class JttpContextThread implements Runnable {

    private final Jttp JTTP;
    private final HttpExchange HTTP_EXCHANGE;
    private final Request REQUEST;
    private final Response RESPONSE;

    private final String requestPath;
    private final String requestMethod;

    public JttpContextThread(HttpExchange httpExchange, Jttp jttp) {
        this.JTTP = jttp;
        this.HTTP_EXCHANGE = httpExchange;
        this.REQUEST = new Request(httpExchange);
        this.RESPONSE = new Response(httpExchange);

        this.requestPath = REQUEST.getURI().getRawPath();
        this.requestMethod = REQUEST.getMethod();
    }

    @Override
    public void run() {
        ArrayList<JttpHandler> middlewareAll = JTTP.MIDDLEWARE.get("*");
        ArrayList<JttpHandler> requestsAll = JTTP.REQUEST.get("*");
        ArrayList<JttpHandler> middleware = JTTP.MIDDLEWARE.get(requestMethod);
        ArrayList<JttpHandler> requests = JTTP.REQUEST.get(requestMethod);

        if (middleware == null && middlewareAll != null)
            middleware = middlewareAll;
        else if (middlewareAll != null)
            middleware.addAll(middlewareAll);

        if (requests == null && requestsAll != null)
            requests = requestsAll;
        else if (requestsAll != null)
            requests.addAll(requestsAll);

        if (middleware != null && !middleware.isEmpty()) {
            middleware.forEach(exh -> {
                HashMap<String, String> params = exh.parseParams(requestPath);
                if (params != null || exh.getContext().equals("*")) {
                    exh.getRequest().handle(REQUEST, RESPONSE);
                    if (RESPONSE.isClosed())
                        return;
                }
            });
        }

        if (requests != null && !requests.isEmpty()) {
            requests.forEach(exh -> {

                HashMap<String, String> params = exh.parseParams(requestPath);
                if (params != null || exh.getContext().equals("*")) {
                    REQUEST.setParams(params);
                    exh.getRequest().handle(REQUEST, RESPONSE);
                    if (RESPONSE.isClosed())
                        return;
                }
            });
        }
    }
}