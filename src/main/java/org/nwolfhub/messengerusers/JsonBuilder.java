package org.nwolfhub.messengerusers;

import org.nwolfhub.shared.database.model.User;

public class JsonBuilder {
    public static String successUsersGet(User u) {
        return "{\"ok\": true, \"user\": {\"id\":" + u.getId() + ", \"username\": \"" + u.getUsername() + "\", \"banned\": \"" + u.banned + "\"}}";
    }
    public static String failUsersGet(String error) {
        return "{\"ok\": false, \"error\": \"" + error + "\"}";
    }
}
