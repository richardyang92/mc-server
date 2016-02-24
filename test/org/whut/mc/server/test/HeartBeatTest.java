package org.whut.mc.server.test;

import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.util.CodecUtil;
import org.whut.mc.server.core.util.Frame;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by yangyang on 16-2-25.
 */
public class HeartBeatTest {
    private static Log log = Log.getLogger(TestClient.class);

    public static void main(String[] args) throws InterruptedException {
        byte arr[] = Frame.HEART_BEAT;
        Socket client = null;
        try {
            client = new Socket("127.0.0.1", 9988);
            OutputStream os = client.getOutputStream();
            BufferedOutputStream bfs = new BufferedOutputStream(os);

            for (int i = 0; i < 20; i++) {
                CodecUtil.showMsg(arr);
                bfs.write(arr);
                bfs.flush();
                Thread.sleep(3000);
            }

        } catch (IOException e) {
            log.error("IOException : {}", e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                log.error("can't close socket client");
            }
        }

    }
}
