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
package fi.mystes.synapse.mediator.serializer;

import static org.junit.Assert.assertTrue;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;
import org.junit.Before;
import org.junit.Test;

import fi.mystes.synapse.mediator.ReadFileMediator;
import fi.mystes.synapse.mediator.config.ReadFileMediatorConfigConstants;

public class ReadFileMediatorSerializerTest {

	private ReadFileMediatorSerializer mediatorSerializer;

	private ReadFileMediator mediator;

	@Before
	public void setUp() throws JaxenException {
		mediatorSerializer = new ReadFileMediatorSerializer();

		mediator = new ReadFileMediator();
		mediator.setFileName("Example.xml");
		mediator.setProperty("fileNameProperty");
		mediator.setAttachXpath(new SynapseXPath("//target"));
	}

	@Test
	public void shouldSerializeWithFileNameAttributeOnly() {
		mediator.setProperty(null);
		mediator.setAttachXpath(null);
		OMElement mediatorElement = mediatorSerializer.serializeSpecificMediator(mediator);
		assertTrue("Mediator element should be created", mediatorElement != null);
		assertTrue("Mediator elment name should be " + ReadFileMediatorConfigConstants.ROOT_TAG_NAME,
				mediatorElement.getLocalName().equals(ReadFileMediatorConfigConstants.ROOT_TAG_NAME));
		assertTrue("Attribute fileName should be present withing mediator element",
				mediatorElement.getAttributeValue(ReadFileMediatorConfigConstants.ATT_FILENAME).equals("Example.xml"));
		assertTrue("Attribute property should not be present withing mediator element",
				mediatorElement.getAttributeValue(ReadFileMediatorConfigConstants.ATT_PROPERTY) == null);
		assertTrue("Attribute attachXpath should not be present withing mediator element",
				mediatorElement.getAttributeValue(ReadFileMediatorConfigConstants.ATT_ATTACH_XPATH) == null);
	}

	@Test
	public void shouldSerializeWithPropertyAttributeOnly() {
		mediator.setFileName(null);
		mediator.setAttachXpath(null);
		OMElement mediatorElement = mediatorSerializer.serializeSpecificMediator(mediator);
		assertTrue("Mediator element should be created", mediatorElement != null);
		assertTrue("Mediator elment name should be " + ReadFileMediatorConfigConstants.ROOT_TAG_NAME,
				mediatorElement.getLocalName().equals(ReadFileMediatorConfigConstants.ROOT_TAG_NAME));
		assertTrue("Attribute fileName not should be present withing mediator element",
				mediatorElement.getAttributeValue(ReadFileMediatorConfigConstants.ATT_FILENAME) == null);
		assertTrue("Attribute property should be present withing mediator element", mediatorElement
				.getAttributeValue(ReadFileMediatorConfigConstants.ATT_PROPERTY).equals("fileNameProperty"));
		assertTrue("Attribute attachXpath should not be present withing mediator element",
				mediatorElement.getAttributeValue(ReadFileMediatorConfigConstants.ATT_ATTACH_XPATH) == null);
	}

	@Test
	public void shouldSerializeWithAllAttributes() {
		OMElement mediatorElement = mediatorSerializer.serializeSpecificMediator(mediator);
		assertTrue("Mediator element should be created", mediatorElement != null);
		assertTrue("Mediator elment name should be " + ReadFileMediatorConfigConstants.ROOT_TAG_NAME,
				mediatorElement.getLocalName().equals(ReadFileMediatorConfigConstants.ROOT_TAG_NAME));
		assertTrue("Attribute fileName not should be present withing mediator element",
				mediatorElement.getAttributeValue(ReadFileMediatorConfigConstants.ATT_FILENAME).equals("Example.xml"));
		assertTrue("Attribute property should be present withing mediator element", mediatorElement
				.getAttributeValue(ReadFileMediatorConfigConstants.ATT_PROPERTY).equals("fileNameProperty"));
		assertTrue("Attribute attachXpath should not be present withing mediator element",
				mediatorElement.getAttributeValue(ReadFileMediatorConfigConstants.ATT_ATTACH_XPATH).equals("//target"));

	}
}
