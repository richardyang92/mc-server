package org.whut.mc.server.core.mina;

import org.apache.mina.core.filterchain.IoFilter;

/**
 * Created by yangyang on 2015/11/17.
 */
public abstract class AcceptorBase extends ServiceBase {
    public AcceptorBase(IoFilter... filters) {
        super(filters);
    }

    protected abstract void resolveClientOfflineException();
    protected abstract void resolveBindFailException();
}
