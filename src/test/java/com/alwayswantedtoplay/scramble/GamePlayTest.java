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
		ctr = new ScrambleController(logger);
		out = new StringWriter();
		pw = new PrintWriter(out);
		
		ctr.comm.addParticipant(pw, nick);
		ctr.addPlayer(0, nick);
		ctr.startGame();
	}

	@Test
	void testDifferentPlayerName() throws SimulationException {
	    nick = "di";
		ctr.comm.addParticipant(pw, nick);
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
		ctr.comm.addParticipant(pw2, "di");	    
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
