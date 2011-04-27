/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.jivesoftware.smackx.muc;

/**
 * Default implementation of the ParticipantStatusListener interface.<p>
 *
 * This class does not provide any behavior by default. It just avoids having
 * to implement all the inteface methods if the user is only interested in implementing
 * some of the methods.
 * 
 * @author Gaston Dombiak
 */
public class DefaultParticipantStatusListener implements ParticipantStatusListener {

    public void joined(String participant) {
    }

    public void left(String participant) {
    }

    public void kicked(String participant, String actor, String reason) {
    }

    public void voiceGranted(String participant) {
    }

    public void voiceRevoked(String participant) {
    }

    public void banned(String participant, String actor, String reason) {
    }

    public void membershipGranted(String participant) {
    }

    public void membershipRevoked(String participant) {
    }

    public void moderatorGranted(String participant) {
    }

    public void moderatorRevoked(String participant) {
    }

    public void ownershipGranted(String participant) {
    }

    public void ownershipRevoked(String participant) {
    }

    public void adminGranted(String participant) {
    }

    public void adminRevoked(String participant) {
    }

    public void nicknameChanged(String participant, String newNickname) {
    }

}
