package org.whut.mc.server.test;

import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.util.CodecUtil;

import java.io.*;
import java.net.Socket;

/**
 * Created by yangyang on 2016/1/17.
 */
public class TestClient {
    private static Log log = Log.getLogger(TestClient.class);

    public static void main(String[] args) throws InterruptedException {
        byte arr[] = {0x68,0x01,0x0e,0x01,0x01,0x01,0x01,0x01,0x03,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x0f,0x13,0x16};
        /*byte[] b = null;
        for (byte i : arr) {
            byte[] bt = new byte[1];
            bt[0] = i;
            b = CodecUtil.merge(b, bt);
        }
        String s = CodecUtil.getHex(b);
        System.out.println(s);*/
        Socket client = null;
        try {
            client = new Socket("127.0.0.1", 9988);
            OutputStream os = client.getOutputStream();
            BufferedOutputStream bfs = new BufferedOutputStream(os);
            bfs.write(arr);
            bfs.flush();

            InputStream is = client.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            int i = 0;
            while (true) {
                byte[] result = new byte[0];
                while (true) {
                    byte[] tmp = new byte[1];
                    bis.read(tmp, 0, 1);
                    result = CodecUtil.merge(result, tmp);
                    if (bis.available() == 0) {
                        i++;
                        break;
                    }

                }
                CodecUtil.showMsg(result);
                if (i == 10) {
                    break;
                }
                Thread.sleep(1000);
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
