/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package net.java.slee.resources.smpp.pdu;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * The SMPP protocol is basically a set of operations, each one taking the form
 * of a request and response Protocol Data Unit (PDU).
 * </p>
 * <p>
 * All the Request/Response implements PDU interface
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public interface PDU extends Serializable {

	/**
	 * <p>
	 * The command_id identifies the SMPP operation e.g. submit_sm,
	 * bind_transmitter etc. The command_id is encoded as a 4-octet integer
	 * value.
	 * </p>
	 * <p>
	 * Command_ids for request PDUs are allocated from a range of numbers;
	 * 0x00000000 to 0x000001FF.
	 * </p>
	 * <p>
	 * Command_ids for response PDUs are allocated from a range of numbers;
	 * 0x80000000 to 0x800001FF.
	 * </p>
	 * <p>
	 * The relationship between the command_id for a request PDU and its
	 * associated response PDU is that bit 31 is cleared for the request and set
	 * for the response. For example, replace_sm has a command_id = 0x00000007
	 * and its’ response PDU replace_sm_resp has a command_id = 0x80000007.
	 * 
	 * </p>
	 * 
	 * @return
	 */
	public int getCommandId();

	/**
	 * <p>
	 * The command_status represents the means by which an ESME or MC sends an
	 * error code to its peer. This field is only relevant in response PDUs.
	 * Thus PDU requests always have this field set to NULL (0x00000000).
	 * </p>
	 * 
	 * <p>
	 * When a response PDU carries a non-NULL command_status field, it is
	 * indicating some form of error or rejection of the original request PDU.
	 * In such circumstances, a PDU body should not be included in the PDU and
	 * the command_length of the PDU should therefore be set to 16 (0x00000010).
	 * However some ESMEs or Message Centers may always include a PDU body
	 * regardless of the command_status being returned. In such circumstances,
	 * the receiving ESME or MC should ignore its contents, based on the
	 * knowledge that the original request failed.
	 * 
	 * </p>
	 * 
	 * @return
	 */
	public int getCommandStatus();

	/**
	 * Each SMPP request PDU has an identifier called a sequence number that is
	 * used to uniquely identify the PDU in the context of its’ originating
	 * entity and the current SMPP session. The resulting response PDU (which
	 * must be returned on the same SMPP session) is expected to mirror the
	 * sequence number of the original request.
	 * 
	 * @return
	 */
	public long getSequenceNum();

	// TLV operations

	/**
	 * Tagged Length Value (TLV) parameters are identified by a tag, length and
	 * value and can be appended to a PDU in any order. The only requirement is
	 * that the PDU’s standard fields are first encoded, and then followed by
	 * the TLV parameters. Otherwise, the PDU decoding by the peer would be
	 * unable to decode the PDU.
	 */
	public void addTLV(Tag tag, Object value) throws TLVNotPermittedException;

	public Object getValue(Tag tag);

	public Object removeTLV(Tag tag);

	public boolean hasTLV(Tag tag);

	public boolean isTLVPermitted(Tag tag);

	public Map<Tag, Object> getAllTLVs();

}
