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

import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.AbstractMediatorFactory;
import org.apache.synapse.config.xml.SynapseXPathFactory;
import org.jaxen.JaxenException;

import fi.mystes.synapse.mediator.ReadFileMediator;
import fi.mystes.synapse.mediator.config.ReadFileMediatorConfigConstants;

public class ReadFileMediatorFactory extends AbstractMediatorFactory {

	/**
	 * The QName of read file mediator element in the XML config
	 * 
	 * @return QName of read file mediator
	 */
	@Override
	public QName getTagQName() {
		return ReadFileMediatorConfigConstants.ROOT_TAG;
	}

	/**
	 * Specific mediator factory implementation to build the
	 * org.apache.synapse.Mediator by the given XML configuration
	 * 
	 * @param OMElement
	 *            element configuration element describing the properties of the
	 *            mediator
	 * @param properties
	 *            bag of properties to pass in any information to the factory
	 * 
	 * @return built read file mediator
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected Mediator createSpecificMediator(OMElement elem,
			Properties properties) {

		ReadFileMediator mediator = new ReadFileMediator();

		processTraceState(mediator, elem);
		OMAttribute fileNameElement = elem.getAttribute(ReadFileMediatorConfigConstants.ATT_FILENAME);
		if (fileNameElement != null) {
			String value = fileNameElement.getAttributeValue();
			mediator.setFileName(value);
		}
        OMAttribute propertyElement = elem.getAttribute(ReadFileMediatorConfigConstants.ATT_PROPERTY);
        if (propertyElement != null) {
            String value = propertyElement.getAttributeValue();
            mediator.setProperty(value);
        }
        
		if (fileNameElement == null && propertyElement == null) {
			handleException("Either fileName or property attribute must be declared");
        }

        OMAttribute expression = elem.getAttribute(ReadFileMediatorConfigConstants.ATT_ATTACH_XPATH);
        if (expression != null) {
            try {
                mediator.setAttachXpath(SynapseXPathFactory.getSynapseXPath(
                        elem, ReadFileMediatorConfigConstants.ATT_ATTACH_XPATH));
            } catch (JaxenException e) {
                handleException("Unable to build the ReadFileMediator. "
                        + "Invalid XPATH " + expression.getAttributeValue(), e);
            }
        }

		return mediator;
	}

}
