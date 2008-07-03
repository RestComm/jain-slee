/*
                Copyright (C) 1996-2003 Intel Corporation.

 Name:          ccp_inc.h

 Description:   Contains message types and parameter names that are
                used in the Common Call Control interface.  The 
                definitions are based on the values used by the ISUP
                protocol but are not identical in all cases.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    19-Nov-96   JER   - Initial code.
   1    05-Dec-96   JER   - Includes updates from code review.
   2    30-Jan-98   JER   - New SSUTR2 messages and parameters added.
   3    27-Feb-98   RBP   - CCPN_tup_inforeqi parameter added.
   4    11-Sep-98    ML   - Commentary changes only.
   5    16-Nov-98    JB   - CCPN_at, CCPN_usinf, CCPN_susresi used in
     			    TUP module now.
   6    07-Jan-99   JER   - Circuit seized message added.
   7    12-Feb-99   RBP   - LPA message added and SZE comment includes TUP.
   8    29-Mar-99    JB   - CCPN_scf_id and CCPN_Correlation_id and
   			  - CCPN_subs_prio_class and CCPN_cam added.
   9    21-May-99   FRR   - Added DPNSS. Added display, dfci and dbci.
   10   16-Aug-99    JB   - CCPN_uk_fci added.
   11   17-Sep-99    JB   - CCPT_CCL (calling party clearing) message added.
   12   09-Dec-99   SRG   - Minor change to comment.
   13   21-Dec-99    ML   - Commentary changes only.
   14   23-Dec-99    JB   - Support for UK_ISUP and for more ITU97 param added.
   15   21-Mar-00    JB   - Support for Japanese TTC ISUP and more ITU97
   			    messages and parameters added.
                          - Extended support for UK ISUP.
   16   13-Apr-00    JB   - Support RLT Isup variant
   17   15-May-00    ML   - CCPT_IDR, CCPT_IRS, CCPN_mcid_reqi and
                            CCPN_mcid_respi now used in ISUP.
   18   07-Jun-00    FR   - CCF Parameters and messages defined.
                            CCPN_service_key added for CCF,
   			    Call Control Function.
                     JB   - Support for the custom parameter feature.
   19	07-Sep-00    JB   - Support for Italian ISUP added.
   20   22-Sep-00    JB	  - Multi-carrier environment parameter added in
   			    German ISUP.
                          - Support for PRI and APM messages added.
   21   12-Dec-00    ML   - Corrected definition for PRI and APM.
   22   21-Jun-01    ML   - Support for ISUP97 (CCPT_NRM, CCPN_ccss,
                            CCPN_fgvns, CCPN_bgvns, CCPN_redir_cap,
                            CCPN_net_man_cntrls, CCPN_call_offer_ti,
                            CCPN_charge_prty_id, CCPN_conference_ti,
                            CCPN_collect_call_req).
                          - Commentary changes.
   23   11-Sep-01    ML   - Support for French SSURF ISUP.
   24   26-Mar-03    ML   - Updated copyright notice.
                          - Support for China ISUP.
 */

/**************************************************************************
 *                                                                        *
 *              Message Type definitions                                  *
 *                                                                        *
 * The comments beside each message indicate the protocols that use       *
 * the message:                                                           *
 * C=CVF I=ISUP T=TUP N=NUP Q=Q931 D=DPNSS                                *
 * e.g.                                                                   *
 *      "__ITNQ_" indicates that the message is used by ISUP, TUP, NUP    *
 *             and Q.931                                                  *
 *      "__I_N__" indicates that the message is used by ISUP and NUP      *
 *                                                                        *
 **************************************************************************/

