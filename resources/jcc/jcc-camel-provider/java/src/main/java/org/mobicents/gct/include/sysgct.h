/*
		Copyright (C) 1991-2000 DataKinetics Ltd.

 Name:		sysgct.h

 Description:	Prototypes for the functions in the file sysgct.c

 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------
   1    30-Aug-91
   2    02-Sep-94   - GCT_set_instance and GCT_get_instance added.
   3    20-Oct-99   - class field changed to hclass in prototypes.
   4    01-Feb-00   - GCT_link, GCT_unlink & GCT_cong_status added.

*/

#ifdef LINT_ARGS
  int  GCT_send(unsigned int task_id, HDR *h);
  HDR  *GCT_receive(unsigned int task_id);
  HDR  *GCT_grab(unsigned int task_id);
  char *GCT_getmem(unsigned int hclass, unsigned int *size);
  int  GCT_relmem(unsigned int hclass, char *addr);
  int  GCT_cput(char c);
  unsigned long GCT_get_time(void);
  int  GCT_set_instance(unsigned int instance, HDR *h);
  unsigned int GCT_get_instance(HDR *h);
  int  GCT_link(void);
  int  GCT_unlink(void);
  int  GCT_cong_status(unsigned int partition_id);
  int  GCT_partition_congestion(int class);
#else
  int  GCT_send();
  HDR  *GCT_receive();
  HDR  *GCT_grab();
  char *GCT_getmem();
  int  GCT_relmem();
  int  GCT_cput();
  unsigned long GCT_get_time();
  int  GCT_set_instance();
  unsigned int GCT_get_instance();
  int  GCT_link();
  int  GCT_unlink();
  int  GCT_cong_status();
#endif
