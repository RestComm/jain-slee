/*
		Copyright (C) 1994-1999 DataKinetics Ltd.

 Name:          siu_inc.h

 Description:   Definitions for SIU users.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    21-Sep-94   MH	  - Initial code.
   1    09-Jan-95   MH    - Modify definitions for MAX_TIME_SLOT, MAX_PC,
                            MAX_SLC, MAX_STREAMS.
   2    10-Feb-95   MH    - Prefix MAX_??? with SIU_.
   2A   22-Feb-95   MH    - Added MGT_CMD_NUBLI + MGT_CMD_NUBLE
   2a   07-Mar-95   MH    - Remove SIU_MAX_* and SIU_VALID_BPOS() to mgt_def.h
   3    10-Mar-95   MH    - Up issue.
   3+   28-Apr-97   MH    - Add support for circuit group activation
                            (MGT_CMD_ACGRP) and deactivation (MGT_CMD_DEACGRP)
                            to API_MSG_COMMAND (siucmd).
                          - New message: API_MSG_USER_EVENT.
   4    30-Apr-98   SFP   - Added API_MSG_SIU_STATUS, board states moved from
                            mgt_def.h.
   5    12-Nov-98   SFP   - CNCGA, CNCGD, CNHMI and SIMBS_RESET added.
                          - MGT_CMD_MNRSI renamed MGT_CMD_RESTART.
   6    12-jan-99   SFP   - Definitions for congestion handling added.
                            (MGTSF_CONG, MGT_CMD_STCNP).
                            STSYP (value 0x000c) corrected to ALSYP.
                            New definition for MSSYP added (value 0x0010).
   7    16-Jul-99   JL    - MGT_CMD_MNRSI added.

 */

#define API_MSG_COMMAND		(0x7f0f)
#define API_MSG_USER_EVENT	(0x0f0e)
#define API_MSG_SIU_STATUS	(0x0f0d)

/*
 * Definitions of length, offset and size of
 * API_MSG_COMMAND message parameter field:
 */
#define	MGTML_COMMAND		(8)
#define MGTMO_COMMAND_cmd_type	(0)
#define MGTMS_COMMAND_cmd_type		(2)
#define MGTMO_COMMAND_id	(2)
#define MGTMS_COMMAND_id		(2)
#define MGTMO_COMMAND_result	(4)
#define MGTMS_COMMAND_result		(4)

/*
 * Command values for API_MSG_COMMAND.
 */
#define	MGT_CMD_RSBOI		(0x0001)   /* Restart a board */
#define MGT_CMD_MNINE		(0x0002)   /* Activate a C7 link */
#define MGT_CMD_MNINI		(0x0003)   /* Deactivate a C7 link */
#define MGT_CMD_STSLP		(0x0004)   /* Read Level 2 state */
#define MGT_CMD_STPCP		(0x0005)   /* Read PCM status */
#define MGT_CMD_NUBLI		(0x0006)   /* Block NUP circuit group */
#define MGT_CMD_NUBLE		(0x0007)   /* Unblock NUP circuit group */
#define MGT_CMD_CNCGA		(0x0008)   /* Activate a group */
#define MGT_CMD_CNCGD  		(0x0009)   /* Deactivate a group */
#define MGT_CMD_STC7P           (0x000a)   /* Read L3 status */
#define MGT_CMD_STBOP           (0x000b)   /* Read Board status */
#define MGT_CMD_ALSYP           (0x000c)   /* Read system alarm status */
#define MGT_CMD_STRLP		(0x000d)   /* Read inter-SIU link state */
#define MGT_CMD_STHLP		(0x000e)   /* Read host-SIU link state */
#define MGT_CMD_CNHMI		(0x000f)   /* Nominate host for management */
#define MGT_CMD_MSSYP		(0x0010)   /* Read congestion status */
#define MGT_CMD_MNRSI		(0x0011)   /* Reboot the SIU */
#define MGT_CMD_RESTART		(0x00ff)   /* Restart */

#define MGT_CMD_ACGRP           (MGT_CMD_CNCGA)
#define MGT_CMD_DEACGRP         (MGT_CMD_CNCGD)

/*
 * PCM status flags :
 */
#define	PCM_SF_PCM_LOSS		(1 << 0)
#define PCM_SF_AIS		(1 << 1)
#define PCM_SF_SYNC_LOSS	(1 << 2)
#define PCM_SF_REM_ALARM	(1 << 3)

/*
 * Board states :
 */
#define SIMBS_INACTIVE			(0)
#define SIMBS_RESETTING			(1)
#define SIMBS_ACTIVE			(2)
#define SIMBS_FAILED			(3)
#define SIMBS_RESET 			(4)

/*
 * L3 states
 */
#define MGTL3S_UNAVAILABLE      (0)
#define MGTL3S_AVAILABLE        (1)

/*
 * System alarm bit masks
 */
#define MGTSF_MINUS5            (0x0001)        /* -5v PSU failure */
#define MGTSF_MINUS12           (0x0002)        /* -12v PSU failure */
#define MGTSF_PLUS12            (0x0004)        /* +12v PSU failure */
#define MGTSF_PLUS5             (0x0008)        /* +5v PSU failure */
#define MGTSF_TEMP              (0x0010)        /* Temperature alarm */
#define MGTSF_FAN3              (0x0020)        /* Fan 3 failure */
#define MGTSF_FAN2              (0x0040)        /* Fan 2 failure */
#define MGTSF_FAN1              (0x0080)        /* Fan 1 failure */
#define MGTSF_PARSE             (0x0100)        /* Parse error in config file */
#define MGTSF_CONFIG            (0x0200)        /* Configuration failed */
#define MGTSF_CONG              (0x0400)        /* System Congestion */

/*
 * User Event length:
 */
#define MGTML_USER_EVENT	(0)

/*
 * Status values for API_MSG_SIU_STATUS
 */
#define SIUS_NEG5_FAIL		(16)
#define SIUS_NEG5_OK		(17)
#define SIUS_NEG12_FAIL		(18)
#define SIUS_NEG12_OK		(19)
#define SIUS_PLUS12_FAIL       	(20)
#define SIUS_PLUS12_OK		(21)
#define SIUS_PLUS5_FAIL		(22)
#define SIUS_PLUS5_OK		(23)
#define SIUS_OVER_TEMP		(24)
#define SIUS_OTEMP_CLRD		(25)
#define SIUS_FAN_FAIL		(26)
#define SIUS_FAN_OK		(27)
#define SIUS_BOARD_FAIL		(28)
#define SIUS_BOARD_OK		(29)
#define SIUS_HOST_FAIL		(30)
#define SIUS_HOST_OK		(31)
#define SIUS_SIUL_FAIL		(32)
#define SIUS_SIUL_OK		(33)
#define SIUS_CONGESTION	        (34)
#define SIUS_NO_CONGESTION      (35)
#define SIUS_MULTIFAN_FAIL      (36)
#define SIUS_MULTIFAN_OK        (37)
#define SIUS_PRO_OVER_TEMP      (38)
#define SIUS_PRO_TEMP_OK        (39)





