/*
		Copyright (C) 1993-1994 DataKinetics Ltd.

 Name:          sdlsig.h

 Description:   Include file for sdl_sig.c.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    23-Aug-93   SRG   - Initial code.
   B    23-Sep-93   SRG   - *** ISUP Alpha Release ****
   1    09-Feb-94   SRG   - *** ISUP Version 1.00 Release ****
   2    25-Mar-94   SRG   - module_id field added to SDL_SIG and as
			    a parameter to SDL_SIG_initialise().

 */

/*
 * Definition of structure used for signalling in
 * accordance with SDL diagrams for signals internal
 * to protocol task.
 */
typedef struct sdl_sig
{
  u16   type;           /* signal type (unique within module) */
  u16   id;             /* id (eg link id, circuit id etc) */
  union
  {
    u32         data;   /* direct data associated with signal */
    void        *ptr;   /* pointer to data associated with signal */
  }     inf;
} SDL_SIG;

/*
 * Definitions for status field in SDL_CTRL:
 */
#define SDLCTR_CONG       (0x01)  /* hi_water exceeded */

typedef struct sdl_ctrl
{
  SDL_SIG       *sigptr;        /* pointer to signal array */
  u16           last_in;        /* index of last write signal */
  u16           last_out;       /* index of last read signal */
  u16           num_sig;        /* total number of signals in array */
				/* following fields maintained for debug only */
  u16           count;          /* current number of signals in queue */
  u16           max;            /* highest number of signals in queue */
  u16           hi_water;       /* no of signals for congestion onset */
  u8            status;         /* SDLCTR_xxx values apply */
  u8            mgmt_id;        /* Management module id for event reports */
  u8            module_id;      /* Our module id for event reports */
} SDL_CTRL;

#ifdef LINT_ARGS
  int SDL_SIG_initialise(SDL_CTRL *sdl_ctrl, SDL_SIG *array, u16 num_sig,
			 u16 hi_water, u8 mgmt_id, u8 module_id);
  SDL_SIG *SDL_SIG_get_free(SDL_CTRL *sdl_ctrl);
  SDL_SIG *SDL_SIG_read_next(SDL_CTRL *sdl_ctrl);
#else
  int SDL_SIG_initialise();
  SDL_SIG *SDL_SIG_get_free();
  SDL_SIG *SDL_SIG_read_next();
#endif


