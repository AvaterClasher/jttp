package test;

import jttp.Jttp;
import jttp.cookie.Cookie;

import java.io.IOException;
import java.util.HashMap;

public class Get {

    public static void main(String[] args) throws IOException {
        Jttp jttp = new Jttp();

        jttp.get("/", (req, res) -> res.send("Called /"));

        jttp.get("/user", (req, res) -> res.send("Called /user"));

        jttp.get("/user/moni", (req, res) -> res.send("Called /user/moni"));

        jttp.get("/getHello", (req, res) -> {
            String person = req.getQuery("Hello");
            String from = req.getQuery("from");
            res.send("hello " + person + " from " + from);
        });

        jttp.get("/hello/:username", (req, res) -> {
            String username = req.getParam("username");
            res.send("User " + username + " said hello!");
        });

        jttp.get("/hello/:username/:count", (req, res) -> {
            String username = req.getParam("username");
            String count = req.getParam("count");
            res.send("User " + username + " want to say " + count + " times hello!");
        });

        jttp.get("/cookie/:name/:val", (req, res) -> {
            String name = req.getParam("name");
            String val = req.getParam("val");
            Cookie cookie = new Cookie(name, val);
            res.setCookie(cookie);
            res.send("ok");
        });

        jttp.get("/showcookies", (req, res) -> {
            HashMap<String, Cookie> cookies = req.getCookies();
            StringBuffer buffer = new StringBuffer();
            cookies.forEach((s, cookie) -> buffer.append(s).append(": ").append(cookie));
            res.send(buffer.toString());
        });

        jttp.listen(() -> System.out.println("Server is listening!"));
    }

}