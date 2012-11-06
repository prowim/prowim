/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-19 13:40:44 +0200 (Di, 19 Okt 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultActivityEntity.java $
 * $LastChangedRevision: 4930 $
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
package org.prowim.services.coreengine.entities.impl;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ActivityArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.entities.ActivityEntity;



/**
 * Implements the interface {@link ActivityEntity}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4930 $
 */
@Stateless
public class DefaultActivityEntity implements ActivityEntity
{

    @IgnoreDependency
    @EJB
    private ProcessEngine processEngine;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ActivityEntity#getActivity(java.lang.String)
     */
    @Override
    public Activity getActivity(String id) throws OntologyErrorException
    {
        final RecordSet activityRecords = processEngine.getActivity(id);
        if (activityRecords != null && activityRecords.getNoOfRecords() > 0)
        {
            /** Get the sub class ProcessElement attributes. */
            final String name = activityRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.NAME_EN);
            final String createTime = activityRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            final String description = activityRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);

            Activity resultActivity = DefaultDataObjectFactory.createActivity(id, createTime, name);
            resultActivity.setDescription(description);

            return resultActivity;
        }
        else
        {
            throw new IllegalStateException("Could not create activity from id = " + id);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ActivityEntity#getMyActiveActivities(java.lang.String)
     */
    @Override
    public ActivityArray getMyActiveActivities(final String userID) throws OntologyErrorException
    {
        final RecordSet result = processEngine.getMyActiveActivities(userID);
        final ActivityArray activities = new ActivityArray();
        if (result.getNoOfRecords() > 0)
        {
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {

                Hashtable<String, String> activityRecord = result.getRecords()[i];
                String activityID = activityRecord.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_ID);
                String createTime = activityRecord.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                String automatic = activityRecord.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_AUTOMATIC);
                boolean automaticValue = false;
                if (automatic != null && automatic.equals(ProcessEngineConstants.Variables.Common.ALGERNON_TRUE))
                {
                    automaticValue = true;
                }
                String status = activityRecord.get(ProcessEngineConstants.Variables.Activity.STATUS);
                String description = (activityRecord.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT));
                String name = activityRecord.get(ProcessEngineConstants.Variables.Common.DESCRIPTION);

                Activity activity = DefaultDataObjectFactory.createActivity(activityID, createTime, name);
                activity.setAutomatic(automaticValue);
                activity.setStatus(status);
                activity.setDescription(description);

                activity.setProcessName(activityRecord.get(ProcessEngineConstants.Variables.Process.PROCESS));
                String startTime = activityRecord.get(ProcessEngineConstants.Variables.Common.START);
                if (startTime == null || startTime.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                {
                    startTime = ProcessEngineConstants.Variables.Common.ZERO;
                }
                activity.setStartTime(startTime);
                boolean finishedValue = false;
                String finished = activityRecord.get(ProcessEngineConstants.Variables.Activity.FINISHED);
                String processID = activityRecord.get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
                if (finished != null && finished.equalsIgnoreCase(ProcessEngineConstants.Variables.Common.OK))
                {
                    finished = ProcessEngineConstants.Variables.Common.TRUE;
                    finishedValue = true;
                }
                else
                {
                    finished = ProcessEngineConstants.Variables.Common.FALSE;
                    finishedValue = false;
                }
                activity.setFinished(finishedValue);
                activity.setProcessID(processID);
                activities.add(activity);
            }
        }

        return activities;
    }
}
