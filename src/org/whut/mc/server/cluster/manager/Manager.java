package org.whut.mc.server.cluster.manager;

import org.apache.mina.core.session.IoSession;
import org.whut.mc.server.core.communication.Request;
import org.whut.mc.server.core.mina.Service;

import java.util.Map;

/**
 * Created by yangyang on 16-2-24.
 */
public interface Manager extends Service{
    void regstry(IoSession sessions, Request request);
    Map<IoSession, Device> getTab();
}
