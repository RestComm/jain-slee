/*
		Copyright (C) 1993 DataKinetics Ltd.

 Name:		ident.h

 Description:	Include file for use with ident.c

  -----	---------  ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------  ---------------------------------------------

   A	18-May-93  - Initial code.
   1    27-Jun-93  - Initial Release.


 */


#ifdef LINT_ARGS
  int ID_req_ident(u8 requesting_id, u8 target_id, u8 id);
  int ID_build_ident(MSG *m, u16 mod_type, u8 maj_rev, u8 min_rev, u8 *text);
#else
  int ID_req_ident();
  int ID_build_ident();
#endif

/*
 * Definitions of type length, offset and size of
 * GEN_MSG_MOD_IDENT message parameter field:
 */
#define GEN_MSG_MOD_IDENT	(0x6111)

#define GENML_MOD_IDENT		(28)
#define GENMO_MOD_IDENT_type  	(0)
#define GENMS_MOD_IDENT_type		(2)
#define GENMO_MOD_IDENT_maj_rev (2)
#define GENMS_MOD_IDENT_maj_rev		(1)
#define GENMO_MOD_IDENT_min_rev (3)
#define GENMS_MOD_IDENT_min_rev		(1)
#define GENMO_MOD_IDENT_text  	(4)
#define GENMS_MOD_IDENT_text		(24)

