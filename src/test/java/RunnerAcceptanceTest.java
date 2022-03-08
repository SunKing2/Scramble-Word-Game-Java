import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

class RunnerAcceptanceTest {

	private ScrambleLogger logger = new ScrambleLogger();
	private ScrambleController ctr = new ScrambleController(logger);
	private  StringWriter out = new StringWriter();
	private  PrintWriter pw = new PrintWriter(out);
	private  String nick = "SunKing2";
	
	{
	  ctr.addPlayer(0, pw, "SunKing2");
	  pw = new PrintWriter(out);
	}

	private void assertGuessesProduceOutput(String expected, String ... guesses) {
		ctr.startGame();
		for(String guess: guesses) {
		  try {
			ctr.processInput(nick, guess);
		} catch (SimulationException e) {
			e.printStackTrace();
		}
		}
		ctr.endGame();
		var actual = out.toString();
		assertEquals(expected, actual);
	}	
    	
	

	@Test
	void testProduceGoodOutput() {
    String expected = "\n" + 
      """
         ** SunKing2 just entered (1 players now) **
        
         Get ready for a round of TSCRAM!
        
         The letters are:
            C  H  E  L
        
            A  T  I  O
        
            N  S  E  S
        
            A  R  I  N
        
        yup, right: 4 points!
        yup, right: 4 points!
        yup, right: 6 points!
        yup, right: 6 points!
        yup, right: 15 points!
        
          !! Time's up !!
        
         SCORE:
        
           SunKing2     :  135 (*CHASTE for 15 points!)
        """;
	    assertGuessesProduceOutput(expected, "eat", "ate", "eats", "seat", "chaste");
	}

}
