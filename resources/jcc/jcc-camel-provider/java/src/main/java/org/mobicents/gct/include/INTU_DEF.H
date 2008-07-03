/*
              Copyright (C) 1998-2004 Intel Corp.

   Name:    intu_def.h

   Description:  INAP Test Utility example application program header
                 using the DataKinetics INAP binary module and API interface.

   Functions:  in_ent

   -----   ---------  -----     ------------------------------------
   Issue    Date       By        Changes
   -----   ---------  -----     ------------------------------------
     A     22-Dec-98   JET      - Initial code.
     B     02-Mar-99   JET      - Updated to use current defintions in INAP API
     1     09-Sep-04   GNK      - Move static declaration to intumain.c.
                                - Update copyright owner & year

*/

/*
 * Standard include files
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/*
 * General System7 include files
 */
#include "system.h"
#include "msg.h"
#include "sysgct.h"
#include "digpack.h"
#include "bit2byte.h"
#include "asciibin.h"
#include "strtonum.h"
#include "ss7_inc.h"

/*
 * INAP specific include files
 */
#include "inap_inc.h"
#include "in_inc.h"


/*
 * Command Line Options
 *
 * -m  : intu module_id - The task id of this module
 *
 * -i  : inap module_id - The task id of the INAP binary module
 *
 * -n  : number of dialogues - The number of dialogues to support
 *       This may be any number less than DEFAULT_NUM_DIALOGUES but
 *       by default all dialogues are available.
 *
 * -b  : base dialogue ID - The base number of the supported dialogue IDs
 *       Supported range from (base) upto (base + num_dlgs)
 *
 * -o  : tracing options - Defines which tracing options are configured
 *
 *
 * Default values for INTU command line options:
 */
#define DEFAULT_MODULE_ID        (APP3_TASK_ID)
#define DEFAULT_INAP_MODULE_ID   (INAP_TASK_ID)
#define DEFAULT_BASE_DIALOGUE_ID (0x0000)
#define DEFAULT_OPTIONS          (0x0300)
/*
 * The following definitions may be changed by the user to
 * allow the module to handle more resources:
 * The number of dialogues intu can use is limited by the number of
 * dialogues available in the INAP binary.
 */
#define DEFAULT_NUM_DIALOGUES (2048)  /* Max number of dialogue resources */

/*
 * Command Line Options: Trace configuration codes:
 *
 * Add the codes together for combinations,
 * e.g. -o0x020a   Traces the dialogue status count and all dialogue and
 *                 service indication messages.
 */
#define INTU_OPT_TR_DLG_REQ   (0x0001) /* Trace transmitted dialogue msgs */
#define INTU_OPT_TR_DLG_IND   (0x0002) /* Trace received dialogue msgs */
#define INTU_OPT_TR_SRV_REQ   (0x0004) /* Trace transmitted service req msgs */
#define INTU_OPT_TR_SRV_IND   (0x0008) /* Trace received service ind msgs */
#define INTU_OPT_TR_DLG_PARAM (0x0010) /* Include dialogue params in trace */
#define INTU_OPT_TR_SRV_PARAM (0x0020) /* Include service req params in trace */
#define INTU_OPT_TR_STATE     (0x0100) /* Trace state changes */
#define INTU_OPT_TR_ACTV_DLG  (0x0200) /* Trace the dialogue status count */
#define INTU_OPT_TR_NUM_TRANS (0x0400) /* Show the numbers translated to/from */

/*
 * Used by intumain.c to return the status of command line option recovery
 */
#define COMMAND_LINE_EXIT_REQ          (-1) /* Option requires immediate exit */
#define COMMAND_LINE_UNRECON_OPTION    (-2) /* Unrecognised option */
#define COMMAND_LINE_RANGE_ERR         (-3) /* Option value is out of range */

/*
 * INTU state definitions
 */
#define INTU_STATE_IDLE              (0)  /* Dialogue waiting for OPEN */
#define INTU_STATE_OPEN              (1)  /* Dialogue opened*/
#define INTU_STATE_PENDING_DELIMIT   (2)  /* Waiting for DELIMIT before */
                                          /* processing Serive Logic or reply */
