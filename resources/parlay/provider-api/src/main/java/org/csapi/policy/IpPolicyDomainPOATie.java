package org.csapi.policy;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPolicyDomain"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPolicyDomainPOATie
	extends IpPolicyDomainPOA
{
	private IpPolicyDomainOperations _delegate;

	private POA _poa;
	public IpPolicyDomainPOATie(IpPolicyDomainOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPolicyDomainPOATie(IpPolicyDomainOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.policy.IpPolicyDomain _this()
	{
		return org.csapi.policy.IpPolicyDomainHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyDomain _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyDomainHelper.narrow(_this_object(orb));
	}
	public IpPolicyDomainOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPolicyDomainOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public void removeGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeGroup(groupName);
	}

	public org.csapi.policy.IpPolicyDomain getParentDomain() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getParentDomain();
	}

	public org.csapi.policy.IpPolicyRule getRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getRule(ruleName);
	}

	public void setAttributes(org.csapi.TpAttribute[] targetAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttributes(targetAttributes);
	}

	public int getVariableSetCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getVariableSetCount();
	}

	public void removeDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeDomain(domainName);
	}

	public org.csapi.policy.IpPolicyDomain getDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getDomain(domainName);
	}

	public void setVariable(java.lang.String variableSetName, org.csapi.TpAttribute variable) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.setVariable(variableSetName,variable);
	}

	public void createVariableSet(java.lang.String variableSetName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.createVariableSet(variableSetName);
	}

	public org.csapi.TpAttribute[] getAttributes(java.lang.String[] attributeNames) throws org.csapi.TpCommonExceptions
	{
		return _delegate.getAttributes(attributeNames);
	}

	public int getEventDefinitionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getEventDefinitionCount();
	}

	public org.csapi.policy.IpPolicyIterator getVariableSetIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getVariableSetIterator();
	}

	public org.csapi.policy.IpPolicyDomain createDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createDomain(domainName);
	}

	public void removeEventDefinition(java.lang.String eventDefinitionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeEventDefinition(eventDefinitionName);
	}

	public int getGroupCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getGroupCount();
	}

	public int createNotification(org.csapi.policy.IpAppPolicyDomain appPolicyDomain, java.lang.String[] events) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createNotification(appPolicyDomain,events);
	}

	public void removeRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeRule(ruleName);
	}

	public void setAttribute(org.csapi.TpAttribute targetAttribute) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttribute(targetAttribute);
	}

	public void removeVariableSet(java.lang.String variableSetName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeVariableSet(variableSetName);
	}

	public org.csapi.policy.IpPolicyIterator getEventDefinitionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getEventDefinitionIterator();
	}

	public org.csapi.policy.IpPolicyIterator getRuleIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getRuleIterator();
	}

	public int getRuleCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getRuleCount();
	}

	public int getDomainCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getDomainCount();
	}

	public org.csapi.TpAttribute getVariable(java.lang.String variableSetName, java.lang.String variableName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getVariable(variableSetName,variableName);
	}

	public org.csapi.policy.IpPolicyEventDefinition getEventDefinition(java.lang.String eventDefinitionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getEventDefinition(eventDefinitionName);
	}

	public org.csapi.policy.IpPolicyIterator getDomainIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getDomainIterator();
	}

	public org.csapi.policy.IpPolicyGroup getGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getGroup(groupName);
	}

	public org.csapi.TpAttribute[] getVariableSet(java.lang.String variableSetName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getVariableSet(variableSetName);
	}

	public void destroyNotification(int assignmentID, java.lang.String[] events) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.destroyNotification(assignmentID,events);
	}

	public org.csapi.policy.IpPolicyRule createRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createRule(ruleName);
	}

	public org.csapi.policy.IpPolicyIterator getGroupIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getGroupIterator();
	}

	public org.csapi.policy.IpPolicyGroup createGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createGroup(groupName);
	}

	public void generateEvent(java.lang.String eventDefinitionName, org.csapi.TpAttribute[] attributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.generateEvent(eventDefinitionName,attributes);
	}

	public org.csapi.policy.IpPolicyEventDefinition createEventDefinition(java.lang.String eventDefinitionName, java.lang.String[] requiredAttributes, java.lang.String[] optionalAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createEventDefinition(eventDefinitionName,requiredAttributes,optionalAttributes);
	}

	public org.csapi.TpAttribute getAttribute(java.lang.String attributeName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getAttribute(attributeName);
	}

}
