package com.txt.grpc.streaming;

import com.txt.grpc.service.StockService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StockServer {

    private final int port;
    private final Server server;

    public StockServer(int port) throws IOException {
        this.port = port;
        server = ServerBuilder.forPort(port)
                .addService(new StockService())
                .build();
    }

    public void start() throws IOException {
        server.start();
        log.info("Server started, listening on " + port);
        Runtime.getRuntime()
                .addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        System.err.println("shutting down server");
                        try {
                            StockServer.this.stop();
                        } catch (InterruptedException e) {
                            e.printStackTrace(System.err);
                        }
                        System.err.println("server shutted down");
                    }
                });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws Exception {
        StockServer stockServer = new StockServer(8980);
        stockServer.start();
        if (stockServer.server != null) {
            stockServer.server.awaitTermination();
        }
    }
}
