/*
                Copyright (C) 1994-2004 Intel Corporation.

 Name:          scp_inc.h

 Description:   Include file for use by modules that interface
                with the SCCP module.

                For details of the use of each message and the
                meaning of the parameters please refer to the
                DataKinetics SCCP Programmer's Manual.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------

   A    01-Mar-94   SRG   - Initial code.
   B    25-Mar-94   SRG   - *** SCCP Alpha Release ****
   B+   11-Apr-94   SFP   - SCP_MSG_R_STATS, SCP_MSG_CNF_TIM  and
                            SCP_MSG_R_SSR_STATS added.
   C    24-May-94   SRG   - *** SCCP Beta Release ****
   C+   07-Jul-94   SFP   - User primitives and parameter defined.
   D    26-Jul-94   SRG   - Issued for code review.
   1    28-Jul-94   SRG   - *** SCCP V1.00 Release ***
   2    18-Nov-94   SRG   - SCP_MSG_SMB_IND added. Parameters for
                            SCP_MSG_ADD_CONC changed. ssr_flags, T_hbt
                            and T_bpa timers, ssr_flags and SCPF_xxx
                            values added.
   3    26-Jan-95   SFP   - SCPF_MPRI added.
   4    25-Apr-96   SFP   - SCPF_XUDT, SCPF_24PC, SCPF_PRI_BA, SCPF_XMPRIM,
                            SCPF_PC_CABT, SCPF_ANSI and SCPPN_HOP_COUNTER
                            defined.
   5    15-Apr-97   SFP   - SSRF_NAT #defined.
   6    07-Aug-97   RBP   - Definitions for connection oriented operation
		    SFP	    added.
			    Definition of SCPN_GTT_MASK added.
   7    05-Dec-97   SFP   - SCPXF_NO_CGPC defined.
   8    10-Aug-97   RBP   - SCPMO_CNF_SSR_pc_mask & SCPMS_CNF_SSR_pc_mask added.
   9    17-Feb-00   JET   - SCPXF_INCL_CDPC extended option added to allow
                            selective inclusion of DPC in called party addresses
   10   11-Oct-00   JET   - Added #define for Originator value - SCP_UNDEFINED.
                          - Added #define for enabling extended options.
                          - Added per-subsystem managment id to SCP_MSG_CNF_SSR
                            message.
   11   27-Jan-03   YTA   - Added SCCP trace message type(5754), length and
                            offset parameters.
                          - Copyright updated to Intel Corporation format.
   12   xx-xxx-xx   ML    - Added SCP_MSG_RESET.
 */

/*
 * Message type definitions:
 * (values for m->hdr.type)
 */

/*
 * Protocol primitive messages:
 */
#define SCP_MSG_TX_REQ          (0xc740)  /* Transmit request from user */
#define SCP_MSG_TX_REQ_ITU      (0xc741)  /* obsolete message */
#define SCP_MSG_RX_IND          (0x8742)  /* Receive indication to user */
#define SCP_MSG_RX_IND_ITU      (0x8743)  /* obsolete message */
#define SCP_MSG_SCMG_REQ        (0xc744)  /* Management request from user */
#define SCP_MSG_SCMG_IND        (0x8745)  /* Management indication to user */

/*
 * Non-primitive messages:
 */
#define SCP_MSG_CONFIG      (0x7740)    /* Configure Module */
#define SCP_MSG_CNF_SSR     (0x7741)    /* Configure Sub-System Resource */
#define SCP_MSG_END_SSR     (0x7742)    /* End Sub-System Resource */
#define SCP_MSG_ADD_CONC    (0x7743)    /* Add concerned resource */
#define SCP_MSG_END_CONC    (0x7744)    /* End concerned resource */
#define SCP_MSG_GTT_ADD     (0x7745)    /* Add Global title translation */
#define SCP_MSG_GTT_REM     (0x7746)    /* Remove global title translation */
#define SCP_MSG_GTT_MOD     (0x7747)    /* Modify global title translation */
#define SCP_MSG_CNF_TIM     (0x7748)    /* Configure timers */
#define SCP_MSG_RESTART     (0x7749)    /* Commence SCOC restart procedure */
#define SCP_MSG_RESET       (0x774A)    /* Reset module */

#define SCP_MSG_R_RAM       (0x6750)    /* Read ram address - debug only */
#define SCP_MSG_R_SSR       (0x6751)    /* Read ssr address - debug only */
#define SCP_MSG_R_STATS     (0x6752)    /* Read global Q.791 statistics */
#define SCP_MSG_R_SSR_STATS (0x6753)    /* Read SSR Q.791 statistics */

#define SCP_MSG_MAINT_IND   (0x0761)    /* Maintenance event indication */
#define SCP_MSG_ERROR_IND   (0x0762)    /* Software event indication */
#define SCP_MSG_SMB_IND     (0x0763)    /* Multiple instance broadcast ind */

