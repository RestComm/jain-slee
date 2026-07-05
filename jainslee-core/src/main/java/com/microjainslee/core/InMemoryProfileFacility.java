/*
 * micro-jainslee 1.1.0
 *
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * See the LICENSE file at the root of this repository for the full text.
 *
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 * Contact: nhanth87@gmail.com
 */

package com.microjainslee.core;

import com.microjainslee.api.Profile;
import com.microjainslee.api.ProfileAbstractCmp;
import com.microjainslee.api.ProfileAlreadyExistsException;
import com.microjainslee.api.ProfileFacility;
import com.microjainslee.api.ProfileID;
import com.microjainslee.api.ProfileLocalObject;
import com.microjainslee.api.ProfileTable;
import com.microjainslee.api.ProfileTablePort;
import com.microjainslee.api.SLEEException;
import com.microjainslee.api.UnrecognizedProfileTableNameException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the JAIN-SLEE 1.1 {@link ProfileFacility}.
 * <p>
 * Backed by a {@link ConcurrentHashMap} of {@link InMemoryProfileTable}
 * instances keyed by table name. Created lazily on
 * {@link #createProfileTable(String)} or implicitly on
 * {@link #createProfile(String, String, Class)}.
 *
 * <p>The {@link #createProfile(String, String, Class)} method
 * instantiates the user-supplied {@link Profile} subclass via its
 * declared no-arg constructor (with
 * {@link Constructor#setAccessible(boolean) setAccessible} so
 * private/package-private ctors work), then calls
 * {@link ProfileAbstractCmp#bindProfile(String, String)} to bind the
 * row identity before inserting it into the table.
 *
 * <p>This implementation also self-registers itself with
 * {@link ProfileFieldStoreLocator} so the reflective
 * {@code ProfileAccessorInvoker} shadow can locate it. It clears the
 * binding in {@link #shutdown()} for a clean shutdown.
 *
 * <p>Implements the deprecated {@link ProfileTablePort} marker as well
 * (the type is a super-interface of {@link ProfileFacility}) so legacy
 * callers that resolve {@code SbbContext.getProfileFacility()} to a
 * {@code ProfileTablePort} still receive a working handle.
 *
 * @author Tran Nhan (nhanth87)
 */
public final class InMemoryProfileFacility implements ProfileTablePort {

    private static final Logger LOG = LogManager.getLogger(InMemoryProfileFacility.class);

    private final ConcurrentHashMap<String, InMemoryProfileTable> tables =
            new ConcurrentHashMap<String, InMemoryProfileTable>();

    public InMemoryProfileFacility() {
        ProfileFieldStoreLocator.set(this);
        LOG.debug("InMemoryProfileFacility constructed and registered with ProfileFieldStoreLocator");
    }

    /** {@inheritDoc} */
    @Override
    public ProfileTable getProfileTable(String tableName) {
        if (tableName == null) {
            return null;
        }
        return tables.get(tableName);
    }

    /** {@inheritDoc} */
    @Override
    public ProfileLocalObject createProfile(String tableName, String profileName,
                                            Class<? extends Profile> profileClass)
            throws UnrecognizedProfileTableNameException,
                   ProfileAlreadyExistsException,
                   SLEEException {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName is required");
        }
        if (profileName == null) {
            throw new IllegalArgumentException("profileName is required");
        }
        if (profileClass == null) {
            throw new IllegalArgumentException("profileClass is required");
        }
        InMemoryProfileTable table = tables.get(tableName);
        if (table == null) {
            throw new UnrecognizedProfileTableNameException(
                    "No profile table named '" + tableName + "'");
        }
        if (table.containsProfile(profileName)) {
            throw new ProfileAlreadyExistsException(
                    "Profile '" + profileName + "' already exists in table '" + tableName + "'");
        }
        Profile profile;
        try {
            Constructor<? extends Profile> ctor = profileClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            profile = ctor.newInstance();
        } catch (NoSuchMethodException nsme) {
            throw new SLEEException(
                    "Profile class " + profileClass.getName() + " must declare a no-arg constructor",
                    nsme);
        } catch (ReflectiveOperationException roe) {
            throw new SLEEException(
                    "Failed to instantiate profile class " + profileClass.getName() + ": " + roe.getMessage(),
                    roe);
        }
        // Bind the identity BEFORE putting the row into the table so the
        // reflective accessor bridge can resolve the table by
        // profile.getProfileID().getProfileTableName().
        if (profile instanceof ProfileAbstractCmp) {
            ((ProfileAbstractCmp) profile).bindProfile(tableName, profileName);
        }
        if (!table.put(profileName, profile)) {
            throw new ProfileAlreadyExistsException(
                    "Profile '" + profileName + "' already exists in table '" + tableName + "'");
        }
        return new SimpleProfileLocalObject(profile, tableName);
    }

    /** {@inheritDoc} */
    @Override
    public ProfileLocalObject getProfile(ProfileID id) {
        if (id == null) {
            return null;
        }
        InMemoryProfileTable table = tables.get(id.getProfileTableName());
        if (table == null) {
            return null;
        }
        return table.getProfile(id.getProfileName());
    }

    /** {@inheritDoc} */
    @Override
    public void removeProfile(ProfileID id) throws UnrecognizedProfileTableNameException {
        if (id == null) {
            throw new IllegalArgumentException("id is required");
        }
        InMemoryProfileTable table = tables.get(id.getProfileTableName());
        if (table == null) {
            throw new UnrecognizedProfileTableNameException(
                    "No profile table named '" + id.getProfileTableName() + "'");
        }
        table.remove(id.getProfileName());
    }

    /** {@inheritDoc} */
    @Override
    public void createProfileTable(String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("tableName is required");
        }
        InMemoryProfileTable prior = tables.putIfAbsent(tableName, new InMemoryProfileTable(tableName));
        if (prior == null) {
            LOG.debug("Created profile table '{}'", tableName);
        } else {
            LOG.debug("Profile table '{}' already exists; createProfileTable is a no-op", tableName);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void removeProfileTable(String tableName) {
        if (tableName == null) {
            return;
        }
        InMemoryProfileTable removed = tables.remove(tableName);
        if (removed != null) {
            LOG.debug("Removed profile table '{}' ({} rows)", tableName, removed.getProfileCount());
        }
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> getProfileTableNames() {
        return Collections.unmodifiableSet(tables.keySet());
    }

    // -----------------------------------------------------------------
    // Package-private accessors used by the reflective accessor bridge.
    // -----------------------------------------------------------------

    /**
     * Look up a table by name without going through the public
     * {@link #getProfileTable(String)} contract (which may be overridden
     * by embedders).
     */
    public InMemoryProfileTable findTableInternal(String tableName) {
        if (tableName == null) {
            return null;
        }
        return tables.get(tableName);
    }

    /**
     * Drop every table and release the {@link ProfileFieldStoreLocator}
     * binding. Tests and shutdown hooks call this for a clean state.
     */
    public void shutdown() {
        tables.clear();
        ProfileFieldStoreLocator.set(null);
        ProfileFieldStoreLocator.clearGlobal(this);
        LOG.debug("InMemoryProfileFacility shut down");
    }
}