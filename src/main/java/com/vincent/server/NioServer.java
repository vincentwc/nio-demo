package com.vincent.server;

public class NioServer {

    public static void main(String[] args) {
        int port = 80;
//创建服务器线程
        NioServerWork nioServerWork = new NioServerWork(port);
        new Thread(nioServerWork, "server").start();
    }
}
