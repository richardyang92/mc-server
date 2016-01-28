package org.whut.mc.server.test;

import org.whut.mc.server.core.util.ResolveUtil;

/**
 * Created by yangyang on 16-1-28.
 */
public class ResolveUtilTest {
    private static ResolveUtil ru = new ResolveUtil();

    public static void main(String[] args) {
        byte bt[] = {0x68,0x01,0x0e,0x01,0x01,0x01,0x01,0x01,0x03,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x0f,0x13,0x16};
        System.out.println(ru.resolveLanyanOpenBB(bt));
    }
}
