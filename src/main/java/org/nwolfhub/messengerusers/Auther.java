package org.nwolfhub.messengerusers;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

public class Auther {
    @Autowired
    private RedisConnectionData redisData;

    public boolean isUsed = true;

    private Jedis jedis;

    public Auther(RedisConnectionData redisData) throws IOException {
        this.redisData = redisData;
        if(!this.redisData.useRedis) {
            System.out.println("Redis not used. This service will deny any request");
        }
        else {
            JedisPool pool = new JedisPool(this.redisData.getUrl(), this.redisData.getPort());
            //jedis = new Jedis(this.redisData.getUrl(), this.redisData.getPort());
            jedis = pool.getResource();
            if(this.redisData.isUsePassword()) jedis.auth(this.redisData.getPassword());
            if(!jedis.isConnected()) throw new IOException("Could not connect to redis server!");
            System.out.println("Jedis up and running!");
        }
    }

    /**
     * Looking for user who owns token
     * @param token user's token
     * @return id of a user
     * @throws NullPointerException in case token is not found
     */
    public Integer auth(String token) throws NullPointerException, RedisNotUsedException {
        if (redisData.useRedis) {
            return Integer.valueOf(jedis.get(token));
        } else {
            throw new RedisNotUsedException("Using Redis is the only authorization way for now");
        }
    }
    public void test() {
        jedis.set("test", "test");
    }

    public static class RedisNotUsedException extends Exception {
        public RedisNotUsedException(String text) {
            super(text);
        }
    }
    public static class RedisConnectionData {
        public boolean useRedis;
        public String url;
        public Integer port;
        public boolean usePassword;
        public String password;

        public RedisConnectionData() {}

        public boolean isUseRedis() {
            return useRedis;
        }

        public RedisConnectionData setUseRedis(boolean useRedis) {
            this.useRedis = useRedis;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public RedisConnectionData setUrl(String url) {
            this.url = url;
            return this;
        }

        public Integer getPort() {
            return port;
        }

        public RedisConnectionData setPort(Integer port) {
            this.port = port;
            return this;
        }

        public boolean isUsePassword() {
            return usePassword;
        }

        public RedisConnectionData setUsePassword(boolean usePassword) {
            this.usePassword = usePassword;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public RedisConnectionData setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
