package com.mapproject;

import com.mapproject.operations.RunHandler;
import com.mapproject.resources.Session;

public final class App {

    private Session newSession;

    public App() {
        this.newSession = new Session();
    }

    public void start() {
        RunHandler gameHandler = new RunHandler(newSession);
        gameHandler.runGame(newSession);

    }

    public static void main(String[] args) {

        System.out.println("\t\tThe Trial\n");
        System.out.println("\tA game by Gianmarco Rutigliano\n");

         App app = new App();
         app.start();

    }
}
