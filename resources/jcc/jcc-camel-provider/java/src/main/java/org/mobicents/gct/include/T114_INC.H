/*
                   Copyright (C) 1998-2002 Intel Corporation.

    Name:          t114_inc.h

    Description:   ANSI TACP Parameter Coding Definitions.
                   Supplements the ITU defintions found in
                   q773_inc.h

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    05-Oct-98  RBP    - Initial code.
   1    06-Nov-98  RBP    - First Release.
   2    28-Jul-00  JET    - Combine #defines from TCAP T114 definitions.
   3    17-May-02  JER    - Additions for AIN.
   4    04-Dec-02  JTD    - Change to use of Intel Corporation Copyright.

 */

/********************************************************************
 *                 TCAP Message tag definitions from                *
 *                    ANSI T1.114.3 and T1.114.5                    *
 ********************************************************************/

/*
 * Transaction Portion Fields:
 */
#define T114MT_unidirectional		(PRIVATE_CONST+1)
#define T114MT_queryWith 		(PRIVATE_CONST+2)
#define T114MT_queryWithout		(PRIVATE_CONST+3)
#define T114MT_response			(PRIVATE_CONST+4)
#define T114MT_conversationWith		(PRIVATE_CONST+5)
#define T114MT_conversationWithout	(PRIVATE_CONST+6)
#define T114MT_abort	 		(PRIVATE_CONST+22)

#define T114_TransactionID		(PRIVATE_PRIM+7)

#define T114_P_AbortCause		(PRIVATE_PRIM+23)
#define T114_userAbortInformation  	(PRIVATE_PRIM+24)
#define T114_dialoguePortion		(PRIVATE_CONST+25)
#define T114_protocolVersion		(PRIVATE_PRIM+26)
#define T114_integerApplContext		(PRIVATE_PRIM+27)
#define T114_objectApplContext		(PRIVATE_PRIM+28)
#define T114_userInformation	  	(PRIVATE_CONST+29)

#define T114_integerSecurityContx	(CONTEXT_PRIM+0)
#define T114_objectSecurityContx	(CONTEXT_PRIM+1)
#define T114_confidentialityInd		(CONTEXT_CONST+2)

#define T114_integerAlgorithmID		(CONTEXT_PRIM+0)
#define T114_objectAlgorithmID		(CONTEXT_PRIM+1)

/*
 * P-Abort cause values:
 */
#define T114PAC_unrecognisedPackageType         (1)
#define T114PAC_incorrectTrPortion              (2)
#define T114PAC_badlyStructuredTrPortion        (3)
#define T114PAC_unassignedRespTID               (4)
#define T114PAC_permissionToRelProblem          (5)
#define T114PAC_resourceUnavailable             (6)
#define T114PAC_unrecognisedDialogueID          (7)
#define T114PAC_badlyStructuredDlgPortion       (8)
#define T114PAC_missingDlgPortion               (9)
#define T114PAC_inconsistentDlgPortion          (10)

#define T114_ComponentSequence		(PRIVATE_CONST+8)

/*
 * Components:
 */
#define T114C_invokeLast		(PRIVATE_CONST+9)
#define T114C_returnResultLast		(PRIVATE_CONST+10)
#define T114C_returnError		(PRIVATE_CONST+11)
#define T114C_reject			(PRIVATE_CONST+12)
#define T114C_invokeNotLast		(PRIVATE_CONST+13)
#define T114C_returnResultNotLast	(PRIVATE_CONST+14)

/*
 * Component lengths:
 */
#define T114CL_reject			(11)

/*
 * Component fields :
 */
#define T114_componentID		(PRIVATE_PRIM+15)
#define T114_nationalOperation          (PRIVATE_PRIM+16)
#define T114_privateOperation           (PRIVATE_PRIM+17)
#define T114_nationalErrorCode	 	(PRIVATE_PRIM+19)
#define T114_privateErrorCode		(PRIVATE_PRIM+20)
#define T114_problemCode		(PRIVATE_PRIM+21)

