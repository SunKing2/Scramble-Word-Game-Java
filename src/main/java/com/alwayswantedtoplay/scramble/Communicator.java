package com.alwayswantedtoplay.scramble;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

public class Communicator {
    private Hashtable<String, PrintWriter> hOutputs;
	
	public Communicator() {
        hOutputs = new Hashtable<>();
	}
	public int addParticipant(PrintWriter out, String sPlayer) {
        hOutputs.put(sPlayer, out);
		
		return hOutputs.size();
	}
	public int removeParticipant(String sPlayer) {
		if (hOutputs.contains(sPlayer)) {
			hOutputs.remove(sPlayer);
		}
        return hOutputs.size();
	}
	public void renameParticipant(String oldName, String newName) {
        // send output to new nick
        hOutputs.put(newName, hOutputs.get(oldName));
        hOutputs.remove(oldName);		
	}
	public String tellOne(String sender, String sRecipient, String sMessage)
	{
	    String sReturn = "Private message to:[" + sRecipient + "] ";
	    //System.out.println("private message to:[" + sRecipient + "], message:[" + sMessage + "]");
        try
        {
            PrintWriter tOut = (PrintWriter)(hOutputs.get(sRecipient));
            if ( tOut != null) 
            {
                tOut.println(sMessage); 
                sReturn = sReturn + "sent.";
            }
            else 
            {
                sReturn = sReturn + "FAILED!!!";
            }
        }
        catch (Exception e)
        {
            sReturn = sReturn + "FAILED!!!";
        }
        return sReturn;
	}
	public void tellAllWithSquelch(String sSender, String sMessage, Hashtable<String, String> hSquelch)
	{
        try
        {
            for (Enumeration<String> e = hOutputs.keys() ; e.hasMoreElements() ;) 
            {
                String sTo = (String)e.nextElement();
                if (hSquelch.get(sTo) != null && ((String)hSquelch.get(sTo)).equals(sSender))
                {
                }
                else
                ((PrintWriter)hOutputs.get(sTo)).println(sMessage); 
            }
        }
        catch (Exception e)
        {
        	// TODO this should go to logger
            System.out.println("tellAllWithSquelch  FAILED!!!");
        }
	}

    public void tellAll(String nameFrom, String sMessage) {
        for (String key : hOutputs.keySet()) {
            PrintWriter out = hOutputs.get(key);
            if (!nameFrom.equals(key)) {
                String sPrefix = nameFrom + "> ";
                if (nameFrom.length() == 0) {sPrefix = "";}
                out.println(sPrefix + sMessage);
            }
        }
    }
	
}
