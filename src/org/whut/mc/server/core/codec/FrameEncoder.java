package org.whut.mc.server.core.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.whut.mc.server.core.util.CodecUtil;

/**
 * Created by yangyang on 2015/12/6.
 */
public class FrameEncoder extends ProtocolEncoderAdapter {

    public FrameEncoder() {
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        if (message instanceof byte[]) {
            byte[] b = (byte[]) message;
            IoBuffer buffer = IoBuffer.allocate(b.length);
            buffer.isAutoExpand();
            buffer.put(b);
            buffer.flip();
            out.write(buffer);
            out.flush();
        }
    }
}
