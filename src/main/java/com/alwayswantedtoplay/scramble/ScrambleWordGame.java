package com.alwayswantedtoplay.scramble;
import java.io.*;
import java.net.*;
import java.util.*;

public class ScrambleWordGame {

    public static void main(String[] args) {
        ScrambleLogger logger = new ScrambleLogger();
        Communicator comm = new PrintWriterCommunicator();
        ScrambleController game = new ScrambleController(comm, logger);
        ServerSocket s;
        try {
            int i = 1;
            s = new ServerSocket(8189);
            System.out.println("telnet localhost 8189 to play");
            System.out.println("waiting for telnet connections...");

            while (true) {
                Socket incoming = s.accept();
                System.out.println("Spawning " + i);
                String name = "Guest" + i;
                Runnable r = new ThreadedEchoHandler(game, incoming, i, name);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ThreadedEchoHandler implements Runnable {
    private Socket incoming;
    private ScrambleController game;
    private String name;

    public ThreadedEchoHandler(ScrambleController game, Socket i, int c, String name) {
        this.game = game;
        incoming = i;
        this.name = name;
    }

    public void run()  {
    	Scanner in;
        try {
            try {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();

                in = new Scanner(inStream);
                PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
                game.addPlayer(0, name);
                game.comm.addParticipant(name, out);

                out.println("%Hello! Enter /exit to exit., you are " + name);

                // echo client input
                boolean done = false;
                while (!done && in.hasNextLine()) {
                    String line = in.nextLine();
                    try {
                        game.processInput(name, line);
                    }
                    catch (SimulationException exc) {
                        System.out.println("pritnln caught:" + exc);
                    }
                    if (line.startsWith("/name ") && line.length() > 6) {
                        this.name = line.substring(6);
                    }
                    if (line.trim().equals("/exit"))
                        done = true;
                }
            	in.close();  // remove me if any problems in this class
            } finally {
                incoming.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}