Quickstart for tftp-server example

Server machine:
- Install Mobicents JSLEE server
- Install TFTP server RA
- Install this example
- Start Jboss/Mobicents

Client machine:
-Install a tftp client
    A tftp client is usually available in standard packaging from the different
    Linux (OS/X) flavours. Likewise in Cygwin for Windows.

By default TFTP listens on port 8069.
    You probably need root access on Linux to listen on the standard port 69.
    To change, edit the config-property in resource-adaptor-jar.xml of the RA
        before starting.

You can now use the tftp client to send and receive files.
Using -for instance- the standard BSD tftp client you can now get a file with:

    $ tftp <Mobicents-hostname> <port> -c get <filename>
    
Using this example Sbb, if you use the special filename "HelloWorld", you'll
get that file with some content after a 2 second delay.
    $ tftp <Mobicents-hostname> 8069 -c get HelloWorld

If you write that file, the example will not write it to disk but merely check
whether the content is identical to that what it send. If not, an error will be
generated.