import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HighScoresTest {
	
	String expected = "";
	String actual = "";
	HighScores hs;
	
	@BeforeEach
	void setUp() throws Exception {
		hs = new HighScores();
	}

	@Test
	void testOneScore() {
	    hs.add(135,"   SunKing2     :  135 (*CHASTE for 15 points!)");
	    expected = "   SunKing2     :  135 (*CHASTE for 15 points!)\n";
	    actual = hs.toString();
		assertEquals(expected, actual);
	}

	@Test
	void testTwoScores() {
	    hs.add(135,  "   SunKing2     :  135 (*CHASTE for 15 points!)");
	    hs.add(1125, "   Guest1       : 1125 (*CHELATIONS for 140 points!)");
	    expected = "   Guest1       : 1125 (*CHELATIONS for 140 points!)\n   SunKing2     :  135 (*CHASTE for 15 points!)\n";
	    actual = hs.toString();
		assertEquals(expected, actual);
	}

	@Test
	void testTenOfElevenScores() {
	    hs.add(135,  "   SunKing2     :  135 (*CHASTE for 15 points!)");
	    hs.add(133,  "   SunKing2     :  133 (*CHASTE for 15 points!)");
	    hs.add(132,  "   SunKing2     :  132 (*CHASTE for 15 points!)");
	    hs.add(144,  "   SunKing2     :  144 (*CHASTE for 15 points!)");
	    hs.add(166,  "   SunKing2     :  166 (*CHASTE for 15 points!)");
	    hs.add(1125, "   Guest1       : 1125 (*CHELATIONS for 140 points!)");
	    hs.add(177,  "   SunKing2     :  177 (*CHASTE for 15 points!)");
	    hs.add(199,  "   SunKing2     :  199 (*CHASTE for 15 points!)");
	    hs.add(111,  "   SunKing2     :  111 (*CHASTE for 15 points!)");
	    hs.add(121,  "   SunKing2     :  121 (*CHASTE for 15 points!)");
	    hs.add(130,  "   SunKing2     :  130 (*CHASTE for 15 points!)");

	    String expected = ""
			+ "   Guest1       : 1125 (*CHELATIONS for 140 points!)\n"
			+ "   SunKing2     :  199 (*CHASTE for 15 points!)\n"
			+ "   SunKing2     :  177 (*CHASTE for 15 points!)\n"
			+ "   SunKing2     :  166 (*CHASTE for 15 points!)\n"
			+ "   SunKing2     :  144 (*CHASTE for 15 points!)\n"
			+ "   SunKing2     :  135 (*CHASTE for 15 points!)\n"
			+ "   SunKing2     :  133 (*CHASTE for 15 points!)\n"
			+ "   SunKing2     :  132 (*CHASTE for 15 points!)\n"
			+ "   SunKing2     :  130 (*CHASTE for 15 points!)\n"
			+ "   SunKing2     :  121 (*CHASTE for 15 points!)\n";
	    actual = hs.toString();
		assertEquals(expected, actual);
	}

}
