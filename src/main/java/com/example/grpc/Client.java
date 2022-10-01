package com.example.grpc;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class Client {
    public static void main(String[] args) throws Exception {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
        final var channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        // It is up to the client to determine whether to block the call
        // Here we create a blocking stub, but an async stub,
        // or an async stub with Future are always possible.
        var stub = GreetingServiceGrpc.newStub(channel);
        var request = GreetingsService.HelloRequest.newBuilder()
                .setName("Stein")
                .build();

        // Make an Asynchronous call. Listen to responses w/ StreamObserver
        stub.greeting(request, new StreamObserver<>() {
            public void onNext(GreetingsService.HelloResponse response) {
                System.out.print(response);
            }

            public void onError(Throwable t) {
            }

            public void onCompleted() {
                // Typically you'll shutdown the channel somewhere else.
                // But for the purpose of the lab, we are only making a single
                // request. We'll shutdown as soon as this request is done.
                channel.shutdownNow();
            }
        });

        System.out.println("Waiting responses...");
        Thread.sleep(3000);
        System.out.println("Exiting.");
    }
}