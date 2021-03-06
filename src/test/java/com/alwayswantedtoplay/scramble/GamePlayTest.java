package com.alwayswantedtoplay.scramble;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GamePlayTest {
	
	ScrambleLogger logger = new ScrambleLogger();
	String nick = "SunKing2";
	String sExpected = "";
	String sActual = "";

	ScrambleController ctr;
	StringWriter out;
	PrintWriter pw;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		Communicator comm = new PrintWriterCommunicator();
		ctr = new ScrambleController(comm, logger);
		ctr.bRandomMode = false;
		out = new StringWriter();
		pw = new PrintWriter(out);
		
		ctr.comm.addParticipant(nick, pw);
		ctr.addPlayer(0, nick);
		ctr.startGame();
	}

	@Test
	void testDifferentPlayerName() throws SimulationException {
	    nick = "di";
		ctr.comm.addParticipant(nick, pw);
	    ctr.addPlayer(0, nick);
	    ctr.processInput(nick, "eat");
	    sExpected = " di: EAT=4, \n";
	    sActual = ctr.showWords();		
		assertEquals(sExpected, sActual);
	}

	@Test
	void testTypeW() throws SimulationException {
	    ctr.processInput(nick, "eat");
	    out.getBuffer().setLength(0);
	    ctr.processInput(nick, "w");
	    sExpected = "  Words used:\n SunKing2: EAT=4, \n";
	    sActual = out.toString();
		assertEquals(sExpected, sActual);
	}
	@Test
	void testSecondPlayer() throws SimulationException {
	    var out2 = new StringWriter();
	    var pw2 = new PrintWriter(out2);
		ctr.comm.addParticipant("di", pw2);	    
	    ctr.addPlayer(1, "di");
	    ctr.processInput(nick, "eat");
	    ctr.processInput("di", "sate");
	    out.getBuffer().setLength(0);
	    ctr.processInput(nick, "w");
	    sExpected = "  Words used:\n di: SATE=6, \n SunKing2: EAT=4, \n";
	    sActual = out.toString();		
		assertEquals(sExpected, sActual);
	}
	@Test
	void testLeaderBoardTwoPlayers() throws SimulationException {
	    var out2 = new StringWriter();
	    var pw2 = new PrintWriter(out2);
		ctr.comm.addParticipant("di", pw2);	    
	    ctr.addPlayer(1, "di");
	    ctr.processInput(nick, "eat");
	    ctr.processInput("di", "sate");
	    ctr.endGame();
	    out.getBuffer().setLength(0);
	    ctr.processInput(nick, "/hi");
	    sExpected = "  High Scores:\n"
	    		+ "   di           :    6 (*SATE for 6 points!)\n"
	    		+ "   SunKing2     :    4\n"
	    		+ "\n";
	    sActual = out.toString();		
		assertEquals(sExpected, sActual);
	}
	@Test
	void testGoodWord1() throws SimulationException {
	    out.getBuffer().setLength(0);
	    ctr.processInput(nick, "eat");
	    sExpected = "yup, right: 4 points!\n";
	    sActual = out.toString();
		assertEquals(sExpected, sActual);
	}
	@Test
	void testGoodWord2() throws SimulationException {
	    out.getBuffer().setLength(0);
	    ctr.processInput(nick, "seat");
	    sExpected = "yup, right: 6 points!\n";
	    sActual = out.toString();
		assertEquals(sExpected, sActual);
	}
	@Test
	void testBadWordLetters() throws SimulationException {
	    out.getBuffer().setLength(0);
	    ctr.processInput(nick, "zuz");
	    sExpected = "nope, not in letters!\n";
	    sActual = out.toString();
		assertEquals(sExpected, sActual);
	}
	@Test
	void testBadWordDictionary() throws SimulationException {
	    out.getBuffer().setLength(0);
	    ctr.processInput(nick, "eae");
	    sExpected = "nope, not in dictionary!\n";
	    sActual = out.toString();
		assertEquals(sExpected, sActual);
	}

}
