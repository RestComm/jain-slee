/*
                Copyright (C) 2000-2003 Intel Corportaion.

 Name:          sctp_inc.h

 Description:   Definitions required by the SCTP software for GCT msgs

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    13-Oct-00   IDP   - Initial code.
   1    19-Apr-01   IDP   - Raised to issue 1 following code review
   1+   04-Jun-01   IDP   - Addition of options field to AS_CONFIG
   1+   19-Jun-01   IDP   - Addition of FULL_CFG field to options
   1+   04-Jul-01   IDP   - Moved address status defines from sctp_def.h
   1+   10-Aug-01   IDP   - Add correct task id
   1+   17-Jan-02   IDP   - Moved to the standard include order
   1+   21-Jan-02   IDP   - Removed SCTP chunk tracing ability
   1+   20-May-02   IDP   - Added reasons for assoc closure
   2    30-Jul-02   IDP   - Raise to Release Version 1
   2+   07-Nov-02   IDP   - Additional options added to module config
   3    18-Jun-03   GNK   - Up-issue to whole number
   4    22-Sep-03   IDP   - Addition of SCTP_CFG_NO_ABORT_OOB flag
 */

#define SCTP_TASK_ID (0xd1)

/*
 * Convert msg to responce ID
 */
#define MK_RESP(a) (a & 0xbfff)
/*
 * Request responce with getm
 */
#define RESPONCE(mod) (mod ? ((u16)(1<<((mod)&0xf))) : 0)

#define SCTP_MSG_TX_REQ               (0xc280)
#define SCTP_MSG_RX_IND               (0x8281)

#define SCTP_MSG_CONFIG               (0x7282)
#define SCTP_MSG_HOST_IP_ADDR_ADD     (0x7283)
#define SCTP_MSG_HOST_IP_ADDR_REM     (0x7284)
#define SCTP_MSG_TRACE_MASK           (0x5285)
#define SCTP_MSG_AS_CONFIG            (0x7286)
#define SCTP_MSG_ADDR_ADD             (0x7287)
#define SCTP_MSG_ADDR_REM             (0x7288)
#define SCTP_MSG_PRIMARY              (0x7289)
#define SCTP_MSG_ACTIVATE             (0x728a)
#define SCTP_MSG_SHUTDOWN             (0x728b)
#define SCTP_MSG_ABORT                (0x728c)
#define SCTP_MSG_STATUS_CHANGE        (0x028d)
#define SCTP_MSG_NETWORK_STATUS       (0x028e)
#define SCTP_MSG_CONG_STATUS          (0x028f)

#define SCTP_MSG_R_GLOBAL_STATS       (0x6290)
#define SCTP_MSG_R_CONFIG             (0x6291)
#define SCTP_MSG_R_STATS              (0x6292)
#define SCTP_MSG_R_PEER_ADDR_STATS    (0x6293)

/*
 * TODO Remove after debugging
 */
#define SCTP_MSG_DEBUGGER             (0xffff)
#define SCTPML_DEBUGGER               (0)

/*
 * Offset and length definitions for all the SCTP messages.
 * Offsets or lengths that cannot be determined at compile time are NOT present
 */
#define SCTPMO_CONFIG_mngt_id                         (0)
#define SCTPMS_CONFIG_mngt_id                             (1)
#define SCTPMO_CONFIG_trace_id                        (1)
#define SCTPMS_CONFIG_trace_id                            (1)
#define SCTPMO_CONFIG_ip_id                           (2)
#define SCTPMS_CONFIG_ip_id                               (1)
#define SCTPMO_CONFIG_max_assocs                      (3)
#define SCTPMS_CONFIG_max_assocs                          (2)
#define SCTPMO_CONFIG_max_i_streams                   (5)
#define SCTPMS_CONFIG_max_i_streams                       (2)
#define SCTPMO_CONFIG_max_o_streams                   (7)
#define SCTPMS_CONFIG_max_o_streams                       (2)
#define SCTPMO_CONFIG_ms_per_tick                     (9)
#define SCTPMS_CONFIG_ms_per_tick                         (2)
#define SCTPMO_CONFIG_options                        (11)
#define SCTPMS_CONFIG_options                             (2)

#define SCTPML_CONFIG_v1_00                          (11)
#define SCTPML_CONFIG                                (20)