#define SCP_MSG_TRACE_MASK  (0x5754)    /* Set SCCP trace mask */
/*
 * Definitions used in status field
 * of confirmation messages:
 */
#define SCPE_BAD_ID             (1)  /* Inappropriate or invalid id */
#define SCPE_BAD_MSG            (5)  /* Inappropriate or unrecognised message */
#define SCPE_BAD_PARAM          (6)  /* Invalid parameters contained in message */
#define SCPE_NO_RESOURCES       (7)  /* Insufficient internal resources */

/*
 * Definitions of length, offset and size of
 * SCP_MSG_CONFIG message parameter field:
 */
#define SCPML_CONFIG_VER_0          (18)
#define SCPML_CONFIG_VER_1          (60)
#define SCPML_CONFIG                (SCPML_CONFIG_VER_1)
#define SCPMO_CONFIG_cnf_ver        (0)        /* must be set to one */
#define SCPMS_CONFIG_cnf_ver            (1)
#define SCPMO_CONFIG_sio            (1)        /* SIO for outgoing messages */
#define SCPMS_CONFIG_sio                (1)
#define SCPMO_CONFIG_options        (2) /* options (SCPF_xxx values) */
#define SCPMS_CONFIG_options            (2)
#define SCPMO_CONFIG_module_id      (4) /* module id for SCCP */
#define SCPMS_CONFIG_module_id          (1)
#define SCPMO_CONFIG_mtp_id         (5) /* module id for MTP */
#define SCPMS_CONFIG_mtp_id             (1)
#define SCPMO_CONFIG_mngt_id        (6) /* module id for management */
#define SCPMS_CONFIG_mngt_id            (1)
#define SCPMO_CONFIG_maint_id       (7) /* module id for maintenance */
#define SCPMS_CONFIG_maint_id           (1)
#define SCPMO_CONFIG_point_code     (8) /* local point code */
#define SCPMS_CONFIG_point_code         (4)
#define SCPMO_CONFIG_max_sif        (12) /* maximum SIF length for MTP */
#define SCPMS_CONFIG_max_sif            (2)
#define SCPMO_CONFIG_sccp_instance  (14) /* Reserved (set to zero) */
#define SCPMS_CONFIG_sccp_instance      (1)
#define SCPMO_CONFIG_smb_id         (15) /* SMB module id */
#define SCPMS_CONFIG_smb_id             (1)
#define SCPMO_CONFIG_smb_flags      (16) /* SMB flags */
#define SCPMS_CONFIG_smb_flags          (2)
#define SCPMO_CONFIG_num_uc         (18) /* No of user connections */
#define SCPMS_CONFIG_num_uc            (2)
#define SCPMO_CONFIG_uc_onset       (20) /* User conn - high water mark*/
#define SCPMS_CONFIG_uc_onset           (2)
#define SCPMO_CONFIG_uc_abmt        (22) /* User conn - low water mark*/
#define SCPMS_CONFIG_uc_abmt            (2)
#define SCPMO_CONFIG_num_ic         (24) /* No of intermediate node conn */
#define SCPMS_CONFIG_num_ic             (2)
#define SCPMO_CONFIG_ic_onset       (26) /* Int node conn - hw mark */
#define SCPMS_CONFIG_ic_onset           (2)
#define SCPMO_CONFIG_ic_abmt        (28) /* Int node conn - lw mark */
#define SCPMS_CONFIG_ic_abmt            (2)
#define SCPMO_CONFIG_num_data       (30) /* No of data buffers (Class 3) */
#define SCPMS_CONFIG_num_data           (2)
#define SCPMO_CONFIG_data_onset     (32) /* Data buf (Class 3) - hw mark */
#define SCPMS_CONFIG_data_onset         (2)
#define SCPMO_CONFIG_data_abmt      (34) /* Data buf (Class 3) - lw mark */
#define SCPMS_CONFIG_data_abmt          (2)
#define SCPMO_CONFIG_num_edata      (36) /* No of expedited data forms */
#define SCPMS_CONFIG_num_edata          (2)
#define SCPMO_CONFIG_edata_onset    (38) /* Expedited data forms - hw mark */
#define SCPMS_CONFIG_edata_onset        (2)
#define SCPMO_CONFIG_edata_abmt     (40) /* Expedited data forms - lw mark */
#define SCPMS_CONFIG_edata_abmt         (2)
#define SCPMO_CONFIG_ext_options    (42) /* Extended options */
#define SCPMS_CONFIG_ext_options        (2)
#define SCPMO_CONFIG_base_id        (44) /* Base connection id for SCCP */
#define SCPMS_CONFIG_base_id            (2)
#define SCPMO_CONFIG_top_id         (46) /* Max connection id for SCCP */
#define SCPMS_CONFIG_top_id             (2)
#define SCPMO_CONFIG_min_id         (48) /* Min connection id used in system */
#define SCPMS_CONFIG_min_id             (2)
#define SCPMO_CONFIG_max_id         (50) /* Max connection id used in system */
#define SCPMS_CONFIG_max_id             (2)
#define SCPMO_CONFIG_isup_id        (52) /* ISUP module ID */
#define SCPMS_CONFIG_isup_id            (1)
#define SCPMO_CONFIG_reserved       (53) /* Reserved (must be set to zero)*/
#define SCPMS_CONFIG_reserved           (7)

