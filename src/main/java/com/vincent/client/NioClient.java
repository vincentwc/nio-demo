package com.vincent.client;

public class NioClient {

    public static void main(String[] args) {
        int port = 80;
        new Thread(new NioClientWork("127.0.0.1",port),"client").start();
    }
}
