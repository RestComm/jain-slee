/*
		Copyright (C) 1998-2003 Intel Corporation.

 Name:          liu_inc.h

 Description:   Generic definitions relating to E1/T1 Line Interface
		Unit (LIU) framer configuration and control.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    04-Feb-98   SRG   - Initial code.
   1    03-Mar-98   SRG   - Raised to issue 1.
   2    24-Aug-98   SRG   - RSTATS definitions removed (obsolete).
   3    13-Aug-99   CJP   - New read statistics and read state definitions added.
   4    13-Jan-03   DJB   - Added definitions for SS7HD card

 */

/*
 * Message type definitions:
 */
#define LIU_MSG_CONFIG          (0x7e34)
#define LIU_MSG_CONTROL         (0x7e35)
#define LIU_MSG_R_STATS         (0x5e36)
#define LIU_MSG_R_CONFIG        (0x5e37)
#define LIU_MSG_R_CONTROL       (0x5e38)
#define LIU_MSG_R_STATE         (0x5e39)

/*
 * Definitions of length, offset and size of
 * LIU_MSG_CONFIG message parameter field:
 */
#define LIUML_CFG                    (40)
#define LIUMO_CFG_liu_type           (0)
#define LIUMS_CFG_liu_type                   (1)
#define LIUMO_CFG_line_code          (1)
#define LIUMS_CFG_line_code                  (1)
#define LIUMO_CFG_frame_format       (2)
#define LIUMS_CFG_frame_format               (1)
#define LIUMO_CFG_crc_mode           (3)
#define LIUMS_CFG_crc_mode                   (1)
#define LIUMO_CFG_build_out          (4)
#define LIUMS_CFG_build_out                  (1)
#define LIUMO_CFG_faw                (5)
#define LIUMS_CFG_faw                        (1)
#define LIUMO_CFG_nfaw               (6)
#define LIUMS_CFG_nfaw                       (1)
#define LIUMO_CFG_lfa_detect         (7)
#define LIUMS_CFG_lfa_detect                 (1)
#define LIUMO_CFG_los_detect         (8)
#define LIUMS_CFG_los_detect                 (1)
#define LIUMO_CFG_ais_detect         (9)
#define LIUMS_CFG_ais_detect                 (1)
#define LIUMO_CFG_rai_detect         (10)
#define LIUMS_CFG_rai_detect                 (1)
#define LIUMO_CFG_ais_gen            (11)
#define LIUMS_CFG_ais_gen                    (1)
#define LIUMO_CFG_rai_gen            (12)
#define LIUMS_CFG_rai_gen                    (1)
#define LIUMO_CFG_idle               (13)
#define LIUMS_CFG_idle                       (1)
#define LIUMO_CFG_clear_mask         (14)
#define LIUMS_CFG_clear_mask                 (4)
#define LIUMO_CFG_high_Z             (18)
#define LIUMS_CFG_high_Z                     (1)
#define LIUMO_CFG_reserved           (19)
#define LIUMS_CFG_reserved                   (21)

/*
 * Definitions of length, offset and size of
 * LIU_MSG_CONTROL message parameter field:
 */
#define LIUML_CTRL                   (16)
#define LIUMO_CTRL_ais_gen           (0)
#define LIUMS_CTRL_ais_gen                   (1)
#define LIUMO_CTRL_rai_gen           (1)
#define LIUMS_CTRL_rai_gen                   (1)
#define LIUMO_CTRL_loop_mode         (2)
#define LIUMS_CTRL_loop_mode                 (1)
#define LIUMO_CTRL_loop_channel      (3)
#define LIUMS_CTRL_loop_channel              (1)
#define LIUMO_CTRL_prbs_gen          (4)
#define LIUMS_CTRL_prbs_gen                  (1)
#define LIUMO_CTRL_reserved          (5)
#define LIUMS_CTRL_reserved                  (11)

/*
 * Definitions of length, offset and size of
 * LIU_MSG_R_STATS message parameter field:
 *
 * These definitions are for version 1 of the message.
 */
#define LIUML_RSTATS                                     (32)
#define LIUML_RSTATS_VERSION2                            (42)
#define LIUMO_RSTATS_version                             (0)
#define LIUMS_RSTATS_version                                     (2)
#define LIUMO_RSTATS_reserved                            (2)
#define LIUMS_RSTATS_reserved                                    (2)
#define LIUMO_RSTATS_duration                            (4)
#define LIUMS_RSTATS_duration                                    (4)
#define LIUMO_RSTATS_bit_errors                          (8)
#define LIUMS_RSTATS_bit_errors                                  (4)
#define LIUMO_RSTATS_code_violations                     (12)
#define LIUMS_RSTATS_code_violations                             (4)
#define LIUMO_RSTATS_frame_slips                         (16)
#define LIUMS_RSTATS_frame_slips                                 (4)
#define LIUMO_RSTATS_oos_transitions                     (20)
#define LIUMS_RSTATS_oos_transitions                             (4)
#define LIUMO_RSTATS_errored_seconds                     (24)
#define LIUMS_RSTATS_errored_seconds                             (4)
#define LIUMO_RSTATS_severely_errored_seconds            (28)
#define LIUMS_RSTATS_severely_errored_seconds                    (4)
#define LIUMO_RSTATS_prbs_status                         (32)
#define LIUMS_RSTATS_prbs_status                                 (2)
#define LIUMO_RSTATS_prbs_error_count                    (34)
#define LIUMS_RSTATS_prbs_error_count                            (4)
#define LIUMO_RSTATS_prbs_bit_count                      (38)
#define LIUMS_RSTATS_prbs_bit_count                              (4)

/*
 * Definitions for the status field used in
 * LIU_MSG_R_STATS.
 */
#define LIUM_RSTATS_READ_RESET          (1)
#define LIUM_RSTATS_READ_ONLY           (0)

/*
 * Definitions for the prbs_status field in
 * LIU_MSG_R_STATS version 2
 */
#define LIUM_RSTATS_PRBS_VALID    (1) /* error/bit count valid */
#define LIUM_RSTATS_PRBS_OVERRUN  (2) /* counter overrun (shouldn't happen) */
#define LIUM_RSTATS_PRBS_NOSYNC   (3) /* not sync'd to pattern */

/*
 * Definitions of length, offset and size of
 * LIU_MSG_R_STATE message parameter field:
 */
#define LIUML_RSTATE                                     (1)
#define LIUMO_RSTATE_state                               (0)
#define LIUMS_RSTATE_state                                       (1)
/*
 * Definitions for the state field of LIU_MSG_R_STATE
 */
#define LIUST_OK           (0)
#define LIUST_PCM_LOSS     (1)
#define LIUST_AIS          (2)
#define LIUST_SYNC_LOSS    (3)
#define LIUST_RAI          (4)

/*
 * Definitions for liu_type parameter values
 */
#define LIUTY_DISABLED       (1)     /* Deactivate LIU */
#define LIUTY_E1_75U         (2)     /* E1 75 Ohm Unbalanced */
#define LIUTY_E1_120B        (3)     /* E1 120 Ohm Balanced */
#define LIUTY_T1             (4)     /* T1 */
#define LIUTY_E1             (5)     /* E1 Bal/Unbal according to hardware */




