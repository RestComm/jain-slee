/*
		Copyright (C) 1992 DataKinetics Ltd.

 Name:		pack.h

 Description:	Prototypes for the functions in the file pack.c


 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------

   A	22-Jun-92
   1	13-Jul-92

*/

#ifdef LINT_ARGS
  void packbits(unsigned char *dest, int dest_bit_offset,
		unsigned long value, int bitcount);
  unsigned long unpackbits(unsigned char *src, int src_bit_offset,
			   int bitcount);
  void rpackbytes(unsigned char *dest, int dest_byte_offset,
		unsigned long value, int bytecount);
  unsigned long runpackbytes(unsigned char *src, int src_byte_offset,
			   int bytecount);
#else
  void packbits();
  unsigned long unpackbits();
  void rpackbytes();
  unsigned long runpackbytes();
#endif