#define INTU_STATE_CLOSING           (3)  /* Waiting for CLOSE indication */

/*
 * Completed dialogue status - did the dialogue progress as expected and
 * complete the number translation.
 */
#define INTU_DLG_SUCCESS             (0)
#define INTU_DLG_FAILED              (1)

/*
 * INTU reply identifier
 */
#define INTU_REPLY_NONE              (0)
#define INTU_REPLY_INVOKE            (1)
#define INTU_REPLY_ERROR             (2)
#define INTU_REPLY_REJECT            (3)
#define INTU_REPLY_U_ABORT           (4)


/*
 * INTU error return codes
 */
#define INTUE_INVALID_DLG_ID         (-1)
#define INTUE_MSG_DECODE_ERROR       (-2)
#define INTUE_MSG_REJECTED           (-3)
#define INTUE_INVALID_DLG_STATE      (-4)
#define INTUE_INVALID_DLG_PTR        (-5)
#define INTUE_INVALID_APPLIC_CONTEXT (-6)
#define INTUE_INIT_CPT_FAILED        (-7)
#define INTUE_INVALID_INVOKE_ID      (-8)
#define INTUE_MSG_ALLOC_FAILED       (-9)
#define INTUE_CODING_FAILURE         (-10)
#define INTUE_MSG_SEND_FAILED        (-11)
#define INTUE_NUM_TRANSLATE_FAILED   (-12)
#define INTUE_NUM_RECOVERY_FAILED    (-13)

/*
 * Defined values to aid the building of operations
 */
#define INTU_SIZEOF_DIGIT_DATA              (10)
#define INTU_NUMBER_OF_DIGITS               (INTU_SIZEOF_DIGIT_DATA * 2)
#define INTU_min_called_party_num_len       (4)
#define INTU_max_called_party_num_len       (11)
#define INTU_Connect_timeout                (4)
#define INTU_ReleaseCall_timeout            (10)
#define INTU_cause_value_normal_unspecified (31)
#define MAX_PARAM_LEN        (320)

/*
 * Example User operation parameter types
 */
typedef struct service_key_type
{
  u8 len;
  u8 data[4];
} SERVICE_KEY_TYPE;

/*
 * Used by paramters:
 *   Called party number
 *   Original Called Party Number
 *   Destination Routing Address
 */
typedef struct pty_addr
{
  u8    noai;           /* Nature of address indicator */
  u8    inni;           /* Internal network number indicator */
  u8    npi;            /* Numbering plan indicator */
  u8    ni;             /* Number incomplete indicator */
  u8    pri;            /* Presentation restricted indicator */
  u8    si;             /* Screening indicator */
  u8    naddr;          /* number of address digits */
  u8    addr[INTU_SIZEOF_DIGIT_DATA];
} PTY_ADDR;

/*
 * Per dialogue resource store - Dialogue Control Block
 *
 * cpt - used to store decoded inap messages, error codes and reject problem
 * for later processing.
 * reply_prepared - Do we have a reply waiting for the delimit
 * state - Holds the dialogue state
 */
typedef struct dlg_cb
{
  IN_CPT cpt;
  u8     reply_prepared;
  u8     state;
} DLG_CB;


/*
 * Procedures in file intu.c for dialogue handling state machine and
 * the main control loop.
 */
