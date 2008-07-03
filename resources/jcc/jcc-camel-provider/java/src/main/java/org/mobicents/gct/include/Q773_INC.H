/*
                   Copyright (C) 1994-2002 Intel Corporation.

 Name:          q773_inc.h

 Description:   CCITT Q.773 TCAP parameter definitions.

 -----  ---------  -----  ---------------------------------------------
 Issue    Date      By                     Changes
 -----  ---------  -----  ---------------------------------------------
   A    04-Jun-94   SRG   - Initial code.
   B    23-Aug-94   SRG   - *** TCAP Alpha Release ****
   B+   26-Aug-94   SRG   - Length of some #defines truncated to be less
                            than 32 bytes. <CR> added at end of file.
   C    18-Oct-94   SRG   - *** TCAP Beta Release ***
   D    07-Feb-95   SRG   - Q773_result changed to (CONTEXT_CONST+2),
                            (Previously it was (CONTEXT_PRIM+2)).
                            Q773_octetAlignedPrim, Q773_octetAlignedConst
                            Q773_arbitraryPrim, Q773_arbitraryConst
                            defined.
   E    10-Mar-95   SFP   - tag class of Q773_dialogueServiceProvider
                            and Q773_dialogueServiceUser corrected from
                            UNIVSL_CONST to CONTEXT_CONST.
   1    05-Jun-95   SRG   - Raised to issue 1.
   2    12-Jun-98   RBP   - Added PRIVATE_PRIM & PRIVATE_CONST definitions
   3    09-Oct-98   JET   - Added tag macros (e.g. APPL_PRIM_TAG(t))
   4    16-Aug-99   JET   - Further #defines added for extended tag form.
   5    28-Jul-00   JET   - Addition of TAG manipulation macros from
                            X208_INC.H.
   6    14-Nov-01   JER   - Addition of Q773_ia5StringPrim.
   7    04-Dec-02   JTD   - Change to use of Intel Corporation Copyright.


 */

/********************************************************************
 *                 TCAP Message tag definitions from                *
 *              Q.773 of the CCITT 1992 Recommendations             *
 ********************************************************************/

#define TagClassUniversal       (0x00)
#define TagClassApplication     (0x40)
#define TagClassContext         (0x80)
#define TagClassPrivate         (0xc0)

#define TagFormPrimitive        (0x00)
#define TagFormConstructor      (0x20)

#define APPL_PRIM       (TagClassApplication+TagFormPrimitive)
#define APPL_CONST      (TagClassApplication+TagFormConstructor)
#define CONTEXT_PRIM    (TagClassContext+TagFormPrimitive)
#define CONTEXT_CONST   (TagClassContext+TagFormConstructor)
#define UNIVSL_PRIM     (TagClassUniversal+TagFormPrimitive)
#define UNIVSL_CONST    (TagClassUniversal+TagFormConstructor)
#define PRIVATE_PRIM    (TagClassPrivate+TagFormPrimitive)
#define PRIVATE_CONST   (TagClassPrivate+TagFormConstructor)

#define APPL_PRIM_TAG(t)     (APPL_PRIM + t)
#define APPL_CONST_TAG(t)    (APPL_CONST + t)
#define CONTEXT_PRIM_TAG(t)  (CONTEXT_PRIM + t)
#define CONTEXT_CONST_TAG(t) (CONTEXT_CONST + t)
#define UNIVSL_PRIM_TAG(t)   (UNIVSL_PRIM + t)
#define UNIVSL_CONST_TAG(t)  (UNIVSL_CONST + t)
#define PRIVATE_PRIM_TAG(t)  (PRIVATE_PRIM + t)
#define PRIVATE_CONST_TAG(t) (PRIVATE_CONST + t)

/*
 * Used for extended format of tag codes:
 */
#define TagCodeExtendedValue  (0x1f)
#define TagExtendedIndicator  (0x80)

#define EXT_APPL_PRIM       (APPL_PRIM_TAG(TagCodeExtendedValue))
#define EXT_APPL_CONST      (APPL_CONST_TAG(TagCodeExtendedValue))
#define EXT_CONTEXT_PRIM    (CONTEXT_PRIM_TAG(TagCodeExtendedValue))
#define EXT_CONTEXT_CONST   (CONTEXT_CONST_TAG(TagCodeExtendedValue))
#define EXT_UNIVSL_PRIM     (UNIVSL_PRIM_TAG(TagCodeExtendedValue))
#define EXT_UNIVSL_CONST    (UNIVSL_CONST_TAG(TagCodeExtendedValue))
#define EXT_PRIVATE_PRIM    (PRIVATE_PRIM_TAG(TagCodeExtendedValue))
#define EXT_PRIVATE_CONST   (PRIVATE_CONST_TAG(TagCodeExtendedValue))

