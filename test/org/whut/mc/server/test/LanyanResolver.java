package org.whut.mc.server.test;

import org.whut.mc.server.core.communication.ResolverBase;
import org.whut.mc.server.core.communication.Response;
import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.util.CodecUtil;

/**
 * Created by yangyang on 2016/1/18.
 */
public class LanyanResolver extends ResolverBase {
    private static Log log;

    static {
        log = Log.getLogger(LanyanResolver.class);
    }

    @Override
    public Response resolve(byte[] data) {
        log.info("LanyanResolver resolve");
        CodecUtil.showMsg(data);
        log.info("test native");
        return null;
    }
}
