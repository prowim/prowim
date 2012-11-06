/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-26 20:28:57 +0200 (Do, 26 Aug 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/MeanEntity.java $
 * $LastChangedRevision: 4707 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.coreengine.entities;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.MeanArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.entities.impl.DefaultMeanEntity;



/**
 * Provides an interface with access to the mean data stored in the ontology.
 * 
 * This interface is implemented by the {@link DefaultMeanEntity} to provide the implementations of the methods specific to change states of a {@link Mean} instance in the ontology.
 * 
 * @author Saad Wardi
 * @version $Revision: 4707 $
 */
@Local
public interface MeanEntity
{
    /**
     * Gets an existing {@link Mean}.
     * 
     * @param id not null ID.
     * @return not null {@link Mean}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Mean getMean(String id) throws OntologyErrorException;

    /**
     * Gets the registered {@link Mean}s of given {@link Activity}.
     * 
     * @param activityID the {@link Activity} id
     * @return {@link RecordSet} IDs of {@link Mean}s
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    MeanArray getActivityMeans(String activityID) throws OntologyErrorException;

}
