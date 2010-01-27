To properly run example set env variable:
- MOBICENTS_SLEE_EXAMPLE_CC2_RECORDINGS_HOME

It should point to where MMS will record data.
Example conf:
- filesRoute = call-controll2
- MMS directory: g:/tmp/RC1/tools/media-server  (media dir is set to g:/tmp/RC1/tools/media-server/media)
- MOBICENTS_SLEE_EXAMPLE_CC2_RECORDINGS_HOME=g:/tmp/RC1/tools/media-server/media
- recorded data is stored in: g:/tmp/RC1/tools/media-server/media/call-controll2

PS: The recorded data directory needs to be created manually.