#ifdef LINT_ARGS
  int intu_ent(void);
  int INTU_dlg_ind(HDR *h);
  int INTU_srv_ind(HDR *h);

  int INTU_open_dialogue(u16 ic_dlg_id, DLG_CB *dlg_ptr, HDR *h);
  int INTU_close_dialogue(u16 ic_dlg_id, DLG_CB *dlg_ptr, HDR *h, u8 dlg_close_rsn);
  int INTU_change_state(u16 ic_dlg_id, DLG_CB *dlg_ptr, u8 new_state);
  int INTU_handle_invoke(u16 ic_dlg_id, DLG_CB *dlg_ptr, HDR *h);

  int INTU_prepare_service_logic(u16 ic_dlg_id, DLG_CB *dlg_ptr);
  int INTU_prepare_u_abort(u16 ic_dlg_id, DLG_CB *dlg_ptr);
  int INTU_prepare_error(u16 ic_dlg_id, DLG_CB *dlg_ptr, u16 err_code);
  int INTU_prepare_reject(u16 ic_dlg_id, DLG_CB *dlg_ptr);

  int INTU_process_pending_req(u16 ic_dlg_id, DLG_CB *dlg_ptr, HDR *h);
  int INTU_process_invoke(u16 ic_dlg_id, DLG_CB *dlg_ptr, HDR *h);
  int INTU_translate_number(PTY_ADDR *called_party, PTY_ADDR *dest_routing_addr);

  u8  INTU_fmt_called_num(u8 *buf, u8 siz, PTY_ADDR *called_num);
  int INTU_rec_called_num(PTY_ADDR *called_num, u8 *pptr, u8 plen);
  DLG_CB *INTU_get_dlg_cb(u16 dlg_id);
  void *INTU_get_protocol_definition(u16 applic_context_index);
#else
  int intu_ent();
  int INTU_dlg_ind();
  int INTU_srv_ind();

  int INTU_open_dialogue();
  int INTU_close_dialogue();
  int INTU_change_state();
  int INTU_handle_invoke();

  int INTU_prepare_service_logic();
  int INTU_prepare_u_abort();
  int INTU_prepare_error();
  int INTU_prepare_reject();

  int INTU_process_pending_req();
  int INTU_process_invoke();
  int INTU_translate_number();

  u8  INTU_fmt_called_num();
  int INTU_rec_called_num();
  DLG_CB *INTU_get_dlg_cb();
  void *INTU_get_protocol_definition();
#endif

/*
 * Message Sending procedures in file intu_sys.c
 */
#ifdef LINT_ARGS
  int INTU_send_open_rsp(u16 ic_dlg_id);
  int INTU_send_delimit(u16 ic_dlg_id);
  int INTU_send_close(u16 ic_dlg_id, u8 release);
  int INTU_send_u_abort(u16 ic_dlg_id);
  int INTU_send_error(u16 ic_dlg_id, DLG_CB *dlg_ptr);
  int INTU_send_reject(u16 ic_dlg_id, DLG_CB *dlg_ptr);
  int INTU_send_message(u8 sending_user_id, u8 inap_module_id, HDR *h);
#else
  int INTU_send_open_rsp();
  int INTU_send_delimit();
  int INTU_send_close();
  int INTU_send_u_abort();
  int INTU_send_error();
  int INTU_send_reject();
  int INTU_send_message();
#endif

/*
 * Tracing display procedures in file intu_trc.c
 */
#ifdef LINT_ARGS
  int INTU_disp_other_msg(HDR *h);
  int INTU_disp_dlg_msg(HDR *h);
  int INTU_disp_srv_ind(DLG_CB *dlg_ptr, HDR *h);
  int INTU_disp_srv_req(DLG_CB *dlg_ptr, HDR *h);
  int INTU_disp_invalid_srv_ind(DLG_CB *dlg_ptr, HDR *h);
  int INTU_disp_invalid_dlg_ind(DLG_CB *dlg_ptr, HDR *h);
  int INTU_disp_unexpected_srv_ind(DLG_CB *dlg_ptr, HDR *h);
  int INTU_disp_unexpected_dlg_ind(DLG_CB *dlg_ptr, HDR *h);
  int INTU_disp_dlg_reopened(DLG_CB *dlg_ptr, HDR *h);
  int INTU_disp_state_change(u16 ic_dlg_id, DLG_CB *dlg_ptr, u8 new_state);
  int INTU_disp_param(char *param_name_text, u16 param_name_id, u8 param_len, u8 *param_data);
#else
  int INTU_disp_other_msg();
  int INTU_disp_dlg_msg();
  int INTU_disp_srv_ind();
  int INTU_disp_srv_req();
  int INTU_disp_invalid_srv_ind();
  int INTU_disp_invalid_dlg_ind();
  int INTU_disp_unexpected_srv_ind();
  int INTU_disp_unexpected_dlg_ind();
  int INTU_disp_dlg_reopened();
  int INTU_disp_state_change();
  int INTU_disp_param();
#endif