/*
 * Definitions for options parameter
 * in the configuration message:
 */
#define SCPF_TFRM      (0x0001) /* Use T_FRAMEs in messages to MTP */
#define SCPF_PLIST     (0x0002) /* CAUTION! Must always be set to one */
#define SCPF_ROI       (0x0004) /* Pass ret_opt and seq_ctrl to user */
#define SCPF_SMB       (0x0008) /* Enable multiple instance broadcast */
#define SCPF_MPRI      (0x0010) /* Enable management message priority */
#define SCPF_XUDT      (0x0020) /* Recognise received XUDT/XUDTS */
#define SCPF_24PC      (0x0040) /* Use 24 bit point codes if set */
#define SCPF_PRI_BA    (0x0080) /* Encode priority in ssf bits BA if set */
#define SCPF_XMPRIM    (0x0100) /* Use extended managemennt primitive fmt */
#define SCPF_PC_CABT   (0x0200) /* MTP controlls congestion abatement */
#define SCPF_ANSI      (0x0400) /* Enable ANSI specific operation */
#define SCPF_TOE       (0x0800) /* Terminate connection on error */
#define SCPF_TFR       (0x1000) /* Freeze conn ref for Tfr on connection end */
#define SCPF_AK_ALL    (0x2000) /* Acknowledge all incomming data form 2 mgs */
#define SCPF_TCONN_INC (0x4000) /* Incomming connection Timer enabled */
#define SCPF_XOP       (0x8000) /* Enable extended option support */

/*
 * Definitions for ext_options parameter
 * in the configuration message:
 *
 * Note: CD is SCCP called party address
 *       CG is SCCP calling party address
 */
#define SCPXF_NO_CGPC   (0x0001) /* Do not add local point code to CG */
#define SCPXF_INCL_CDPC (0x0002) /* Allow DPC to be included in CD */
#define SCPXF_DEL_CGPC  (0x0004) /* Remove PC from calling party address */
#define SCPXF_CG_RTE_GT (0x0008) /* Make CG route on GT if GT present */


/*
 * Definitions of length, offset and size of
 * SCP_MSG_CNF_SSR and SCP_MSG_END_SSR
 * message parameter fields:
 */
#define SCPML_CNF_SSR                (40)
#define SCPMO_CNF_SSR_cnf_ver        (0)        /* must be set to zero */
#define SCPMS_CNF_SSR_cnf_ver            (1)
#define SCPMO_CNF_SSR_ssr_type       (1)        /* Sub-system resource type */
#define SCPMS_CNF_SSR_ssr_type           (1)
#define SCPMO_CNF_SSR_module_id      (2)        /* Module id (for LSS only) */
#define SCPMS_CNF_SSR_module_id          (1)
#define SCPMO_CNF_SSR_mult_ind       (3)        /* Multiplicity indicator (for LSS only) */
#define SCPMS_CNF_SSR_mult_ind           (1)
#define SCPMO_CNF_SSR_spc            (4)        /* Signalling Point Code */
#define SCPMS_CNF_SSR_spc                (4)
#define SCPMO_CNF_SSR_ssn            (8)        /* Sub-System number */
#define SCPMS_CNF_SSR_ssn                (1)
#define SCPMO_CNF_SSR_mgmt_id        (9)        /* Subsystem mgmt id (optional) */
#define SCPMS_CNF_SSR_mgmt_id            (1)
#define SCPMO_CNF_SSR_ssr_flags      (10)       /* Per resource flags SSRF_xxx  */
#define SCPMS_CNF_SSR_ssr_flags          (2)
#define SCPMO_CNF_SSR_pc_mask        (12)       /* Point Code mask (RSP only) */
#define SCPMS_CNF_SSR_pc_mask            (4)
#define SCPMO_CNF_SSR_reserved       (16)       /* must be set to zero */
#define SCPMS_CNF_SSR_reserved           (24)

/*
 * Definitions for ssf_flags parameter
 * in the SCP_MSG_CNF_SSR and SCP_MSG_END_SSR
 * message:
 */
#define SSRF_HBT        (0x0001)  /* Enable LSS heartbeat detection */
#define SSRF_NAT        (0x0002)  /* Encode national/international ind as 1 */

