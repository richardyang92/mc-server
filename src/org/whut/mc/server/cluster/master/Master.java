package org.whut.mc.server.cluster.master;

import org.whut.mc.server.core.communication.Commend;
import org.whut.mc.server.core.communication.Server;
import org.whut.mc.server.core.log.Log;

import java.util.Scanner;

/**
 * Created by yangyang on 16-1-27.
 */
public class Master implements Server, Runnable {
    private static Log log;
    private MasterCommend cmdThread;

    public Master(int port, String configPath) {
        cmdThread = new MasterCommend(port, configPath, true);
    }

    static {
        log = Log.getLogger(Master.class);
    }

    @Override
    public void start() {
        Thread mt = new Thread(cmdThread);
        mt.start();
    }

    @Override
    public void stop() {
        cmdThread.setFlag(false);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            if (str.equals(Commend.START.getCommend())) {
                log.info("start commend!");
                start();
            } else if (str.equals(Commend.STOP.getCommend())) {
                log.info("stop commend");
                stop();
                break;
            }
        }
        Thread.interrupted();
    }
}
