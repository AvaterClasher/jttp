package jttp.events;

import jttp.http.Request;
import jttp.http.Response;

public interface HttpRequest {
    void handle(Request req, Response res);
}
