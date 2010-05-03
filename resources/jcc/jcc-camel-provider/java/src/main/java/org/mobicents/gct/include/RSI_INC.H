/*
                Copyright (C) 1994-2000 DataKinetics Ltd.

 Name:          rsi_inc.h

 Description:   Definitions for interface to RSI.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    01-Nov-94   MH    - Initial code.
   1    20-Nov-94   MH    - Up issue
   1+   02-Apr-97   MH    - Enclose all #define'd values in brackets.
   2    20-Jan-98   MH    - Up issue.
   2+   18-Feb-00   MH    - Add RSI_MSG_READ_LINK (from SIU).
   3    18-Feb-00   MH    - Up issue.
 */

/*
 * RSI Message type definitions :
 */
#define RSI_MSG_CONFIG          (0x7f80)
#define RSI_MSG_UPLINK          (0x7f81)
#define RSI_MSG_DOWNLINK        (0x7f82)
#define RSI_MSG_LNK_STATUS      (0x0f83)
#define RSI_MSG_READ_LINK       (0x6f84)


#define RSI_NO_LINK             (0)
#define RSI_LINK_UP             (1)
#define RSI_LINK_DOWN           (2)
#define RSI_LINK_DEACTIVATED    (3)


#define RSIML_CONFIG                    (68)
#define RSIMO_CONFIG_cnc_type           (0)
#define RSIMS_CONFIG_cnc_type                   (1)
#define RSIMO_CONFIG_conc_id            (1)
#define RSIMS_CONFIG_conc_id                    (1)
#define RSIMO_CONFIG_flags              (2)
#define RSIMS_CONFIG_flags                      (2)
#define RSIMO_CONFIG_local_port         (4)
#define RSIMS_CONFIG_local_port                 (2)
#define RSIMO_CONFIG_remote_port        (6)
#define RSIMS_CONFIG_remote_port                (2)
#define RSIMO_CONFIG_local_addr         (8)
#define RSIMS_CONFIG_local_addr                 (20)
#define RSIMO_CONFIG_remote_addr        (28)
#define RSIMS_CONFIG_remote_addr                (20)
#define RSIMO_CONFIG_reserved           (48)
#define RSIMS_CONFIG_reserved                   (20)

/*
 * Definitions for RSI_MSG_READ_LINK
 */
#define RSIML_READ_LINK                      (14)
#define RSIMO_READ_LINK_laddr             (0)     /* Local IP address */
#define RSIMS_READ_LINK_laddr                 (4)
#define RSIMO_READ_LINK_lport             (4)     /* Local port */
#define RSIMS_READ_LINK_lport                 (2)
#define RSIMO_READ_LINK_faddr             (6)     /* Foreign IP address */
#define RSIMS_READ_LINK_faddr                 (4)
#define RSIMO_READ_LINK_fport             (10)    /* Foreign IP port */
#define RSIMS_READ_LINK_fport                 (2)
#define RSIMO_READ_LINK_tcpstate          (12)    /* TCP socket state */
#define RSIMS_READ_LINK_tcpstate              (2)

/*
 * Definition of tcpstate values:
 */
#define RSITCPS_CLOSED                    (0)
#define RSITCPS_LISTEN                    (1)
#define RSITCPS_SYN_SENT                  (2)
#define RSITCPS_SYN_RECIEVED              (3)
#define RSITCPS_ESTABLISHED               (4)
#define RSITCPS_CLOSE_WAIT                (5)
#define RSITCPS_FIN_WAIT_1                (6)
#define RSITCPS_CLOSING                   (7)
#define RSITCPS_LAST_ACK                  (8)
#define RSITCPS_FIN_WAIT_2                (9)
#define RSITCPS_TIME_WAIT                 (10)
#define RSITCPS_UNKNOWN                   (255)

/*
 * Bit definitions for use in flags field.
 */
#define RSILF_CONFIG_server             (0x0001)


/*
 * Error values (will be stored in status field.
 */
#define RSIE_BAD_ID                     (1)
#define RSIE_BAD_MSG                    (5)
#define RSIE_BAD_PARAM                  (6)
#define RSIE_NO_RESOURCES               (7)

/*
 * Macro for copying a string into a known length buffer
 * and ensuring it is terminated:
 */
#ifndef strncpyt
#define strncpyt(dest,src,size) { strncpy(dest,src,size); *(char *)((dest)+size-1)='\0'; }
#endif
