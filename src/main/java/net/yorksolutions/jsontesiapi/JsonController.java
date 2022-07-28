package net.yorksolutions.jsontesiapi;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@RestController
@RequestMapping("/")
public class JsonController {

    JsonService service;
    public JsonController(JsonService service) {
        this.service = service;
    }

    @GetMapping("/ip")
    public HashMap<String, String> ip(HttpServletRequest request) {
        return service.getIpFromRequest(request);
    }

    @GetMapping("/headers")
    public HashMap<String, String> headers(HttpServletRequest request) {
        return service.getHeadersFromRequest(request);
    }

    @GetMapping(value = {"/time", "/date"})
    public HashMap date() {
        return service.getCurrentTimeInfo();
    }

    @GetMapping("/echo/**")
    public HashMap echo(HttpServletRequest request) {
        return service.createMapFromRequest(request);
    }

    @GetMapping("/code")
    public String code(HttpServletRequest request){
        return service.getCodeFromRequest(request);
    }

    @GetMapping("/cookie")
    public HashMap cookie(HttpServletResponse response) {
        return service.generateCookie(response);
    }

    @GetMapping("/md5")
    public HashMap md5(@RequestParam String text) throws NoSuchAlgorithmException {
        return service.textToMd5(text);
    }

    @PostMapping("/validate")
    public HashMap validate(@RequestParam String json) {
        return service.validateJsonString(json);
    }
}
