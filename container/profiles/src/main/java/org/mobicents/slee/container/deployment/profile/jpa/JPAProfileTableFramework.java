/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.deployment.profile.jpa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.ValidationMode;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTableAlreadyExistsException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernatePersistence;
import org.jboss.as.jpa.config.PersistenceUnitMetadataImpl;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.management.ProfileManagementImpl;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

/**
 * JPAProfileTableFramework.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class JPAProfileTableFramework {

    private static Logger logger = Logger.getLogger(JPAProfileTableFramework.class);
    private final ProfileManagementImpl sptm;
    private final SleeTransactionManager sleeTransactionManager;
    private final Configuration configuration;
    private EntityManagerFactory entityManagerFactory;
    private boolean isInitialized = false;

    public JPAProfileTableFramework(ProfileManagementImpl sptm, SleeTransactionManager sleeTransactionManager, Configuration configuration) {
        this.sptm = sptm;
        this.sleeTransactionManager = sleeTransactionManager;
        this.configuration = configuration;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void createPersistenceUnit() {
        try {
            HibernatePersistence hp = new HibernatePersistence();
            PersistenceUnitMetadataImpl pumd = new PersistenceUnitMetadataImpl();
            pumd.setTransactionType(PersistenceUnitTransactionType.JTA);
            pumd.setValidationMode(ValidationMode.NONE);

            // FIXME: Do we need this property here
            //pumd.setPersistenceProviderClassName("org.hibernate.ejb.HibernatePersistence");
            //pumd.setPersistenceProviderClassName("org.hibernate.jpa.HibernatePersistenceProvider");
            pumd.setJtaDataSourceName(configuration.getHibernateDatasource());
            pumd.setExcludeUnlistedClasses(false);

            boolean persistProfiles = configuration.isPersistProfiles();

            Properties properties = new Properties();

            // Remote invocations via JNDI?
            // https://docs.jboss.org/author/display/AS71/Remote+EJB+invocations+via+JNDI+-+EJB+client+API+or+remote-naming+project
            //properties.setProperty("hibernate.jndi.java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
            //properties.setProperty(Environment.JNDI_PREFIX+"."+ Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

            properties.setProperty(Environment.JNDI_CLASS, "org.jboss.as.naming.InitialContextFactory");
            //properties.setProperty("hibernate.jndi.java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
            //properties.setProperty(Environment.JNDI_PREFIX+"."+Context.INITIAL_CONTEXT_FACTORY, "org.jboss.as.naming.InitialContextFactory");

            properties.setProperty(Environment.HBM2DDL_AUTO, persistProfiles ? "update" : "create-drop");
            properties.setProperty(Environment.DIALECT, configuration.getHibernateDialect());
            pumd.setPersistenceUnitName("restcomm-profile-persistence-pu");

            List<String> classes = new ArrayList<String>();
            classes.add(JPAProfileTable.class.getName());
            pumd.setManagedClassNames(classes);

            properties.setProperty(Environment.DATASOURCE, pumd.getJtaDataSourceName());

            if(pumd.getJtaDataSourceName() != null) {
                pumd.setJtaDataSource((javax.sql.DataSource) new InitialContext().lookup(pumd.getJtaDataSourceName()));
            }
            else if (pumd.getTransactionType() == PersistenceUnitTransactionType.JTA) {
                throw new RuntimeException("Specification violation [EJB3 JPA 6.2.1.2] - "
                        + "You have not defined a jta-data-source for a JTA enabled persistence context named: "
                        + pumd.getPersistenceUnitName());
            }

            // FIXME: Do we need this property here (Environment.TRANSACTION_STRATEGY)
            //properties.setProperty(Environment.TRANSACTION_STRATEGY, "org.hibernate.transaction.JTATransactionFactory");
            //properties.setProperty(Environment.TRANSACTION_STRATEGY, "org.hibernate.transaction.CMTTransactionFactory");
            //properties.setProperty(Environment.TRANSACTION_STRATEGY, "org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory");

            // SEE: https://developer.jboss.org/thread/172307
            //properties.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY, "org.hibernate.transaction.JBossTransactionManagerLookup");

            //properties.setProperty(Environment.JTA_PLATFORM, "org.hibernate.service.jta.platform.spi.JtaPlatform");
            //properties.setProperty(Environment.JTA_PLATFORM, "org.hibernate.service.jta.platform.internal.JBossStandAloneJtaPlatform");
            properties.setProperty(Environment.JTA_PLATFORM, "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform");

            // FIXME: Do we need this property here (Environment.CONNECTION_PROVIDER)
            //properties.setProperty(Environment.CONNECTION_PROVIDER, "org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider");
            //properties.setProperty(Environment.CONNECTION_PROVIDER, "org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl");

            properties.setProperty(Environment.DIALECT, configuration.getHibernateDialect());
            // FIXME: Should be Environment.JACC_CONTEXTID but it's
            // hibernate.jacc_context_id vs hibernate.jacc.ctx.id. Bug?
            properties.setProperty("hibernate.jacc.ctx.id", "persistence.xml");
            properties.setProperty(Environment.CACHE_REGION_PREFIX, "persistence.unit:unitName=#" + pumd.getPersistenceUnitName());

            properties.setProperty(Environment.SESSION_FACTORY_NAME, "persistence.unit:unitName=#" + pumd.getPersistenceUnitName());
            //properties.setProperty(Environment.SESSION_FACTORY_NAME, "");

            // SessionFactory binding
            // WARN [org.hibernate.internal.SessionFactoryRegistry] HHH000277: Could not bind factory to JNDI:
            // org.hibernate.service.jndi.JndiException: Error performing bind [persistence.unit:unitName=#JSLEEProfiles...]
            // java.lang.UnsupportedOperationException: JBAS011859: Naming context is read-only
            properties.setProperty(Environment.SESSION_FACTORY_NAME_IS_JNDI, "false");

            // Validation?
            properties.setProperty(Environment.CHECK_NULLABILITY, "true");

            properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER, "false");
            properties.setProperty(Environment.BYTECODE_PROVIDER, "javassist");
            properties.setProperty(Environment.STATEMENT_BATCH_SIZE, "0");
            properties.setProperty(Environment.SHOW_SQL, "false");
            properties.setProperty(Environment.FORMAT_SQL, "false");

            pumd.setProperties(properties);
            pumd.setClassLoader(Thread.currentThread().getContextClassLoader());
            pumd.setPersistenceUnitRootUrl(ClassLoader.getSystemResource("."));
            pumd.setJarFiles(new ArrayList<String>());

            Transaction tx = null;

            try {
                tx = sleeTransactionManager.suspend();
                entityManagerFactory = hp.createContainerEntityManagerFactory(pumd, null);
                isInitialized = true;
            } catch (Exception e) {
                logger.error("Failure creating Persistence Unit for profile persistency.", e);
            } catch (Throwable t) {
                logger.error("Failure creating Persistence Unit for profile persistency.", t);
            } finally {
                if (tx != null)
                    sleeTransactionManager.resume(tx);
            }
        } catch (Exception e) {
            logger.error("Failure creating Persistence Unit for profile persistency.", e);
        }
    }

    public void storeProfileTable(ProfileTableImpl profileTable) {

        if (logger.isTraceEnabled()) {
            logger.trace("Storing into backend storage profile table " + profileTable);
        }

        EntityManager em = null;

        try {
            em = entityManagerFactory.createEntityManager();
            em.persist(new JPAProfileTable(profileTable));
            em.flush();
        } finally {
            if (em != null) {
                em.close();
                em = null;
            }
        }
    }

    public void removeProfileTable(String profileTableName) {
        if (logger.isTraceEnabled()) {
            logger.trace("Removing from backend storage profile table" + profileTableName);
        }

        EntityManager em = null;

        try {
            em = entityManagerFactory.createEntityManager();

            Query q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_DELETE_TABLE)
                    .setParameter("profileTableName", profileTableName);

            q.executeUpdate();
        } finally {
            if (em != null) {
                em.close();
                em = null;
            }
        }
    }

    public void renameProfileTable(String oldProfileTableName, String newProfileTableName) {
        if (logger.isTraceEnabled()) {
            logger.trace("Renaming " + oldProfileTableName + " to " + newProfileTableName + " in backend storage.");
        }

        EntityManager em = null;

        try {
            em = entityManagerFactory.createEntityManager();

            Query q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_RENAME_TABLE)
                    .setParameter("newProfileTableName", newProfileTableName)
                    .setParameter("oldProfileTableName", oldProfileTableName);

            q.executeUpdate();
        } finally {
            if (em != null) {
                em.close();
                em = null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public ProfileSpecificationID getProfileSpecificationID(String profileTableName) {

        EntityManager em = null;

        try {

            em = entityManagerFactory.createEntityManager();

            Query q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_FIND_TABLE_BY_NAME)
                    .setParameter("profileTableName", profileTableName);

            List<JPAProfileTable> tables = q.getResultList();
            if (tables.isEmpty()) {
                return null;
            } else {
                // single result only, since profile table name is primary key
                return tables.get(0).getProfileSpecId();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    public void loadProfileTables(ProfileSpecificationComponent component) throws SLEEException, ProfileTableAlreadyExistsException {

        if (logger.isTraceEnabled()) {
            logger.trace("Loading from backend storage profile tables for " + component.getProfileSpecificationID());
        }

        for (String profileTableName : _getProfileTableNames(component.getProfileSpecificationID())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Profile Table named " + profileTableName + " found in JPA data source.");
            }
            synchronized (this) {
                // ask for the profile table, the manager will build the local object
                try {
                    sptm.loadProfileTableLocally(profileTableName, component);
                } catch (Throwable e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Unable to load profile table named " + profileTableName, e);
                    }
                }
            }
        }
    }

    /**
     * Retrieves profile table names from database
     *
     * @return
     */
    public Set<String> getProfileTableNames() {
        return _getProfileTableNames(null);
    }

    /**
     * Retrieves profile table names from database, which are bound to the specified {@link ProfileSpecificationID}
     *
     * @return
     */
    public Set<String> getProfileTableNames(ProfileSpecificationID profileSpecificationID) {
        return _getProfileTableNames(profileSpecificationID);
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public Set<String> _getProfileTableNames(ProfileSpecificationID profileSpecificationID) {

        Set<String> resultSet = null;

        EntityManager em = null;

        try {

            if (!isInitialized) {
                createPersistenceUnit();
            }

            em = entityManagerFactory.createEntityManager();

            Query q = null;
            if (profileSpecificationID == null) {
                q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_SELECT_ALL_TABLES);
            } else {
                q = em.createNamedQuery(JPAProfileTable.JPA_NAMED_QUERY_SELECT_TABLES_BY_PROFILE_SPEC_ID)
                        .setParameter("profileSpecId", profileSpecificationID);
            }
            List<JPAProfileTable> resultList = q.getResultList();
            if (resultList.isEmpty()) {
                resultSet = Collections.emptySet();
            } else {
                resultSet = new HashSet<String>();
                for (JPAProfileTable result : resultList) {
                    resultSet.add(result.getProfileTableName());
                }
            }
        } finally {
            if (em != null) {
                em.close();
                em = null;
            }
        }

        return resultSet;
    }

    /**
     * @return
     */
    public Configuration getConfiguration() {
        return configuration;
    }

}
