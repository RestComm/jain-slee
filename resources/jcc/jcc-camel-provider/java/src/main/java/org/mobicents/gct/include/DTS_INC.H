/*
                Copyright (C) 2001-2004 Intel Corporation

 Name:          dts_inc.h

 Description:   Contains defintions used in DTC and DTS

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    21-Feb-01 YTA/JET - Initial code.
   1    26-Sep-01   YTA   - Changes to DTS to support routing on subsystem
                            number.
   2    05-Apr-04   YTA   - Added intel coptright notice.

 */

/*
 * Client/Server Configuration messages
 */
#define DTS_MSG_CONFIG              (0x7768)
#define DTC_MSG_CONFIG              (0x776c)

/*
 * DTS routing request message
 */
#define DTS_ROUTING_REQ             (0x776d)

/*
 * Client startup/shutdown messages
 */
#define DTS_CLIENT_REQ              (0x776a)
#define DTS_CLIENT_CONF             (0x076b)

/*
 * Trace mask config. message
 */
#define DTS_MSG_TRACE_MASK          (0x5769)

/*
 * Server selection mechanism
 */
#define DTC_SELECT_SEQ              (0x00)
#define DTC_SELECT_REV_SEQ          (0x01)

/*
 * Client selection mechanism
 */
#define DTS_SELECT_SEQ              (0x00)
#define DTS_SELECT_REV_SEQ          (0x01)

/*
 * Request/acknowledgement type octets used in
 * DTS_CLIENT_REQ/DTS_CLIENT_CONF messages
 */
#define DTS_CLIENT_STARTUP          (0x01)
#define DTS_CLIENT_SHUT_PRE         (0x02)
#define DTS_CLIENT_SHUTDOWN         (0x03)

/*
 * Display options
 */
#define DTC_DISP_TX                 (0x0001)  /* Trace messages sent */
#define DTC_DISP_RX                 (0x0002)  /* Trace messages received */
#define DTC_DISP_ERROR              (0x0004)  /* Trace errors */
#define DTC_DISP_MGMT               (0x0008)  /* Trace client/server mgt msg */
#define DTC_DISP_STATUS             (0x0010)  /* Trace progress of program */

/*
 * Trace options that can be set in DTS_MSG_TRACE_MASK
 * Input and output events
 */
#define DTS_TRACE_UDT               (0x1)    /* Trace UDTs */

/*
 * Trace options that can be set in DTS_MSG_TRACE_MASK
 * Non-primitive events
 */
#define DTS_TRACE_CLNT_MGT          (0x1)    /* Trace client mgt msgs */
#define DTS_TRACE_RSI_STATUS        (0x2)    /* Trace RSI status msgs */
#define DTS_TRACE_SERVER_ERR        (0x4)    /* Trace server error msgs */

/*
 * Definitions of length, offset and size of
 * DTC_MSG_CONFIG message parameter field:
 */
#define DTCML_CONFIG                (40)
#define DTCMO_CONFIG_cnf_ver        (0)     /* Must be set to zero */
#define DTCMS_CONFIG_cnf_ver            (1)
#define DTCMO_CONFIG_sccp_id        (1)     /* SCCP module id */
#define DTCMS_CONFIG_sccp_id            (1)
#define DTCMO_CONFIG_tcap_id        (2)     /* TCAP module id */
#define DTCMS_CONFIG_tcap_id            (1)
#define DTCMO_CONFIG_dts_id         (3)     /* DTS module id */
#define DTCMS_CONFIG_dts_id             (1)
#define DTCMO_CONFIG_mgmt_id       (4)      /* Management module id */
#define DTCMS_CONFIG_mgmt_id           (1)
#define DTCMO_CONFIG_rsi_status_id  (5)     /* Id to send RSI message status*/
#define DTCMS_CONFIG_rsi_status_id      (1)
#define DTCMO_CONFIG_num_servers    (6)     /* Number of DTS servers */
#define DTCMS_CONFIG_num_servers        (1)
#define DTCMO_CONFIG_serv_selection (7)     /* Server selection mechanism */
#define DTCMS_CONFIG_serv_selection     (1)
#define DTCMO_CONFIG_options        (8)     /* Run-time configuration flags */
#define DTCMS_CONFIG_options            (2)
#define DTCMO_CONFIG_reserved       (10)    /* Reserved (must be set to zero) */
#define DTCMS_CONFIG_reserved           (30)

/*
 * Definitions of length, offset and size of
 * DTS_MSG_CONFIG message parameter field:
 */
