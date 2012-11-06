/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/EntityManager.java $
 * $LastChangedRevision: 4510 $
 *------------------------------------------------------------------------------
 * (c) 07.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 This file is part of ProWim.

ProWim is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ProWim is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ProWim.  If not, see <http://www.gnu.org/licenses/>.

Diese Datei ist Teil von ProWim.

ProWim ist Freie Software: Sie können es unter den Bedingungen
der GNU General Public License, wie von der Free Software Foundation,
Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
veröffentlichten Version, weiterverbreiten und/oder modifizieren.

ProWim wird in der Hoffnung, dass es nützlich sein wird, aber
OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
Siehe die GNU General Public License für weitere Details.

Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.services.coreengine.entities;

import java.util.HashMap;

import org.prowim.datamodel.prowim.Activity;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.entities.impl.DefaultActivityEntity;



/**
 * EntityManager interface registers the supported entities that mappes the processelements classes and get their instances.
 * 
 * @author Saad Wardi
 * @version $Revision: 4510 $
 */
public interface EntityManager
{
    /**
     * Stores the entities classes names and maps them from the ontology class names to java dataobjects class names.
     */
    HashMap<String, String> DATAOBJECTS_ELEMENTS_MAP = new HashMap<String, String>();

    /**
     * Stores the entities classes names and maps them from dataobjects class names to the ontology class names.
     */
    HashMap<String, String> ELEMENTS_DATAOBJECTS_MAP = new HashMap<String, String>();

    /**
     * {@link DefaultActivityEntity#getActivity(String)}.
     * 
     * @param id not null ID.
     * @return not null {@link Activity}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Object getObject(String id) throws OntologyErrorException;

    /**
     * Checks if an element is supported..
     * 
     * @param element not null element.
     * @return true if the element is supported.
     */
    boolean containsElement(String element);

    /**
     * Checks if an element is supported..
     * 
     * @param elementKey not null element.
     * @return true if the element is supported.
     */
    boolean containsDataObject(String elementKey);

    /**
     * Maps the ontology class names to java class names.
     * 
     * @param instanceClassName ontology class name
     * @return not null dataobject classname.
     */
    String getDataObjectClassName(String instanceClassName);

    /**
     * Maps the ontology class names to java class names.
     * 
     * @param dataObjectClassName ontology class name
     * @return not null dataobject classname.
     */
    String getOntologyClassName(String dataObjectClassName);

}
