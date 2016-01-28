package org.whut.mc.server.core.mina;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.whut.mc.server.core.log.Log;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yangyang on 2015/11/17.
 */
public class ISocketConnector<C extends SocketConnector> extends ConnectorBase
        implements Connector {
    private static Log log;

    private static int connectTimeout;

    private C c;
    private IoSession session;
    private boolean flag = false;
    private boolean isConnected = false;

    private Queue<Object> queue = new LinkedList<Object>();
    private Lock lock = new ReentrantLock();

    public IoSession getSession() {
        return session;
    }

    {
        connectTimeout = 1000;
    }

    static {
        log = Log.getLogger(ISocketConnector.class);
    }

    public ISocketConnector(C c, IoHandler handler, IoFilter... filters) {
        super(filters);
        this.c = c;
        this.c.setFilterChainBuilder(chain);
        this.c.setHandler(handler);
    }

    public ISocketConnector(C c, IoFilter... filters) {
        super(filters);
        this.c = c;
        this.c.setFilterChainBuilder(chain);
        this.c.setHandler(new ISocketConnectorHandler());
    }

    @ErrorMethod(Error.CONNECT_FAIL_EXCEPTION_ENUM)
    public boolean connect(String IP, int port) {
        InetSocketAddress defaultAddr = new InetSocketAddress(IP, port);
        c.setDefaultRemoteAddress(defaultAddr);
        isConnected = connect0(defaultAddr);

        if (isConnected) {
            log.info("connect to {}:{} success", IP, port);
        }
        return isConnected;
    }

    public boolean connect(InetSocketAddress addr) {
        return connect(addr.getHostName(), addr.getPort());
    }

    public void send(Object obj) {
        lock.lock();
        try {
            if (session.isConnected()) {
                log.info("发送对象{}", obj);
                while (!queue.isEmpty()) {
                    Object o = queue.poll();
                    WriteFuture wf = session.write(o);
                    wf.awaitUninterruptibly();
                }
                WriteFuture wf = session.write(obj);
                wf.awaitUninterruptibly();
            } else if (session.isClosing() && isConnected) {
                log.info("缓存对象{}", obj);
                queue.offer(obj);
            } else {
                destroy();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void resolveConnectFailException() {

    }

    @Override
    protected void resolveConnectCloseException() {

    }

    public void init() {
        c.setConnectTimeoutMillis(connectTimeout);
        SocketSessionConfig scfg = c.getSessionConfig();
        scfg.setBothIdleTime(bothIdleTime);
        scfg.setSendBufferSize(sendBufferSize);
        scfg.setReadBufferSize(readBufferSize);
        scfg.setReceiveBufferSize(receiveBufferSize);
        log.info("ISocketConnector init complete");
    }

    public void destroy() {
        session.close(true);
        session.getCloseFuture().awaitUninterruptibly();
        log.info("session已关闭");
        isConnected = false;
        c.dispose();
    }

    private boolean connect0(InetSocketAddress address) {
        ConnectFuture cf = c.connect(address);
        cf.awaitUninterruptibly();

        if (cf.isConnected()) {
            session = cf.getSession();
            return true;
        } else {
            try {
                resolveError(ISocketConnector.class, "connect", String.class, int.class);
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage());
            }
        }

        return false;
    }

    private class ISocketConnectorHandler extends IoHandlerAdapter {
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            log.info("ISocketConnector has received message: {}", message.toString());
        }
    }
}
