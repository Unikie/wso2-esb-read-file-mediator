/**
 * Copyright 2016 Mystes Oy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.mystes.synapse.mediator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.OMNamespaceImpl;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.impl.llom.SOAPEnvelopeImpl;
import org.apache.axiom.soap.impl.llom.soap11.SOAP11BodyImpl;
import org.apache.axiom.soap.impl.llom.soap11.SOAP11Factory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.xml.SynapseXPathFactory;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReadFileMediatorTest {
	
	private ReadFileMediator mediator;
	
	@Mock
	private MessageContext context;
	
	private OMFactory omFactory = OMAbstractFactory.getOMFactory();

	private OMElement payloadElement;

	private SOAPFactory soapFactory;

	private SOAPEnvelope envelope;

	private String fileName = getClass().getClassLoader().getResource("Example.xml").getFile();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		payloadElement = omFactory.createOMElement(new QName(null, "root"));

		soapFactory = new SOAP11Factory();
		envelope = new SOAPEnvelopeImpl(new OMNamespaceImpl("http://schemas.xmlsoap.org/soap/envelope/", "env"),
				soapFactory);

		SOAP11BodyImpl body = new SOAP11BodyImpl(envelope, soapFactory);

		body.addChild(payloadElement);

		when(context.getEnvelope()).thenReturn(envelope);

		mediator = new ReadFileMediator();
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNullPointerExceptionDueToMissingReadableFile() {
		mediator.mediate(context);
	}
	
	@Test
	public void shouldReadXmlFileContentIntoTargetElement() throws JaxenException, OMException {
		mediator.setAttachXpath(SynapseXPathFactory.getSynapseXPath(envelope.getBody(), "//root"));
		mediator.setFileName(fileName);
		assertTrue("File content should be read into target element", mediator.mediate(context));

		SynapseXPath xpath = SynapseXPathFactory.getSynapseXPath(envelope.getBody(), "//content/child");
		@SuppressWarnings("unchecked")
		List<OMElement> childElements = (List<OMElement>) xpath.evaluate(envelope.getBody());
		assertTrue("Two child elements should be present in payload", childElements.size() == 2);
		assertTrue("child 1 should be present in payload",
				childElements.get(0).getText().equals("1") || childElements.get(1).getText().equals("1"));
		assertTrue("child 2 should be present in payload",
				childElements.get(0).getText().equals("2") || childElements.get(1).getText().equals("2"));
	}

	@Test
	public void shouldReadXmlFileContentIntoTargetElementUsingProperty() throws JaxenException, OMException {
		mediator.setAttachXpath(SynapseXPathFactory.getSynapseXPath(envelope.getBody(), "//root"));
		mediator.setProperty("fileName");
		when(context.getProperty("fileName")).thenReturn(fileName);
		assertTrue("File content should be read into target element", mediator.mediate(context));

		SynapseXPath xpath = SynapseXPathFactory.getSynapseXPath(envelope.getBody(), "//content/child");
		@SuppressWarnings("unchecked")
		List<OMElement> childElements = (List<OMElement>) xpath.evaluate(envelope.getBody());
		assertTrue("Two child elements should be present in payload", childElements.size() == 2);
		assertTrue("child 1 should be present in payload",
				childElements.get(0).getText().equals("1") || childElements.get(1).getText().equals("1"));
		assertTrue("child 2 should be present in payload",
				childElements.get(0).getText().equals("2") || childElements.get(1).getText().equals("2"));
	}

	@Test
	public void shouldReadXmlFileContentIntoBodyElement() throws JaxenException, OMException {
		mediator.setFileName(fileName);
		assertTrue("File content should be read into target element", mediator.mediate(context));
		List<OMElement> children = new LinkedList<OMElement>();

		for (@SuppressWarnings("unchecked")
		Iterator<OMElement> i = envelope.getBody().getChildElements(); i.hasNext();) {
			OMElement child = i.next();
			children.add(child);
		}
		
		assertTrue("Body element should have two children", children.size() == 2);
		
		SynapseXPath xpath = SynapseXPathFactory.getSynapseXPath(envelope.getBody(), "//content/child");
		@SuppressWarnings("unchecked")
		List<OMElement> childElements = (List<OMElement>) xpath.evaluate(envelope.getBody());
		assertTrue("Two child elements should be present in payload", childElements.size() == 2);
		assertTrue("child 1 should be present in payload",
				childElements.get(0).getText().equals("1") || childElements.get(1).getText().equals("1"));
		assertTrue("child 2 should be present in payload",
				childElements.get(0).getText().equals("2") || childElements.get(1).getText().equals("2"));
	}


}
