package org.whut.mc.server.core.communication;

import org.json.JSONObject;
import org.whut.mc.server.core.config.Key;
import org.whut.mc.server.core.config.ParserConfig;
import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.util.CodecUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangyang on 16-1-26.
 */
public class RequestParser implements Parser {
    private static Log log;

    private ParserConfig config;
    private List<JSONObject> allResolver;

    static {
        log = Log.getLogger(RequestParser.class);
    }

    public RequestParser(String xmlPath) {
        config = new ParserConfig(xmlPath);
        allResolver = config.getAllResolver();
    }

    @Override
    public Request parser(byte[] msg) {
        for (JSONObject resolver : allResolver) {
            Pattern pattern = Pattern.compile(resolver.getString(Key.REGX.getKey()));
            Matcher matcher = pattern.matcher(CodecUtil.getHex(msg));

            if (matcher.matches()) {
                Request request = new Request();
                request.setData(msg);
                request.setResolver(resolver.getString("class"));
                return request;
            }
        }
        return null;
    }
}
