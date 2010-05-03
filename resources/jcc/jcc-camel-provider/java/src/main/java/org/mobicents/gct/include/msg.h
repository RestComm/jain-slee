/*
		Copyright (C) 1993-2002 DataKinetics Ltd.

 Name:		msg.h

 Description:	Definitions for MSG, T_FRAME and R_FRAME message
		structures as defined in the Motorola
		Software Programmer's Manual.

		This file is based on the file msg.h supplied by
		Motorola but it has been modified to permit use on
		hardware platforms other that the 68000 series.

 -----	---------   ---------------------------------------------
 Issue	  Date			      Changes
 -----	---------   ---------------------------------------------
   A	18-Feb-93   - This file created, header added, layout
		      aligned with DataKinetics standards.
   B    22-Feb-93   - Function prototypes added.
   1    20-Sep-93   - Raised to issue 1.
   2    24-Nov-93   - get_param macro added.
   3    25-Jul-94   - Internal definition READ change to READ_
		      in order to prevent conflict on systems where
		      READ is a system definition.
   4    02-Sep-94   - struct msg changed to struct _msg to prevent
		      conflict on unix systems.
   5    11-Mar-96   - #define USE_360 added for use in conjunction
		      with Motorola 360 driver code.
		      pchild_id added to R_FRAME (for 360 only).
   6    19-Apr-96   - USE_360 removed, pchild_id now used unconditionally,
		      WHICH_SCC changed (for 302) from 0x03 to 0x07.
		      GLOBAL_SCC changed (for 302) from 3 to 4.
   7    29-May-98   - class field in HDR changed to hclass.
		      MSG structure renamed M_FRAME and MSG/MSF
		      definitions added.
   8    20-Oct-99   - class field changed to hclass in prototypes.
   9    07-Jul-02   - SHORT_HDR added.

 */

#ifndef MSG_H
#define MSG_H

/*
 *	Common msg structure
 */
typedef struct hdr
{
	unsigned short	type;		/* type of message	*/
	unsigned short	id;		/* module instantiation	*/
	unsigned char	src;		/* sending module ID	*/
	unsigned char	dst;		/* destination module ID*/
	unsigned short	rsp_req;	/* response required	*/
	unsigned char   hclass;         /* generic MSG type     */
	unsigned char	status;		/* returned status	*/
	unsigned long	err_info;	/* status information	*/
	struct hdr	*next;		/* for message chaining	*/
} HDR;

/*
 * control message format
 */
#define	MAX_PARAM	80
typedef struct m_frame
{
	HDR		hdr;
	unsigned short	len;		/* param field length	*/
	long		param[MAX_PARAM];
} M_FRAME;


/*
 * Allow the use of MSF as a shorthand
 * for M_FRAME (ie. 'message frame')
 */
#define MSF     M_FRAME

/*
 * Allow the use of MSG as a shorthand
 * for M_FRAME unless MSG has already
 * been defined for some other use.
 *
 * Users who are developing a Windows
 * (WINMAIN) program should ensure that
 * the <windows.h> header file is
 * included before msg.h.
 */
#ifndef MSG
#define MSG     M_FRAME
#endif


/*
 * Macro to get (unsigned char *) pointer to MSG parameter area:
 */
#define get_param(x)   ((unsigned char *)((MSF *)(x))->param)


/*
 * Special format of HDR structure used where the overhead
 * of allocating and releasing messages would be an unacceptable
 * overhead. This message must be recognised by the receiving
 * module so that it is not released. Sometimes referred to as a
 * 'dummy' MSG.
 */
typedef struct short_hdr
{
  unsigned short  type;         /* type of message      */
  unsigned short  id;           /* module instantiation */
  unsigned char   src;          /* sending module ID    */
  unsigned char   dst;          /* destination module ID*/
} SHORT_HDR;


/*
 * Transmit frame structure
 */
#define	HDR_SIZE	16		/* Tx data header size	*/

typedef struct layer
{
	unsigned char    flags;
	unsigned char    hoff;          /* Header buffer offset */
	unsigned short	 boff;		/* Data buffer offset */
	struct t_frame	*nextf;		/* Next frame */
} LAYER_INFO;

/* 
 * T_FRAME and R_FRAME flag bits
 */
#define	TX_POOL		0x80	/* T_FRAME:  frame in this layer's */
				/* 		transmit pool */
#define	RX_POOL		0x80	/* R_FRAME: frame belongs to */
				/* 	driver's receive pool */
