import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WordChecker {
	
	private static final Pattern reEmpty = Pattern.compile("");
	private static List<String> dict = null;

	public static void readDictionary(String sLetters) {
		try {
			dict = Files.readAllLines(new File("large_words.txt").toPath(), Charset.defaultCharset() );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String largeWords(int iWordsLength) {
		// TODO Auto-generated method stub
		return "not implemented :(  sowwy";
	}

	public static boolean isWord(String sString) {
		 
		return dict.contains(sString);
	}
	
	//TODO contains compile, move it to top of class
	private static List<Character> StringAsList(String test) {
		return reEmpty.splitAsStream(test)
			    .map(i -> i.charAt(0))
			    .collect(Collectors.toList());
	}
	
	
	public static String inLetters(String letters, String sInput) {
		var lisLetters = StringAsList(letters.toLowerCase());
		var lisInput  = StringAsList(sInput);
		
		for(Character letter: lisInput) {
			if (! lisLetters.remove(letter)) {
			   return "oops";	
			}
		}
		return "";
	}

	public static String inDictionary(String word) {
		if(isWord(word)) {
			return "";
		}
		return "oops";
	}

	public static int score(String word) {
        int iLen = word.length();
        int iScore;
        switch (iLen)
        {
            case 1: 
            case 2:
                iScore = iLen;
                break;
            case 3:
                iScore = 4;
                break;
            case 4:
                iScore = 6;
                break;
            case 5:
                iScore = 9;
                break;
            case 6:
                iScore = 15;
                break;
            case 7:
                iScore = 30;
                break;
            case 8:
                iScore = 55;
                break;
            case 9:
                iScore = 90;
                break;
            case 10:
                iScore = 140;
                break;
            case 11:
                iScore = 200;
                break;
            case 12:
                iScore = 280;
                break;
            case 13:
                iScore = 400;
                break;
            case 14:
                iScore = 550;
                break;
            case 15:
                iScore = 800;
                break;
            case 16:
                iScore = 1200;
                break;
            default:
               iScore = (int)Math.round(Math.pow(iLen,4.471) / 201.6);
               break;
        }
        return iScore;
    }


	public static int myRandom(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void readDictionary(ScrambleLogger logger) {
		// TODO Auto-generated method stub
		
	}

}
