package com.alwayswantedtoplay.scramble;
import java.io.*;
import java.util.*;

public class SoloCommandLineGame {
	
	public static void main(String[] args) throws SimulationException {

		String nick = "SunKing2";
		ScrambleLogger logger = new ScrambleLogger();
		Communicator comm = new PrintWriterCommunicator();
		ScrambleController g = new ScrambleController(comm, logger);
		var out = new StringWriter();
		var pw = new PrintWriter(out);
		g.addPlayer(0, nick);
		g.comm.addParticipant(nick, pw);

		
	    System.out.println("$:");
	    Scanner input = new Scanner(System.in);
	    for(int i = 0; i < 1000; i++){
	        String line = input.nextLine();
	        g.processInput(nick, line);
	        System.out.println(out.toString());
			out.getBuffer().setLength(0);
	    }
	    input.close();
	}

}