package org.stathry.nettyx;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.stathry.nettyx.util.Executors2;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

/**
 * HttpServerBootstrap
 * Created by dongdaiming on 2018-11-14 16:45
 */
public class HttpServerBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerBootstrap.class);

    public static void main(String[] args) throws Exception {
        startSpringContext();

        startHttpServer(8080);
    }

    private static void startSpringContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
    }

    private static void startHttpServer(int port) throws Exception {
        ChannelFactory channelFactory = newChannelFactory();
        ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

        bootstrap.setPipelineFactory(new HttpPipelineFactory.SimpleHttpPipelineFactory1());

        final Channel channel = bootstrap.bind(new InetSocketAddress(port));

        LOGGER.info("http server started at {}.", port);

        addShutdownHook(channelFactory, channel);
    }

    private static void addShutdownHook(ChannelFactory channelFactory, Channel channel) {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                ChannelFuture future = channel.close();
                future.awaitUninterruptibly();
                channelFactory.releaseExternalResources();
            }
        });
    }

    private static ChannelFactory newChannelFactory() {
        Executor bossExe = Executors2.newDefaultExecutorService("boss-", 1, 2, 16);
        Executor workExe = Executors2.newDefaultExecutorService("worker-", 16, 1024, 10240);
        return new NioServerSocketChannelFactory(bossExe, workExe);
    }



}
