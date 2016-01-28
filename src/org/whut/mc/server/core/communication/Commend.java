package org.whut.mc.server.core.communication;

/**
 * Created by yangyang on 16-1-27.
 */
public enum Commend {
    START("start"),STOP("stop");

    private String commend;

    public String getCommend() {
        return this.commend;
    }

    private Commend(String commend) {
        this.commend = commend;
    }
}
