package org.stathry.nettyx;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.handler.timeout.WriteTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;
import org.stathry.nettyx.handler.HttpChannelHandler;
import org.stathry.nettyx.util.ApplicationContextUtils;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Created by dongdaiming on 2018-11-14 20:04
 */
public class HttpPipelineFactory {

    public static class SimpleHttpPipelineFactory1 implements ChannelPipelineFactory {

        @Override
        public ChannelPipeline getPipeline() throws Exception {
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
            pipeline.addLast("handler", ApplicationContextUtils.getBean(HttpChannelHandler.class));
            return pipeline;
        }
    }

    public static class SimpleHttpPipelineFactory2 implements ChannelPipelineFactory {

        @Override
        public ChannelPipeline getPipeline() throws Exception {
            return Channels.pipeline(new ObjectEncoder(),
                    new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
                    new SimpleChannelUpstreamHandler());
        }
    }

    public static class SimpleHttpPipelineFactory3 implements ChannelPipelineFactory {

        @Override
        public ChannelPipeline getPipeline() throws Exception {
            final Timer timer = new HashedWheelTimer(60, TimeUnit.SECONDS);
            ChannelPipeline pipeline = Channels.pipeline();
            pipeline.addLast("writeTimeout", new WriteTimeoutHandler(timer, 60));
            pipeline.addLast("readTimeout", new ReadTimeoutHandler(timer, 60));
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
            pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
            pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
//            pipeline.addLast("handler", restHandler);
            return pipeline;
        }
    }
}
