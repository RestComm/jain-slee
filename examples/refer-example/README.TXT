This is simple refer example.


To run:
1. do standard deploy of examples via ant deploy-all (deploys RA and example service) - default run container with "-b 127.0.0.1" parameter
2. in sipp dir run "sipp -sf uas.xml -p 5999 -i 127.0.0.1"
3. in sipp dir run "sipp 127.0.0.1:5060 -m 1 -sf uac.xml -i 127.0.0.1"


NOTE: Following assumptions were made:
* Container is bound to 127.0.0.1
* ra is bound to 127.0.0.1:5060

-- if container is bound to different ip or ra is bound to different ip/port this has to be taken into consideration, see diagram below.



Scenario is simplest REFER usage, where client AAA, asks UAS BBB to refer with current dialog to UA CCC.

Agent AAA                Agent BBB                Agent CCC
      |       F1 INVITE        |                        |
      |----------------------->|                        |
      |       F2 200 OK        |                        |
      |<-----------------------|                        |
      |       F3 ACK           |                        |
      |----------------------->|                        |
      |                        |                        |
      |   F4 REFER             |                        |
      |----------------------->|        F5 INVITE       |
      |                        |----------------------->|
      |        F5 202 Accepted |                        |
      |<-----------------------|       F6 180 Ringing   |
      |                        |<-----------------------|
      |        F7 NOTIFY       |                        |
      |<-----------------------|                        |
      |  F8 200 OK             |                        |
      |----------------------->|                        |
      |                        |      F9 200 OK         |
      |                        |<-----------------------|
      |                        |                        |
      |                        |      F10 ACK           |
      |                        |----------------------->|
      |         F11 NOTIFY     |                        |
      |<-----------------------|                        |
      |   F12 200 OK           |                        |
      |----------------------->|                        |
      |                        |                        |
      |   F13 BYE              |                        |
      |----------------------->|                        |
      |                        |                        |
      |   F14 200 OK           |                        |
      |<-----------------------|                        |
      
      
Agent AAA is being emulated by uac.xml
Agent BBB is beeing emulated by example service
Agent CCC is beeing emulated by uas.xml

Agent AAA IP:127.0.0.1, PORT: sipp generated (-i controls IP) , destination IP:PORT == 127.0.0.1:5060 - should point to Agent BBB 
Agent BBB IP:stack/container default (127.0.0.1), PORT: 5060(default)
Agent CCC IP:127.0.0.1, PORT:5999 (hardcoded, never change -p 5999 option value), (-i controls ip, should be the same as for Agent AAA)