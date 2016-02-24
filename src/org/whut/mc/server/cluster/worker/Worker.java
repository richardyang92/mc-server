package org.whut.mc.server.cluster.worker;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.whut.mc.server.core.communication.Server;
import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.mina.Acceptor;
import org.whut.mc.server.core.mina.Connector;

/**
 * Created by yangyang on 16-2-24.
 */
public class Worker implements Server {
    private static Log log;

    private Acceptor<NioSocketAcceptor> a;
    private Connector<NioSocketConnector> c;

    static {
        log = Log.getLogger(Worker.class);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
