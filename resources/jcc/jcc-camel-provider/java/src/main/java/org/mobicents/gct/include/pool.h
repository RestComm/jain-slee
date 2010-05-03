/*
		Copyright (C) 1991 DataKinetics Ltd.

 Name:		pool.h

 Description:	Prototypes for the functions in pool.c


 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------

   A	08-Apr-91
   1    25-Aug-92
   2    04-Nov-94   - build_tx_pool moved from queue.h into this file.

*/

typedef struct pool
{
    T_FRAME *head;
    T_FRAME *tail;
    u16     length;
} POOL;

#ifdef LINT_ARGS
  T_FRAME *get_TF(POOL *pool, u8 layer);
  T_FRAME *build_tx_pool(unsigned int len, unsigned int class,
			 unsigned int layer, T_FRAME **tail);
#else
  T_FRAME *get_TF();
  T_FRAME *build_tx_pool();
#endif
