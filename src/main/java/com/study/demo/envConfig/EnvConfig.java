package com.study.demo.envConfig;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * CreatedBy:   Francis Xirui Yang
 * Date:        4/5/20
 * mailto:      xiruiyang@hiretual.com
 * version:     1.0 since 1.0
 */
public class EnvConfig {

    private final Dotenv dotenv;

    public EnvConfig() {
        dotenv = Dotenv.configure()
                .directory("src/main")
                .load();
    }

    public enum Key {
        ZK_URL
    }

    private String notEmptyString(String value, String key) {
        if (value == null) {
            throw new RuntimeException(key + " is null");
        }
        if (value.length() == 0) {
            throw new RuntimeException(key + " is empty string");
        }
        return value;
    }

    public String get(Key key) {
        String value = dotenv.get(key.name());
        return notEmptyString(value, key.name());
    }

    public Integer getInteger(Key key) {
        String value = dotenv.get(key.name());
        return Integer.valueOf(notEmptyString(value, key.name()));
    }
}