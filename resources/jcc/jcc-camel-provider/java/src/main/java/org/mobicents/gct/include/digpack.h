/*
		Copyright (C) 1992-1993 DataKinetics Ltd.

 Name:		digpack.h

 Description:	Prototypes for functions in digpack.c.

 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------

   A	14-Dec-92   - Original code.
   1	24-Nov-93   - Raised to issue 1.


 */
#ifdef LINT_ARGS
  int move_digits(u8 *dest, u16 d_dig_off, u8 * src, u16 s_dig_off, u16 num_digits);
  int pack_digits(u8 *dest_base, u16 digit_offest, u8 *src_digits, u16 num_digits);
  int unpack_digits(u8 *dest, u8 *src_base, u16 digit_offset,  u16 num_digits);
#else
  int move_digits();
  int pack_digits();
  int unpack_digits();
#endif


