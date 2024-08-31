package jttp;

import com.sun.net.httpserver.Headers;
import jttp.cookie.Cookie;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JttpUtils {
    private final static HashMap<String, String> CTS = new HashMap<>();

    static {
        CTS.put("css", "text/css");
        CTS.put("csv", "text/csv");
        CTS.put("gif", "image/gif");
        CTS.put("htm", "text/html");
        CTS.put("html", "text/html");
        CTS.put("ico", "image/x-icon");
        CTS.put("jar", "application/java-archive");
        CTS.put("jpeg", "image/jpeg");
        CTS.put("jpg", "image/jpeg");
        CTS.put("js", "application/javascript");
        CTS.put("json", "application/json");
        CTS.put("mpeg", "video/mpeg");
        CTS.put("otf", "font/otf");
        CTS.put("png", "image/png");
        CTS.put("rar", "application/x-rar-compressed");
        CTS.put("svg", "image/svg+xml");
        CTS.put("ttf", "font/ttf");
        CTS.put("wav", "audio/x-wav");
        CTS.put("weba", "audio/webm");
        CTS.put("webm", "video/webm");
        CTS.put("webp", "image/webp");
        CTS.put("woff", "font/woff");
        CTS.put("woff2", "font/woff2");
        CTS.put("xhtml", "application/xhtml+xml");
        CTS.put("xml", "application/xml");
        CTS.put("zip", "application/zip");
        CTS.put("7z", "application/x-7z-compressed");
    }

    public static HashMap<String, Cookie> parseCookies(Headers headers) {
        HashMap<String, Cookie> cookieList = new HashMap<>();
        List<String> headerCookies = headers.get("Cookie");

        if (headerCookies == null || headerCookies.isEmpty()) {
            return cookieList;
        }

        String hcookies = headerCookies.getFirst();

        String[] cookies = hcookies.split(";");
        for (String cookie : cookies) {
            String[] split = cookie.split("=");
            String name = split[0].trim();
            String value = split[1].trim();
            cookieList.put(name, new Cookie(name, value));
        }

        return cookieList;
    }

    public static HashMap<String, String> parseRawQuery(URI uri) {
        HashMap<String, String> querys = new HashMap<>();
        String rawQuery = uri.getRawQuery();

        if (rawQuery == null)
            return querys;

        Matcher mat = Pattern.compile("(.+?)=(.+?)(&|$)").matcher(rawQuery);
        while (mat.find()) {
            try {
                String key = URLDecoder.decode(mat.group(1), "UTF8");
                String val = URLDecoder.decode(mat.group(2), "UTF8");
                querys.put(key, val);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return querys;
    }

    public static String getContentType(String fileExtension) {
        String ct = CTS.get(fileExtension);

        if (ct == null)
            ct = "text/plain";

        return ct;
    }
}