/*
 * Definitions of length, offset and size of
 * SCP_MSG_ADD_CONC and SCP_MSG_END_CONC
 * message parameter fields:
 */
#define SCPML_ADD_CONC                (32)
#define SCPMO_ADD_CONC_cnf_ver        (0)        /* must be set to zero */
#define SCPMS_ADD_CONC_cnf_ver           (1)
#define SCPMO_ADD_CONC_ssr_type       (1)
#define SCPMS_ADD_CONC_ssr_type          (1)
#define SCPMO_ADD_CONC_ssr_spc        (2)
#define SCPMS_ADD_CONC_ssr_spc           (4)
#define SCPMO_ADD_CONC_ssr_ssn        (6)
#define SCPMS_ADD_CONC_ssr_ssn           (1)
#define SCPMO_ADD_CONC_conc_type      (7)
#define SCPMS_ADD_CONC_conc_type         (1)
#define SCPMO_ADD_CONC_conc_spc       (8)
#define SCPMS_ADD_CONC_conc_spc          (4)
#define SCPMO_ADD_CONC_conc_ssn       (12)
#define SCPMS_ADD_CONC_conc_ssn          (1)
#define SCPMO_ADD_CONC_reserved       (13)
#define SCPMS_ADD_CONC_reserved          (19)

/*
 * Definitions of length, offset and size of
 * version 0 SCP_MSG_CNF_TIM message parameter fields:
 * Connectionless and Managment timers.
 */
#define SCPML_CNF_TIM              (40)
#define SCPMO_CNF_TIM_tim_ver      (0)     /* reserved, must be set to 1 */
#define SCPMS_CNF_TIM_tim_ver          (1)
#define SCPMO_CNF_TIM_padding      (1)     /* reserved, must be set to 0 */
#define SCPMS_CNF_TIM_padding          (1)
#define SCPMO_CNF_TIM_T_stat_info  (2)
#define SCPMS_CNF_TIM_T_stat_info      (2)
#define SCPMO_CNF_TIM_T_coord_chg  (4)
#define SCPMS_CNF_TIM_T_coord_chg      (2)
#define SCPMO_CNF_TIM_T_ignore_sst (6)
#define SCPMS_CNF_TIM_T_ignore_sst     (2)
#define SCPMO_CNF_TIM_Tx           (8)
#define SCPMS_CNF_TIM_Tx               (2)
#define SCPMO_CNF_TIM_Ts           (10)
#define SCPMS_CNF_TIM_Ts               (2)
#define SCPMO_CNF_TIM_T_hbt        (12)
#define SCPMS_CNF_TIM_T_hbt            (2)
#define SCPMO_CNF_TIM_T_bpa        (14)
#define SCPMS_CNF_TIM_T_bpa            (2)
/*
 * Definitions of length, offset and size of
 * version 1 SCP_MSG_CNF_TIM message parameter fields:
 * Connection Orientated Timers.
 */
#define SCPMO_CNF_TIM_Tconn        (16)
#define SCPMS_CNF_TIM_Tconn            (2)
#define SCPMO_CNF_TIM_Tias         (18)
#define SCPMS_CNF_TIM_Tias             (2)
#define SCPMO_CNF_TIM_Tiar         (20)
#define SCPMS_CNF_TIM_Tiar             (2)
#define SCPMO_CNF_TIM_Trel         (22)
#define SCPMS_CNF_TIM_Trel             (2)
#define SCPMO_CNF_TIM_Trpt_rel     (24)
#define SCPMS_CNF_TIM_Trpt_rel         (2)
#define SCPMO_CNF_TIM_Tint         (26)
#define SCPMS_CNF_TIM_Tint             (2)
#define SCPMO_CNF_TIM_Treset       (28)
#define SCPMS_CNF_TIM_Treset           (2)
#define SCPMO_CNF_TIM_Tfr          (30)
#define SCPMS_CNF_TIM_Tfr              (2)
#define SCPMO_CNF_TIM_Tconn_inc    (32)
#define SCPMS_CNF_TIM_Tconn_inc        (2)
#define SCPMO_CNF_TIM_Tguard       (34)
#define SCPMS_CNF_TIM_Tguard           (2)
#define SCPMO_CNF_TIM_reserved     (36)    /* reserved, must be set to 0 */
#define SCPMS_CNF_TIM_reserved         (4)

/*
 * Definitions of length, offset and size of
 * SCP_MSG_R_SSR_STATS  message parameter fields:
 */
