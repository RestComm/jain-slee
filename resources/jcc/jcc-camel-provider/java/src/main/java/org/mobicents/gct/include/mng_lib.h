/*
		Copyright (C) 1991-2001 DataKinetics Ltd.

 Name:		mng_lib.h

 Description:	Prototypes for the functions in trace.c


 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------
   A    08-Apr-91
   1	25-Aug-92
   2    25-Mar-94   - Copyright banner changed.
   3    31-Aug-95   - Size and offset definitions added for
		      trace event indications.
   4    20-Jun-01   - MNG_sel_trace_event() added.

*/

#ifdef LINT_ARGS
  int MNG_trace_event(HDR *h, u8 module_id, u8 mngt_id);
  int MNG_seltrace_event(HDR *h, u8 ev, u8 module_id, u8 mngt_id);
  int MNG_error_event(u8 err, u16 id, u8 module_id, u8 mngt_id);
#else
  int MNG_trace_event();
  int MNG_seltrace_event();
  int MNG_error_event();
#endif

/*
 * Definitions for use in the trace event message:
 * (Note that the maximum data size has been increased
 * from a value of 32 used in Motorola Code).
 */
#define EV_DATA_SIZE_EXT        (280)   /* Extended event data size */

#define EVINFO_src              (0)
#define EVINFS_src                      (1)
#define EVINFO_dst              (1)
#define EVINFS_dst                      (1)
#define EVINFO_id               (2)
#define EVINFS_id                       (2)
#define EVINFO_type             (4)
#define EVINFS_type                     (2)
#define EVINFO_status           (6)
#define EVINFS_status                   (2)
#define EVINFO_time             (8)
#define EVINFS_time                     (4)
#define EVINFO_par              (12)
#define EVINFS_par                      (4)
#define EVINFO_data_length      (16)
#define EVINFS_data_length              (2)
#define EVINFO_data             (18)
#define EVINFS_data                     (EV_DATA_SIZE_EXT)

#define EVINFL          (EVINFO_data + EVINFS_data)


