/*
		Copyright (C) 1991 DataKinetics Ltd.

 Name:		numtoa.h

 Description:	Prototypes for the functions in the file numtoa.c


  -----  ---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------

   1	30-Aug-91

*/

#ifdef LINT_ARGS
  char *numtoa(char *dest_ptr, unsigned long value, int size);
  unsigned long atonum(char *src_ptr);
  unsigned long atonumn(char *src_ptr, int size);
#else
  char *numtoa();
  unsigned long atonum();
  unsigned long atonumn();
#endif
