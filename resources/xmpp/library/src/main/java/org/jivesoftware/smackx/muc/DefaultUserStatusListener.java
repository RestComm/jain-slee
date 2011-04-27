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
 * Default implementation of the UserStatusListener interface.<p>
 *
 * This class does not provide any behavior by default. It just avoids having
 * to implement all the inteface methods if the user is only interested in implementing
 * some of the methods.
 * 
 * @author Gaston Dombiak
 */
public class DefaultUserStatusListener implements UserStatusListener {

    public void kicked(String actor, String reason) {
    }

    public void voiceGranted() {
    }

    public void voiceRevoked() {
    }

    public void banned(String actor, String reason) {
    }

    public void membershipGranted() {
    }

    public void membershipRevoked() {
    }

    public void moderatorGranted() {
    }

    public void moderatorRevoked() {
    }

    public void ownershipGranted() {
    }

    public void ownershipRevoked() {
    }

    public void adminGranted() {
    }

    public void adminRevoked() {
    }

}
