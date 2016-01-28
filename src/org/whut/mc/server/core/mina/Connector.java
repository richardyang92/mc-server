package org.whut.mc.server.core.mina;

import org.apache.mina.core.service.IoConnector;

import java.net.InetSocketAddress;

/**
 * Created by yangyang on 2015/12/3.
 */
public interface Connector<C extends IoConnector> extends Service {
    boolean connect(String IP, int port);
    boolean connect(InetSocketAddress addr);
    void send(Object obj);
}
