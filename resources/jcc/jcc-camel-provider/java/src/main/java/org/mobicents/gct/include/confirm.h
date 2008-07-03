/*
		Copyright (C) 1993 DataKinetics Ltd.

 Name:		confirm.h

 Description:	Include file for use with confirm.c

 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------

   A    06-Apr-93   - Initial code.
   1    03-May-93   - Released.


 */

#ifdef LINT_ARGS
  int confirm_msg(MSG *m);
#else
  int confirm_msg();
#endif