#define	RETX		0x40	/* T_FRAME: retransmission needed. */
#define	NOACK		0x20	/* T_FRAME: frame not acknowledged */

/*
 * the following three bits are used by the bisync driver.
 * they are all defined for T_FRAMEs only.
 * note that they overlap the previous three bits: this is
 * acceptable since the previous bits are never
 * used by the driver module.
 */
#define	BISYNC_FLAGS	0xe0
#define	BISYNC_TRANSP	0x80	/* enter transparent mode 	*/
#define	BISYNC_SOH	0x40	/* transmit a SOH character 	*/
#define	BISYNC_STX	0x20	/* transmit an STC character 	*/
/*
 * the following two bits are used by the driver of the DMA
 * (they overlap bisync, higher layers flag bits)
 */
#define	DMA_HDONE	0x80	/* header buffer transmitted 		*/

#define DMA_FR_PERIPH   0x40    /* peripheral is source for DMA (360 only) */
#define DMA_TO_PERIPH   0x20    /* peripheral is destination for DMA (360 only)*/
#define DMA_PERIPHERAL  0x40    /* peripheral is destination for DMA (302 only)   */

#define HDR_USED        0x10    /* T_FRAME: header buffer used.         */
#define	RETX_NO_CONF	0x08	/* T_FRAME: frame NOT retransmitted, 	*/
				/* no confirmation from lower yet. 	*/
#define	REPEAT		0x04	/* T_FRAME: Repeat the frame 		*/
#define LINKED_TO_TABLE	0x01	/* BOTH: frame linked to BD table 	*/

typedef struct t_frame
{
  HDR		 	hdr;
  unsigned char		*buf;		/* Data buffer pointer 	 	*/
  unsigned char		*hbuf;		/* Header buffer pointer 	*/
					/* (last bytes filled first) 	*/
  unsigned short	bsize;		/* Data buffer size		*/
  unsigned short	hsize;		/* Header buffer size		*/
  LAYER_INFO	 	layer[7];	/* Structures for each module,	*/
					/* must allocate enough memory	*/
} T_FRAME; 

/*
 * The following macro may be used to obtain the
 * pointer to the beginning of the specified layer's
 * header in a T_FRAME.
 */
#define	HDR_LOC(t, lyr)		(((t)->layer[lyr].flags & HDR_USED) ? \
				&((t)->hbuf[(t)->layer[lyr].hoff]) : \
				&((t)->buf[(t)->layer[lyr].boff]))

/*
 * Receive frame structure
 */
typedef struct r_frame
{
  HDR		 	hdr;
  unsigned char	 	flags;
  unsigned char	 	boff;	/* Data buffer offset		*/
  unsigned short	flen;	/* Frame length (valid only 	*/
				/* in first buffer in frame 	*/
  unsigned short	blen;	/* Buffer length		*/
  unsigned char		*buf;	/* Data buffer pointer		*/
  struct r_frame	*nextb;	/* Next buffer in frame 	*/
  unsigned short        pchild_id;  /* Physical channel id      */
} R_FRAME;

/*
 * msg classes
 * the 6 MSBits describe the type of
 * memory block, the 2 LSBits differentiate
 * between different channels
 */
#define	TX		0x80	/* T_FRAME structure	*/
#define	RX		0x40	/* R_FRAME structure	*/
#define	BUFFER		0x20	/* data buffer attached	*/
#define	HEADER		0x10	/* header buffer attached (TX only) */

#define WHICH_SCC       0x07    /* selects SCC0, 1, 2   */
#define SCC0            0x00
#define SCC1            0x01
#define SCC2            0x02
#define SCC3            0x03
#define GLOBAL_SCC      0x04    /* global, not associated with a */
				/* specific SCC                  */

/*
 * The following derive from class field in HDR
 * the class of memory block used as parameter for
 * calling getmem or relmem for a message structure, a data
 * buffer or a header buffer:
 */
#define	FRM_CLASS	(TX | RX | WHICH_SCC)
#define	BUF_CLASS	(TX | RX | BUFFER | WHICH_SCC)
#define	HDR_CLASS	(TX | HEADER | WHICH_SCC)

/*
 * The following classes of memory blocks
 * are defined. Each one if these may be further
 * partitioned according to SCC port number:
 * SCC0,2,3 or global.
 */
#define	CONTROL	0x00			/* MSG structure	*/
#define	T_FRM	TX			/* T_FRAME structure	*/
#define	R_FRM	RX			/* R_FRAME structure	*/
#define	T_BUF	(TX | BUFFER)		/* Tx buffer		*/
#define	T_HDR	(TX | HEADER)		/* Tx header		*/
#define	R_BUF	(RX | BUFFER)		/* Rx buffer		*/

