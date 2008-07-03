/*
		Copyright (C) 1994 DataKinetics Ltd.

 Name:          fmtmsg.h

 Description:   Include file for use with fmtmsg.c

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    31-May-94   MH    - Initial code.
   1    05-Sep-94   SRG   - Raised to issue 1.

 */


/*
 * Useful constants when dimensioning
 * buffers and handling machine independent
 * MSG data:
 */
#define BIN_MSG_FIX_LEN (20)    /* fixed length (= param offset) */
#define BIN_MSG_MAX_LEN (340)   /* total (maximum) length */

#ifdef LINT_ARGS
  int MSG_to_bytes(unsigned char *dst_buf, MSG *src_msg);
  int bytes_to_MSG(MSG *dst_msg, unsigned char *src_buf, int src_len);
#else
  int MSG_to_bytes();
  int bytes_to_MSG();
#endif
