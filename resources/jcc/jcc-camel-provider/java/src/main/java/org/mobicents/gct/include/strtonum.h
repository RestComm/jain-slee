/*
		Copyright (C) 1994-1996 DataKinetics Ltd.

 Name:		strtonum.h

 Description:	Prototypes for the functions in strtonum.c

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A	20-Nov-94   MH	  - Initial code
   1    22-Nov-94   MH    - Up issue
   2    25-Jan-96   MH    - Add strtou32 function and up-issue.

 */

#ifdef LINT_ARGS
  int strtonum(u32 *destval, char *srcstr);
  int strtou32(u32 *destval, char *str);
#else
  int strtonum();
  int strtou32();
#endif
