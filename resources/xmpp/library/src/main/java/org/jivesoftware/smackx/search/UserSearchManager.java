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

package org.jivesoftware.smackx.search;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * The UserSearchManager is a facade built upon Jabber Search Services (JEP-055) to allow for searching
 * repositories on a Jabber Server. This implementation allows for transparency of implementation of
 * searching (DataForms or No DataForms), but allows the user to simply use the DataForm model for both
 * types of support.
 * <pre>
 * XMPPConnection con = new XMPPConnection("jabber.org");
 * con.login("john", "doe");
 * UserSearchManager search = new UserSearchManager(con, "users.jabber.org");
 * Form searchForm = search.getSearchForm();
 * Form answerForm = searchForm.createAnswerForm();
 * answerForm.setAnswer("last", "DeMoro");
 * ReportedData data = search.getSearchResults(answerForm);
 * // Use Returned Data
 * </pre>
 *
 * @author Derek DeMoro
 */
public class UserSearchManager {

    private XMPPConnection con;
    private UserSearch userSearch;

    /**
     * Creates a new UserSearchManager.
     *
     * @param con           the XMPPConnection to use.
     */
    public UserSearchManager(XMPPConnection con) {
        this.con = con;
        userSearch = new UserSearch();
    }

    /**
     * Returns the form to fill out to perform a search.
     *
     * @param searchService the search service to query.
     * @return the form to fill out to perform a search.
     * @throws XMPPException thrown if a server error has occurred.
     */
    public Form getSearchForm(String searchService) throws XMPPException {
        return userSearch.getSearchForm(con, searchService);
    }

    /**
     * Submits a search form to the server and returns the resulting information
     * in the form of <code>ReportedData</code>
     *
     * @param searchForm the <code>Form</code> to submit for searching.
     * @param searchService the name of the search service to use.
     * @return the ReportedData returned by the server.
     * @throws XMPPException thrown if a server error has occurred.
     */
    public ReportedData getSearchResults(Form searchForm, String searchService) throws XMPPException {
        return userSearch.sendSearchForm(con, searchForm, searchService);
    }


    /**
     * Returns a collection of search services found on the server.
     *
     * @return a Collection of search services found on the server.
     * @throws XMPPException thrown if a server error has occurred.
     */
    public Collection getSearchServices() throws XMPPException {
        List searchServices = new ArrayList();
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(con);
        DiscoverItems items = discoManager.discoverItems(con.getServiceName());
        for (Iterator it = items.getItems(); it.hasNext();) {
            DiscoverItems.Item item = (DiscoverItems.Item) it.next();
            try {
                DiscoverInfo info = discoManager.discoverInfo(item.getEntityID());
                if (info.containsFeature("jabber:iq:search")) {
                    searchServices.add(item.getEntityID());
                }
            }
            catch (XMPPException e) {
                // No info found.
                break;
            }
        }
        return searchServices;
    }
}
