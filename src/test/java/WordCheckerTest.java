import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WordCheckerTest {
	
	{	
	WordChecker.readDictionary("bla");
	}

	@Test
	void testInLetters() {
	    var sLetters = "CHELATIONSESARIN";
	    var guess1 = "";
	    var sExpected = "";
	    var sActual = "";
	    
	    guess1 = "chic";
	    sExpected = "oops";
	    sActual = WordChecker.inLetters(sLetters, guess1);
	    assertEquals(sExpected, sActual);
	    
	    guess1 = "chi";
	    sExpected = "";
	    sActual = WordChecker.inLetters(sLetters, guess1);
	    assertEquals(sExpected, sActual);
	    
	    guess1 = "nine";
	    sExpected = "";
	    sActual = WordChecker.inLetters(sLetters, guess1);
	    assertEquals(sExpected, sActual);
	}

	@Test
	void testInDictionary() {
	    var guess1 = "";
	    var sExpected = "";
	    var sActual = "";

	    guess1 = "chice";
	    sExpected = "oops";
	    sActual = WordChecker.inDictionary(guess1);
	    assertEquals(sExpected, sActual);
	    
	    guess1 = "ich";
	    sExpected = "";
	    sActual = WordChecker.inDictionary(guess1);
	    assertEquals(sExpected, sActual);
	    
	    guess1 = "chi";
	    sExpected = "";
	    sActual = WordChecker.inDictionary(guess1);
	    assertEquals(sExpected, sActual);
	    
	    guess1 = "nine";
	    sExpected = "";
	    sActual = WordChecker.inDictionary(guess1);
	    assertEquals(sExpected, sActual);
	}

}
