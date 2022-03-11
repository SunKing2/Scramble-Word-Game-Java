package com.alwayswantedtoplay.scramble;

import java.util.HashMap;

public interface Communicator {

	int addParticipant(String sPlayer, Object other);

	int removeParticipant(String sPlayer);

	void renameParticipant(String oldName, String newName);

	String tellOne(String sender, String sRecipient, String sMessage);

	void tellAllWithSquelch(String sSender, String sMessage, HashMap<String, String> hSquelch);

	void tellAll(String nameFrom, String sMessage);

}