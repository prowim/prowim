/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-20 17:24:29 +0200 (Mi, 20 Okt 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultControlFlowEntity.java $
 * $LastChangedRevision: 4934 $
 *------------------------------------------------------------------------------
 * (c) 08.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.entities.impl;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.entities.ControlFlowEntity;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the interface {@link ControlFlowEntity}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4934 $
 */
@Stateless
public class DefaultControlFlowEntity implements ControlFlowEntity
{

    private static final Logger LOG = Logger.getLogger(DefaultControlFlowEntity.class);

    @IgnoreDependency
    @EJB
    private ProcessEngine       processEngine;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ControlFlowEntity#getControlFlow(java.lang.String)
     */
    public ControlFlow getControlFlow(String controlFlowID) throws OntologyErrorException
    {
        Validate.notNull(controlFlowID);
        final RecordSet result = processEngine.getControlFlow(controlFlowID);
        if (result.getNoOfRecords() > 0)
        {
            Hashtable<String, String> controlFlowRecord = result.getRecords()[0];
            String id = controlFlowRecord.get(ProcessEngineConstants.Variables.ControlFlow.ID);
            String name = controlFlowRecord.get(ProcessEngineConstants.Variables.Common.NAME_EN);
            String createTime = controlFlowRecord.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            String activityID = controlFlowRecord.get(ProcessEngineConstants.Variables.Activity.VID);
            ControlFlow controlFlow = DefaultDataObjectFactory.createControlFlow(id, name, createTime);
            controlFlow.setActivityID(activityID);
            return controlFlow;
        }
        else
        {
            LOG.error("Could not find ControlFlow with id =  " + controlFlowID);
            throw new IllegalArgumentException("Could not find ControlFlow with id =  " + controlFlowID);
        }

    }

}