#define SCPML_RS_SSR                (32)
#define SCPMO_RS_SSR_version        (0)        /* must be set to zero */
#define SCPMS_RS_SSR_version            (1)
#define SCPMO_RS_SSR_ssr_type        (1)        /* Sub-system resource type */
#define SCPMS_RS_SSR_ssr_type           (1)
#define SCPMO_RS_SSR_spc             (2)        /* Signalling Point code */
#define SCPMS_RS_SSR_spc                (4)
#define SCPMO_RS_SSR_ssn             (6)        /* Sub-system number */
#define SCPMS_RS_SSR_ssn                (1)
#define SCPMO_RS_SSR_state           (7)        /* Sub-system flags */
#define SCPMS_RS_SSR_state              (1)
#define SCPMO_RS_SSR_cong_level      (8)         /* Sub-system congestion level */
#define SCPMS_RS_SSR_cong_level         (1)
#define SCPMO_RS_SSR_bss_msg         (9)         /* No of messages sent to a backup */
#define SCPMS_RS_SSR_bss_msg            (4) /* sub-system (Table 9/Q.791, para 9.8) */
#define SCPMO_RS_SSR_reserved        (13)    /* must be set to zero */
#define SCPMS_RS_SSR_reserved           (19)

/*
 * Definitions of length, offset and size of
 * SCP_MSG_R_STATS message parameter fields:
 */
#define SCPML_R_STATS                   (92)
#define SCPMO_R_STATS_version      (0)     /* reserved, must be set to 0 */
#define SCPMS_R_STATS_version           (1)
#define SCPMO_R_STATS_padding      (1)     /* reserved, must be set to 0 */
#define SCPMS_R_STATS_padding           (3)
#define SCPMO_R_STATS_rtf_ttype    (4)     /* Table 7/Q.791 7.1 */
#define SCPMS_R_STATS_rtf_ttype         (4)
#define SCPMO_R_STATS_rtf_tval     (8)     /* Table 7/Q.791 7.2 */
#define SCPMS_R_STATS_rtf_tval          (4)
#define SCPMO_R_STATS_rtf_net_fail (12)    /* Table 7/Q.791 7.3 */
#define SCPMS_R_STATS_rtf_net_fail      (4)
#define SCPMO_R_STATS_rtf_net_cong (16)    /* Table 7/Q.791 7.4 */
#define SCPMS_R_STATS_rtf_net_cong      (4)
#define SCPMO_R_STATS_rtf_ss_fail  (20)    /* Table 7/Q.791 7.5 */
#define SCPMS_R_STATS_rtf_ss_fail       (4)
#define SCPMO_R_STATS_rtf_ss_cong  (24)    /* Table 7/Q.791 7.6 */
#define SCPMS_R_STATS_rtf_ss_cong       (4)
#define SCPMO_R_STATS_rtf_uu       (28)    /* Table 7/Q.791 7.7 */
#define SCPMS_R_STATS_rtf_uu            (4)
#define SCPMO_R_STATS_rtf_sif_err  (32)
#define SCPMS_R_STATS_rtf_sif_err       (4)
#define SCPMO_R_STATS_rtf_too_long (36)
#define SCPMS_R_STATS_rtf_too_long      (4)
#define SCPMO_R_STATS_rtf_unknown  (40)    /* Table 7/Q.791 7.9 */
#define SCPMS_R_STATS_rtf_unknown       (4)
#define SCPMO_R_STATS_stx_err      (44)    /* Table 7/Q.791 7.8 */
#define SCPMS_R_STATS_stx_err           (4)
#define SCPMO_R_STATS_sor_grant    (48)    /* Table 8/Q.791 8.6 */
#define SCPMS_R_STATS_sor_grant         (4)
#define SCPMO_R_STATS_sor_deny     (52)    /* Table 8/Q.791 8.7 */
#define SCPMS_R_STATS_sor_deny          (4)
#define SCPMO_R_STATS_udts_tx      (56)    /* Table 9/Q.791 9.1 */
#define SCPMS_R_STATS_udts_tx           (4)
#define SCPMO_R_STATS_udts_rx      (60)    /* Table 9/Q.791 9.2 */
#define SCPMS_R_STATS_udts_rx           (4)
#define SCPMO_R_STATS_num_msg      (64)    /* Table 9/Q.791 9.3 */
#define SCPMS_R_STATS_num_msg           (4)
#define SCPMO_R_STATS_lss_msg      (68)    /* Table 9/Q.791 9.4 */
#define SCPMS_R_STATS_lss_msg           (4)
#define SCPMO_R_STATS_num_gtt      (72)    /* Table 9/Q.791 9.5 */
#define SCPMS_R_STATS_num_gtt           (4)
#define SCPMO_R_STATS_txm_cl0      (76)    /* Table 9/Q.791 9.6 */
#define SCPMS_R_STATS_txm_cl0           (4)
#define SCPMO_R_STATS_txm_cl1      (80)    /* Table 9/Q.791 9.6 */
#define SCPMS_R_STATS_txm_cl1           (4)
#define SCPMO_R_STATS_rxm_cl0      (84)    /* Table 9/Q.791 9.7 */
#define SCPMS_R_STATS_rxm_cl0           (4)
#define SCPMO_R_STATS_rxm_cl1      (88)    /* Table 9/Q.791 9.7 */
#define SCPMS_R_STATS_rxm_cl1           (4)

