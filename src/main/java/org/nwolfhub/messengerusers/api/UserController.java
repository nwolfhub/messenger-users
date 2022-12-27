package org.nwolfhub.messengerusers.api;

import org.nwolfhub.messengerusers.Auther;
import org.nwolfhub.messengerusers.JsonBuilder;
import org.nwolfhub.messengerusers.config.DatabaseConfigurator;
import org.nwolfhub.shared.database.UserDao;
import org.nwolfhub.shared.database.model.Dao;
import org.nwolfhub.shared.database.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfigurator.class);

    private static Auther auther = context.getBean(Auther.class);
    private static UserDao dao = (UserDao) new AnnotationConfigApplicationContext(DatabaseConfigurator.class).getBean("usersDao");

    @GetMapping("/users/get")
    public static ResponseEntity<String> get(@RequestParam(name = "username", defaultValue = "", required = false) String username, @RequestParam(name = "id", defaultValue = "", required = false) String id, @RequestHeader(name = "token", defaultValue = "") String token, @RequestHeader(name = "X-Forwarded-For", defaultValue = "0.0.0.0") String ip) throws Auther.RedisNotUsedException {
        try {
            if (ip.equals("0.0.0.0"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(JsonBuilder.failUsersGet("Direct requests are not allowed"));
            try {
                Integer authId = auther.auth(token);
                User u = null;
                if(id.equals("") && !username.equals("")) {
                    u = (User) dao.get(username);
                } else if (!id.equals("") && username.equals("")) {
                    u = dao.getUser(Integer.valueOf(id));
                }
                if (u!= null) {
                    return ResponseEntity.status(HttpStatus.OK).body(JsonBuilder.successUsersGet(u));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JsonBuilder.failUsersGet("User does not exist"));
            } catch (NullPointerException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JsonBuilder.failUsersGet("Wrong token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(JsonBuilder.failUsersGet("Internal server error: " + e));
        }
    }

}
