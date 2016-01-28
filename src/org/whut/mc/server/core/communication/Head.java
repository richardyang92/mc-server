package org.whut.mc.server.core.communication;

/**
 * Created by yangyang on 16-1-27.
 */
public class Head {
    public static final String APPID = "appId";
    public static final String TRENTID = "trentId";
    public static final String FRMID = "frmId";
    public static final String ENCRYPT = "encrypt";
    public static final String TOTALLEN = "totalLen";
    public static final String CS = "CS";
    public static final int HEAD_LEN = 9;
    private byte appId;
    private byte trentId;
    private int frmId;
    private byte encrypt;
    private byte totalLen;
    private byte cs;

    private boolean status;

    public byte getAppId() {
        return appId;
    }

    public void setAppId(byte appId) {
        this.appId = appId;
    }

    public byte getTrentId() {
        return trentId;
    }

    public void setTrentId(byte trentId) {
        this.trentId = trentId;
    }

    public int getFrmId() {
        return frmId;
    }

    public void setFrmId(int frmId) {
        this.frmId = frmId;
    }

    public byte getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(byte encrypt) {
        this.encrypt = encrypt;
    }

    public byte getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(byte totalLen) {
        this.totalLen = totalLen;
    }

    public byte getCs() {
        return cs;
    }

    public void setCs(byte cs) {
        this.cs = cs;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
