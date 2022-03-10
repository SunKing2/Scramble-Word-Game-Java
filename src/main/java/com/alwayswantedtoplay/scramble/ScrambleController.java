package com.alwayswantedtoplay.scramble;
import java.io.*;
import java.util.*;

public  class ScrambleController extends java.lang.Object
{
    private boolean bGameOn;
    private Hashtable<String, Integer> hScores;
    private Hashtable<String, String> hHighs;
    private Hashtable<String, String> hWords; // hWords words used by each person + score
    private Hashtable<String, String> hWordOwners; // just the current game's list of used words
    
    private ThreadedTimer thrTimer;
    private String sLetters = "CHELATIONSESARIN";
    private String sHumanLetters;
	
	private int iLongestWord;
	private String sLongestWord;
	private String sLongestPlayer;
	private Hashtable<String, String> hSquelch = new Hashtable<>();
	private int iGhostCount = 0;
	private Hashtable<String, Integer> hGhost = new Hashtable<>();
	private String sOldScores = ""; 
	
	private Hashtable<String, String> hRd = new Hashtable<>();
	
	private ScrambleLogger logger; //  = Globals.getLogger();
    private HighScores hs = new HighScores();
    public Communicator comm = new Communicator();
    private boolean bShowSender = true;

	//private ScramPlayer player = new ScramPlayer();

    public ScrambleController(ScrambleLogger logger)
    {
		this.logger = logger;
        if (hScores == null)
        {
            hScores = new Hashtable<>();
            hHighs = new Hashtable<>();
            hWords = new Hashtable<>();
            hWordOwners = new Hashtable<>();
        }
        
        // uncommented these 3 february 15
        WordChecker.readDictionary(logger);
        //logger.println("WordGameServerInterface() calling readDictionary");
        //startGame();
    }
        
 
    
	public ScrambleLogger getLogger()
	{
		return logger;
	}
    public void startGame()
    {
        if (bGameOn)
        {
            logger.println("game already on, cannot start");
            return;
        }

        if (thrTimer != null && thrTimer.canStart() == false) 
        {
            logger.println("Failed attempt to jumpstart the game.");
            //tellAll("jumpstarting failed");
            return;                    
        }
        tellAll("", "\n Get ready for a round of " + getGameName() + "!\n");

        hRd.clear();  // clear list of players who typed rd
        hWordOwners.clear();
        System.gc(); // garbage collection so it doesn't do during game.

        try
        {
            // for each player, reinitialize his hash tables
            for (Enumeration<String> enn = hScores.keys() ; enn.hasMoreElements() ;) 
            {
                String sKey = (String)enn.nextElement();
                //logger.println(sKey);
                // sKey is the player's name
                hScores.put(sKey, 0);
                hHighs.put(sKey, "");
                hWords.put(sKey, new String());
				iLongestWord = 0;
				sLongestPlayer = "";
				sLongestWord = "";
            }
        }
        catch (Exception enume)
        {
            logger.println("WordGameServerInterface.startGame(refreshing hashtables:" + enume);
        }

        thrTimer = null;
        thrTimer = new ThreadedTimer(this, logger);
        try
        {
            String sLetters = setLetters();
            bGameOn = true;
            WordChecker.readDictionary(sLetters);
            
//            tellAll(" *** Starting a " + getGameName() + " Round! (" + Globals.getGameLength()/1000 +" seconds)");
            showLetters();
            thrTimer.start();
            
        }
        catch (Exception ioe)
        {
            logger.println(ioe.toString() + "...cannot tell anyone of first game");
        }
    }
    public void setShowSender(boolean b) {
    	this.bShowSender = b;
    }
    public boolean getShowSender() {
    	return bShowSender;
    }

	public String getGameName()
	{
		return "TSCRAM";
	}

