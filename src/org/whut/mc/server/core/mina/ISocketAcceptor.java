package org.whut.mc.server.core.mina;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.whut.mc.server.core.log.Log;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by yangyang on 2015/11/17.
 */
public class ISocketAcceptor<A extends SocketAcceptor> extends AcceptorBase
        implements Acceptor {
    private static Log log;

    private A a;

    public ISocketAcceptor(A a, IoHandler handler, IoFilter... filters) {
        this(a, filters);
        this.a.setFilterChainBuilder(chain);
        this.a.setHandler(handler);
    }

    static {
        log = Log.getLogger(ISocketAcceptor.class);
    }

    public ISocketAcceptor(A a, IoFilter... filters) {
        super(filters);
        this.a = a;
        this.a.setFilterChainBuilder(chain);
        this.a.setHandler(new ISocketAcceptorHandler());
    }

    @ErrorMethod(Error.BIND_FAIL_EXCEPTION_ENUM)
    public void bind(int port) {
        InetSocketAddress defaultAddr = new InetSocketAddress(port);
        this.a.setDefaultLocalAddress(defaultAddr);
        try {
            this.a.bind(defaultAddr);
            log.info("ISocketAcceptor bind success");
        } catch (IOException e) {
            log.error(e.getMessage());
            try {
                resolveError(ISocketAcceptor.class, "bind", int.class);
            } catch (NoSuchMethodException e1) {
                log.error(e1.getMessage());
            } finally {
                this.a.dispose();
            }
        }
    }

    public void send(byte[] bytes, IoSession session) {
        if (a.getManagedSessions().values().contains(session)) {
            session.write(bytes);
        }
    }

    @Override
    protected void resolveClientOfflineException() {

    }

    @Override
    protected void resolveBindFailException() {
        log.info("resolve bind fail exception!!!");
    }

    public void init() {
        SocketSessionConfig scfg = this.a.getSessionConfig();
        scfg.setBothIdleTime(bothIdleTime);
        scfg.setSendBufferSize(sendBufferSize);
        scfg.setReadBufferSize(readBufferSize);
        scfg.setReceiveBufferSize(receiveBufferSize);
        log.info("ISocketAcceptor init complete");
    }

    public void destroy() {
        a.unbind();
        a.dispose();
        log.info("ISocketAcceptor destroy complete");
    }

    private class ISocketAcceptorHandler extends IoHandlerAdapter {
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            log.info("ISocketAcceptor has received message: {}", message.toString());
        }
    }
}
