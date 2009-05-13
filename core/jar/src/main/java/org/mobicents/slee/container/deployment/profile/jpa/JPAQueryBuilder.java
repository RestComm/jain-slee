package org.mobicents.slee.container.deployment.profile.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.slee.profile.query.And;
import javax.slee.profile.query.Equals;
import javax.slee.profile.query.GreaterThan;
import javax.slee.profile.query.GreaterThanOrEquals;
import javax.slee.profile.query.HasPrefix;
import javax.slee.profile.query.LessThan;
import javax.slee.profile.query.LessThanOrEquals;
import javax.slee.profile.query.LongestPrefixMatch;
import javax.slee.profile.query.Not;
import javax.slee.profile.query.NotEquals;
import javax.slee.profile.query.Or;
import javax.slee.profile.query.QueryExpression;
import javax.slee.profile.query.RangeMatch;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MCompare;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MHasPrefix;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MLongestPrefixMatch;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQuery;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQueryExpression;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQueryExpressionType;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQueryOptions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQueryParameter;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MRangeMatch;

/**
 * 
 * JPAQueryBuilder.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class JPAQueryBuilder {

  private static Logger logger = Logger.getLogger(JPAQueryBuilder.class);

  private ProfileSpecificationComponent psc;

  public JPAQueryBuilder(ProfileSpecificationComponent psc)
  {
    this.psc = psc;
  }

  // ####################
  // #  STATIC QUERIES  #
  // ####################

  private static HashMap<String, QueryWrapper> queriesMap = new HashMap<String, QueryWrapper>();

  public void parseStaticQueries()
  {
    List<MQuery> queries = this.psc.getDescriptor().getQueryElements();

    for(MQuery query : queries)
    {
      MQueryOptions queryOptions = query.getQueryOptions();

      boolean queryIsReadOnly = false;
      long queryMaxMatches = -1;
      if(queryOptions != null)
      {
        queryIsReadOnly = query.getQueryOptions().isReadOnly();
        queryMaxMatches = query.getQueryOptions().getMaxMatches();
      }

      logger.info("Query :: Name[" + query.getName() + "], Options[" + (queryOptions == null ? "" : ("Read-Only[" + queryIsReadOnly + "], Max-Matches[" + queryMaxMatches + "]")) + "]" );

      HashMap<String, Integer> queryParameters = new HashMap<String, Integer>();

      int i = 0;
      for(MQueryParameter parameter : query.getQueryParameters())
      {
        i++;
        logger.info("Parameter :: Index[" + i + "], Name[" + parameter.getName() + "], Type[" + parameter.getType() + "]");
        queryParameters.put( parameter.getName(), i );
      }

      long s = System.currentTimeMillis();
      String sqlQuery = "SELECT " + (queryMaxMatches != -1 ? ("TOP " + queryMaxMatches) : "") + " * " + getExpression( query.getQueryExpression(), queryParameters, 0 ) + " AND safeProfileName != ''";
      logger.info("Query :: SQL[" + sqlQuery + "]");
      long e = System.currentTimeMillis();
      logger.info("Query :: Parsed in " + (e-s) + "ms.");

      queriesMap.put( query.getName(), new QueryWrapper(sqlQuery, queryMaxMatches, queryIsReadOnly) );
    }
  }

  private String getExpression(MQueryExpression expression, HashMap<String, Integer> queryParameters, int deepness)
  {
    return getExpression( expression, queryParameters, deepness, null );
  }

  private String getExpression(MQueryExpression expression, HashMap<String, Integer> queryParameters, int deepness, String sqlQuery)
  {
    List<MQueryExpression> expressions = new ArrayList<MQueryExpression>();
    expressions.add(expression);
    return getExpression( expressions, queryParameters, deepness, sqlQuery );
  } 

  private String getExpression(List<MQueryExpression> expressions, HashMap<String, Integer> queryParameters, int deepness, String sqlQuery)
  {
    if(sqlQuery == null || sqlQuery.length() == 0)
      sqlQuery = "FROM <CLASS> WHERE ";

    String prefix = "  ";

    for(int i = 0; i < deepness; i++)
      prefix += "  ";

    for(MQueryExpression expression : expressions)
    {
      switch(expression.getType())
      {
      case And:
        logger.info( prefix + "+--> And" );
        sqlQuery += " ( "; 
        sqlQuery = getExpression( expression.getAnd(), queryParameters, deepness+1, sqlQuery );
        sqlQuery = replaceLast( sqlQuery, getLogicalOperator(expression.getType()), " ) ");
        break;
      case Compare:
        MCompare compare = expression.getCompare();
        logger.info( prefix + "+--> Compare[" + compare.getAttributeName() + " " + compare.getOp() + " " + getNotNull(queryParameters, compare.getAttributeName(), compare.getParameter(), compare.getValue()) + "]" );
        sqlQuery += "C" + compare.getAttributeName() + getSQLOperator(compare.getOp()) + getNotNull(queryParameters, compare.getAttributeName(), compare.getParameter(), compare.getValue());
        break;
      case HasPrefix:
        MHasPrefix hasPrefix = expression.getHasPrefix();
        logger.info( prefix + "+--> HasPrefix[" + hasPrefix.getAttributeName() + " => " + getNotNull(queryParameters, hasPrefix.getAttributeName(), hasPrefix.getParameter(), hasPrefix.getValue()) + "]" );
        sqlQuery += "C" + hasPrefix.getAttributeName() + " LIKE CONCAT(" + getNotNull(queryParameters, hasPrefix.getAttributeName(), hasPrefix.getParameter(), hasPrefix.getValue()) + ", '%') ";
        break;
      case LongestPrefixMatch:
        MLongestPrefixMatch longestPrefixMatch = expression.getLongestPrefixMatch();
        logger.info( prefix + "+--> LongestPrefixMatch[" + longestPrefixMatch.getAttributeName() + " >=< " + getNotNull(queryParameters, longestPrefixMatch.getAttributeName(), longestPrefixMatch.getParameter(), longestPrefixMatch.getValue()) + "]" );
        sqlQuery += getNotNull(queryParameters, longestPrefixMatch.getAttributeName(), longestPrefixMatch.getParameter(), longestPrefixMatch.getValue()) + " LIKE CONCAT(" + "C" + longestPrefixMatch.getAttributeName() + ", '%')";
        break;
      case Not:
        logger.info( prefix + "+--> Not" );
        sqlQuery += " NOT ( ";
        sqlQuery = getExpression( expression.getNot(), queryParameters, deepness+1, sqlQuery );
        sqlQuery = replaceLast( sqlQuery, getLogicalOperator(expression.getType()), " ) ");
        break;
      case Or:
        logger.info( prefix + "+--> Or" );
        sqlQuery += " ( "; 
        sqlQuery = getExpression( expression.getOr(), queryParameters, deepness+1, sqlQuery );
        sqlQuery = replaceLast( sqlQuery, getLogicalOperator(expression.getType()), " ) ");
        break;
      case RangeMatch:
        MRangeMatch rangeMatch = expression.getRangeMatch();
        logger.info( prefix + "+--> RangeMatch[" + rangeMatch.getAttributeName() + " => (" + getNotNull(queryParameters, rangeMatch.getAttributeName(), rangeMatch.getFromParameter(), rangeMatch.getFromValue()) + " : " + getNotNull(queryParameters, rangeMatch.getAttributeName(), rangeMatch.getToParameter(), rangeMatch.getToValue()) + ") ]" );
        sqlQuery += "C" + rangeMatch.getAttributeName() + " >= " + getNotNull(queryParameters, rangeMatch.getAttributeName(), rangeMatch.getFromParameter(), rangeMatch.getFromValue()) + " AND " + "C" + rangeMatch.getAttributeName() + " <= " + getNotNull(queryParameters, rangeMatch.getAttributeName(), rangeMatch.getToParameter(), rangeMatch.getToValue());
        break;
      }

      sqlQuery += getLogicalOperator(expression.getParentType());  
    }

    return sqlQuery;
  }

  private String getNotNull(HashMap<String, Integer> queryParameters, String attributeName, String...strings)
  {
    for(String s : strings)
      if( s != null)
      {
        if(queryParameters.containsKey(s))
        {
          return "?" + queryParameters.get(s);
        }
        else
        {
          Class retType = Integer.class;

          try
          {
            retType = this.psc.getProfileCmpInterfaceClass().getMethod("get" + attributeName.replace(attributeName.charAt(0), Character.toUpperCase(attributeName.charAt(0)))).getReturnType();
          }
          catch ( Exception e ) {
            e.printStackTrace();
          }

          if(retType == String.class || retType == Character.class || retType == char.class)
          {
            return "'" + s + "'";
          }

          return s;
        }
      }

    return null;
  }

  private String getSQLOperator(String op)
  {
    // An op attribute. 
    // This attribute identifies the binary operator to apply to the Profile attribute 
    // value. It can be one of the following values: “equals”, “not-equals”, “less-than”, 
    // “less-than-or-equals”, “greater-than”, or “greater-than-or-equals”. If the Java 
    // type of the Profile attribute is boolean or java.lang.Boolean then only 
    // the “equals”, or “not-equals” operator are allowed.

    String sqlOperator = null;

    if(op.equals("equals"))
    {
      sqlOperator = " = ";
    }
    else if(op.equals("not-equals"))
    {
      sqlOperator = " != ";
    }
    else if(op.equals("less-than"))
    {
      sqlOperator = " < ";
    }
    else if(op.equals("less-than-or-equals"))
    {
      sqlOperator = " <= ";
    }
    else if(op.equals("greater-than"))
    {
      sqlOperator = " > ";
    }
    else if(op.equals("greater-than-or-equals"))
    {
      sqlOperator = " >= ";
    }

    return sqlOperator;
  }

  private String getLogicalOperator(MQueryExpressionType expType)
  {
    if (expType == null)
      return "";

    switch (expType)
    {
    case And:
      return " AND ";
    case Or:
      return " OR ";
    case Not:
      return " NOT ";
    default:
      return "";
    }
  }
  
  public static QueryWrapper getQuery(String queryName)
  {
    return queriesMap.get(queryName);
  }

  // #####################
  // #  DYNAMIC QUERIES  #
  // #####################

  public static QueryWrapper parseDynamicQuery(QueryExpression query)
  {
    ArrayList<Object> params = new ArrayList<Object>();

    long s = System.currentTimeMillis();
    String sqlQuery = "SELECT * " + parseDynamicQuery( query, 0, null, params ) + " AND safeProfileName != ''";
    logger.info("Query :: SQL[" + sqlQuery + "]");
    long e = System.currentTimeMillis();
    logger.info("Query :: Parsed in " + (e-s) + "ms.");

    return new QueryWrapper( sqlQuery, params );
  }

  private static String parseDynamicQuery(QueryExpression query, int deepness, String sqlQuery, ArrayList<Object> params)
  {
    if(sqlQuery == null || sqlQuery.length() == 0)
      sqlQuery = "FROM <CLASS> WHERE ";

    String prefix = "  ";

    for(int i = 0; i < deepness; i++)
      prefix += "  ";

    if(query instanceof Equals)
    {
      Equals equals = (Equals)query;

      logger.info( prefix + "+--> Equals[" + equals.getAttributeName() + " = " + equals.getAttributeValue() + "]" );
      //sqlQuery += " C" + equals.getAttributeName() + " = " + escapeAttributeValue(equals.getAttributeValue()) + " ";
      params.add( equals.getAttributeValue() );
      sqlQuery += " C" + equals.getAttributeName() + " = ?" + params.size() + " ";
    }
    else if (query instanceof NotEquals)
    {
      NotEquals notEquals = (NotEquals)query;

      logger.info( prefix + "+--> NotEquals[" + notEquals.getAttributeName() + " != " + notEquals.getAttributeValue() + "]" );
      //sqlQuery += " C" + notEquals.getAttributeName() + " != " + escapeAttributeValue(notEquals.getAttributeValue()) + " ";
      params.add( notEquals.getAttributeValue() );
      sqlQuery += " C" + notEquals.getAttributeName() + " != ?" + params.size() + " ";
    }
    else if (query instanceof LessThan)
    {
      LessThan lessThan = (LessThan)query;

      logger.info( prefix + "+--> LessThan[" + lessThan.getAttributeName() + " < " + lessThan.getAttributeValue() + "]" );
      //sqlQuery += " C" + lessThan.getAttributeName() + " < " + escapeAttributeValue(lessThan.getAttributeValue()) + " ";
      params.add( lessThan.getAttributeValue() );
      sqlQuery += " C" + lessThan.getAttributeName() + " < ?" + params.size() + " ";
    }
    else if (query instanceof LessThanOrEquals)
    {
      LessThanOrEquals lessThanOrEquals = (LessThanOrEquals)query;

      logger.info( prefix + "+--> LessThanOrEquals[" + lessThanOrEquals.getAttributeName() + " <= " + lessThanOrEquals.getAttributeValue() + "]" );
      //sqlQuery += " C" + lessThanOrEquals.getAttributeName() + " <= " + escapeAttributeValue(lessThanOrEquals.getAttributeValue()) + " ";
      params.add( lessThanOrEquals.getAttributeValue() );
      sqlQuery += " C" + lessThanOrEquals.getAttributeName() + " <= ?" + params.size() + " ";
    }
    else if (query instanceof GreaterThan)
    {
      GreaterThan greaterThan = (GreaterThan)query;

      logger.info( prefix + "+--> GreaterThan[" + greaterThan.getAttributeName() + " > " + greaterThan.getAttributeValue() + "]" );
      //sqlQuery += " C" + greaterThan.getAttributeName() + " > " + escapeAttributeValue(greaterThan.getAttributeValue()) + " ";
      params.add( greaterThan.getAttributeValue() );
      sqlQuery += " C" + greaterThan.getAttributeName() + " > ?" + params.size() + " ";
    }
    else if (query instanceof GreaterThanOrEquals)
    {
      GreaterThanOrEquals greaterThanOrEquals = (GreaterThanOrEquals)query;

      logger.info( prefix + "+--> GreaterThanOrEquals[" + greaterThanOrEquals.getAttributeName() + " >= " + greaterThanOrEquals.getAttributeValue() + "]" );
      //sqlQuery += " C" + greaterThanOrEquals.getAttributeName() + " >= " + escapeAttributeValue(greaterThanOrEquals.getAttributeValue()) + " ";
      params.add( greaterThanOrEquals.getAttributeValue() );
      sqlQuery += " C" + greaterThanOrEquals.getAttributeName() + " >= ?" + params.size() + " ";
    }
    else if (query instanceof And)
    {
      logger.info( prefix + "+--> And" );
      And and = (And)query;
      sqlQuery += "(";
      for(QueryExpression subAnd : and.getExpressions())
      {
        sqlQuery = parseDynamicQuery( subAnd, deepness+1, sqlQuery, params );
        sqlQuery += " AND ";
      }
      sqlQuery = replaceLast(sqlQuery, " AND ", ")");
    }
    else if (query instanceof Or)
    {
      logger.info( prefix + "+--> Or" );
      Or or = (Or)query;
      sqlQuery += "(";
      for(QueryExpression subOr : or.getExpressions())
      {
        sqlQuery = parseDynamicQuery( subOr, deepness+1, sqlQuery, params );
        sqlQuery += " OR ";
      }
      sqlQuery = replaceLast(sqlQuery, " OR ", ")");
    }
    else if (query instanceof Not)
    {
      logger.info( prefix + "+--> Not" );
      Not not = (Not)query;
      sqlQuery += " NOT ( ";
      sqlQuery = parseDynamicQuery( not.getExpression(), deepness+1, sqlQuery, params );
      sqlQuery += ")";
    }
    else if (query instanceof LongestPrefixMatch)
    {
      LongestPrefixMatch longestPrefixMatch = (LongestPrefixMatch)query;

      logger.info( prefix + "+--> LongestPrefixMatch[" + longestPrefixMatch.getAttributeName() + " >=< " + longestPrefixMatch.getAttributeValue() + "]" );
      //sqlQuery += " " + escapeAttributeValue(longestPrefixMatch.getAttributeValue()) + " LIKE CONCAT(" + "C" + longestPrefixMatch.getAttributeName() + ", '%') ";
      params.add( longestPrefixMatch.getAttributeValue() );
      sqlQuery += " ?" + params.size() + " LIKE CONCAT(" + "C" + longestPrefixMatch.getAttributeName() + ", '%') ";
    }
    else if (query instanceof HasPrefix)
    {
      HasPrefix hasPrefix = (HasPrefix)query;

      logger.info( prefix + "+--> HasPrefix[" + hasPrefix.getAttributeName() + " << " + hasPrefix.getAttributeValue() + "]" );
      //sqlQuery += " C" + hasPrefix.getAttributeName() + " LIKE CONCAT(" + escapeAttributeValue(hasPrefix.getAttributeValue()) + ", '%') ";
      params.add( hasPrefix.getAttributeValue() );
      sqlQuery += " C" + hasPrefix.getAttributeName() + " LIKE CONCAT(" + "?" + params.size() + ", '%') ";
    }
    else if (query instanceof RangeMatch)
    {
      RangeMatch rangeMatch = (RangeMatch)query;

      logger.info( prefix + "+--> RangeMatch[" + rangeMatch.getAttributeName() + " > " + rangeMatch.getFromValue() + " && " + rangeMatch.getAttributeName() + " < " + rangeMatch.getToValue() + "]" );
      params.add( rangeMatch.getFromValue() );
      params.add( rangeMatch.getToValue() );
      sqlQuery += " C" + rangeMatch.getAttributeName() + " >= " + "?" + (params.size()-1) + " AND " + "C" + rangeMatch.getAttributeName() + " <= " + "?" + params.size() + " ";
    }

    return sqlQuery;
  }

  // ######################
  // # COMMON FOR QUERIES #
  // ######################

  private static String replaceLast(String sourceString, String toBeReplaced, String replacement)
  {
    StringBuilder x = new StringBuilder(sourceString);  
    int liof = x.lastIndexOf(toBeReplaced);

    if(liof >= 0)
      x.replace(liof, liof+4, replacement);

    return new String(x);
  }

}
