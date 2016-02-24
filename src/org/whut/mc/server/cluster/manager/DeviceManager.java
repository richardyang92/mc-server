package org.whut.mc.server.cluster.manager;

import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import org.whut.mc.server.core.communication.Codec;
import org.whut.mc.server.core.communication.Request;
import org.whut.mc.server.core.log.Log;
import org.whut.mc.server.core.util.HeartBeat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yangyang on 16-2-24.
 */
public class DeviceManager implements Manager {
    private static Log log;
    private static Map<IoSession, Device> tab;
    private static int count = 0;

    private DeviceManagerThread dMThread;
    private TabThread tThread;
    private List<TabKV> tabKVList;
    private Lock lock = new ReentrantLock();

    static {
        log = Log.getLogger(DeviceManager.class);
        tab = new HashMap<>();
    }

    {
        tabKVList = new ArrayList<>();
        dMThread = new DeviceManagerThread(true);
        tThread = new TabThread(true);
    }

    @Override
    public void regstry(IoSession session, Request request) {
        lock.lock();
        tabKVList.add(new TabKV(request, session));
        lock.unlock();
    }

    @Override
    public Map<IoSession, Device> getTab() {
        return tab;
    }

    @Override
    public void init() {
        Thread thread = new Thread(dMThread);
        thread.start();
        Thread thread1 = new Thread(tThread);
        thread1.start();
    }

    @Override
    public void destroy() {
        dMThread.setFlag(false);
        tThread.setFlag(false);
    }

    private class DeviceManagerThread implements Runnable {
        private boolean flag;
        private HeartBeat heartBeat = new HeartBeat();

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public DeviceManagerThread(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            log.info("dm start!");
            while (flag) {
                lock.lock();
                if (tabKVList.size() != 0) {
                    for (TabKV tabKV : tabKVList) {
                        log.info(tabKV.toString());
                        Request request = tabKV.getRequest();
                        IoSession session = tabKV.getSession();
                        byte[] msg = request.getData();
                        String resolver = request.getResolver();
                        log.info("msg: {}, resolver: {}", msg, resolver);
                        if (resolver.equals("org.whut.mc.server.core.util.HeartBeat")) {
                            try {
                                String json = heartBeat.resolve(msg);
                                JSONObject object = new JSONObject(json);
                                Device device = new Device();
                                device.setBbh(object.getString("bbh"));
                                device.setCount(count);
                                tab.put(session, device);
                            } catch (UnsupportedEncodingException e) {
                                log.error(e.getMessage());
                            }
                        } else {
                            try {
                                Class clazz = Class.forName(resolver);
                                Codec codec = (Codec) clazz.newInstance();
                                String json = codec.resolve(msg);
                                log.info("JSON Msg: {}", json);
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        }
                    }
                    tabKVList.clear();
                }
                lock.unlock();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
            log.info("dm stop!");
            Thread.interrupted();
        }
    }

    private class TabKV {
        private Request request;
        private IoSession session;

        public TabKV(Request request, IoSession session) {
            this.request = request;
            this.session = session;
        }

        public Request getRequest() {
            return request;
        }

        public IoSession getSession() {
            return session;
        }
    }

    private class TabThread implements Runnable {
        private boolean flag;

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public TabThread(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            log.info("tT start!");
            List<IoSession> ulSessions = new ArrayList<>();
            while (flag) {
                count++;
                lock.lock();
                for (Map.Entry<IoSession, Device> entry : tab.entrySet()) {
                    IoSession session = entry.getKey();
                    Device device = entry.getValue();
                    if (Math.abs(device.getCount() - count) > 5) {
                        ulSessions.add(session);
                    }
                }

                for (IoSession session : ulSessions) {
                    tab.remove(session);
                }

                lock.unlock();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
                log.info(tab.toString());
            }
            log.info("tT stop!");
            Thread.interrupted();
        }
    }
}