#define DTSML_CONFIG_LEN            (40)
#define DTSMO_CONFIG_cnf_ver        (0)     /* Must be set to zero */
#define DTSMS_CONFIG_cnf_ver            (1)
#define DTSMO_CONFIG_sccp_id        (1)     /* SCCP module id */
#define DTSMS_CONFIG_sccp_id            (1)
#define DTSMO_CONFIG_tcap_id        (2)     /* TCAP module id */
#define DTSMS_CONFIG_tcap_id            (1)
#define DTSMO_CONFIG_dtc_id         (3)     /* DTC module id */
#define DTSMS_CONFIG_dtc_id             (1)
#define DTSMO_CONFIG_trace_id      (4)      /* Trace module id */
#define DTSMS_CONFIG_trace_id          (1)
#define DTSMO_CONFIG_clnt_selection (5)     /* Client selection mechanism */
#define DTSMS_CONFIG_clnt_selection     (1)
#define DTSMO_CONFIG_options        (6)     /* Run-time configuration flags */
#define DTSMS_CONFIG_options            (2)
#define DTSMO_CONFIG_tid_inst       (8)     /* Number of bits used for the */
                                            /* client instance */
#define DTSMS_CONFIG_tid_inst           (1)
#define DTSMO_CONFIG_reserved       (9)     /* Reserved (must be set to zero) */
#define DTSMS_CONFIG_reserved           (31)

/*
 * Definitions of length, offset and size of
 * DTS_CLIENT_REQ message parameter field:
 */
#define DTSML_CLIENT_REQ_LEN        (1)
#define DTSMO_CLIENT_REQ_TYPE       (0)     /* Type of request */
                                            /* startup, shutdown, shutdown-pre */
#define DTSMS_CLIENT_REQ_TYPE           (1)

/*
 * Definitions of length, offset and size of
 * DTS_CLIENT_CONF message parameter field:
 */
#define DTSML_CLIENT_CONF_LEN       (1)
#define DTSMO_CLIENT_CONF_TYPE      (0)     /* Type of confirmation */
                                            /* startup, shutdown, shutdown-pre */
#define DTSMS_CLIENT_CONF_TYPE          (1)
/*
 * Definitions of length, offset and size of
 * DTS_MSG_TRACE_MASK message parameter field:
 */
#define DTSML_TRACE_MASK_LEN        (12)
#define DTSMO_op_evt_mask           (0)     /* Output event trace mask */
#define DTSMS_op_evt_mask               (4)
#define DTSMO_ip_evt_mask           (4)     /* Input event trace mask */
#define DTSMS_ip_evt_mask               (4)
#define DTSMO_non_prim_mask         (8)     /* Non-primitive trace mask */
#define DTSMS_non_prim_mask             (4)

/*
 * Error return values
 */
#define DTSE_BAD_ID                 (1)     /* Inappropriate/invalid id */
#define DTSE_BAD_MSG                (5)     /* Inappropriate/unrecognised message */
#define DTSE_BAD_PARAM              (6)     /* Invalid parameters in message */
#define DTSE_NO_RESOURCES           (7)     /* Insufficient internal resources */
#define DTSE_INST_OUT_OF_RANGE      (8)     /* Instance out of range */
#define DTSE_MISSING_MAND_PARAM     (9)     /* Mandatory parameter missing from message */
#define DTSE_INCOMPATIBLE_PARAMS    (10)    /* Parameters in the same message are incompatible */
#define DTSE_RK_LIST_FULL           (11)    /* Route key list full */
#define DTSE_RK_NOT_FOUND           (12)    /* Route key could not be found */

/*
 * Maximum number of routing keys allowed
 */
#define DTS_MAX_ROUTE_KEYS          (128)

/*
 * Routing enable indicators
 */
#define DTS_ROUTING_ENABLE          (1)
#define DTS_ROUTING_DISABLE         (2)

/*
 * Client selection order
 */
#define DTS_STRICT_ORDER            (0)
#define DTS_PREFERRED_ROUTING       (1)

/*
 * Parameter names for DTS_ROUTING_REQ message
 */
#define DTSPN_ssn                   (1)
#define DTSPN_seq_number            (2)
#define DTSPN_routing_opt           (3)
#define DTSPN_network_app_id        (4)

/*
 * Mask used if 24 bit ITU point codes are present in incoming messages
 */
#define DTS_OPT_ITU_24BIT            (0x1)