/*
 * The following classes should be supported by getmem
 * and relmem:
 * 	CONTROL
 *	T_FRM0/1/2/G
 *	R_FRM0/1/2/G
 *	T_BUF0/1/2/G
 *	T_HDR0/1/2/G
 *	R_BUF0/1/2/G
 */

#define	T_FRM0		(T_FRM | SCC0)
#define	R_FRM0		(R_FRM | SCC0)
#define	T_BUF0		(T_BUF | SCC0)
#define	T_HDR0		(T_HDR | SCC0)
#define	R_BUF0		(R_BUF | SCC0)

#define	T_FRM1		(T_FRM | SCC1)
#define	R_FRM1		(R_FRM | SCC1)
#define	T_BUF1		(T_BUF | SCC1)
#define	T_HDR1		(T_HDR | SCC1)
#define	R_BUF1		(R_BUF | SCC1)

#define	T_FRM2		(T_FRM | SCC2)
#define	R_FRM2		(R_FRM | SCC2)
#define	T_BUF2		(T_BUF | SCC2)
#define	T_HDR2		(T_HDR | SCC2)
#define	R_BUF2		(R_BUF | SCC2)

#define	T_FRMG		(T_FRM | GLOBAL_SCC)
#define	R_FRMG		(R_FRM | GLOBAL_SCC)
#define	T_BUFG		(T_BUF | GLOBAL_SCC)
#define	T_HDRG		(T_HDR | GLOBAL_SCC)
#define	R_BUFG		(R_BUF | GLOBAL_SCC)

/*
 *	MSG type definition 
 * message type field consists of two sub-fields:
 * 	category:	bits 10 - 15
 *	subtype: 	bits 0 - 9
 * the categories of messages are defined as bits for easy
 * checking, but no more than one bit may be set in
 * the category sub-field (except the REQUEST bit, which may be
 * set together with PRMTV, ACTION, READ or SET).
 */
#define	CTG		0xf000	/* category sub-field	*/
#define	SUBTYPE		0x00ff	/* subtype sub-field	*/
#define	SUB(x)		(x & SUBTYPE)

#define	PRMTV		0x8000	/* protocol primitive	*/
#define	REQUEST		0x4000	/* request		*/

/*
 * For primitives, the REQUEST bit determines the
 * direction in which the prmitive is issued:
 * PRIM_UP is used for primitives passed from lower
 * layer to upper layer, PRIM_DOWN is used for
 * primitives passed from upper layer to lower layer,
 * and PRIM_INT is used for internal events 
 * (like timer expiry).
 *
 * "UP" primitives always contain the sending module's
 * child ID, whilst "INT" and "DOWN" primitives always
 * contain the receiving module's child ID.
 */
#define	PRIM_UP		(PRMTV)
#define	PRIM_DOWN	(PRMTV | REQUEST)
#define	PRIM_INT	(PRMTV | REQUEST)

/*
 * the following bits are significant only if 
 * PRMTV is cleared.
 */
#define	ACTION	 	0x3000	/* action 		*/
#define	READ_	 	0x2000	/* read 		*/
#define	SET	 	0x1000	/* set	 		*/
#define	EVENT_IND	0x0000	/* event indication	*/

#define	ACT_REQ		(ACTION | REQUEST)
#define	ACT_CONF	(ACTION)
#define	READ_REQ	(READ_ | REQUEST)
#define	READ_CONF	(READ_)
#define	SET_REQ		(SET | REQUEST)
#define	SET_CONF	(SET)

/*
 * the following macro sets fields in the
 * header of a MSG, T_FRAME or R_FRAME
 */
#define	FILL_HDR(h, htype, hid, hrsp)	{ \
						(h)->type = (htype); \
						(h)->id = (hid); \
						(h)->rsp_req = (hrsp); \
					}

/*
 * Prototypes for routines in mem.c
 */
#ifdef LINT_ARGS
  MSF *getm(unsigned short type, unsigned short id,
	    unsigned short rsp_req, unsigned short len);
  T_FRAME *gett(unsigned short hclass, unsigned short layer,
		unsigned short id, unsigned short rsp_req);
  R_FRAME *getr(unsigned short hclass, unsigned short id);
  int relm(HDR *h);
#else
  MSF *getm();
  T_FRAME *gett();
  R_FRAME *getr();
  int relm();
#endif

#endif /* MSG_H */