#define T114_parameterSet    	        (PRIVATE_CONST+18)
#define T114_parameterSequence          (UNIVSL_CONST+16)


/********************************************************************
 *                 TCAP Message tag definitions from                *
 *              T1.114.3 of the ANSI 1992 Recommendations           *
 *                                                                  *
 *  WARNING: These #defines are to be removed. Any new references   *
 *  should use those given above. Existing references should be     *
 *  changed at the next suitable module update.                     *
 *                                                                  *
 ********************************************************************/

/*
 * Component tags (Identifiers) - Specified in ANSI T1.114 TCAP.
 */
#define T114_invoke_cpt                         (T114C_invokeLast)
#define T114_result_last_cpt                    (T114C_returnResultLast)
#define T114_error_cpt                          (T114C_returnError)
#define T114_reject_cpt                         (T114C_reject)

/*
 * IS41 component ID tag (Identifier) - Specified in ANSI T1.114 TCAP.
 */
#define T114_cpt_id                             (T114_componentID)

/*
 * IS41 operation code tag (Identifier) - Specified in ANSI T1.114 TCAP.
 */
#define T114_op_code_id                         (T114_privateOperation)

/*
 * IS41 error code tag (Identifier) - Specified in ANSI T1.114 TCAP.
 */
#define T114_err_code_id                        (T114_privateErrorCode)

/*
 * IS41 Problem code tag (Identifier) - Specified in ANSI T1.114 TCAP.
 */
#define T114_prob_code_id                       (T114_problemCode)

/*
 * IS41 Parameter Set tag (Identifier) - Specified in ANSI T1.114 TCAP.
 */
#define T114_param_set                          (T114_parameterSet)


/*
***********************************************************
*                                                         *
*       ANSI T1.114.3 Reject component values.            *
*                                                         *
***********************************************************
*/

/*
 * Definitions used for Reject Component - Problem type code.
 * (Alligned with ANSI T1.114.3 problem type codes).
 */
#define  T114PT_General_Problem                 (1)
#define  T114PT_Invoke_Problem                  (2)
#define  T114PT_Result_Problem                  (3)
#define  T114PT_Error_Problem                   (4)
#define  T114PT_Transaction_Problem             (5)

/*
 * ANSI - General Problem code.
 */
#define  T114GPC_UnrecognisedComponentType      (1)
#define  T114GPC_IncorrectComponentPortion      (2)
#define  T114GPC_BadlyStructuredComponentPortion (3)
#define  T114GPC_IncorrectComponentCoding       (4)

/*
 * ANSI - Invoke Problem code.
 */
#define  T114IPC_DuplcateInvokeID               (1)
#define  T114IPC_UnrecognisedOperationCode      (2)
#define  T114IPC_IncorrectParameter             (3)
#define  T114IPC_UnrecognisedCorrelationID      (4)
#define  T114IPC_MissingMandatoryParameter	(5)

/*
 * ANSI - Return Result Problem code.
 */
#define  T114RRPC_UnassignedCorrelationID       (1)
#define  T114RRPC_UnexpectedReturnResult        (2)
#define  T114RRPC_IncorrectParameter            (3)
#define  T114RRPC_MissingMandatoryParameter	(4)

/*
 * ANSI - Return Error Problem code.
 */
#define  T114REPC_UnassignedCorrelationID       (1)
#define  T114REPC_UnexpectedReturnError         (2)
#define  T114REPC_UnrecognisedError             (3)
#define  T114REPC_UnexpectedError               (4)
#define  T114REPC_IncorrectParameter            (5)
#define  T114REPC_MissingMandatoryParameter	(6)

/*
 * ANSI - Transaction Portion Problem code.
 */
#define  T114TPPC_UnrecognisedPackageType               (1)
#define  T114TPPC_IncorrectTransactionPortion           (2)
#define  T114TPPC_BadlyStructuredTransactionPortion     (3)
#define  T114TPPC_UnassignedRespondingTransactionID     (4)
#define  T114TPPC_PermissionToRelease                   (5)
#define  T114TPPC_ResourceUnavailable                   (6)