/*
 * Definitions of length, offset and size of
 * SCP_MSG_TRACE_MASK message parameter field:
 */
#define SCPML_TRACE_MASK                    (12)
#define SCPMO_TRACE_MASK_op_evt_mask    (0)
#define SCPMS_TRACE_MASK_op_evt_mask        (4)
#define SCPMO_TRACE_MASK_ip_evt_mask    (4)
#define SCPMS_TRACE_MASK_ip_evt_mask        (4)
#define SCPMO_TRACE_MASK_non_prim_mask  (8)
#define SCPMS_TRACE_MASK_non_prim_mask       (4)

/*
 * Output event trace mask definitions
 */
#define SCPOEM_USER_RX_IND  (0x00000001)    /* Dialogue or Component indication */
#define SCPOEM_MTP_TX_REQ   (0x00000002)    /* MTP Transfer request  */

/*
 * Input event trace mask definitions
 */
#define SCPIEM_USER_TX_REQ  (0x00000001)    /* User UDT request */
#define SCPIEM_MTP_RX_IND   (0x00000002)    /* MTP indication */
#define SCPIEM_MTP_STAT_IND (0x00000004)    /* MTP status indication (pause, resume, status) */

/*
 * Non-primtive event trace mask definitions
 */
#define SCPNPM_MGT_MSG      (0x00000001)    /* Management messages */
#define SCPNPM_UNK_MSG      (0x00000002)    /* Unknown messages */

/*
 * Definitions for type of internal resource allocated
 * to a sub-system resource structure.
 */
#define SCP_SSRT_FREE         (0)        /* Resource available for use/Invalid type */
#define SCP_SSRT_RSP          (1)        /* Remote signalling point */
#define SCP_SSRT_RSS          (2)        /* Remote sub-system */
#define SCP_SSRT_LSS          (3)        /* Local sub-system */

/*
 * Definitions for use with
 * SCP_MSG_ERROR_IND messages:
 */
#define SCPSWE_SCPM_LOW       (3)  /* Running short of SCPM structures */
#define SCPSWE_NO_SCPM        (4)  /* SCPM structures exhausted */
#define SCPSWE_BAD_MSG        (5)  /* Unsupported message received */
#define SCPSWE_TX_FMT_ERR     (7)  /* Failed to format message for tx */
#define SCPSWE_TIM_START_ERR  (8)  /* Attempt made to re-use active timer resource */
#define SCPSWE_FMT_ERR        (9)  /* Format error in user primitive req */
#define SCPSWE_TALLOC_ERR     (10) /* Failed to allocate T_FRAME */
/*
 * Additional Connection orientated SCP_MSG_ERROR_IND error codes.
 */
#define SCPSWE_UC_ERROR          (11)  /* User CONN structures exhausted */
#define SCPSWE_IC_ERROR          (12)  /* Intermediate node CONN structures exhausted */
#define SCPSWE_DATA_ERROR        (13)  /* DATA structures exhausted */
#define SCPSWE_EDATA_ERROR       (14)  /* EXP_DATA structures exhausted */
#define SCPSWE_UC_CONG           (15)  /* running out of user connection sections */
#define SCPSWE_UC_ABMT           (16)  /* above condition abated */
#define SCPSWE_IC_CONG           (17)  /* running out of intermediate node connection sections */
#define SCPSWE_IC_ABMT           (18)  /* above condition abated */
#define SCPSWE_DATA_CONG         (19)  /* running out of class 3 data forms sections */
#define SCPSWE_DATA_ABMT         (20)  /* above condition abated */
#define SCPSWE_EDATA_CONG        (21)  /* running out of expedited data forms sections */
#define SCPSWE_EDATA_ABMT        (22)  /* above condition abated */

/*
 * Definitions of status field
 * in SCP_MSG_MAINT_IND        messages
 *
 * Pertaining to Routing Failures :
 */
#define SCPEV_RTF_TTYPE         (1)   /* No translation for addr of such nature */
#define SCPEV_RTF_TVAL          (2)   /* No translation for a specific addr */
#define SCPEV_RTF_NET_FAIL      (3)   /* Network failure */
#define SCPEV_RTF_NET_CONG      (4)   /* Network congestion */
#define SCPEV_RTF_SS_FAIL       (5)   /* Sub-system failure */
#define SCPEV_RTF_SS_CONG       (6)   /* Sub-system congestion */
#define SCPEV_RTF_UU            (7)   /* Unequiped user */
#define SCPEV_RTF_SIF_ERR       (8)   /* Greater that 62 octets msg for 62 oct route */
#define SCPEV_RTF_TOO_LONG      (9)   /* Fomratted message is too long */
#define SCPEV_RTF_UNKNOWN       (10)  /* Reason unknown */

