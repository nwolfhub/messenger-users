package org.nwolfhub.messengerusers.config;

import org.nwolfhub.shared.Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

@Configuration
public class DatabaseConfigurator {

    private static HashMap<String, String> data;


    private boolean read() {
        try (FileInputStream in = new FileInputStream("users.cfg")) {
            String input = new String(in.readAllBytes());
            data = Utils.parseValues(input.split("\n"));
            in.close();
            return true;
        } catch (FileNotFoundException e) {
            File f = new File("users.cfg");
            try {
                f.createNewFile();
                System.out.println("Please fill users.cfg");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Bean(name = "hibernateProperties")
    @Primary
    public Properties getHibernateProperties() {
        if(read()) {
            Properties prop = new Properties();
            prop.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
            prop.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            prop.put("hibernate.connection.url", data.get("db_url"));
            prop.put("hibernate.connection.username", data.get("db_username"));
            prop.put("hibernate.connection.password", data.get("db_password"));
            prop.put("hibernate.current_session_context_class", "thread");
            prop.put("hibernate.connection.CharSet", "utf8");
            prop.put("hibernate.connection.characterEncoding", "utf8");
            prop.put("hibernate.connection.useUnicode", true);
            prop.put("hibernate.connection.pool_size", 100);
            return prop;
        }
        else throw new RuntimeException("Could not read config file!");
    }
}