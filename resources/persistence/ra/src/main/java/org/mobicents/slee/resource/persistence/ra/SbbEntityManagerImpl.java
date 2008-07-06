package org.mobicents.slee.resource.persistence.ra;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

public class SbbEntityManagerImpl extends
		org.mobicents.slee.resource.persistence.ratype.SbbEntityManager {

	private EntityManager hidden = null;
	private ActivityManipulation am = null;
	private String puName = null;

	public SbbEntityManagerImpl(EntityManager toHide, ActivityManipulation am,
			String puName) {
		this.hidden = toHide;
		this.am = am;
		this.puName = puName;

	}

	EntityManager getManagedActivity() {
		return hidden;
	}

	/**
	 * Return unique ID for this SbbEntityMnager
	 */
	public String getID() {

		return "SbbEntityManagerImpl[" + hidden.toString() + "]";
	}

	public void clear() {
		hidden.clear();

	}

	public void close() {
		hidden.close();
		synchronized (puName) {
			am.removeActivity(this);
		}
	}

	public boolean contains(Object entity) {

		return hidden.contains(entity);
	}

	public Query createNamedQuery(String name) {

		return hidden.createNamedQuery(name);
	}

	public Query createNativeQuery(String sql) {

		return hidden.createNativeQuery(sql);
	}

	public Query createNativeQuery(String sql, Class resultClass) {

		return hidden.createNativeQuery(sql, resultClass);
	}

	public Query createNativeQuery(String sqlString, String resultSetMapping) {

		return hidden.createNativeQuery(sqlString, resultSetMapping);
	}

	public Query createQuery(String qlString) {

		return hidden.createQuery(qlString);
	}

	public Object find(Class resultEntityClass, Object primaryKey) {

		return hidden.find(resultEntityClass, primaryKey);
	}

	public void flush() {
		hidden.flush();

	}

	public Object getDelegate() {

		throw new UnsupportedOperationException(
				"This operation is not supported, undedrlying objects are confidential.");
	}

	public FlushModeType getFlushMode() {

		return hidden.getFlushMode();
	}

	public Object getReference(Class entityClass, Object primaryKey) {

		return hidden.getReference(entityClass, primaryKey);
	}

	public EntityTransaction getTransaction() {
		throw new UnsupportedOperationException(
				"This operation is not supported.");
	}

	public boolean isOpen() {

		return hidden.isOpen();
	}

	public void joinTransaction() {
		throw new UnsupportedOperationException(
				"This operation is not supported.");

	}

	public void lock(Object entity, LockModeType lockMode) {

		hidden.lock(entity, lockMode);
	}

	public Object merge(Object entity) {

		return hidden.merge(entity);
	}

	public void persist(Object entity) {
		hidden.merge(entity);

	}

	public void refresh(Object entity) {
		hidden.refresh(entity);
	}

	public void remove(Object entity) {
		hidden.remove(entity);

	}

	public void setFlushMode(FlushModeType mode) {
		hidden.setFlushMode(mode);
	}

}
