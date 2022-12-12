package org.nwolfhub.messengerusers.api;

import org.nwolfhub.messengerusers.Auther;
import org.nwolfhub.messengerusers.JsonBuilder;
import org.nwolfhub.messengerusers.config.DatabaseConfigurator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static Auther auther = new AnnotationConfigApplicationContext(DatabaseConfigurator.class).getBean(Auther.class);
    @GetMapping("/users/get")
    public static ResponseEntity<String> get(@RequestParam(name = "username", defaultValue = "", required = false) String username, @RequestParam(name = "id", defaultValue = "", required = false) String id, @RequestHeader(name = "token", defaultValue = "") String token, @RequestHeader(name = "X-Forwarded-For", defaultValue = "0.0.0.0") String ip) {
        if(ip.equals("0.0.0.0")) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(JsonBuilder.failUsersGet("Direct requests are not allowed"));
        Integer authId = auther.auth(token);
        if()
    }
}
