package io.reactivesocket.cli.i9n;

import io.reactivesocket.cli.Main;

public class PingClient {
    public static void main(String[] args) throws Exception {
        Main.main("--str", "--debug", "-i", "Hello", "tcp://localhost:7878");
    }
}
