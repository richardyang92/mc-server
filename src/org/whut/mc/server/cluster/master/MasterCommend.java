package org.whut.mc.server.cluster.master;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.whut.mc.server.cluster.manager.DeviceManager;
import org.whut.mc.server.cluster.manager.Manager;
import org.whut.mc.server.core.codec.FrameCodecFactory;
import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.mina.ISocketAcceptor;

/**
 * Created by yangyang on 16-1-27.
 */
public class MasterCommend implements Runnable {
    private static Log log;

    private ISocketAcceptor<NioSocketAcceptor> acceptor;
    private int port;
    private boolean flag;
    private Manager manager;

    static {
        log = Log.getLogger(MasterCommend.class);
    }

    {
        manager = new DeviceManager();
    }

    public MasterCommend(int port, String configPath, boolean flag) {
        acceptor = new ISocketAcceptor<>(new NioSocketAcceptor(),
                new MasterHandler(manager), new ProtocolCodecFilter(new FrameCodecFactory(configPath)));
        this.flag = flag;
        this.port = port;
    }

    private void init() {
        acceptor.init();
        acceptor.bind(port);
        manager.init();
    }

    private void destroy() {
        acceptor.destroy();
        manager.destroy();
    }

    @Override
    public void run() {
        log.info("mt start");
        init();
        while (flag) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("pause fail");
            }
        }
        log.info("mt stop");
        destroy();
        Thread.interrupted();
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