#define CCPT_IAM        (1)     /*CITNQD - Initial Address */
#define CCPT_SAM        (2)     /*CITNQD - Subsequent Address */
#define CCPT_INR        (3)     /*CIT___ - Information Request */
#define CCPT_INF        (4)     /*CIT___ - Information */
#define CCPT_COT        (5)     /*CIT___ - Continuity */
#define CCPT_ACM        (6)     /*CITNQD - Address Complete */
#define CCPT_CON        (7)     /*CI__Q_ - Connect */
#define CCPT_FOT        (8)     /*CIT___ - Forward Transfer */
#define CCPT_ANM        (9)     /*CITNQD - Answer */
#define CCPT_10         (10)    /*______ - Reserved */
#define CCPT_11         (11)    /*______ - Reserved */
#define CCPT_REL        (12)    /*CITNQD - Release */
#define CCPT_SUS        (13)    /*CIT_Q_ - Suspend */
#define CCPT_RES        (14)    /*CIT_Q_ - Resume */
#define CCPT_15         (15)    /*______ - Reserved */
#define CCPT_RLC        (16)    /*CITNQD - Release Complete */
#define CCPT_17         (17)    /*______ - Reserved */
#define CCPT_18         (18)    /*______ - Reserved */
#define CCPT_19         (19)    /*______ - Reserved */
#define CCPT_20         (20)    /*______ - Reserved */
#define CCPT_21         (21)    /*______ - Reserved */
#define CCPT_22         (22)    /*______ - Reserved */
#define CCPT_23         (23)    /*______ - Reserved */
#define CCPT_24         (24)    /*______ - Reserved */
#define CCPT_25         (25)    /*______ - Reserved */
#define CCPT_26         (26)    /*______ - Reserved */
#define CCPT_27         (27)    /*______ - Reserved */
#define CCPT_28         (28)    /*______ - Reserved */
#define CCPT_29         (29)    /*______ - Reserved */
#define CCPT_30         (30)    /*______ - Reserved */
#define CCPT_FAR        (31)    /*CI__Q_ - Facility Request */
#define CCPT_FAA        (32)    /*CI____ - Facility Accepted */
#define CCPT_FRJ        (33)    /*CI__Q_ - Facility Reject */
#define CCPT_34         (34)    /*______ - Reserved */
#define CCPT_35         (35)    /*______ - Reserved */
#define CCPT_LPA        (36)    /*_I____ - Loop Back Acknowledgement */
#define CCPT_37         (37)    /*______ - Reserved */
#define CCPT_38         (38)    /*______ - Reserved */
#define CCPT_39         (39)    /*______ - Reserved */
#define CCPT_PAM        (40)    /*CI____ - Pass Along */
#define CCPT_41         (41)    /*______ - Reserved */
#define CCPT_42         (42)    /*______ - Reserved */
#define CCPT_43         (43)    /*______ - Reserved */
#define CCPT_CPG        (44)    /*CI__QD - Call Progress */
#define CCPT_USR        (45)    /*CIT___ - User to User Information */
#define CCPT_46         (46)    /*______ - Reserved */
#define CCPT_CFN        (47)    /*_I____ - Confusion */
#define CCPT_OLM        (48)    /*_I____ - Overload */
#define CCPT_CRG        (49)    /*_IT___ - Reserved */
#define CCPT_NRM        (50)    /*_I____ - Network resource management */
#define CCPT_FAC        (51)    /*_I____ - Facility */
#define CCPT_52         (52)    /*______ - Reserved */
#define CCPT_53         (53)    /*______ - Reserved */
#define CCPT_IDR        (54)    /*_I____ - Identification Request */
#define CCPT_IRS        (55)    /*_I____ - Identification Response */
#define CCPT_SEG        (56)    /*CI____ - Segmentation */
#define CCPT_LOP        (64)    /*_I____ - Loop prevention */
#define CCPT_APM        (65)    /*_I____ - Application Transport */
#define CCPT_PRI        (66)    /*_I____ - Pre-Release Information */

#define CCPT_NEED       (128)   /*___N__ - Nodal end-to-end data */
#define CCPT_SZE        (199)   /*CIT__D - Circuit seized */
#define CCPT_MCE        (200)   /*__T___ - End-to-end Information (SSUTR2) */
#define CCPT_MPM        (201)   /*_IT___ - Collection charging (SSURF ITX, SSUTR2 ITX & China) */
#define CCPT_TXA        (202)   /*_IT___ - Charging acknowledgement (SSURF & SSUTR2) */
#define CCPT_CHT        (203)   /*__T___ - Tariff change (SSUTR2) */
#define CCPT_CCL        (204)   /*_IT___ - Calling party clearing (China) */ 
#define CCPT_OPR        (205)   /*_I____ - Operator (China) */

#define CCPT_SETUP_ACK  (221)   /*____Q_ - Setup acknowledgement */
#define CCPT_UMT        (254)   /*_I____ - Unrecognised message type */

/**************************************************************************
 *                                                                        *
 *           Parameter Name definitions                                   *
 *                                                                        *
 * The comments beside each message indicate the protocols that use       *
 * the message:                                                           *
 * C=CVF I=ISUP T=TUP N=NUP Q=Q931 D=DPNSS                                *
 * e.g.                                                                   *
 *      "__ITNQ_" indicates that the parameter is used by ISUP, TUP,      *
 *             NUP, and Q.931                                             *
 *      "__I_N__" indicates that the parameter is used by ISUP and NUP    *
 *                                                                        *
 **************************************************************************/

