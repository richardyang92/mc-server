package org.whut.mc.server.test;

import org.whut.mc.server.cluster.master.Master;
import org.whut.mc.server.core.config.PropConfig;

/**
 * Created by yangyang on 16-1-27.
 */
public class MasterServerTest {
    public static void main(String[] args) {
        int port = Integer.parseInt(PropConfig.getPropConfig().get("server.master.port").toString());
        String configPath = System.getProperty("user.dir") +
                PropConfig.getPropConfig().get("server.config").toString();
        Master server = new Master(port, configPath);
        Thread thread = new Thread(server);
        thread.start();
    }
}
