package com.shinhan.bananaapp.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "shinhan")
@Component
@Getter@Setter
public class ShinhanProperties {
    ApiClass api1;
    ApiClass api2;
    String api3;

    @Getter@Setter
    public static class ApiClass{
        String key1;
        String key2;
    }
}
