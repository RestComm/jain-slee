/**
 * 
 */
package org.mobicents.xcap.client.uri;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.xcap.client.uri.enconding.UriComponentEncoder;

/**
 * @author martins
 *
 */
public class UriBuilderTest {

	@Test
	public void test() throws URISyntaxException {
		
		URI expected = new URI("http://host" + UriComponentEncoder.encodePath("/xcapRoot/auid/users/user/documentName/~~/byName/byPos[1]/byAttr[@attrName=\"attrValue\"]/byPosAttr[1][@attrName=\"attrValue\"]/@attrName") + "?" + UriComponentEncoder.encodeQuery("xmlns(prefix1=urn:test:namespace1-uri)xmlns(prefix2=urn:test:namespace2-uri)"));
		
		DocumentSelectorBuilder documentSelectorBuilder = DocumentSelectorBuilder.getUserDocumentSelectorBuilder("auid", "user", "documentName");
		String documentSelector = documentSelectorBuilder.toPercentEncodedString();
		System.out.println("Document selector built: "+documentSelector);
				
		AttributeSelectorBuilder attributeSelectorBuilder = new AttributeSelectorBuilder("attrName");
		String attributeSelector = attributeSelectorBuilder.toPercentEncodedString();
		System.out.println("Attribute selector built: "+attributeSelector);
				
		ElementSelectorBuilder elementSelectorBuilder = new ElementSelectorBuilder().appendStepByName("byName").appendStepByPos("byPos", 1).appendStepByAttr("byAttr", "attrName", "attrValue").appendStepByPosAttr("byPosAttr",1, "attrName", "attrValue");
		String elementSelector = elementSelectorBuilder.toPercentEncodedString();
		System.out.println("Element selector built: "+elementSelector);
		
		NamespaceBindingsBuilder namespaceBindingsBuilder = new NamespaceBindingsBuilder().appendBinding("prefix1", "urn:test:namespace1-uri").appendBinding("prefix2", "urn:test:namespace2-uri");
		String namespaceBindings = namespaceBindingsBuilder.toPercentEncodedString();
		System.out.println("Namespace bindings built: "+namespaceBindings);

		URI built = new UriBuilder()
			.setSchemeAndAuthority("http://host")
			.setXcapRoot("/xcapRoot")
			.setDocumentSelector(documentSelector)
			.setElementSelector(elementSelector)
			.setTerminalSelector(attributeSelector)
			.setNamespaceBindings(namespaceBindings)
			.toURI();
		
		System.out.println("URI expected: "+expected);
		System.out.println("URI built   : "+built);
		
		Assert.assertEquals(expected,built);
		
	}
	
}
