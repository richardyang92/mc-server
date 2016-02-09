package org.whut.mc.server.core.communication;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.util.CodecUtil;

import java.io.IOException;

/**
 * Created by yangyang on 16-1-27.
 */
public class MasterServerHandler extends IoHandlerAdapter {
    private static Log log;

    static {
        log = Log.getLogger(MasterServerHandler.class);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Request request = (Request) message;
        request.setSession(session);
        log.info("session: {}, resolver: {}", session, request.getResolver());
        Class clazz = Class.forName(request.getResolver());
        Codec codec = (Codec) clazz.newInstance();
        String json = codec.resolve(request.getData());
        log.info("response string: {}", json);
        JSONObject jsonObject = new JSONObject(json);
        byte[] btm = codec.code(jsonObject);
        CodecUtil.showMsg(btm);
        session.write(btm);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.info("{} sessionCreated", session.getRemoteAddress());
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.info("{} sessionOpened", session.getRemoteAddress());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("{} sessionClosed", session.getRemoteAddress());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        log.info("{} exceptionCaught: {}", session.getRemoteAddress(), cause.getMessage());
        if (cause instanceof IOException) {
            session.close(true).awaitUninterruptibly();
        }
    }
}
