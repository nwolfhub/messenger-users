package org.nwolfhub.messengerusers;

import org.nwolfhub.shared.database.model.User;

public class JsonBuilder {
    public static String successUsersGet(User u) {
        return "{\"ok\": true, \"user\": {\"is\":" + u.getId() + ", \"username\": \"" + u.getUsername() + "\"}";
    }
    public static String failUsersGet(String error) {
        return "{\"ok\": false, \"error\": \"" + error + "\"}";
    }
}