#define SCTPMO_HOST_IP_ADDR_ADD_ip_type               (0)
#define SCTPMS_HOST_IP_ADDR_ADD_ip_type                   (1)
#define SCTPMO_HOST_IP_ADDR_ADD_ip_addr               (1)
#define SCTPMS_HOST_IP_ADDR_ADD_ip_addr                  (16)
#define SCTPML_HOST_IP_ADDR_ADD                      (17)

#define SCTPMO_HOST_IP_ADDR_REM_ip_type               (0)
#define SCTPMS_HOST_IP_ADDR_REM_ip_type                   (1)
#define SCTPMO_HOST_IP_ADDR_REM_ip_addr               (1)
#define SCTPMS_HOST_IP_ADDR_REM_ip_addr                  (16)
#define SCTPML_HOST_IP_ADDR_REM                      (17)

#define SCTPMO_TRACE_MASK_op_evt_mask                 (0)
#define SCTPMS_TRACE_MASK_op_evt_mask                     (4)
#define SCTPMO_TRACE_MASK_ip_evt_mask                 (4)
#define SCTPMS_TRACE_MASK_ip_evt_mask                     (4)
#define SCTPML_TRACE_MASK                               (12)

#define SCTPMO_AS_CONFIG_peer_port                    (0)
#define SCTPMS_AS_CONFIG_peer_port                        (2)
#define SCTPMO_AS_CONFIG_host_port                    (2)
#define SCTPMS_AS_CONFIG_host_port                        (2)
#define SCTPMO_AS_CONFIG_max_i_streams                (4)
#define SCTPMS_AS_CONFIG_max_i_streams                    (2)
#define SCTPMO_AS_CONFIG_max_o_streams                (6)
#define SCTPMS_AS_CONFIG_max_o_streams                    (2)
#define SCTPMO_AS_CONFIG_rto_min                      (8)
#define SCTPMS_AS_CONFIG_rto_min                          (4)
#define SCTPMO_AS_CONFIG_rto_max                     (12)
#define SCTPMS_AS_CONFIG_rto_max                          (4)
#define SCTPMO_AS_CONFIG_rto_init                    (16)
#define SCTPMS_AS_CONFIG_rto_init                         (4)
#define SCTPMO_AS_CONFIG_cookie_life                 (20)
#define SCTPMS_AS_CONFIG_cookie_life                      (4)
#define SCTPMO_AS_CONFIG_heartbeat                   (24)
#define SCTPMS_AS_CONFIG_heartbeat                        (4)
#define SCTPMO_AS_CONFIG_max_retx_init               (28)
#define SCTPMS_AS_CONFIG_max_retx_init                    (2)
#define SCTPMO_AS_CONFIG_max_retx_cookie             (30)
#define SCTPMS_AS_CONFIG_max_retx_cookie                  (2)
#define SCTPMO_AS_CONFIG_max_retx_shutdown           (32)
#define SCTPMS_AS_CONFIG_max_retx_shutdown                (2)
#define SCTPMO_AS_CONFIG_max_retx_heartbeat          (34)
#define SCTPMS_AS_CONFIG_max_retx_heartbeat               (2)
#define SCTPMO_AS_CONFIG_max_retx_data               (36)
#define SCTPMS_AS_CONFIG_max_retx_data                    (2)
#define SCTPMO_AS_CONFIG_T1_init                     (38)
#define SCTPMS_AS_CONFIG_T1_init                          (4)
#define SCTPMO_AS_CONFIG_T2_init                     (42)
#define SCTPMS_AS_CONFIG_T2_init                          (4)
#define SCTPMO_AS_CONFIG_cong_abate                  (46)
#define SCTPMS_AS_CONFIG_cong_abate                       (2)
#define SCTPMO_AS_CONFIG_cong_onset                  (48)
#define SCTPMS_AS_CONFIG_cong_onset                       (2)
#define SCTPMO_AS_CONFIG_cong_discard                (50)
#define SCTPMS_AS_CONFIG_cong_discard                     (2)
#define SCTPMO_AS_CONFIG_rx_window                   (52)
#define SCTPMS_AS_CONFIG_rx_window                        (4)
#define SCTPMO_AS_CONFIG_options                     (56)
#define SCTPMS_AS_CONFIG_options                          (2)
#define SCTPML_AS_CONFIG                             (80)

