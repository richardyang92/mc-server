package org.whut.mc.server.core.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;

/**
 * Created by yangyang on 2015/12/3.
 */
public interface Acceptor<A extends IoAcceptor> extends Service {
    void bind(int port);
    void send(byte[] bytes, IoSession session);
}
