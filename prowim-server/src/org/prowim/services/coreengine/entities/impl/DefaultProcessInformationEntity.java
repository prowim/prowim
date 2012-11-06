/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-22 16:50:39 +0200 (Mi, 22 Sep 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultProcessInformationEntity.java $
 * $LastChangedRevision: 4816 $
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
package org.prowim.services.coreengine.entities.impl;

import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.entities.ProcessInformationEntity;
import org.prowim.utils.StringConverter;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the interface {@link ProcessInformationEntity}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4816 $
 */
@Stateless
public class DefaultProcessInformationEntity implements ProcessInformationEntity
{

    private static final Logger LOG = Logger.getLogger(DefaultProcessInformationEntity.class);

    @IgnoreDependency
    @EJB
    private ProcessEngine       processEngine;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ProcessInformationEntity#getProcessInformation(java.lang.String)
     */
    @Override
    public ProcessInformation getProcessInformation(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        final RecordSet records = processEngine.getProcessinformation(id);

        if (records.getNoOfRecords() > 0)
        {
            Hashtable<String, String> record = records.getRecords()[0];

            String name = record.get(ProcessEngineConstants.Variables.Common.NAME_EN);
            String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            String description = record.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
            String editable = record.get(ProcessEngineConstants.Variables.Common.EDITABLE);
            boolean editableBooleanValue = StringConverter.strToBool(editable);

            ProcessInformation processInformation = DefaultDataObjectFactory.createProcessInformation(id, name, createTime);
            processInformation.setDescription(description);
            processInformation.setEditable(editableBooleanValue);
            LOG.debug("Returns processinformation :  " + id + "  " + name);
            return processInformation;

        }
        else
        {
            throw new IllegalStateException("Could not create processinformation with id =  " + id);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ProcessInformationEntity#getPossibleSelection(java.lang.String)
     */
    @Override
    public StringArray getPossibleSelection(String processInformationID) throws OntologyErrorException

    {
        Validate.notNull(processInformationID);
        final StringArray result = new StringArray();
        final RecordSet records = processEngine.getPossibleSelection(processInformationID);

        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> prcessInformationRecord = records.getRecords()[i];
                result.add(prcessInformationRecord.get("?value"));

            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ProcessInformationEntity#setPossibleSelection(java.lang.String, org.prowim.datamodel.collections.StringArray)
     */
    @Override
    public void setPossibleSelection(String processInformationID, StringArray values) throws OntologyErrorException
    {
        processEngine.clearSlot(processInformationID, "Auswahl-Liste");
        Iterator<String> it = values.iterator();
        while (it.hasNext())
        {
            String element = it.next();
            processEngine.setSlotValue(processInformationID, "Auswahl-Liste", element);
        }

    }
}