#define SCTPMO_ADDR_ADD_ip_type                    (0)
#define SCTPMS_ADDR_ADD_ip_type                        (1)
#define SCTPMO_ADDR_ADD_ip_addr                    (1)
#define SCTPMS_ADDR_ADD_ip_addr                       (16)
#define SCTPML_ADDR_ADD                           (17)

#define SCTPMO_ADDR_REM_ip_type                    (0)
#define SCTPMS_ADDR_REM_ip_type                        (1)
#define SCTPMO_ADDR_REM_ip_addr                    (1)
#define SCTPMS_ADDR_REM_ip_addr                       (16)
#define SCTPML_ADDR_REM                           (17)

#define SCTPMO_PRIMARY_ip_type                     (0)
#define SCTPMS_PRIMARY_ip_type                         (1)
#define SCTPMO_PRIMARY_ip_addr                     (1)
#define SCTPMS_PRIMARY_ip_addr                        (16)
#define SCTPML_PRIMARY                            (17)

#define SCTPML_ACTIVATE                            (0)

#define SCTPML_SHUTDOWN                            (0)

#define SCTPMO_ABORT_error                         (0)
#define SCTPMS_ABORT_error                             (2)
#define SCTPML_ABORT                               (2)

#define SCTPMO_RX_IND_data                       (0)
#define SCTPMS_RX_IND_flags                          (2)
#define SCTPMS_RX_IND_stream                         (2)
#define SCTPMS_RX_IND_ppid                           (4)

#define SCTPMO_TX_REQ_data                       (0)
#define SCTPMS_TX_REQ_flags                          (2)
#define SCTPMS_TX_REQ_stream                         (2)
#define SCTPMS_TX_REQ_ppid                           (4)

#define SCTPMO_STATUS_CHANGE_i_streams             (0)
#define SCTPMS_STATUS_CHANGE_i_streams                 (2)
#define SCTPMO_STATUS_CHANGE_o_streams             (2)
#define SCTPMS_STATUS_CHANGE_o_streams                 (2)
#define SCTPML_STATUS_CHANGE                       (4)

#define SCTPMO_NETWORK_STATUS_ip_type              (0)
#define SCTPMS_NETWORK_STATUS_ip_type                  (1)
#define SCTPMO_NETWORK_STATUS_ip_addr              (1)
#define SCTPMS_NETWORK_STATUS_ip_addr                 (16)
#define SCTPML_NETWORK_STATUS                     (17)

#define SCTPML_CONG_STATUS                         (0)

#define SCTPMO_R_GLOBAL_STATS_est_current             (0)
#define SCTPMS_R_GLOBAL_STATS_est_current                 (2)
#define SCTPMO_R_GLOBAL_STATS_est_client              (2)
#define SCTPMS_R_GLOBAL_STATS_est_client                  (4)
#define SCTPMO_R_GLOBAL_STATS_est_server              (6)
#define SCTPMS_R_GLOBAL_STATS_est_server                  (4)
#define SCTPMO_R_GLOBAL_STATS_aborted                (10)
#define SCTPMS_R_GLOBAL_STATS_aborted                     (4)
#define SCTPMO_R_GLOBAL_STATS_shutdown               (14)
#define SCTPMS_R_GLOBAL_STATS_shutdown                    (4)
#define SCTPMO_R_GLOBAL_STATS_OOB                    (18)
#define SCTPMS_R_GLOBAL_STATS_OOB                         (4)
#define SCTPMO_R_GLOBAL_STATS_chunks_tx              (22)
#define SCTPMS_R_GLOBAL_STATS_chunks_tx                   (4)
#define SCTPMO_R_GLOBAL_STATS_chunks_rx              (26)
#define SCTPMS_R_GLOBAL_STATS_chunks_rx                   (4)
#define SCTPMO_R_GLOBAL_STATS_chunks_retx            (30)
#define SCTPMS_R_GLOBAL_STATS_chunks_retx                 (4)
#define SCTPMO_R_GLOBAL_STATS_chunks_unorder_tx      (34)
#define SCTPMS_R_GLOBAL_STATS_chunks_unorder_tx           (4)
#define SCTPMO_R_GLOBAL_STATS_chunks_unorder_rx      (38)
#define SCTPMS_R_GLOBAL_STATS_chunks_unorder_rx           (4)
#define SCTPMO_R_GLOBAL_STATS_bad_checksum           (42)
#define SCTPMS_R_GLOBAL_STATS_bad_checksum                (4)
#define SCTPML_R_GLOBAL_STATS                        (46)

