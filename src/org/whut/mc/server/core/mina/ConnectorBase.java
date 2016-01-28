package org.whut.mc.server.core.mina;

import org.apache.mina.core.filterchain.IoFilter;

/**
 * Created by yangyang on 2015/11/17.
 */
public abstract class ConnectorBase extends ServiceBase {
    public ConnectorBase(IoFilter... filters) {
        super(filters);
    }

    protected abstract void resolveConnectFailException();
    protected abstract void resolveConnectCloseException();
}
