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

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.AbstractMediatorSerializer;
import org.apache.synapse.config.xml.SynapseXPathSerializer;

import fi.mystes.synapse.mediator.ReadFileMediator;
import fi.mystes.synapse.mediator.config.ReadFileMediatorConfigConstants;


public class ReadFileMediatorSerializer extends AbstractMediatorSerializer {

    @Override
    public String getMediatorClassName() {
        return ReadFileMediator.class.getName();
    }

    @Override
    protected OMElement serializeSpecificMediator(Mediator m) {
        OMElement element = fac.createOMElement(
                ReadFileMediatorConfigConstants.ROOT_TAG_NAME, synNS);

        ReadFileMediator mediator = (ReadFileMediator) m;

		if (mediator.getFileName() == null && mediator.getProperty() == null) {
			handleException("Either fileName or property must be declared");
		}

		if (mediator.getFileName() != null) {
        	element.addAttribute("fileName", mediator.getFileName(),
                nullNS);
        }
		if (mediator.getProperty() != null) {
			element.addAttribute("property", mediator.getProperty(),
                nullNS);
		}


        if (mediator.getAttachXpath() != null) {
            SynapseXPathSerializer.serializeXPath(mediator.getAttachXpath(),
                    element, "attachXpath");
        }

        return element;

    }

}