/*
 * Macros' for tag value manipulation:
 */
#define TAG_CODE(t)   ((t) & 0x1f)
#define TAG_CLASS(t)  ((t) & 0xc0)
#define TAG_FORM(t)   ((t) & 0x20)
#define TAG_EXT(t)    ((t) & 0x80)


/*
 * Specific codes reserved for formatting:
 */
#define Q773_EOC                        (UNIVSL_PRIM + 0)
/*
 * General ASN.1 type tags
 */
#define Q773_integer                    (UNIVSL_PRIM+2)
#define Q773_bitStringPrim              (UNIVSL_PRIM+3)
#define Q773_bitStringConst             (UNIVSL_CONST+3)
#define Q773_octetStringPrim            (UNIVSL_PRIM+4)
#define Q773_octetStringConst           (UNIVSL_CONST+4)
#define Q773_null                       (UNIVSL_PRIM+5)
#define Q773_objectIdentifier           (UNIVSL_PRIM+6)
#define Q773_objectDescriptor           (UNIVSL_PRIM+7)
#define Q773_external                   (UNIVSL_CONST+8)
#define Q773_enumerated                 (UNIVSL_PRIM+10)
#define Q773_sequence                   (UNIVSL_CONST+16)
#define Q773_set                        (UNIVSL_CONST+17)
#define Q773_ia5StringPrim		(UNIVSL_PRIM+22)

#define Q773_singleASN1Type             (CONTEXT_CONST+0)
#define Q773_octetAlignedPrim           (CONTEXT_PRIM+1)
#define Q773_octetAlignedConst          (CONTEXT_CONST+1)
#define Q773_arbitraryPrim              (CONTEXT_PRIM+2)
#define Q773_arbitraryConst             (CONTEXT_CONST+2)

/*
 * Transaction Portion Fields:
 */
#define Q773MT_unidirectional     (APPL_CONST+1)
#define Q773MT_begin              (APPL_CONST+2)
#define Q773MT_end                (APPL_CONST+4)
#define Q773MT_continue           (APPL_CONST+5)
#define Q773MT_abort              (APPL_CONST+7)

#define Q773_OrigTransactionID    (APPL_PRIM+8)
#define Q773_DestTransactionID    (APPL_PRIM+9)

#define Q773_P_AbortCause         (APPL_PRIM+10)

#define Q773_DialoguePortion      (APPL_CONST+11)
#define Q773_ComponentPortion     (APPL_CONST+12)

/*
 * Components:
 */
#define Q773C_invoke              (CONTEXT_CONST+1)
#define Q773C_returnResultLast    (CONTEXT_CONST+2)
#define Q773C_returnError         (CONTEXT_CONST+3)
#define Q773C_reject              (CONTEXT_CONST+4)
#define Q773C_returnResultNotLast (CONTEXT_CONST+7)

/*
 * Component fields :
 */
#define Q773_invokeID             (UNIVSL_PRIM+2)
#define Q773_linkedID             (CONTEXT_PRIM+0)

#define Q773_null                 (UNIVSL_PRIM+5)

#define Q773_localOperationCode   (UNIVSL_PRIM+2)
#define Q773_globalOperationCode  (UNIVSL_PRIM+6)

#define Q773_sequence             (UNIVSL_CONST+16)
#define Q773_set                  (UNIVSL_CONST+17)

#define Q773_localErrorCode       (UNIVSL_PRIM+2)
#define Q773_globalErrorCode      (UNIVSL_PRIM+6)

#define Q773_generalProblem       (CONTEXT_PRIM+0)
#define Q773_invokeProblem        (CONTEXT_PRIM+1)
#define Q773_returnResultProblem  (CONTEXT_PRIM+2)
#define Q773_returnErrorProblem   (CONTEXT_PRIM+3)

/*
 * P-Abort cause values:
 */
#define Q773PAC_unrecognisedMessageType         (0)
#define Q773PAC_unrecognisedTID                 (1)
#define Q773PAC_badlyFormattedTrPortion         (2)
#define Q773PAC_incorrectTrPortion              (3)
#define Q773PAC_resourceLimitation              (4)


