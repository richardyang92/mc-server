package org.whut.mc.server.core.communication;

import org.json.JSONObject;
import org.whut.mc.server.core.log.Log;

/**
 * Created by yangyang on 2016/1/18.
 */
public abstract class CodecBase implements Codec {
    private static Log log;

    private JSONObject jsonObject;

    {
        jsonObject = new JSONObject();
    }

    static {
        log = Log.getLogger(CodecBase.class);
    }
}