#define CCPN_eoop               (0)     /* CITNQ_ - End of Optional Parameters */
#define CCPN_call_ref           (1)     /* CI____ - Call Reference */
#define CCPN_tmr                (2)     /* CITNQD - Transmission Medium Requirement */
#define CCPN_at                 (3)     /* CIT_QD - Access Transport */
#define CCPN_called_num         (4)     /* CITNQD - Called Party Number */
#define CCPN_subsq_num          (5)     /* CITNQD - Subsequent Number */
#define CCPN_noci               (6)     /* CITNQD - Nature of Connection Indicators */
#define CCPN_fci                (7)     /* CITNQD - Forward Call Indicators */
#define CCPN_ofci               (8)     /* CIT___ - Optional Forward Call Indicators */
#define CCPN_cpc                (9)     /* CITNQD - Called Party's Category */
#define CCPN_calling_num        (10)    /* CITNQD - Calling Party Number */
#define CCPN_redirecting_num    (11)    /* CI_NQ_ - Redirecting Number */
#define CCPN_redirection_num    (12)    /* CI_N_D - Redirection Number */
#define CCPN_connection_req     (13)    /* CI____ - Connection Request */
#define CCPN_inf_reqi           (14)    /* CIT___ - Information Request Indicators */
#define CCPN_infi               (15)    /* CIT___ - Information Indicators */
#define CCPN_conti              (16)    /* C_T___ - Continuity indicators */
#define CCPN_bci                (17)    /* CITNQD - Backward Call Indicators */
#define CCPN_causei             (18)    /* CITNQD - Cause Indicators */
#define CCPN_redirection_inf    (19)    /* CITNQD - Redirection Information */

/*
 * This specific parameter value is used to indicate that
 * the formatting/recovery of the following parameter uses
 * the custom parameter feature extension mechanism.
 */
#define CCPN_custparam          (20)    /* _I____ - Custom parameter */

#define CCPN_21                 (21)    /* ______ - Reserved */
#define CCPN_22                 (22)    /* ______ - Reserved */
#define CCPN_23                 (23)    /* ______ - Reserved */
#define CCPN_faci               (24)    /* CI____ - Facility Indicators */
#define CCPN_service_key        (25)    /* C_____ - Call Control Service Key */
#define CCPN_cugic              (26)    /* CIT___ - CUG Interlock Code */
#define CCPN_27                 (27)    /* ______ - Reserved */
#define CCPN_28                 (28)    /* ______ - Reserved */
#define CCPN_usinf              (29)    /* CIT_QD - User Service Information */
#define CCPN_spc                (30)    /* CIT___ - Signalling Point Code */
#define CCPN_31                 (31)    /* ______ - Reserved */
#define CCPN_uuinf              (32)    /* CIT__D - User to User Information */
#define CCPN_con_num            (33)    /* CI___D - Connected Number */
#define CCPN_susresi            (34)    /* CIT_Q_ - Suspend/Resume Indicators */
#define CCPN_tns                (35)    /* CI__Q_ - Transit Network Selection */
#define CCPN_event_inf          (36)    /* CIT_QD - Event Information */
#define CCPN_cam                (37)    /* C_____ - Circuit Assignment Map */
#define CCPN_38                 (38)    /* ______ - Reserved */
#define CCPN_acl                (39)    /* CI____ - Automatic Congestion Level */
#define CCPN_orig_called_num    (40)    /* CIT_QD - Original Called Number */
#define CCPN_obci               (41)    /* CI__Q_ - Optional Backward Call Indicators */
#define CCPN_uui                (42)    /* CI__QD - User to User Indicators */
#define CCPN_oipc               (43)    /* CI____ - Originating ISC Point Code */
#define CCPN_gen_not            (44)    /* CI__Q_ - Generic Notification */
#define CCPN_chinf              (45)    /* CI____ - Call History Information */
#define CCPN_adinf              (46)    /* CI__Q_ - Access Delivery Information */
#define CCPN_nsf                (47)    /* CI____ - Network Specific Facilities */
#define CCPN_usinf_prime        (48)    /* CI____ - User Service Information Prime */
#define CCPN_pdc                (49)    /* CI____ - Propagation Delay Counter */
#define CCPN_remote_op          (50)    /* CI____ - Remote Operations */
#define CCPN_sa                 (51)    /* CI____ - Service Activation */
#define CCPN_utinf              (52)    /* CI____ - User teleservice Information */
#define CCPN_tmu                (53)    /* CI____ - Transmission Medium Used */
#define CCPN_cdinf              (54)    /* CI____ - Call Diversion Information */
#define CCPN_ecinf              (55)    /* CI____ - Echo Control Information */
#define CCPN_mcinf              (56)    /* CI____ - Message Compatibility Information */
#define CCPN_pcinf              (57)    /* C_____ - Parameter Compatibility Information */
#define CCPN_precedence         (58)    /* CI____ - MLPP Precedence */
#define CCPN_mcid_reqi          (59)    /* CI____ - MCID Request Indicators */
#define CCPN_mcid_respi         (60)    /* CI____ - MCID Response Indicators */
#define CCPN_hop_counter        (61)    /* CI____ - Hop Counter */
#define CCPN_tmr_prime          (62)    /* CI____ - TMR Prime */
#define CCPN_loc_num            (63)    /* CI____ - Location Number */
#define CCPN_redir_restrict     (64)    /* CI____ - Redirection Number Restriction */
#define CCPN_fi                 (65)    /* CI____ - Freephone indicators */
#define CCPN_gen_ref            (66)    /* CI____ - Generic Reference */
#define CCPN_call_trans_ref 	(67)    /* CI____ - Call Transfer Reference */
#define CCPN_lpi  	 	(68)    /* CI____ - Loop prevention indicators */
#define CCPN_call_trans_num 	(69)    /* CI____ - Call Transfer Number */
#define CCPN_ccss		(75)    /* _I____ - CCSS */
#define CCPN_fgvns		(76)    /* _I____ - Forward GVNS */
#define CCPN_bgvns              (77)    /* _I____ - Backward GVNS */
#define CCPN_redir_cap	 	(78)    /* CI____ - Redirect Capability */
#define CCPN_net_man_cntrls	(91)    /* _I____ - Network Management Controls */