/*
 * Other SCP_MAINT_IND values :
 */
#define SCPEV_STX_ERR           (11)  /* Syntax error */
#define SCPEV_SOR_GRANT         (12)  /* SS out-of-service request granted */
#define SCPEV_SOR_DENY          (13)  /* SS out-of-service request denied */

/*
 * SCP_MAINT_IND values for use with connection orientated services:
 */
#define SCPEV_CONN_FAIL         (14)  /* received message time out - connection lost */
#define SCPEV_RESET_FAIL        (15)  /* class 3 reset fail - permenant connections only */
#define SCPEV_RESTART_IND       (16)  /* Connection Orientated restart complete */

/*
 * Definition of ISUP User Part Sub-System Number
 * Note : As defines in ITU Q.713 Section 3.4.2.2
 * Note : This is used in the handling of "Type A User Part" REQUEST type 1
 *        and REQUEST type 2 primitives. ISUP is the only Type A User Part.
 */
#define SCPTAUP_ISUP_SSN        (3)  /* ISUP Sub System Number */


/*
 * Definition of Network Service Primitives for
 * the Connectionless Service:
 */
#define SCPPT_N_UNITDATA_REQ     (1)   /* N-UNITDATA Request */
#define SCPPT_N_UNITDATA_IND     (2)   /* N-UNITDATA Indication */
#define SCPPT_N_NOTICE_IND       (3)   /* N-NOTICE Indication */
/*
 * Network Service Primitives for Connection Orientated Service :
 */
#define SCPPT_N_CONNECT_REQ      (4)  /* N-CONNECT Request */
#define SCPPT_N_CONNECT_IND      (5)  /* N-CONNECT Indication */
#define SCPPT_N_CONNECT_RESP     (6)  /* N-CONNECT Response */
#define SCPPT_N_CONNECT_CONF     (7)  /* N-CONNECT Confirm */
#define SCPPT_N_DATA_REQ         (8)  /* N-DATA Request */
#define SCPPT_N_DATA_IND         (9)  /* N-DATA Indication */
#define SCPPT_N_EXP_DATA_REQ     (10) /* N-EXPEDITED-DATA Request */
#define SCPPT_N_EXP_DATA_IND     (11) /* N-EXPEDITED-DATA Indication */
#define SCPPT_N_RESET_REQ        (12) /* N-RESET Request */
#define SCPPT_N_RESET_IND        (13) /* N-RESET Indication */
#define SCPPT_N_RESET_RESP       (14) /* N-RESET Response */
#define SCPPT_N_RESET_CONF       (15) /* N-RESET Confirmation */
#define SCPPT_N_DISCONNECT_REQ   (16) /* N-DISCONNECT Request */
#define SCPPT_N_DISCONNECT_IND   (17) /* N-DISCONNECT Indication */
#define SCPPT_N_INFORM_REQ       (18) /* N-INFORM Request */
#define SCPPT_N_INFORM_IND       (19) /* N-INFORM Indication */
#define SCPPT_REQUEST_TYPE_1     (20) /* User Part Type A REQUEST type 1 interface element */
#define SCPPT_REPLY              (21) /* User Part Type A REPLY interface element */
#define SCPPT_REQUEST_TYPE_2     (22) /* User Part Type A REQUEST type 2 interface element */


#define SCPPT_PRIM_MAX                (22)

/*
 * Definition of management primitives
 */
#define SCPMPT_NSTATE_REQ        (1)     /* N-STATE Request  */
#define SCPMPT_NSTATE_IND        (2)     /* N-STATE Inidication  */
#define SCPMPT_NCOORD_REQ        (3)     /* N-COORD Request */
#define SCPMPT_NCOORD_IND        (4)     /* N-COORD Indication  */
#define SCPMPT_NPCSTATE_IND      (5)     /* N-PCSTATE Indication */

/*
 * Primitive parameter name definitions
 */
#define SCPPN_RET_OPT             (1)        /* Return option */
#define SCPPN_SEQ_CTRL            (2)        /* Sequence control */
#define SCPPN_MSG_PRI             (3)        /* Message priority */
#define SCPPN_CALLING_ADDR        (4)        /* Calling address */
#define SCPPN_CALLED_ADDR         (5)        /* Called address */
#define SCPPN_USER_DATA           (6)        /* User data */
#define SCPPN_REASON_FOR_RET      (7)        /* Reason for return */
#define SCPPN_TARGET_ADDR         (8)        /* Global title target address */
#define SCPPN_PRIMARY_ADDR        (9)        /* Primary translation */
#define SCPPN_BACKUP_ADDR        (10)        /* Backup translation */
#define SCPPN_HOP_COUNTER        (11)        /* SCCP Hop Counter */


