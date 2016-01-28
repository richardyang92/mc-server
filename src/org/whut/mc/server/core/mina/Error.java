package org.whut.mc.server.core.mina;

/**
 * Created by yangyang on 2015/12/3.
 */
public enum Error {
    BIND_FAIL_EXCEPTION_ENUM("bind_fail"),
    CONNECT_FAIL_EXCEPTION_ENUM("connect_fail"),
    CONNECT_CLOSE_EXCEPTION_ENUM("connect_close"),
    CLINET_OFFLINE_EXCEPTION_ENUM("client_offline"),
    NULL("null");

    private String type;

    private Error(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
