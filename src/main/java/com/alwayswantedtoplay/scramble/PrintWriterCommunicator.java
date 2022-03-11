package com.alwayswantedtoplay.scramble;
import java.io.PrintWriter;
import java.util.HashMap;

public class PrintWriterCommunicator implements Communicator {
    private HashMap<String, PrintWriter> hOutputs;
	
	public PrintWriterCommunicator() {
        hOutputs = new HashMap<>();
	}
	@Override
	public int addParticipant(String sPlayer, Object out) {
		
        hOutputs.put(sPlayer, (PrintWriter)out);
		
		return hOutputs.size();
	}
	@Override
	public int removeParticipant(String sPlayer) {
		if (hOutputs.containsKey(sPlayer)) {
			hOutputs.remove(sPlayer);
		}
        return hOutputs.size();
	}
	@Override
	public void renameParticipant(String oldName, String newName) {
        // send output to new nick
        hOutputs.put(newName, hOutputs.get(oldName));
        hOutputs.remove(oldName);		
	}
	@Override
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
	@Override
	public void tellAllWithSquelch(String sSender, String sMessage, HashMap<String, String> hSquelch)
	{
        try
        {
            for (String sTo: hOutputs.keySet()) 
            {
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

    @Override
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
