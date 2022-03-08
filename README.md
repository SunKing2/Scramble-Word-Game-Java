# Scramble-Word-Game-Java
This is a multiplayer word game like Boggle.

Each player connects using telnet 

Try the online live game right now at

http://alwayswantedtoplay.com

## To Run
git clone https://github.com/SunKing2/Scramble-Word-Game-Java.git

cd Scramble-Word-Game-Java

mvn test exec:java -Dexec.mainClass="ScrambleWordGame"

after doing that...

telnet localhost 8189 to play

## Game commands
/go  to start the round

/name myCoolNickName to rename yourself

/hi to see the all-time high scores
