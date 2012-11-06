/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-27 17:12:21 +0200 (Fr, 27 Aug 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/ProcessInformationEntity.java $
 * $LastChangedRevision: 4712 $
 *------------------------------------------------------------------------------
 * (c) 07.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 
 *This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.services.coreengine.entities;

import javax.ejb.Local;

import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.entities.impl.DefaultProcessInformationEntity;



/**
 * Provides an interface with access to the processinformation data stored in the ontology.
 * 
 * This interface is implemented by the {@link DefaultProcessInformationEntity} to provide the implementations of the methods specific to change states of a processInformation instance in the ontology.
 * 
 * @author Saad Wardi
 * @version $Revision: 4712 $
 */
@Local
public interface ProcessInformationEntity
{
    /**
     * Gets the processinformation.
     * 
     * @param id not null ID.
     * @return not null {@link Parameter}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessInformation getProcessInformation(String id) throws OntologyErrorException;

    /**
     * Gets the processInformation possible selection list.
     * 
     * @param processInformationID the not null processInformationID.
     * @return not null values. if no item exists exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    StringArray getPossibleSelection(String processInformationID) throws OntologyErrorException;

    /**
     * Sets the processInformation possible selection list.
     * 
     * @param values the input values.
     * @param processInformationID the not null ID of the process information.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setPossibleSelection(String processInformationID, StringArray values) throws OntologyErrorException;

}
