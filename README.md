# Scramble-Word-Game-Java
This is a multiplay word game like Boggle.

Each player connects using telnet 

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
