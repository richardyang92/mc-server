package org.whut.mc.server.core.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.whut.mc.server.core.communication.Parser;
import org.whut.mc.server.core.communication.Request;
import org.whut.mc.server.core.communication.RequestParser;
import org.whut.mc.server.core.util.CodecUtil;

/**
 * Created by yangyang on 2015/12/6.
 */
public class FrameDecoder extends CumulativeProtocolDecoder {
    private Parser parser;
    public FrameDecoder(String xmlPath) {
        parser = new RequestParser(xmlPath);
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (in.remaining() > 0) {
            in.mark();
            byte[] b = new byte[in.limit()];
            for (int i = 0; i < in.limit(); i++) {
                b[i] = in.get();
            }
            CodecUtil.showMsg(b);

            Request request = parser.parser(b);

            if (request == null) {
                in.reset();
                return false;
            } else {
                out.write(request);
            }
        }

        return false;
    }
}
