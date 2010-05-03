/*
		Copyright (C) 1993 DataKinetics Ltd.

 Name:		timer.h

 Description:	This file contains all definitions needed by other
		modules to use the timer facilities offered by the
		timer module. (Note the timer module may either
		be the Motorola driver module or a stand-alone timer
		module).

 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------

   A	22-Feb-93   - Based on file (timer.h) supplied by Motorola
		      with the following changes:
		      22-Feb-93 prototypes added for functions in
				timer.c
		      22-Feb-93 New module and message mnemonics
				used to save header file conflicts,
				however the message types remain the
				same actual values.
		      22-Feb-92 struct t_child now typedef'd.
   1    26-Jul-94   - Raised to issue 1.
   2    24-Sep-04   - Allow overriding TIM_MOD_ID

 */

/*
 * defines used in the flags field below
 * this must be reset by the module following notification
 * of timer expiry
 */
#define	TIMER_RUNNING	((unsigned char)0x80)	/* written by module */

/*
 * a timer table is kept in each module requiring
 * services of the timer.
 * it is scanned by the driver module after a
 * (programmable) number of timer ticks (this
 * is the resolution of the timer for this module).
 * the time field is incremented by the timer
 * each time the table is scanned: this is
 * equivalent to (resolution * ticks/second) seconds.
 * timer ticks will be taken as 1/10th second (every
 * 1/10th of a second, the timer interrupt routine
 * should send the driver a TIMER_TICK message).
 *
 * note that the notion of time as seen by the module
 * is the scanning of the table by the driver:
 * e.g. if the driver is notified of a TIMER_TICK every
 * 1/10th second and module requested resolution of 5,
 * its table is scanned every 1/2 second. All timer and
 * timeout values within that module are in units of 1/2 second,
 * so a timer value of 1 second is represented by 2 timer units.
 */
typedef	struct t_child
{
  unsigned char		flags;
  unsigned char		t_type;		/* timer type */
  unsigned short	timeout;	/* when to timeout */
} T_CHILD;

typedef struct t_table
{
  unsigned short	module;		/* module requesting timekeeping */
  unsigned short	max;		/* number of children */
  unsigned short	time;		/* "time" of last scan */
  T_CHILD		t_child[1];	/* one entry for each t_child */
} TIMER_TABLE;

/*
 * the following structure is sent in m->param
 * when initially requesting timer services
 * (in the KEEP_TIME action request)
 */
typedef struct
{
  TIMER_TABLE		*timer_table;
  unsigned short	resolution;
} TIMER_SERVICES;

/*
 * Prototypes for the functions in timer.c
 */
#ifdef LINT_ARGS
  int service_timer(int module, int max, TIMER_TABLE *timer_table,
				int resolution, int rsp);
  int start_timer(TIMER_TABLE *tab, int id, int timer, int timeout);
  int stop_timer(TIMER_TABLE *tab, int id);
  int start_if_stopped(TIMER_TABLE *tab, int id, int timer, int timeout);
  int remove_timer(TIMER_TABLE *t, int rsp);
#else
  int service_timer();
  int start_timer();
  int stop_timer();
  int start_if_stopped();
  int remove_timer();
#endif

/*
 * Message definitions (note these are the same values
 * as used by Motorola):
 */
#define TIM_MSG_TICK		(0x0002)
#define TIM_MSG_KEEP_TIME	(0x7006)
#define TIM_MSG_REMOVE_TIMER	(0x7007)
#define TIM_MSG_TM_EXP		(0xc002)

/*
 * Timer module definition:
 * (Currently the same as DRIVER for compatibility, however
 * this will eventually be changed to a unique id. It will
 * of course be possible to re-direct messages for the new
 * id to the DRIVER module if required).
 */
#ifndef TIM_MOD_ID
#define TIM_MOD_ID	(0)
#endif
