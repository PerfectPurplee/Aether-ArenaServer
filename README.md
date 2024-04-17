# Server for Aether-Arena

This is a server for https://github.com/PerfectPurplee/Aether-Arena 
## Setup
You can run this chain of commands to run a server directly from you terminal:
```
git clone git@github.com:PerfectPurplee/Aether-ArenaServer.git && cd Aether-ArenaServer && mvn clean package && java -jar target/Server.jar

```
Or otherwise if you dont have git/maven/java in your system's path you can use IDE of your choice to compile and run the project.

On a startup server will print:  
  your local IP: usable for lan gameplay.  
  your public IP: no restrictions, but you have to have traffic to port 1337 (port on wich DatagramSocket listens) redirected to your current machine you have server on (read more about port forwarding).  

You need to provide IP for players you want to play with. There is a dialog box before joining the game requiring ip adress of a host.

Feel free to join the magical battles and may the best mage triumph in the Aether-Arena!

---