/********************************************************************
 *                                                                  *
 *                            Problems                              *
 *                                                                  *
 ********************************************************************/

/*
 * General Problem:
 */
#define Q773GP_unrecognisedComponent    (0)
#define Q773GP_mistypedComponent        (1)
#define Q773GP_badlyStructuredComponent (2)

/*
 * Invoke Problem:
 */
#define Q773IP_duplicateInvokeID          (0)
#define Q773IP_unrecognisedOperation      (1)
#define Q773IP_mistypedParameter          (2)
#define Q773IP_resourceLimitation         (3)
#define Q773IP_initiatingRelease          (4)
#define Q773IP_unrecognisedLinkedID       (5)
#define Q773IP_linkedResponseUnexpected   (6)
#define Q773IP_unexpectedLinkedOp         (7)

/*
 * Return Result Problem:
 */
#define Q773RRP_unrecognisedInvokeID      (0)
#define Q773RRP_returnResultUnexpected    (1)
#define Q773RRP_mistypedParameter         (2)

/*
 * Return Error Problem:
 */
#define Q773REP_unrecognisedInvokeID      (0)
#define Q773REP_returnErrorUnexpected     (1)
#define Q773REP_unrecognisedError         (2)
#define Q773REP_unexpectedError           (3)
#define Q773REP_mistypedParameter         (4)

/********************************************************************
 *                                                                  *
 *                     Dialogue APDU definitions                    *
 *                                                                  *
 ********************************************************************/

#define Q773_dialogueRequest         (APPL_CONST+0)
#define Q773_dialogueResponse        (APPL_CONST+1)
#define Q773_dialogueAbort           (APPL_CONST+4)
#define Q773_unidirectionalDialogue  (APPL_CONST+0)

#define Q773_protocolVersion         (CONTEXT_PRIM+0)
#define Q773_applicationContextName  (CONTEXT_CONST+1)
#define Q773_userInformation         (CONTEXT_CONST+30)
#define Q773_abortSource             (CONTEXT_PRIM+0)
#define Q773_result                  (CONTEXT_CONST+2)
#define Q773_resultSourceDiagnostic  (CONTEXT_CONST+3)

/*
 * The following definitions are shown in Table 57/Q.773
 * as 'Universal'. However, the Abstract Syntax definition
 * in Q.773 section 3.2.1 defines the tag class as 'Context'.
 */
#define Q773_dialogueServiceProvider (CONTEXT_CONST+2)
#define Q773_dialogueServiceUser     (CONTEXT_CONST+1)

/*
 * Dialogue service user diagnostic values:
 */
#define Q773DSU_null                   (0)
#define Q773DSU_noReasonGiven          (1)
#define Q773DSU_applicContNameNotSupp  (2)

/*
 * Dialogue service provider diagnostic values:
 */
#define Q773DSP_null                    (0)
#define Q773DSP_noReaonGiven            (1)
#define Q773DSP_noCommonDialoguePortion (2)

/*
 * Result value:
 */
#define Q773RV_accepted                 (0)
#define Q773RV_rejectedPermanent        (1)

/*
 * Abort source:
 */
#define Q773AS_dialogueServiceUser      (0)
#define Q773AS_dialogueServiceProvider  (1)

/********************************************************************
 *                                                                  *
 *                          Length Encoding                         *
 *                                                                  *
 ********************************************************************/

#define Q773L_indefinite              (0x80)

#define Q773L_null                    (0)
#define Q773EL_null                   (2)

#define Q773L_singleOctet             (1)
#define Q773EL_singleOctet            (Q773L_singleOctet+2)

#define Q773L_transactionID           (4)
#define Q773EL_transactionID          (Q773L_transactionID+2)

#define Q773L_protocolVersion         (2)
#define Q773EL_protocolVersion        (Q773L_protocolVersion+2)

#define Q773L_resultSourceDiagnostic  (5)
#define Q773EL_resultSourceDiagnostic (Q773L_resultSourceDiagnostic+2)

#define Q773L_resultDiagnostic        (3)
#define Q773EL_resultDiagnostic       (Q773L_resultDiagnostic+2)

/*
 * Component lengths:
 */
#define Q773CL_reject      (8)

/*
 * Macros' for tag value manipulation:
 */
#define Q773_TAG_CODE(t)   ((t) & 0x1f)
#define Q773_TAG_CLASS(t)  ((t) & 0xc0)
#define Q773_TAG_FORM(t)   ((t) & 0x20)
#define Q773_TAG_EXT(t)    ((t) & 0x80)

