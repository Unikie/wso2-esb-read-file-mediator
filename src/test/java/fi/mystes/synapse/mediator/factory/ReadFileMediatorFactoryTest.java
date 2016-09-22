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
package fi.mystes.synapse.mediator.factory;

import static org.junit.Assert.assertTrue;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.synapse.SynapseException;
import org.junit.Before;
import org.junit.Test;

import fi.mystes.synapse.mediator.ReadFileMediator;
import fi.mystes.synapse.mediator.config.ReadFileMediatorConfigConstants;

public class ReadFileMediatorFactoryTest {

	private ReadFileMediatorFactory mediatorFactory;
	private OMFactory omFactory = OMAbstractFactory.getOMFactory();

	private OMElement mediatorElement;

	@Before
	public void setUp() {
		mediatorFactory = new ReadFileMediatorFactory();
		mediatorElement = omFactory.createOMElement(ReadFileMediatorConfigConstants.ROOT_TAG);
		mediatorElement.addAttribute(omFactory
				.createOMAttribute(ReadFileMediatorConfigConstants.ATT_FILENAME.getLocalPart(), null, "Example.xml"));
		mediatorElement.addAttribute(omFactory.createOMAttribute(
				ReadFileMediatorConfigConstants.ATT_PROPERTY.getLocalPart(), null, "fileNameProperty"));
	}

	@Test(expected = SynapseException.class)
	public void shouldThrowExceptionDueToMissingOneOfMandatoryAttributes() {
		mediatorElement.removeAttribute(omFactory.createOMAttribute(
				ReadFileMediatorConfigConstants.ATT_PROPERTY.getLocalPart(), null, "fileNameProperty"));
		mediatorElement.removeAttribute(omFactory
				.createOMAttribute(ReadFileMediatorConfigConstants.ATT_FILENAME.getLocalPart(), null, "Example.xml"));
		mediatorFactory.createSpecificMediator(mediatorElement, null);
	}

	@Test
	public void shouldCreateReadFileMediatorWithFileNameAttribute() {
		mediatorElement.removeAttribute(omFactory.createOMAttribute(
				ReadFileMediatorConfigConstants.ATT_PROPERTY.getLocalPart(), null, "fileNameProperty"));
		ReadFileMediator mediator = (ReadFileMediator) mediatorFactory.createSpecificMediator(mediatorElement, null);

		assertTrue("Instance of read file mediator should be created", mediator != null);

		assertTrue("Attach Xpath should be null", mediator.getAttachXpath() == null);

		assertTrue("Property should be null", mediator.getProperty() == null);

		assertTrue("File name should contain value of \"Example.xml\"", mediator.getFileName().equals("Example.xml"));
	}

	@Test
	public void shouldCreateReadFileMediatorWithPropertyAttribute() {
		mediatorElement.removeAttribute(omFactory
				.createOMAttribute(ReadFileMediatorConfigConstants.ATT_FILENAME.getLocalPart(), null, "Example.xml"));
		ReadFileMediator mediator = (ReadFileMediator) mediatorFactory.createSpecificMediator(mediatorElement, null);

		assertTrue("Instance of read file mediator should be created", mediator != null);

		assertTrue("Attach Xpath should be null", mediator.getAttachXpath() == null);

		assertTrue("File name property should be null", mediator.getFileName() == null);

		assertTrue("Property should contain value of \"Example.xml\"",
				mediator.getProperty().equals("fileNameProperty"));
	}


}
