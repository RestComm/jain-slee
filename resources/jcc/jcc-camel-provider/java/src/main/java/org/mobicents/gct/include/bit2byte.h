/*
		Copyright (C) 1993-1994 DataKinetics Ltd.

 Name:          bit2byte.h

 Description:   Include file for bit2byte.c.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    07-Sep-93   SRG   - Initial code.
   B    23-Sep-93   SRG   - *** ISUP Alpha Release ****
   1    09-Feb-94   SRG   - *** ISUP Version 1.00 Release ****
   2    24-Mar-94   SRG   - bit_set, bit_clear and bit_test added.


 */

#ifdef LINT_ARGS
  u8 bits_from_byte(u8 val, u8 bitoff, u8 nbit);
  u8 bit_from_byte(u8 val, u8 bitoff);
  void bits_to_byte(u8 *pdest, u8 val, u8 bitoff, u8 nbit);
  void bit_to_byte(u8 *pdest, u8 val, u8 bitoff);
  void bit_set(u8 *base, u16 index);
  void bit_clear(u8 *base, u16 index);
  int bit_test(u8 *base, u16 index);
#else
  u8   bits_from_byte();
  u8   bit_from_byte();
  void bits_to_byte();
  void bit_to_byte();
  void bit_set();
  void bit_clear();
  int bit_test();
#endif