    public void showLetters()
    {
        tellAll("", getHumanLetters());
    }
    public void addPlayer(int iPlayer, String sPlayer)
    {
        try
        {
            
            hScores.put(sPlayer, 0);
            hHighs.put(sPlayer, "");
            hWords.put(sPlayer, new String());

            tellOne(sPlayer, sOldScores);

            tellAll(""," ** " + sPlayer + " just entered (" + hScores.size() + " players now) **");

            
            if (bGameOn)
            {
                tellOne(sPlayer, getHumanLetters());
            }
        }
        catch (Exception e)
        {
            logger.println("addPlayer:" + e);
        }
        
    }
    public void removePlayer(int iPlayer, PrintWriter out, String sPlayer)
    {
        removePlayer(sPlayer);
    }
    public void removePlayer(String sPlayer)
    {
        try
        {
            tellAll("", "%" + sPlayer + " has been removed from the game.");

			hScores.remove(sPlayer);
            hHighs.remove(sPlayer);
            hWords.remove(sPlayer);
        }
        catch (Exception e)
        {
            logger.println("removePlayer:" + e);
        }
        
    }
	public String tellOne(String sRecipient, String sMessage)
	{
        return comm.tellOne("System", sRecipient, sMessage);
	}
	public void tellAllWithSquelch(String sSender, String sMessage)
	{
		String sNewMessage = "" + sMessage;
		if (bShowSender) {
			sNewMessage = sSender + "> " + sMessage;
		}
		comm.tellAllWithSquelch(sSender, sNewMessage, hSquelch);
	}

