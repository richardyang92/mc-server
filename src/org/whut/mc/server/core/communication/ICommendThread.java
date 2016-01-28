package org.whut.mc.server.core.communication;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.whut.mc.server.core.codec.FrameCodecFactory;
import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.mina.ISocketAcceptor;

/**
 * Created by yangyang on 16-1-27.
 */
public class ICommendThread implements Runnable {
    private static Log log;

    private ISocketAcceptor<NioSocketAcceptor> acceptor;
    private int port;
    private boolean flag;

    static {
        log = Log.getLogger(ICommendThread.class);
    }

    public ICommendThread(int port, String configPath, boolean flag) {
        acceptor = new ISocketAcceptor<>(new NioSocketAcceptor(),
                new MasterServerHandler(), new ProtocolCodecFilter(new FrameCodecFactory(configPath)));
        this.flag = flag;
        this.port = port;
    }

    private void init() {
        acceptor.init();
        acceptor.bind(port);
    }

    private void destroy() {
        acceptor.destroy();
    }

    @Override
    public void run() {
        log.info("mt start");

        init();
        while (flag) {
            try {
                //log.info("run");
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
