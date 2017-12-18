package com.nepxion.thunder.protocol.netty;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

import com.nepxion.thunder.serialization.SerializerExecutor;

public class NettyObjectDecoder extends LengthFieldBasedFrameDecoder {
    // 半包解码器。超出maxMessageSize上限，会抛出异常
    public NettyObjectDecoder(int maxMessageSize) {
        super(maxMessageSize, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext context, ByteBuf in) throws Exception {
        Object object = null;
        ByteBuf buf = null;
        try {
            Object decode = super.decode(context, in);
            if (decode == null) {
                return null;
            }

            buf = (ByteBuf) decode;

            int startIndex = buf.readerIndex();
            int endIndex = startIndex + buf.readableBytes();
            buf.markReaderIndex();

            byte[] bytes = new byte[endIndex - startIndex];
            buf.readBytes(bytes);

            object = SerializerExecutor.deserialize(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            ReferenceCountUtil.release(buf);
        }

        return object;
    }
}