package net.yorksolutions.jsontesiapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

@Service
public class JsonService {

    public HashMap<String, String> getIpFromRequest(HttpServletRequest request) {
        var data = new HashMap<String, String>();
        data.put("ip", request.getRemoteAddr());

        return data;
    }

    public HashMap<String, String> getHeadersFromRequest(HttpServletRequest request) {
        var rtnData = new HashMap<String, String>();
        var headerNameList = request.getHeaderNames();
        while (headerNameList.hasMoreElements()) {
            String headerName = headerNameList.nextElement();
            String headerData = request.getHeader(headerName);
            rtnData.put(headerName, headerData);
        }

        return rtnData;
    }

    public HashMap getCurrentTimeInfo() {
        var rtnData = new HashMap();
        var dateObj = new Date();

        var date = new SimpleDateFormat("MM-dd-yyyy").format(dateObj);
        var time = new SimpleDateFormat("HH:mm:ss a").format(dateObj);

        rtnData.put("date", date);
        rtnData.put("time", time);
        rtnData.put("milliseconds_since_epoch", Instant.now().toEpochMilli());

        return rtnData;
    }

    public HashMap<String, String> createMapFromRequest(HttpServletRequest request) {
        var rtnData = new HashMap<String, String>();

        String[] tokenList = request.getRequestURI().split("/");
        for (int i = 2; i < tokenList.length; i += 2) {
            var key = tokenList[i];

            var value = "";
            if (i + 1 < tokenList.length) {
                value = tokenList[i + 1];
            }

            rtnData.put(key, value);
        }

        return rtnData;
    }

    public String getCodeFromRequest(HttpServletRequest request) {
        return "alert(\"Your IP address is: " + request.getRemoteAddr() + "\");";
    }

    public HashMap<String, String> generateCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jsontestdotcom", "ms:" + Instant.now().toEpochMilli());
        response.addCookie(cookie);
        var rtnData = new HashMap<String, String>();
        rtnData.put("cookie_status", "Cookie set with name jsontestdotcom");

        return rtnData;
    }

    public HashMap<String, String> textToMd5(String text) throws NoSuchAlgorithmException {
        var rtnData = new HashMap<String, String>();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(text.getBytes());
        byte[] digest = messageDigest.digest();
        String hashedValue = DatatypeConverter.printHexBinary(digest).toLowerCase();

        rtnData.put("original", text);
        rtnData.put("md5", hashedValue);
        return rtnData;
    }

    public HashMap validateJsonString(String jsonString) {
        var rtnData = new HashMap();

        ObjectMapper mapper = new ObjectMapper();
        try {
            var startTime = Instant.now().getNano();
            JsonNode jsonObj = mapper.readTree(jsonString);
            var endTime = Instant.now().getNano();

            rtnData.put("validate", true);
            rtnData.put("size", jsonObj.size());
            rtnData.put("empty", jsonObj.size() == 0);
            rtnData.put("parse_time_nanoseconds", endTime - startTime);
            rtnData.put("object_or_array",
                    jsonObj.isArray() ? "array" : "object"
            );
        } catch (Exception exception) {
            rtnData.put("validate", false);
            rtnData.put("error_info", exception.getClass());
            rtnData.put("error", exception.getMessage());
            rtnData.put("object_or_array",
                    jsonString.charAt(0) == '[' ? "array" : "object"
            );
        }

        return rtnData;
    }
}