#define SCTPMO_R_CONFIG_peer_port                  (0)
#define SCTPMS_R_CONFIG_peer_port                      (2)
#define SCTPMO_R_CONFIG_host_port                  (2)
#define SCTPMS_R_CONFIG_host_port                      (2)
#define SCTPMO_R_CONFIG_max_i_streams              (4)
#define SCTPMS_R_CONFIG_max_i_streams                  (2)
#define SCTPMO_R_CONFIG_max_o_streams              (6)
#define SCTPMS_R_CONFIG_max_o_streams                  (2)
#define SCTPMO_R_CONFIG_rto_min                    (8)
#define SCTPMS_R_CONFIG_rto_min                        (4)
#define SCTPMO_R_CONFIG_rto_max                   (12)
#define SCTPMS_R_CONFIG_rto_max                        (4)
#define SCTPMO_R_CONFIG_rto_init                  (16)
#define SCTPMS_R_CONFIG_rto_init                       (4)
#define SCTPMO_R_CONFIG_cookie_life               (20)
#define SCTPMS_R_CONFIG_cookie_life                    (4)
#define SCTPMO_R_CONFIG_heartbeat                 (24)
#define SCTPMS_R_CONFIG_heartbeat                      (4)
#define SCTPMO_R_CONFIG_max_retx_init             (28)
#define SCTPMS_R_CONFIG_max_retx_init                  (2)
#define SCTPMO_R_CONFIG_max_retx_cookie           (30)
#define SCTPMS_R_CONFIG_max_retx_cookie                (2)
#define SCTPMO_R_CONFIG_max_retx_shutdown         (32)
#define SCTPMS_R_CONFIG_max_retx_shutdown              (2)
#define SCTPMO_R_CONFIG_max_retx_heartbeat        (34)
#define SCTPMS_R_CONFIG_max_retx_heartbeat             (2)
#define SCTPMO_R_CONFIG_max_retx_data             (36)
#define SCTPMS_R_CONFIG_max_retx_data                  (2)
#define SCTPMO_R_CONFIG_T1_init                   (38)
#define SCTPMS_R_CONFIG_T1_init                        (4)
#define SCTPMO_R_CONFIG_T2_init                   (42)
#define SCTPMS_R_CONFIG_T2_init                        (4)
#define SCTPMO_R_CONFIG_cong_abate                (46)
#define SCTPMS_R_CONFIG_cong_abate                     (2)
#define SCTPMO_R_CONFIG_cong_onset                (48)
#define SCTPMS_R_CONFIG_cong_onset                     (2)
#define SCTPMO_R_CONFIG_cong_discard              (50)
#define SCTPMS_R_CONFIG_cong_discard                   (2)
#define SCTPMO_R_CONFIG_rx_window                 (52)
#define SCTPMS_R_CONFIG_rx_window                      (4)
#define SCTPMO_R_CONFIG_options                   (56)
#define SCTPMS_R_CONFIG_options                        (2)
#define SCTPMO_R_CONFIG_state                     (80)
#define SCTPMS_R_CONFIG_state                          (1)
#define SCTPMO_R_CONFIG_i_streams                 (81)
#define SCTPMS_R_CONFIG_i_streams                      (2)
#define SCTPMO_R_CONFIG_o_streams                 (83)
#define SCTPMS_R_CONFIG_o_streams                      (2)
#define SCTPML_R_CONFIG                           (85)

#define SCTPMO_R_STATS_est_client                  (0)
#define SCTPMS_R_STATS_est_client                      (4)
#define SCTPMO_R_STATS_est_server                  (4)
#define SCTPMS_R_STATS_est_server                      (4)
#define SCTPMO_R_STATS_aborted                     (8)
#define SCTPMS_R_STATS_aborted                         (4)
#define SCTPMO_R_STATS_shutdown                   (12)
#define SCTPMS_R_STATS_shutdown                        (4)
#define SCTPMO_R_STATS_chunks_tx                  (16)
#define SCTPMS_R_STATS_chunks_tx                       (4)
#define SCTPMO_R_STATS_chunks_rx                  (20)
#define SCTPMS_R_STATS_chunks_rx                       (4)
#define SCTPMO_R_STATS_chunks_retx                (24)
#define SCTPMS_R_STATS_chunks_retx                     (4)
#define SCTPMO_R_STATS_chunks_unorder_tx          (28)
#define SCTPMS_R_STATS_chunks_unorder_tx               (4)
#define SCTPMO_R_STATS_chunks_unorder_rx          (32)
#define SCTPMS_R_STATS_chunks_unorder_rx               (4)
#define SCTPMO_R_STATS_t1_expires                 (36)
#define SCTPMS_R_STATS_t1_expires                      (2)
#define SCTPMO_R_STATS_t2_expires                 (38)
#define SCTPMS_R_STATS_t2_expires                      (2)
#define SCTPMO_R_STATS_peer_addr                  (40)
#define SCTPMS_R_STATS_peer_addr                       (1)
#define SCTPMO_R_STATS_period                     (41)
#define SCTPMS_R_STATS_period                          (4)
#define SCTPMO_R_STATS_time_oos                   (45)
#define SCTPMS_R_STATS_time_oos                        (4)
#define SCTPML_R_STATS                            (49)

