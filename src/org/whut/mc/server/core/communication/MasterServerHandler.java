package org.whut.mc.server.core.communication;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.whut.mc.server.core.log.Log;

import java.io.IOException;
import java.net.InetSocketAddress;

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
        Class clazz = Class.forName(request.getResolver());
        Resolver resolver = (Resolver) clazz.newInstance();
        Response response = resolver.resolve(request.getData());
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
