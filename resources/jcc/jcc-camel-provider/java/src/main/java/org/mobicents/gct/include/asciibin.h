/*
		Copyright (C) 1991 DataKinetics Ltd.

 Name:		asciibin.h

 Description:	Prototypes for the functions in asciibin.c


  -----  ---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------

   2	04-Jul-91

*/

#ifdef LINT_ARGS
  unsigned char asctobin(char *hex);
  void bintoasc(char *hex, unsigned char bin);
  unsigned char hextobin(char c);
  void bintohex(char *ptr, unsigned char bin);
#else
  unsigned char asctobin();
  void bintoasc();
  unsigned char hextobin();
  void bintohex();
#endif
