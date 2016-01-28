package org.whut.mc.server.core.util;

/**
 * Created by yangyang on 16-1-27.
 */
public class ResolveUtil {
    static {
        System.loadLibrary("resolve_util");
    }

    public native String resolveLanyanOpenBB(byte[] data);
}
