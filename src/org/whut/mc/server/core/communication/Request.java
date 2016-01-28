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

    private static final String METHOD_PREFIX = "generate";
    private static final String METHOD_SUFFIX = "FromConfig";

    private String resolver;
    private IoSession session;
    private Head head;
    private byte[] hBt;
    private byte[] dBt;

    static {
        log = Log.getLogger(Request.class);
    }

    public void generateHeadFromConfig(Head head) {
        Field[] fields = Head.class.getFields();
        for (Field field : fields) {
            String name = field.getName();
            String methodName = generateMethodName(name);
            try {
                Method method = Request.class.getMethod(methodName, Head.class);
                byte[] bt = (byte[]) method.invoke(this, head);
                hBt = CodecUtil.merge(hBt, bt);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            } catch (InvocationTargetException e) {
                log.error(e.getMessage());
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
            }
        }

        this.head = head;
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

    private String generateMethodName(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        name = sb.toString();
        return METHOD_PREFIX + name + METHOD_SUFFIX;
    }

    private byte[] generateAppIdFromConfig(Head head) {
        byte appId = head.getAppId();
        return new byte[] {appId};
    }

    private byte[] generateTrentIdFromConfig(Head head) {
        byte trentId = head.getTrentId();
        return new byte[] {trentId};
    }

    private byte[] generateFrmIdFromConfig(Head head) {
        int frmId = head.getFrmId();
        return CodecUtil.int2Bytes(frmId, 0);
    }

    private byte[] generateEncryptFromConfig(Head head) {
        byte encrypt = head.getEncrypt();
        return new byte[] {encrypt};
    }

    private byte[] generateTotalLenFromConfig(Head head) {
        byte totalLen = head.getTotalLen();
        return new byte[] {totalLen};
    }

    private byte[] generateCsFromConfig(Head head) {
        byte cs = head.getCs();
        return new byte[] {cs};
    }
}
