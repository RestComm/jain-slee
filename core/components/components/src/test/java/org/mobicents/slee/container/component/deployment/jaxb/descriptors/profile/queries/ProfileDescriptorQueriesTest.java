/**
 * Start time:13:14:54 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.queries;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;

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
 * Start time:13:14:54 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileDescriptorQueriesTest extends TCUtilityClass {

	private static final String _FILE_QUERY_COMPARE = "xml/profile-spec-jar-querry-compare.xml";
	private static final String _FILE_QUERY_LONGEST_PREFIX_MATCH = "xml/profile-spec-jar-querry-longest-prefix-match.xml";
	private static final String _FILE_QUERY_HAS_PREFIX = "xml/profile-spec-jar-querry-has-prefix.xml";
	private static final String _FILE_QUERY_RANGE_MATCH_ = "xml/profile-spec-jar-querry-range-match.xml";
	private static final String _FILE_QUERY_AND_ = "xml/profile-spec-jar-querry-and.xml";
	private static final String _FILE_QUERY_OR_ = "xml/profile-spec-jar-querry-or.xml";
	private static final String _FILE_QUERY_NOT_ = "xml/profile-spec-jar-querry-not.xml";

	private static final String _QUERY_NAME = "query";
	private static final String _QUERY_PARAMETER_NAME = "query-parameter";
	private static final String _QUERY_PARAMETER_TYPE = "java.lang.String";
	private static final String _COMPARE_PARAMETER = "parameter";
	private static final String _COMPARE_ATTRIBUTE_NAME = "attribute-name";
	private static final String _COLLATOR_REF = "collator-ref";
	private static final String _COMPARE_OP = "equals";
	private static final String _COMPARE_VALUE = "value";

	private static final String _RANGE_MATCH_TO_VALUE = "to-value";
	private static final String _RANGE_MATCH_TO_PARAMETER = "to-parameter";
	private static final String _RANGE_MATCH_FROM_VALUE = "from-value";
	private static final String _RANGE_MATCH_FROM_PARAMETER = "from-parameter";

	private static final MQueryExpressionType[] insideType = new MQueryExpressionType[] {
			MQueryExpressionType.Compare, MQueryExpressionType.Compare,
			MQueryExpressionType.HasPrefix,
			MQueryExpressionType.LongestPrefixMatch };

	public void testQueryCompare() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
				.parse(super.getFileStream(_FILE_QUERY_COMPARE));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		assertNotNull("Query elements list is null", descriptor
				.getQueryElements());
		assertTrue("Query elements list size is not 1", descriptor
				.getQueryElements().size() == 1);

		MQuery query = descriptor.getQueryElements().get(0);

		performBasicValidation(query, MQueryExpressionType.Compare);

		MCompare compare = query.getQueryExpression().getCompare();

		performCheckOnCompare(compare);

	}

	public void testQueryLongest() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
				.parse(super.getFileStream(
						_FILE_QUERY_LONGEST_PREFIX_MATCH));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		assertNotNull("Query elements list is null", descriptor
				.getQueryElements());
		assertTrue("Query elements list size is not 1", descriptor
				.getQueryElements().size() == 1);

		MQuery query = descriptor.getQueryElements().get(0);

		performBasicValidation(query, MQueryExpressionType.LongestPrefixMatch);

		MLongestPrefixMatch longest = query.getQueryExpression()
				.getLongestPrefixMatch();
		performCheckOnLongestPrefixMatch(longest);

	}

	public void testHasPrefix() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
				.parse(super.getFileStream(_FILE_QUERY_HAS_PREFIX));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		assertNotNull("Query elements list is null", descriptor
				.getQueryElements());
		assertTrue("Query elements list size is not 1", descriptor
				.getQueryElements().size() == 1);

		MQuery query = descriptor.getQueryElements().get(0);

		performBasicValidation(query, MQueryExpressionType.HasPrefix);

		MHasPrefix hasPrefix = query.getQueryExpression().getHasPrefix();

		performCheckOnHasprefix(hasPrefix);

	}

	public void testRangeMatch() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
				.parse(super.getFileStream(_FILE_QUERY_RANGE_MATCH_));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		assertNotNull("Query elements list is null", descriptor
				.getQueryElements());
		assertTrue("Query elements list size is not 1", descriptor
				.getQueryElements().size() == 1);

		MQuery query = descriptor.getQueryElements().get(0);

		performBasicValidation(query, MQueryExpressionType.RangeMatch);

		MRangeMatch range = query.getQueryExpression().getRangeMatch();

		performCheckOnRangeMatch(range);

	}

	public void testAnd() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
				.parse(super.getFileStream(_FILE_QUERY_AND_));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		assertNotNull("Query elements list is null", descriptor
				.getQueryElements());
		assertTrue("Query elements list size is not 1", descriptor
				.getQueryElements().size() == 1);

		MQuery query = descriptor.getQueryElements().get(0);

		performBasicValidation(query, MQueryExpressionType.And);

		testComplicated(query.getQueryExpression(), insideType);

	}

	public void testOr() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
				.parse(super.getFileStream(_FILE_QUERY_OR_));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		assertNotNull("Query elements list is null", descriptor
				.getQueryElements());
		assertTrue("Query elements list size is not 1", descriptor
				.getQueryElements().size() == 1);

		MQuery query = descriptor.getQueryElements().get(0);

		performBasicValidation(query, MQueryExpressionType.Or);

		testComplicated(query.getQueryExpression(), insideType);

	}

	public void testNot() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
				.parse(super.getFileStream(_FILE_QUERY_NOT_));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		assertNotNull("Query elements list is null", descriptor
				.getQueryElements());
		assertTrue("Query elements list size is not 1", descriptor
				.getQueryElements().size() == 1);

		MQuery query = descriptor.getQueryElements().get(0);

		performBasicValidation(query, MQueryExpressionType.Not);

		testComplicated(query.getQueryExpression(), insideType);

	}

	private void testComplicated(MQueryExpression expression,
			MQueryExpressionType[] memberTypes) {

		// FIXME: make this check on nested and/or
		switch (expression.getType()) {
		case Or:
			MQueryExpression child = null;
			for (int index = 0; index < expression.getAnd().size(); index++) {
				child = expression.getAnd().get(index);
				assertTrue("Child type is not proper, it is[" + child.getType()
						+ "] should be[" + memberTypes[index] + "] on And",
						child.getType() == memberTypes[index]);
				assertTrue("Child parent type[" + child.getParentType()
						+ "] does not match parent type["
						+ expression.getType() + "]",
						child.getParentType() == expression.getType());
				switch (expression.getType()) {
				case Or:

					break;
				case And:

					break;
				case Not:

					break;
				case HasPrefix:
					performCheckOnHasprefix(child.getHasPrefix());
					break;
				case LongestPrefixMatch:
					performCheckOnLongestPrefixMatch(child
							.getLongestPrefixMatch());
					break;
				case RangeMatch:
					performCheckOnRangeMatch(child.getRangeMatch());
					break;
				case Compare:
					performCheckOnCompare(child.getCompare());
					break;
				}
			}
			break;
		case And:

			for (int index = 0; index < expression.getAnd().size(); index++) {
				child = expression.getAnd().get(index);
				assertTrue("Child type is not proper, it is[" + child.getType()
						+ "] should be[" + memberTypes[index] + "] on And",
						child.getType() == memberTypes[index]);
				assertTrue("Child parent type[" + child.getParentType()
						+ "] does not match parent type["
						+ expression.getType() + "]",
						child.getParentType() == expression.getType());
				switch (expression.getType()) {
				case Or:

					break;
				case And:

					break;
				case Not:

					break;
				case HasPrefix:
					performCheckOnHasprefix(child.getHasPrefix());
					break;
				case LongestPrefixMatch:
					performCheckOnLongestPrefixMatch(child
							.getLongestPrefixMatch());
					break;
				case RangeMatch:
					performCheckOnRangeMatch(child.getRangeMatch());
					break;
				case Compare:
					performCheckOnCompare(child.getCompare());
					break;
				}
			}

			break;
		case Not:

			// There will be only one loop go
			assertTrue("Not statement must have more than one member",
					expression.getAnd().size() == 1);
			for (int index = 0; index < expression.getAnd().size(); index++) {
				child = expression.getAnd().get(index);
				assertTrue("Child type is not proper, it is[" + child.getType()
						+ "] should be[" + memberTypes[index] + "] on And",
						child.getType() == memberTypes[index]);
				assertTrue("Child parent type does not match parent type",
						child.getParentType() == expression.getType());
				switch (expression.getType()) {
				case Or:

					break;
				case And:

					break;
				case Not:

					break;
				case HasPrefix:
					performCheckOnHasprefix(child.getHasPrefix());
					break;
				case LongestPrefixMatch:
					performCheckOnLongestPrefixMatch(child
							.getLongestPrefixMatch());
					break;
				case RangeMatch:
					performCheckOnRangeMatch(child.getRangeMatch());
					break;
				case Compare:
					performCheckOnCompare(child.getCompare());
					break;
				}
			}
			break;

		default:
			throw new RuntimeException("Wrong expression type in switch?");

		}
	}

	private void performCheckOnCompare(MCompare compare) {
		validateValue(compare.getAttributeName() + "",
				"Compare  attribute name", _COMPARE_ATTRIBUTE_NAME);
		validateValue(compare.getOp() + "", "Compare op", _COMPARE_OP);
		validateValue(compare.getParameter() + "", "Compare parameter",
				_COMPARE_PARAMETER);
		validateValue(compare.getValue() + "", "Compare value", _COMPARE_VALUE);
		validateValue(compare.getCollatorRef() + "", "Compare colaltor-ref",
				_COLLATOR_REF);
	}

	private void performCheckOnLongestPrefixMatch(MLongestPrefixMatch longest) {
		validateValue(longest.getAttributeName() + "",
				"Longest Prefix Match  attribute name", _COMPARE_ATTRIBUTE_NAME);
		validateValue(longest.getParameter() + "",
				"Longest Prefix Match parameter", _COMPARE_PARAMETER);
		validateValue(longest.getValue() + "", "Longest Prefix Match value",
				_COMPARE_VALUE);
		validateValue(longest.getCollatorRef() + "",
				"Longest Prefix Match colaltor-ref", _COLLATOR_REF);
	}

	private void performCheckOnHasprefix(MHasPrefix hasPrefix) {
		validateValue(hasPrefix.getAttributeName() + "",
				"Has Prefix Match  attribute name", _COMPARE_ATTRIBUTE_NAME);
		validateValue(hasPrefix.getParameter() + "",
				"Has Prefix Match parameter", _COMPARE_PARAMETER);
		validateValue(hasPrefix.getValue() + "", "Has Prefix Match value",
				_COMPARE_VALUE);
		validateValue(hasPrefix.getCollatorRef() + "",
				"Has Prefix Match colaltor-ref", _COLLATOR_REF);
	}

	private void performCheckOnRangeMatch(MRangeMatch range) {
		validateValue(range.getAttributeName() + "",
				"Range Match  attribute name", _COMPARE_ATTRIBUTE_NAME);
		validateValue(range.getCollatorRef() + "", "Range Match colaltor-ref",
				_COLLATOR_REF);

		validateValue(range.getFromValue() + "", "Range Match from-value",
				_RANGE_MATCH_FROM_VALUE);
		validateValue(range.getToValue() + "", "Range Match to-value",
				_RANGE_MATCH_TO_VALUE);
		validateValue(range.getFromParameter() + "",
				"Range Match from-parameter", _RANGE_MATCH_FROM_PARAMETER);
		validateValue(range.getToParameter() + "", "Range Match to-parameter",
				_RANGE_MATCH_TO_PARAMETER);
	}

	private void performBasicValidation(MQuery query,
			MQueryExpressionType presumedTypeExpresionType) {
		assertNotNull("Query element is null", query);
		List<MQueryParameter> parameters = query.getQueryParameters();
		assertNotNull("Query parameters list is null", parameters);
		assertTrue("Query parameters list size is not 1",
				parameters.size() == 1);
		MQueryParameter parameter = parameters.get(0);
		validateValue(query.getName(), "Query name", _QUERY_NAME);
		assertNotNull("Query parameter is null", parameter);

		validateValue(parameter.getName(), "Query parameter name",
				_QUERY_PARAMETER_NAME);
		validateValue(parameter.getType(), "Query parameter type",
				_QUERY_PARAMETER_TYPE);

		MQueryOptions options = query.getQueryOptions();
		assertNotNull("Query options is null", options);

		validateValue(options.getMaxMatches() + "",
				"Query options max matches", "100");
		validateValue(options.isReadOnly() + "", "Query options read only",
				"" + true);

		MQueryExpression expression = query.getQueryExpression();
		assertTrue("Root expression type[" + expression.getType() + "] is not "
				+ presumedTypeExpresionType,
				presumedTypeExpresionType == expression.getType());
		assertNull("Root expression must not have parent type", expression
				.getParentType());

		Object expressionWrapper = null;
		switch (expression.getType()) {
		case Or:
			expressionWrapper = expression.getOr();
			assertNotNull("Expression wrapper is null", expressionWrapper);
			assertTrue(
					"Expression wrapper is not instance of"
							+ MQueryExpression.class,
					((List) expressionWrapper).get(0) instanceof MQueryExpression);
			break;
		case And:
			expressionWrapper = expression.getAnd();
			assertNotNull("Expression wrapper is null", expressionWrapper);
			assertTrue(
					"Expression wrapper is not instance of"
							+ MQueryExpression.class,
					((List) expressionWrapper).get(0) instanceof MQueryExpression);
			break;
		case Not:
			expressionWrapper = expression.getNot();
			assertNotNull("Expression wrapper is null", expressionWrapper);
			break;
		case HasPrefix:
			expressionWrapper = expression.getHasPrefix();
			assertNotNull("Expression wrapper is null", expressionWrapper);

			break;
		case LongestPrefixMatch:
			expressionWrapper = expression.getLongestPrefixMatch();
			assertNotNull("Expression wrapper is null", expressionWrapper);

			break;
		case RangeMatch:
			expressionWrapper = expression.getRangeMatch();
			assertNotNull("Expression wrapper is null", expressionWrapper);

			break;
		case Compare:
			expressionWrapper = expression.getCompare();
			assertNotNull("Expression wrapper is null", expressionWrapper);

			break;
		default:
			throw new RuntimeException("Wrong expression type in switch?");

		}

	}

}
