package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.And;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Compare;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.HasPrefix;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.LongestPrefixMatch;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Not;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Or;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.RangeMatch;

/**
 * This class is agregation of query expresion elements defined in: slee.1.1
 * specs chapter 10.20.2. Generaly it creates expression tree from passed
 * arguments to match one defined in xml descriptor. This is an abstraction from plain translation from xml structure <br>
 * 
 * Start time:11:10:10 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MQueryExpression {

	private MQueryExpressionType type;
	private MQueryExpressionType parentType;

	//for not, or, and
	private ArrayList<MQueryExpression> childExpressions = new ArrayList<MQueryExpression>();
	
	//For others
	private MCompare compare;
	private MLongestPrefixMatch longestPrefixMatch;
	private MHasPrefix hasPrefix;
	private MRangeMatch rangeMatch;
	
	/**
	 * Constructs this query expression tree
	 * 
	 * @param operator
	 */
	public MQueryExpression(Object operator)
	{
		if(operator instanceof Compare)
		{
			this.parentType = null;
			this.type = MQueryExpressionType.Compare;
			this.compare = new MCompare((Compare) operator);
		}
		else if(operator instanceof HasPrefix)
		{
			this.parentType = null;
			this.type = MQueryExpressionType.HasPrefix;
			this.hasPrefix = new MHasPrefix( (HasPrefix) operator);
		}
		else if(operator instanceof LongestPrefixMatch)
		{
			this.parentType = null;
			this.type = MQueryExpressionType.LongestPrefixMatch;
			this.longestPrefixMatch = new MLongestPrefixMatch( (LongestPrefixMatch) operator);
		}
		else if(operator instanceof RangeMatch)
		{
			this.parentType = null;
			this.type = MQueryExpressionType.RangeMatch;
			this.rangeMatch = new MRangeMatch( (RangeMatch) operator);
		}
		else if(operator instanceof Or)
		{
			//here we will have atleast two elements
			Or or = (Or)operator;
			this.parentType = null;
			this.type = MQueryExpressionType.Or;

			for(Object childRaw : or.getCompareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot())
			{
				MQueryExpression child = new MQueryExpression(childRaw);
				child.parentType = this.type;
				this.childExpressions.add(child);
			}
			
		}
		else if(operator instanceof And)
		{
			//here we will have atleast two elements
			And and = (And)operator;
			this.parentType = null;
			this.type = MQueryExpressionType.And;

			for(Object childRaw  :  and.getCompareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot())
			{
				MQueryExpression child = new MQueryExpression(childRaw);
				child.parentType = this.type;
				this.childExpressions.add(child);
			}
		}
		else if(operator instanceof Not)
		{
			//Not should have one, we will get one, this is xml validation part
			Not not = (Not)operator;
			this.parentType = null;
			this.type = MQueryExpressionType.Not;

			for(Object childRaw : not.getCompareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot())
			{
				MQueryExpression child = new MQueryExpression(childRaw);
				child.parentType = this.type;
				this.childExpressions.add(child);
			}
		}
		else
			throw new IllegalArgumentException("Can not match query expression element to any known: " + operator);
		
	}
	
	public MQueryExpressionType getType()
	{
		return type;
	}
	
	public MQueryExpressionType getParentType()
	{
		return parentType;
	}
	
	public MCompare getCompare()
	{
		return compare;
	}
	
	public MLongestPrefixMatch getLongestPrefixMatch()
	{
		return longestPrefixMatch;
	}
	
	public MHasPrefix getHasPrefix()
	{
		return hasPrefix;
	}
	
	public MRangeMatch getRangeMatch()
	{
		return rangeMatch;
	}
	
	public MQueryExpression getNot()
	{
	  for(MQueryExpression expr : this.childExpressions)
	  {
	    if(expr.getType() == MQueryExpressionType.Not);
	      return expr;
	  }
	  
	  return null;
	}
	
	public List<MQueryExpression> getAnd()
	{
		return this.childExpressions;
	}
	
	public List<MQueryExpression> getOr()
	{
		return this.childExpressions;
	}

}
