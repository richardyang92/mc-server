package org.whut.mc.server.core.communication;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by yangyang on 16-1-26.
 */
public interface Codec {
    String resolve(byte[] data) throws UnsupportedEncodingException;
    byte[] code(JSONObject jsonObject) throws UnsupportedEncodingException;
}
