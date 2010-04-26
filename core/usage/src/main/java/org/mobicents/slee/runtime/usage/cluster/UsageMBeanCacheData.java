package org.mobicents.slee.runtime.usage.cluster;

import java.util.Collection;
import java.util.HashSet;

import javax.slee.management.NotificationSource;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.mobicents.cache.CacheData;
import org.mobicents.cache.MobicentsCache;
import org.mobicents.slee.runtime.usage.UsageMBeanData;
import org.mobicents.slee.runtime.usage.UsageParameter;

/**
 * Cache proxy for usage param cached data.
 * 
 * @author baranowb
 * 
 */
public class UsageMBeanCacheData extends CacheData  implements UsageMBeanData {

	private static final String _PARENT_NODE = "usage";
	private static final String _PARAMETER_DATA_ = "set-values";

	public UsageMBeanCacheData(NotificationSource notificationSource,
			String parameterSetName, MobicentsCache cache) {
		super(Fqn.fromElements(_PARENT_NODE, notificationSource,
				parameterSetName), cache);
	}

	public void setParameter(String parameterName, UsageParameter parameter) {
		if (!exists())
			throw new IllegalStateException("Set(" + super.getNodeFqn()
					+ ") does not exist.");
		Node child = super.getNode().getChild(parameterName);
		if (child == null)
			child = super.getNode().addChild(Fqn.fromElements(parameterName));
		// we could have each param under different node and store values in
		// this map, but this way its atomic op per parameter.
		child.put(_PARAMETER_DATA_, parameter);

	}

	public UsageParameter getParameter(String parameterName) {
		if (!exists())
			throw new IllegalStateException("Set(" + super.getNodeFqn()
					+ ") does not exist.");
		Node child = super.getNode().getChild(parameterName);
		if (child == null) {
			return null;
		} else {
			return (UsageParameter) child.get(_PARAMETER_DATA_);

		}

	}

	public void removeParameter(String parameterName) {
		if (!exists())
			throw new IllegalStateException("Set(" + super.getNodeFqn()
					+ ") does not exist.");
		Node child = super.getNode().getChild(parameterName);
		if (child == null) {
			return;
		} else {
			super.getNode().removeChild(parameterName);
			return;
		}
	}
	
	public Collection getParameterNames()
	{
		if (!exists())
			throw new IllegalStateException("Set(" + super.getNodeFqn()
					+ ") does not exist.");
		return super.getNode().getChildrenNames();
	}

	public final static Collection<String> getExistingSets(NotificationSource notificationSource, MobicentsCache mcCache) {
		Fqn usageSetRootFqn = Fqn.fromElements(_PARENT_NODE, notificationSource);
		Node n =mcCache.getJBossCache().getNode(usageSetRootFqn);
		//hmm can it be null? dont think so. but just in case.
		if(n == null)
			return new HashSet<String>();
		
		
		return n.getChildrenNames();
	}
	public static final boolean setExists(NotificationSource notificationSource, String parameterSetName, MobicentsCache mcCache)
	{
		Fqn usageSetRootFqn = Fqn.fromElements(_PARENT_NODE, notificationSource,parameterSetName);
		Node n =mcCache.getJBossCache().getNode(usageSetRootFqn);
		
		return n!=null;
	}
	
}