    private void tellAll(String nameFrom, String sMessage) {
    	comm.tellAll(nameFrom, sMessage);
    }
    public void processInput(String nick, String sInput) throws SimulationException
    {
            sInput = sInput.trim();
            if (sInput.length() < 1) { return; }
            if (sInput.substring(0,1).equals ("/"))
            {
                if (sInput.equals( "/exit"))
                {
                    logger.println("/exit typed");
                    removePlayer(nick);
                    throw new SimulationException("exit");
                }
                else if (bGameOn ==  false  && sInput.equals("/go"))
                {
                    logger.println("/go typed by: " + nick);
                    startGame();
                }
                
                else if (sInput.length() > 6  && ( sInput.substring(0,5).equals("/name")))
                {
                    String sNewNick = "" + sInput.substring(6);
                    hScores.remove(nick);
                    hHighs.remove(nick);
                    hWords.remove(nick);

                    comm.renameParticipant(nick, sNewNick);

                    tellAll("", " " + nick + " is now " + sNewNick + ".");
                    logger.println(nick + " is now " + sNewNick);
                    nick = sNewNick; //dunno how to do this
                    hScores.put(sNewNick, 0);
                    hHighs.put(sNewNick, "");
                    hWords.put(sNewNick, new String());
                }
                else if (sInput.length() > 4  && ( sInput.substring(0,4).equals("/add")))
                {
                    String sString = "" + sInput.substring(5);
                    if (sString.indexOf(' ') < 0)
                    {
                         tellOne(nick, "Not added. Must say /add " + sString + " and the name of the dictionary you checked");
                    }
                    else
                    {
                        try
                        {
                            BufferedWriter out = new BufferedWriter(new FileWriter("addwords.txt",true));
                            out.write("" + sString + "\n");
                            out.close();
                            tellAll("", "%" + sString + " added to dictionary.");
                        }
                        catch (Exception efio)
                        {
                            logger.println("trying to add word" + sString + ":" + efio);
                        }
                    }
                }
                
                else if (bGameOn ==  false  && sInput.length() > 5  && ( sInput.substring(0,6).equals("/words")))
                {
                    int iWordsLength = 10;
                    String sWordList = "%Large words:";
                    try
                    {
                        String sWordLength = "";
                        if (sInput.length() > 6)
                        {
                                sWordLength = sInput.substring(6).trim();
                                iWordsLength = Integer.parseInt(sWordLength);
                        }
                        if (iWordsLength < 7)
                        {
                            iWordsLength = 10;
                        }
                        sWordList += WordChecker.largeWords(iWordsLength);

                        tellOne(nick, sWordList.trim());
                    }
                    catch (Exception wdsexc)
                    {
                        logger.println("processing /words command:" + wdsexc);
                    }
                    
                }
                
                else if (sInput.length() > 5  && ( sInput.substring(0,5).equals("/word")))
                {
                    String sString = "" + sInput.substring(6);
                    boolean bIsWord = false;
                    try
                    {
                        bIsWord = WordChecker.isWord(sString);
                        if (bIsWord)
                        {
                            tellOne(nick, "%Word: " + sString + " is ok.");
                        }
                        else
                        {
                            tellOne(nick, "%Word: " + sString + " is not a word.");
                        }
                    }
                    catch (Exception efio)
                    {
                        logger.println("trying to check word" + sString + ":" + efio);
                    }
                }
                
                else if (sInput.indexOf("/squelch") >= 0)
                {
                    //System.out.println("processing squelch:");
                    sInput = sInput.trim();
                    //     /squelch StarMan
                    if (sInput.length() > 9)
                    {
                        String sSendee = sInput.substring(9);
                        String sKey = nick;
                        hSquelch.put(sKey, sSendee);
                        String sMsg = "%" + nick + " is now squelching " + sSendee + ".";
                        tellOne(nick, sMsg);
                        logger.println(sMsg);
                    }
                    else 
                    {
                        tellOne(nick, hSquelch.toString());
                    }
                }
                else if (sInput.indexOf("/flash") >= 0)
                {
                    //System.out.println("processing flash:");
                    sInput = sInput.trim();
                    //     /flash StarMan It was nice talking to you star!
                    int iMessage = sInput.indexOf(' ', 7);
                    //System.out.println("index=" + iMessage);
                    if (iMessage > 7)
                    {
                        String sSendee = sInput.substring(7,iMessage);
                        String sFlash = "%(Flash from:" + nick + "):" + sInput.substring(iMessage);
                        String sReturn = tellOne(sSendee, sFlash);
                        //System.out.println("returned:" + sReturn);
                        tellOne(nick, sReturn);
                    }
                }

                //Sends a system message to everyone.
                else if (sInput.length() > 5  && (sInput.substring(0,5).equals("/sysm") || sInput.substring(0,5).equals("/smsg")))
                {
                    String sString = "" + sInput.substring(6);
                    tellAll("", "%" + sString);
                }
                else if (sInput.equals("/hi"))
                {
                    tellOne(nick, "  High Scores:\n" + hs.toString());
                }
                else
                {
                    tellOne(nick, "valid commands are /exit /name newname /go /hi /squelch name /flash name message");
                }

            }
            else if (sInput.equals("w"))
            {
                tellOne(nick, "  Words used:\n" + showWords());
            }
            else if (bGameOn ==  false  && sInput.equals("rd"))
            {
                // add player's name to list of players who rd
                // if everyone rd'd, start the game
                tellAllWithSquelch(nick, sInput);
                hRd.put(nick, "rd");
                if (hRd.size() == hScores.size())
                {
                    //logger.println("final rd typed by: " + player.getNick());
                    startGame();
                }
            }
            
//            else if (sInput.equals("$"))
//            {
//                tellOne(player.getNick(), " SCORE:\n" + getScores());
//            }

            else if (bGameOn)
            {
                if (sInput.equals("."))
                {
                    String sScore = (hScores.get(nick)).toString();
                    tellOne(nick, " Score so far: " + sScore + "\n" + getHumanLetters());
                    return;
                }
                String sInLetters = WordChecker.inLetters(getLetters(), sInput);
                if (sInLetters.length() > 1)
                {
                    tellOne(nick, "nope, not in letters!");
                }
                else  // check dict. too
                {
                    sInput = sInput.toLowerCase();
                    sInLetters = WordChecker.inDictionary(sInput);
                    if (sInLetters.length() < 1 && !hWordOwners.containsKey(sInput))
                    {
                        int iScore = WordChecker.score(sInput);
                        //logger.println("ya got" + iScore);
                        tellOne(nick, "yup, right: " + iScore + " points!");
                        int iAccumulated = ((Integer)(hScores.get(nick))).intValue();
                        //logger.println("you were:" + iAccumulated);
                        iAccumulated += iScore;
                        hScores.put(nick, iAccumulated);
                        hWordOwners.put(sInput, nick);

                        if (hHighs.get(nick) == null || ((String)hHighs.get(nick)).length() < sInput.length())
                        {
                            //logger.println("your length!:" + sInput.length());
                            hHighs.put(nick, sInput);
							if (sInput.length() > iLongestWord)
							{
								iLongestWord = sInput.length();
								sLongestWord = sInput.toUpperCase() + " for " + iScore + " points!";
								sLongestPlayer = nick;
							}
                            
                        }
                        String sWordsSoFar = "" + hWords.get(nick) + sInput + "=" + iScore + ", ";
                        hWords.put(nick, sWordsSoFar);
                        
                        //logger.println("now you are:" + iAccumulated);
                        //logger.println("words so far=" + sWordsSoFar);
                    }
                    else if (hWordOwners.containsKey(sInput)) {
                        String sOwner = hWordOwners.get(sInput);
                        tellOne(nick, "nope, already guessed by " + sOwner + "!");
                    }
                    else { // word checker rejected this word
                        tellOne(nick, "nope, not in dictionary!");
                        return;
                    }
                }
            }
            else
            {
                tellAllWithSquelch(nick, sInput);
            }
            //return ("");
    }
    public String getLetters()
    {
        return sLetters;
    }
    public String getHumanLetters()
    {
	  int iPosition = ScrambleHelper.myRandom(16);
        String s2 = " The letters are:\n    ";
        for (int i = 0; i < 16; i++)
        {
		 if (iPosition > 15) 
		 {
			iPosition = 0;
		 }
             s2 += sLetters.charAt(iPosition++); 

             if (i == 3 || i == 7 || i == 11)
             {
                s2 += "\n\n    ";
             }
             else if (i != 15) {
                 s2+= "  ";
             }
        }
	  s2 += "\n";
        sHumanLetters = s2;
        return sHumanLetters;
    }
    public String setLetters()
    {
        //sLetters = ScrambleHelper.makeLetters();
        return sLetters;
    }
    public String showWords()
	{
		int max = hWords.size() - 1;
		StringBuffer buf = new StringBuffer();
		Enumeration<String> k = hWords.keys();
		Enumeration<String> e = hWords.elements();
		buf.append("");

		for (int i = 0; i <= max; i++) 
		{
		    String s1 = k.nextElement().toString();
		    String s2 = e.nextElement().toString();
			String s3 = " " + (s1) + ": " + s2.toUpperCase();
		    if (s2.length() > 5 )
		    {
		        buf.append(s3); 
    		    if (i < max) 
    			{
    			    buf.append("\n");
    		    }
    		}
		}
		buf.append("");
		return buf.toString();
    }
    public void almostOver()
    {
        tellAll("", "%5 seconds");
    }
    public void endGame()
    {
        bGameOn = false;
        sOldScores = " SCORE:\n\n" + getScores();
        tellAll("", "\n  !! Time's up !!\n\n" + sOldScores);
    }
	public static String formatScore(String score)
	{
		int iSc = 0;
		try {iSc = Integer.parseInt(score);} catch (Exception pexc) {;}
		
		if (iSc < 10)
			return "    " + iSc;
		else if (iSc < 100)
			return "   " + iSc;
		else if (iSc < 1000)
			return "  " + iSc;
		else if (iSc < 10000)
			return " " + iSc;
		else 
			return "" + iSc;
	}
	
