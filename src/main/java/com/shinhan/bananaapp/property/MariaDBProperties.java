package com.shinhan.bananaapp.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "shinhan2")
@Component
@Getter@Setter
public class MariaDBProperties {
    DataSource datasource;

    @Getter@Setter
    public static class DataSource{
        String url;
        String username;
        String password;
    }
}


