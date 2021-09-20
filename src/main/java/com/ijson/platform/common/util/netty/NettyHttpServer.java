package com.ijson.platform.common.util.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.DispatcherServlet;

@Slf4j
public class NettyHttpServer {

    private int port;
    private DispatcherServlet servlet;

    public NettyHttpServer(Integer port) {
        this.port = port;
    }

    public NettyHttpServer(Integer port, DispatcherServlet servlet) {
        this.port = port;
        this.servlet = servlet;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer(servlet))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            log.info("NettyHttpServer Run successfully");
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync();
            // 等待服务器 socket 关闭 。在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("NettySever start fail", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


}