#define SCTPMO_R_PEER_ADDR_STATS_addr_num          (0)
#define SCTPMS_R_PEER_ADDR_STATS_addr_num              (1)
#define SCTPMO_R_PEER_ADDR_STATS_ip_type           (1)
#define SCTPMS_R_PEER_ADDR_STATS_ip_type               (1)
#define SCTPMO_R_PEER_ADDR_STATS_ip_addr           (2)
#define SCTPMS_R_PEER_ADDR_STATS_ip_addr              (16)
#define SCTPMO_R_PEER_ADDR_STATS_status           (18)
#define SCTPMS_R_PEER_ADDR_STATS_status                (1)
#define SCTPMO_R_PEER_ADDR_STATS_rto              (19)
#define SCTPMS_R_PEER_ADDR_STATS_rto                   (4)
#define SCTPMO_R_PEER_ADDR_STATS_lost_heartbeat   (23)
#define SCTPMS_R_PEER_ADDR_STATS_lost_heartbeat        (4)
#define SCTPMO_R_PEER_ADDR_STATS_lost_data        (27)
#define SCTPMS_R_PEER_ADDR_STATS_lost_data             (4)
#define SCTPML_R_PEER_ADDR_STATS                  (31)

#define SCTPMO_SCTP_EVENT_param                       (0)

/*
 * IP Address defines
 */
#define ADR_NONE (0)
#define ADR_IPV4 (1)
#define ADR_IPV6 (2)

/*
 * Special values for addresses
 */
#define IPV4_ANY_ADDR (0x00000000)

/*
 * Special values for ports
 */
#define SCTP_ANY_PORT (0x0000)
#define SCTP_EPH_PORT (0x8000)
#define SCTP_MAX_PORT (0xffff)

/*
 * Options field for SCTP_MSG_CONFIG
 */
#define SCTP_CFG_OPT_CRC32         (0x0001)
#define SCTP_CFG_OPT_NO_ABORT_INIT (0x0002)
#define SCTP_CFG_OPT_NO_ABORT_OOB  (0x0004)

/*
 * Bitmaps for SCTP_MSG_TRACE_MASK
 */
#define SCTP_EVTO_DATA_IND    (0x00000001)
#define SCTP_EVTO_STATUS_CHG  (0x00000002)
#define SCTP_EVTO_NET_CHG     (0x00000004)
#define SCTP_EVTO_CONG_CHG    (0x00000008)
#define SCTP_EVTO_DATA_REQ    (0x00000010)

#define SCTP_EVTI_ACT_ASSOC   (0x00000001)
#define SCTP_EVTI_SHUT_REQ    (0x00000002)
#define SCTP_EVTI_ABORT_REQ   (0x00000004)
#define SCTP_EVTI_DATA_REQ    (0x00000008)
#define SCTP_EVTI_PRI_ADDR    (0x00000010)