#define CCPN_correlation_id     (101)   /* CI____ - Correlation ID  */
#define CCPN_scf_id             (102)   /* CI____ - SCF_id */
#define CCPN_call_div_ti        (110)   /* _I____ - Call Diversion Treatment Indicators */
#define CCPN_called_in_num      (111)   /* CI____ - Called IN Number */
#define CCPN_call_offer_ti      (112)   /* _I____ - Call Offering Treatment Indicators */
#define CCPN_charge_prty_id     (113)   /* _I____ - Charged Party Identification */
#define CCPN_conference_ti	(114)   /* _I____ - Conference Treatment Indicators */
#define CCPN_display            (115)   /* CI___D - Display Information */
#define CCPN_uid_act_ind 	(116)   /* CI____ - UID Action Indicators */
#define CCPN_uid_cap_ind 	(117)   /* CI____ - UID Capability Indicators  */
#define CCPN_redir_cntr 	(119)   /* CI____ - Redirect Counter */
#define CCPN_app	 	(120)   /* _I____ - Application Transport */
#define CCPN_collect_call_req	(121)   /* _I____ - Collect call Request */

/*
 * This specific parameter value is used to indicate that the
 * formatting/recovery of the following parameter uses the
 * extension mechanism.
 */
#define CCPN_EXTPARAM_MECH	(128)


#define CCPN_gen_number         (192)   /* CIT___ - Generic number (address) */
#define CCPN_gen_digits         (193)   /* CI____ - Generic digits */
#define CCPN_osinf              (194)   /* CI____ - Operator Services Information */
#define CCPN_egress             (195)   /* CI____ - Egress */
#define CCPN_jurisdiction       (196)   /* CI____ - Jurisdiction */
#define CCPN_carrier_ident      (197)   /* CI____ - Carrier Identification */
#define CCPN_bg                 (198)   /* CI____ - Business Group */
#define CCPN_gen_name           (199)   /* CI____ - Generic Name */

