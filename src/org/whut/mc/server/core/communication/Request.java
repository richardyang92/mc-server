package org.whut.mc.server.core.communication;

import org.apache.mina.core.session.IoSession;
import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.util.CodecUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yangyang on 16-1-26.
 */
public class Request {
    private static Log log;

    private String resolver;
    private IoSession session;
    private byte[] dBt;

    static {
        log = Log.getLogger(Request.class);
    }

    public void setData(byte[] data) {
        this.dBt = data;
    }

    public byte[] getData() {
        return this.dBt;
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

}
