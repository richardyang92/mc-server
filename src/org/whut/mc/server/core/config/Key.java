package org.whut.mc.server.core.config;

/**
 * Created by yangyang on 2015/12/4.
 */
public enum Key {
    NAME("name"),TYPE("type"),REGX("regx"),CLASS("class"),
    HEAD("head"),RESOLVER("resolver"),
    MASTER_SERVER_PORT("server.master.port");
    private String key;

    public String getKey() {
        return key;
    }

    private Key(String key) {
        this.key = key;
    }
}
