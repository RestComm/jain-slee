/*
		Copyright (C) 1993 DataKinetics Ltd.

 Name:		hqueue.h

 Description:	Include file for hqueue.c


 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------

   A	03-Mar-93   - Initial Code.
   B    14-May-93   - max field added.
   1	26-Nov-93   - Raised to issue 1.

*/

typedef struct hqueue
{
  HDR	*head;	 /* ptr to first HDR in queue (zero if queue empty) */
  HDR	*tail;   /* ptr to last HDR in queue (valid only if head != 0) */
  u16	len;	 /* length of the queue (ie. the number of messages in it) */
  u16   max;	 /* the highest number of entries that the queue has contained */
} HQUEUE;

#ifdef LINT_ARGS
  int  HQ_init(HQUEUE *hq);
  int  HQ_add(HQUEUE *hq, HDR *h);
  HDR  *HQ_remove(HQUEUE *hq);
#else
  int  HQ_init();
  int  HQ_add();
  HDR  *HQ_remove();
#endif
