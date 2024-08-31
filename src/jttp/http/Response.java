package jttp.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import jttp.cookie.Cookie;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class Response {

    private final HttpExchange HTTP_EXCHANGE;
    private final OutputStream BODY;
    private final Headers HEADER;

    private String contentType = "text/plain";
    private boolean isClose = false;
    private long contentLength = 0;
    private int status = 200;

    public Response(HttpExchange exchange) {
        this.HTTP_EXCHANGE = exchange;
        this.HEADER = exchange.getResponseHeaders();
        this.BODY = exchange.getResponseBody();
    }

    public Response setCookie(Cookie cookie) {
        if (checkIfClosed())
            return this;
        this.HEADER.add("Set-Cookie", cookie.toString());
        return this;
    }

    public int getStatus() {
        return this.status;
    }

    public Response setStatus(int status) {
        if (checkIfClosed())
            return this;
        this.status = status;
        return this;
    }

    public void send() {
        if (checkIfClosed())
            return;
        this.contentLength = 0;
        sendHeaders();
        close();
    }

    public void send(String s) {
        if (checkIfClosed())
            return;
        this.contentLength += s.length();
        sendHeaders();

        try {
            this.BODY.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    public void send(File file, String contentType) {
        if (checkIfClosed())
            return;
        this.contentLength += file.length();
        this.contentType = contentType;
        sendHeaders();

        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            this.BODY.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    public boolean isClosed() {
        return this.isClose;
    }

    private boolean checkIfClosed() {
        if (isClose) {
            System.err.println("Response is already closed.");
            return true;
        }
        return false;
    }

    private void sendHeaders() {
        try {
            this.HEADER.set("Content-Type", contentType);
            this.HTTP_EXCHANGE.sendResponseHeaders(status, contentLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            this.BODY.close();
            this.isClose = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}