#define SCTP_EVTS_RX_DATA     (0x00000001)
#define SCTP_EVTS_RX_INIT     (0x00000002)
#define SCTP_EVTS_RX_INIT_ACK (0x00000004)
#define SCTP_EVTS_RX_SACK     (0x00000008)
#define SCTP_EVTS_RX_HBEAT    (0x00000010)
#define SCTP_EVTS_RX_HBACK    (0x00000020)
#define SCTP_EVTS_RX_ABORT    (0x00000040)
#define SCTP_EVTS_RX_SDOWN    (0x00000080)
#define SCTP_EVTS_RX_ERROR    (0x00000100)
#define SCTP_EVTS_RX_CECHO    (0x00000200)
#define SCTP_EVTS_RX_CACK     (0x00000400)
#define SCTP_EVTS_RX_SCOMP    (0x00000800)
#define SCTP_EVTS_TX_DATA     (0x00001000)
#define SCTP_EVTS_TX_INIT     (0x00002000)
#define SCTP_EVTS_TX_INIT_ACK (0x00004000)
#define SCTP_EVTS_TX_SACK     (0x00008000)
#define SCTP_EVTS_TX_HBEAT    (0x00010000)
#define SCTP_EVTS_TX_HBACK    (0x00020000)
#define SCTP_EVTS_TX_ABORT    (0x00040000)
#define SCTP_EVTS_TX_SDOWN    (0x00080000)
#define SCTP_EVTS_TX_ERROR    (0x00100000)
#define SCTP_EVTS_TX_CECHO    (0x00200000)
#define SCTP_EVTS_TX_CACK     (0x00400000)
#define SCTP_EVTS_TX_SCOMP    (0x00800000)

/*
 * Options for AS_CONFIG
 */
#define SCTP_AS_CNF_OPT_CLIENT     (0x0000)	/* Assoc is a client */
#define SCTP_AS_CNF_OPT_SERVER     (0x0001)	/* Assoc is a server */
#define SCTP_AS_CNF_OPT_NO_RESTART (0x0002)	/* Assoc disallows restarts */
#define SCTP_AS_CNF_OPT_KEEP_ALIVE (0x0004)	/* No quit on hbeat loss */
#define SCTP_AS_CNF_OPT_FULL_CFG   (0x0008)	/* Config IP adrs */

/*
 * Flags passed within AS_DATA_xxx
 */
#define SCTP_UNBUNDLED (0x0001)
#define SCTP_UNORDERED (0x0002)

/*
 * Status of the association
 */
#define SCTP_STATUS_CLOSED     (0)
#define SCTP_STATUS_CONNECTING (1)
#define SCTP_STATUS_CONNECTED  (2)
#define SCTP_STATUS_CLOSING    (3)
#define SCTP_STATUS_RESTART    (4)

/*
 * Congestion status of the association
 */
#define SCTP_TX_CONG_ABATE     (0)
#define SCTP_TX_CONG_ONSET     (1)
#define SCTP_TX_CONG_DISCARD   (2)

/*
 * State of the association
 */
#define SCTP_STATE_CONFIGURING       (0)
#define SCTP_STATE_CLOSED            (1)
#define SCTP_STATE_COOKIE_WAIT       (2)
#define SCTP_STATE_COOKIE_ECHOED     (3)
#define SCTP_STATE_ESTABLISHED       (4)
#define SCTP_STATE_SHUTDOWN_PENDING  (5)
#define SCTP_STATE_SHUTDOWN_SENT     (6)
#define SCTP_STATE_SHUTDOWN_RECEIVED (7)
#define SCTP_STATE_SHUTDOWN_ACK_SENT (8)

/*
 * Maximum size of data allowed to be txed / rxed over SCTP
 */
#define SCTP_MAX_IP_DATA       (300)
#define SCTP_MAX_DATA_TX_SIZE  (300)
#define SCTP_MAX_DATA_RX_SIZE  (300)

/*
 * Returns status codes for confirmation messages
 */
#define SCTPE_BAD_ID    (1)
#define SCTPE_BAD_MSG   (5)
#define SCTPE_BAD_PARAM (6)

/*
 * State of the peer address
 */
#define SCTP_ADDRESS_INACTIVE     (0x00)
#define SCTP_ADDRESS_ACTIVE       (0x01)

/*
 * Resets defines for the statistic messages
 */
#define SCTP_STATS_RESET    (1)
#define SCTP_STATS_NO_RESET (0)

/*
 * Reasons for ASSOC closing
 */
#define SCTP_REASON_STARTUP        (0)
#define SCTP_REASON_NORMAL_CLOSE   (1)
#define SCTP_REASON_USER_ABORT     (2)
#define SCTP_REASON_PEER_ABORT     (3)
#define SCTP_REASON_COOKIE_RETX    (4)
#define SCTP_REASON_DATA_RETX      (5)
#define SCTP_REASON_HEARTBEAT_RETX (6)
#define SCTP_REASON_DATA_ERROR     (7)
#define SCTP_REASON_OLD_COOKIE     (8)
#define SCTP_REASON_RESTART        (9)