	 //* right pads word until it's 16 characters
	public static String formatName(String name)
	{
		int iLen = name.length();
		StringBuffer buf = new StringBuffer(name);
		
		for (int i = iLen; i < 13; i++)
		{
			buf.append(" ");
		}
		return buf.toString();
	}
	
    public static void bubbleSort(Vector<Integer> toBeSorted, Vector<String> otherVector)
    {
      int n = toBeSorted.size();
      for (int i = 0; i < n;  i++)
      {
         for (int j = i+1; j < n; j++)
         {
            Integer oi = ((Integer)toBeSorted.elementAt(i));
            Integer oj = ((Integer)toBeSorted.elementAt(j));
           if ( oj.intValue() >= oi.intValue())  
           {
                  //swap(toBeSorted.elementAt(i),toBeSorted.elementAt(j));
                  Integer temp = oi;
                  toBeSorted.setElementAt(oj,i);
                  toBeSorted.setElementAt(temp, j);
                  String pi = otherVector.elementAt(i);
                  String pj = (otherVector.elementAt(j));
                  String temp2 = pi;
                  otherVector.setElementAt(pj,i);
                  otherVector.setElementAt(temp2,j);
           }
         }
      }
    }
    private Vector<String> hashSort(Hashtable<String, Integer> h)
    {
        Vector<String> vKeys = new Vector<>();
        Vector<Integer> vElements = new Vector<>();

        for (Enumeration<String> e = h.keys() ; e.hasMoreElements() ;) 
        {
            vKeys.addElement(e.nextElement());
        }
        for (Enumeration<Integer> e = h.elements() ; e.hasMoreElements() ;) 
        {
            vElements.addElement(e.nextElement());
        }

        bubbleSort(vElements, vKeys);
        
        return vKeys;
        
    }
    public String getScores()  // synchronized?
	{
		StringBuffer xbuf = new StringBuffer();
		xbuf.append("");
		boolean bRemoved;

		//add all the bonuses for length
		//lengthBonus(s1) +
        for (Enumeration<String> ess = hScores.keys() ; ess.hasMoreElements() ;) 
        {
            String scoreKey = (String)ess.nextElement();
            int iScore = ((Integer)(hScores.get(scoreKey))).intValue();
            iScore += lengthBonus(scoreKey);
            hScores.put(scoreKey, iScore);
        }

		
		Vector<String> vOrder = hashSort(hScores);
		int max = vOrder.size() - 1;
		for (int i = 0; i <= max; i++) 
		{
            StringBuffer buf = new StringBuffer("");
            int isc = 0;
		    String s1 = (String)(vOrder.elementAt(i));
		    String s2 = "" + (hScores.get(s1));
		    bRemoved = false;
		    if (s2.equals("0"))
			{
			    //Ghosting
			    iGhostCount = 1;
			    Integer ig = (Integer)hGhost.get(s1);
			    if (ig == null)
			    {
    			    //tellAll ("no ghosting yet for " + i);
			    }
			    else
			    {
			        iGhostCount = ig.intValue();
			        iGhostCount++;
			    }
		        hGhost.put(s1, iGhostCount);

		        logger.println("iGhostCount for:" + s1 + "=" + iGhostCount);

		        bRemoved = false;
			    try
			    {
    			    if (iGhostCount >= 2)
    			    {
    			        logger.println("WordGameServerInterface removing ghost:" + s1 + ". Calling removePlayer");
    			        removePlayer(s1);
//    			        TScramThread th = (TScramThread)(Globals.getPlayerScheduler());
//    			        th.kill(s1);
    			        bRemoved = true;
    			    }
    			}
    			catch (Exception eth)
    			{
    			    logger.println("ghost removing:" + eth);
    			}
			}
			else
			{
			    iGhostCount = 0;
			}
		    if (bRemoved == false)   
			{

				// at the end of the game, each person's longest word gets a bonus
				int iTotalScore = Integer.parseInt(s2);
                isc = iTotalScore;
				String sTotalScore = "" + iTotalScore;
				
			    buf.append("   " + formatName(s1) + ":" + formatScore(sTotalScore));
				if (sLongestPlayer.equals(s1))
				{
					buf.append (" (*" + sLongestWord + ")");
				}
				else 
				{
				    String sLongest = (String)hHighs.get(s1);
				    if (sLongest != null && sLongest.length() > 5 )
				    {
				        buf.append (" (" + sLongest.toUpperCase() + " for " + WordChecker.score(sLongest) + " points!)");
				    }
				
				}
				
			    if (i < max) 
				{
					buf.append("\n");
				}
			}
            xbuf.append(buf);
            hs.add(isc, buf.toString());
		}
		xbuf.append("");
		return xbuf.toString();
    }
    /*
     At the end of the game, each person gets a bonus for his longest word entered
     If you entered a 9 letter word, you will get a bonus of 500
     06 letters = 0100
     07 letters = 0200
     08 letters = 0300
     ...
     14 letters = 2500
     15 letters = 3000
     16 letters = 4000
     */
    private int lengthBonus(String s)
    {
        int iLen = 0;
   		try 
   		{
   		    
//    		tellAll("sbonus for" + s + "=" + (hHighs.get(s)));
   		    iLen = 0 + ((String)hHighs.get(s)).length()  ;
   		    
   		} 
   		catch (Exception pexc) 
   		{
   		    logger.println("exc 3");

   		    return 0;
   		}
        int iScore = 0;
        switch (iLen)
        {
            case 1: 
            case 2:
            case 3:
            case 4:
            case 5:
                iScore = 0;
                break;
            case 6:
                iScore = 100;
                break;
            case 7:
                iScore = 200;
                break;
            case 8:
                iScore = 300;
                break;
            case 9:
                iScore = 500;
                break;
            case 10:
                iScore = 800;
                break;
            case 11:
                iScore = 1200;
                break;
            case 12:
                iScore = 1600;
                break;
            case 13:
                iScore = 2000;
                break;
            case 14:
                iScore = 2500;
                break;
            case 15:
                iScore = 3000;
                break;
            case 16:
                iScore = 4000;
                break;
            default:
               iScore = 0;
               break;
        }
        return iScore;
    }

    
}
class ThreadedTimer extends Thread
{
    private ScrambleController simMain;
	private ScrambleLogger logger;
	private boolean bCanStart = true;
	
    public ThreadedTimer(ScrambleController s , ScrambleLogger logger)
    {
        simMain = s;
		this.logger = logger;
    }
    public void run()
    {
        try
        {
            sleep(90000);
            simMain.almostOver();
            sleep(5000);
            simMain.endGame();
            // game is over, now anyone who types /go will call canStart(), and so will wait
            bCanStart = false;
            //sleep(16000); # use this in real game
            //sleep(4000);
            bCanStart = true;
        }
        catch (InterruptedException e)
        {
            logger.println("run.unhandled exc:" + e);
        }

    }
    // a game cannot be started until a certain amount of time passes
    public boolean canStart()
    {
        return bCanStart;
    }
}
