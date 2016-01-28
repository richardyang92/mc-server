package org.whut.mc.server.core.mina;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.whut.mc.server.core.log.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yangyang on 2015/12/3.
 */
public abstract class ServiceBase implements Service {
    private static Log log;

    protected int bothIdleTime;
    protected int sendBufferSize;
    protected int readBufferSize;
    protected int receiveBufferSize;

    {
        bothIdleTime = 10;
        sendBufferSize = 2048;
        readBufferSize = 2048;
        receiveBufferSize = 5000;
    }

    static {
        log = Log.getLogger(ServiceBase.class);
    }

    protected DefaultIoFilterChainBuilder chain =
            new DefaultIoFilterChainBuilder();

    public ServiceBase(IoFilter... filters) {
        int index = 0;
        for (IoFilter filter : filters) {
            this.chain.addLast("filter" + index, filter);
            index++;
        }
    }

    protected void resolveError(Class<?> clazz, String methodName, Class<?>... params)
            throws NoSuchMethodException {
        Error type = getType(clazz, methodName, params);

        if (type != null) {
            log.info("resolve error -> {}", type.getType());
            try {
                Method method;
                switch (type) {
                    case BIND_FAIL_EXCEPTION_ENUM:
                        method = AcceptorBase.class.getDeclaredMethod("resolveBindFailException", null);
                        method.invoke(this, null);
                        break;
                    case CLINET_OFFLINE_EXCEPTION_ENUM:
                        method = AcceptorBase.class.getDeclaredMethod("resolveClientOfflineException", null);
                        method.invoke(this, null);
                        break;
                    case CONNECT_CLOSE_EXCEPTION_ENUM:
                        method = ConnectorBase.class.getDeclaredMethod("resolveConnectCloseException", null);
                        method.invoke(this, null);
                        break;
                    case CONNECT_FAIL_EXCEPTION_ENUM:
                        method = ConnectorBase.class.getDeclaredMethod("resolveConnectFailException", null);
                        method.invoke(this, null);
                        break;
                    default: break;
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            } catch (InvocationTargetException e) {
                log.error(e.getMessage());
            }
        }
    }

    private Error getType(Class<?> clazz, String methodName, Class<?>... params)
            throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, params);
        ErrorMethod em = method.getAnnotation(ErrorMethod.class);
        return em.value();
    }
}