#define SCPPN_EXP_DATA          (12)    /* expedited data */
#define SCPPN_PRO_CLASS         (13)    /* protocol Class */
#define SCPPN_FLOW_CNTRL        (14)    /* protocol Class */
#define SCPPN_CREDIT            (15)    /* Flow control window size */
#define SCPPN_RESP_ADDR         (16)    /* Responding Address */
#define SCPPN_REASON_RESET      (17)    /* Reson for rteset of flow control */
#define SCPPN_REASON_DISCON     (18)    /* Reson for connection release */
#define SCPPN_CONNECT_ID        (19)    /* Connection identitity */
#define SCPPN_CONF_SELECT       (20)    /* Confirm data received */
#define SCPPN_CONFIRM_REQ       (21)    /* Confirm data request */
#define SCPPN_ORIGINATOR        (22)    /* Originator */
#define SCPPN_SOURCE_REF        (23)    /* Source connection reference */
#define SCPPN_OPC               (24)    /* Originator point code */
#define SCPPN_REPLY_REQ         (25)    /* REPLY request */
#define SCPPN_REFUSE_IND        (26)    /* Refuse indication */
#define SCPPN_SEGMENTING        (27)    /* More data segments */
#define SCPPN_GTT_MASK		(28)    /* GTT digit algorithm mask */



#define SCPPN_PARAM_MAX         (28)

/*
 * Length of fixed length parameters, or
 * maximum length of variable parameters:
 */
#define SCPPL_SINGLE_OCTET        (1)

/*
 * SCPPN_RET_OPT values:
 */
#define SCPRO_DISCARD_ON_ERR      (0)
#define SCPRO_RETURN_ON_ERR       (1)

/*
 * Values for 'state' returned
 * for SCP_MSG_R_SSR_STATS:
 */
#define SCPSSRS_PROHIBITED        (0)
#define SCPSSRS_ALLOWED         (1)

/*
 * Code for undefined parameters
 */
#define SCP_param_undefined 0xff

/*
 * Parameter values : N-DISCONECT reason
 */
#define SCPNDREL_Normal_Condition                      (0)
#define SCPNDREL_End_User_Congestion                   (1)
#define SCPNDREL_End_User_Failure                      (2)
#define SCPNDREL_SCCP_User_Originated                  (3)
#define SCPNDREL_Abnormal_Condition_Transient          (4)
#define SCPNDREL_Abnormal_Condition_non_transient      (5)
#define SCPNDREL_Access_failure                        (6)
#define SCPNDREL_Access_Congestion                     (7)
#define SCPNDREL_Subsystem_Congestion                  (8)
#define SCPNDREL_Abnormal_Condition                    (9)
#define SCPNDREL_Invalid_State                         (10)
#define SCPNDREL_Release_in_progress                   (11)

#define SCPNDREF_Dest_Addr_Unknown_Non_Transient       (12)
#define SCPNDREF_Dest_Inaccessible_Non_Transient       (13)
#define SCPNDREF_Dest_Inaccessible_Transient           (14)
#define SCPNDREF_End_User_Originated                   (15)
#define SCPNDREF_End_User_Congestion                   (16)
#define SCPNDREF_End_User_Failure                      (17)
#define SCPNDREF_SCCP_User_Originated                  (18)
#define SCPNDREF_QOS_Unavailable_Non_Transient         (19)
#define SCPNDREF_QOS_Unavailable_Transient             (20)
#define SCPNDREF_Access_Failure                        (21)
#define SCPNDREF_Access_Congestion                     (22)
#define SCPNDREF_Subsystem_Congestion                  (23)
#define SCPNDREF_Reason_Unspecified_Transient          (24)
#define SCPNDREF_Reason_Unspecified_Non_Transient      (25)
#define SCPNDREF_Incompatible_Info_in_NSDU             (26)
#define SCPNDREF_Local_Error                           (27)
#define SCPNDREF_Invalid_State                         (28)
#define SCPNDREF_No_Translation                        (29)
#define SCPNDREF_In_Restart_Phase                      (30)
#define SCPNDREF_Non_Transient_Condition               (31)
#define SCPNDREF_Transient_Condition                   (32)

#define SCPNDREF_max                                   (32)
/*
 * Define values for use with originator parameter in User primitives.
 */
#define SCP_NETWORK_SERVICE_USER     (0)
#define SCP_NETWORK_SERVICE_PROVIDER (1)
#define SCP_UNDEFINED                (SCP_param_undefined)

/*
 * Parameter values : N-RESET reason
 */
#define SCPNR_reason_unspecified                  (0)
#define SCPNR_User_synchronisation                (1)
#define SCPNR_network_service_provider_congestion (2)
#define SCPNR_local_SCCP_originated               (3)


