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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAPBody;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;

/**
 * Custom mediator to read XML file content into the current payload either
 * directly to SOAP body element or to defined target element.
 * 
 * <readFile (fileName="path" | property="propertyName")
 * [attachXpath="expression"]/>
 */
public class ReadFileMediator extends AbstractMediator {

    private String fileName;

    private String property;
    
    private SynapseXPath attachXpath = null;

    /**
     * Invokes the mediator passing the current message for mediation. Each
     * mediator performs its mediation action, and returns true if mediation
     * should continue, or false if further mediation should be aborted.
     *
     * @param context
     *            Current message context for mediation
     * @return true if further mediation should continue, otherwise false
     */
    @Override
    public boolean mediate(MessageContext context) {
        SynapseLog synLog = getLog(context);

        if (synLog.isTraceOrDebugEnabled()) {
        	synLog.traceOrDebug("Starting ReadFileMediator");
        }

        SOAPBody body = context.getEnvelope().getBody();
        File xmlFileToProcess = initProcessableFile(context);


        if (xmlFileToProcess.exists()) {
			if (synLog.isTraceOrDebugEnabled()) {
				synLog.traceOrDebug("File " + xmlFileToProcess.getName() + " exists, processing.");
			}
            OMElement fileElement;
            try {
                InputStream xmlInputStream = new FileInputStream(xmlFileToProcess);
                fileElement = new StAXOMBuilder(xmlInputStream).getDocumentElement();
            } catch (Exception e) {
                synLog.error("Error while parsing XML file : " + xmlFileToProcess.getAbsolutePath());
                return false;
            }

            appendFileContentIntoGivenElement(context, body, fileElement);
        } else {
			if (synLog.isTraceOrDebugEnabled()) {
				synLog.traceOrDebug("File " + xmlFileToProcess.getName() + " doesn't exist.");
			}
            return false;
        }
        
        if (synLog.isTraceOrDebugEnabled()) {
        	synLog.traceOrDebug("Ending ReadFileMediator");
        }
        return true;
    }

    /**
     * Appends XML file content into the given target element (attachXpath or body).
     * 
     * @param context Used to log errors
     * @param body SOAP body to append file element to if attachXpath not set
     * @param fileElement Element containing file content to be appended to body
     */
	private void appendFileContentIntoGivenElement(MessageContext context, SOAPBody body, OMElement fileElement) {
		SynapseLog synLog = getLog(context);
		if (fileElement != null) {
			if (synLog.isTraceOrDebugEnabled()) {
				synLog.traceOrDebug("Element containing file content created");
			}
		    if (attachXpath != null) {
		        try {
					if (synLog.isTraceOrDebugEnabled()) {
						synLog.traceOrDebug("Adding to target element");
					}
		            OMElement node = (OMElement) attachXpath.selectSingleNode(body);
		            node.addChild(fileElement);
		        } catch (JaxenException e) {
		            handleException("Error occurred while evaluating xpath", e, context);
		        }

		    } else {
				if (synLog.isTraceOrDebugEnabled()) {
					synLog.traceOrDebug("Adding to body");
				}
		        body.addChild(fileElement);
		    }
		} else {
			if (synLog.isTraceOrDebugEnabled()) {
				synLog.traceOrDebug("Element containing file content not created");
			}
		}
	}

	/**
	 * Helper method to initialize file to read content from. Will try to get file name
	 * first from fileName attribute. If it fails, it will try to read file name from property.
	 * 
	 * @param context Contains property which may hold XML file name.
	 * 
	 * @return Reference to file object
	 */
	private File initProcessableFile(MessageContext context) {
		File xmlFileToProcess = null;
        if (fileName != null) {
            xmlFileToProcess = new File(fileName);
        } else if (property != null) {
            xmlFileToProcess = new File((String) context.getProperty(property));
        }
		return xmlFileToProcess;
	}
    
    /**
     * Getter for attachXpath
     * 
     * @return
     */
    public SynapseXPath getAttachXpath() {
        return attachXpath;
    }

    /**
     * Setter for attachXpath
     * 
     * @param attachXpath
     */
    public void setAttachXpath(SynapseXPath attachXpath) {
        this.attachXpath = attachXpath;
    }

    /**
     * Getter for property.
     * 
     * @return
     */
    public String getProperty() {
        return property;
    }

    /**
     * Setter for property which contains XML file to be processed.
     * 
     * @param property
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Getter for XML file name.
     * 
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Setter for XML file name.
     * 
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