#define CCPN_acinf              (200)   /* C_T___ - Additional calling party information   */
#define CCPN_artinf             (201)   /* C_T___ - Additional routing information   */
#define CCPN_charginf           (202)   /* C_T___ - Charging information   */
#define CCPN_iai_nat            (203)   /* C_T___ - IAI national use octet   */
#define CCPN_itti               (204)   /* C_T___ - Incoming trunk and transit line identity   */
#define CCPN_tup_infoi          (205)   /* C_T___ - TUP information indicator  */
#define CCPN_ssutr2_rdinfo      (206)   /* C_T___ - SSUTR2 Further Redirection information  */
#define CCPN_ssutr2_acdinf      (207)   /* C_T___ - SSUTR2 Additional Called Party Information  */
#define CCPN_called_sbsm        (208)   /* CI____ - Called subscriber's basic service marks */
#define CCPN_calling_sbsm       (209)   /* CI____ - Calling subscriber's basic service marks  */
#define CCPN_trf_ind            (210)   /* C_T___ - Tariff indicator   */
#define CCPN_trf_fac            (211)   /* C_T___ - Tariff factor   */
#define CCPN_tim_ind            (212)   /* C_T___ - Time indicator   */
#define CCPN_pkt_chg            (213)   /* C_T___ - Packet charging  */
#define CCPN_add_cse            (214)   /* C_T___ - Additional Cause information   */
#define CCPN_mnf                (215)   /* CIT___ - Message number   */
#define CCPN_tup_inforeqi       (216)   /* C_T___ - TUP information request indicators   */
#define CCPN_dfci 		(217)   /* C____D - DPNSS Forward Call Indicators */
#define CCPN_dbci 		(218)   /* C____D - DPNSS Backward Call Indicators */
#define CCPN_calling_sofm       (219)   /* CI____ - Calling subscriber's originating facility marks  */
#define CCPN_uk_infi		(220)   /* CI___D - Uk information indicators */
#define CCPN_uk_iri		(221)   /* CI___D - Uk information request indicators */
#define CCPN_pres_num           (222)   /* CI____ - Presentation Number */
#define CCPN_sbsb_num           (223)   /* CI____ - Contractor number (Subscriber number) */
#define CCPN_phs                (224)   /* CI____ - Personal station number (PHS terminal id number) */
#define CCPN_not_ind            (225)   /* CI____ - Notification Indicators */
#define CCPN_mceit              (226)   /* CI____ - Mobile communication end information transfer */
#define CCPN_trans_req          (227)   /* CI____ - Transaction Request */
#define CCPN_charginf_type      (228)   /* CI____ - Charge information type */
#define CCPN_otgn               (231)   /* CI____ - Outgoing Trunk Group Number */
#define CCPN_orig_inf           (234)   /* CI____ - Originating Line Information */
#define CCPN_charge_num         (235)   /* CI____ - Charge Number */
#define CCPN_sci                (236)   /* CI____ - Service Code Indicator */
#define CCPN_spr                (237)   /* CI____ - Special Processing Request */
#define CCPN_carrier_inf        (238)   /* CI____ - Carrier Selection Information */
#define CCPN_nt                 (239)   /* CI____ - Network Transport */
#define CCPN_mccn               (240)   /* CI____ - Mobile communication call number */
#define CCPN_cit                (241)   /* CI____ - Carrier information transfer */
#define CCPN_charginf_dl        (242)   /* CI____ - Charge information delay */
#define CCPN_apc                (243)   /* CI____ - Additional party's category */
#define CCPN_nrr                (244)   /* CI____ - National redirection reason */
#define CCPN_cause_noid         (245)   /* CI____ - Cause of no ID */
#define CCPN_uk_fci 	        (246)   /* CI____ - UK National Forward Call Indicators */
#define CCPN_subs_prio_class    (247)   /* CI____ - Subscriber Priority Class */
#define CCPN_nup_fci            (248)   /* C__N__ - NUP forward call indicators */
#define CCPN_unrec_par          (249)   /* CI____ - Unrecognised parameter */
#define CCPN_msg_data           (250)   /* CI____ - Contents of a transparent msg e.g. PAM */
#define CCPN_partial_cli        (251)   /* CI_N__ - Partial calling line identity */
#define CCPN_need               (252)   /* C__N__ - Nodal end-to-end data */
#define CCPN_nup_event_inf      (253)   /* C__N__ - NUP event information */
#define CCPN_tariff_type        (254)   /* CI____ - Tariff type */
#define CCPN_num_pulses         (255)   /* CIT___ - Number of metering pulses */

/*
 * Extension mechanism to provide more parameter values
 * and deal with more ISUP variants.
 */
#define CCPN_called_dir_num     (493)   /* _I____ - Called directory number (French(SSURF)) */
#define CCPN_mlt_crr_env        (494)   /* _I____ - Multi-carrier environment (German) */
#define CCPN_chg_band_req       (495)   /* CI____ - Charge Band Request (Italian) */
#define CCPN_chg_band	        (496)   /* CI____ - Charge Band (Italian) */
#define CCPN_opinf	        (497)   /* CI____ - RLT Operator Information */
#define CCPN_chargadj           (498)   /* CI____ - RLT Charge adjustment*/
#define CCPN_sl_inf	        (499)   /* CI____ - RLT supplementary Line information */

#define CCPN_uk_fci_lbl	        (500)   /* CI____ - UK National Forward Call Indicators (link-by-link) */
#define CCPN_called_stfm      	(503)   /* CI____ - Called subscriber's terminating facility marks */
#define CCPN_ldli	      	(508)   /* CI____ - Last diverting line identity */
#define CCPN_cai                (509)   /* CI____ - Charge area information */
#define CCPN_net_fnc_type       (510)   /* CI____ - Network function type */


