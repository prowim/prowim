/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 *
 */

package org.prowim.services.ejb.activity;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.collections.ActivityArray;
import org.prowim.datamodel.collections.MeanArray;
import org.prowim.datamodel.collections.ProductArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.entities.ActivityEntity;
import org.prowim.services.coreengine.entities.MeanEntity;
import org.prowim.services.coreengine.entities.ProcessInformationEntity;
import org.prowim.services.coreengine.entities.RoleEntity;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.activity.ActivityRemote;



/**
 * Implements the functions for {@link ActivityRemote}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4772 $
 * 
 */
@Stateless(name = ServiceConstants.ACTIVITY_BEAN)
@WebService(name = ServiceConstants.PROWIM_ACTIVITY_SERVICE_NAME, serviceName = ServiceConstants.PROWIM_ACTIVITY_SERVICE_NAME, endpointInterface = ServiceConstants.PROWIM_ACTIVITY_SEI)
@WebContext(contextRoot = "/" + ServiceConstants.CONTEXT_ROOT)
public class ActivityBean implements ActivityRemote
{

    @IgnoreDependency
    @EJB
    private ActivityEntity           activityEntity;

    @IgnoreDependency
    @EJB
    private ProcessHelper            processHelper;

    @IgnoreDependency
    @EJB
    private RoleEntity               roleEntity;

    @IgnoreDependency
    @EJB
    private ProcessInformationEntity processInformationEntity;

    @IgnoreDependency
    @EJB
    private MeanEntity               meanEntity;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#getCurrentTasks(String)
     */
    public ActivityArray getCurrentTasks(String userID) throws OntologyErrorException
    {
        return activityEntity.getMyActiveActivities(userID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#getProcessRoles(java.lang.String)
     */
    public RoleArray getProcessRoles(String processID) throws OntologyErrorException
    {
        RoleArray roles = processHelper.getRolesForWorkFlow(processID);
        return roles;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#getActivityInputProduct(java.lang.String)
     */
    @Override
    public ProductArray getActivityInputProduct(String activityID) throws OntologyErrorException
    {
        return processHelper.getActivityInput(activityID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#getActivityOututProduct(java.lang.String)
     */
    @Override
    public ProductArray getActivityOututProduct(String activityID) throws OntologyErrorException
    {
        return processHelper.getActivityOutput(activityID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#saveOutputProducts(org.prowim.datamodel.collections.ProductArray)
     */
    @Override
    public void saveOutputProducts(ProductArray outputProducts) throws OntologyErrorException
    {
        processHelper.saveActivityOutputProducts(outputProducts);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#getPossibleSelection(java.lang.String)
     */
    @Override
    public StringArray getPossibleSelection(String processInformationID) throws OntologyErrorException
    {
        return processInformationEntity.getPossibleSelection(processInformationID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#setPossibleSelection(java.lang.String, org.prowim.datamodel.collections.StringArray)
     */
    public void setPossibleSelection(String processInformationID, StringArray values) throws OntologyErrorException
    {
        processInformationEntity.setPossibleSelection(processInformationID, values);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#getActivityRoles(java.lang.String)
     */
    @Override
    public RoleArray getActivityRoles(String activityID) throws OntologyErrorException
    {
        return roleEntity.getActivityRoles(activityID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#getControlFlow()
     */
    @Override
    @Deprecated
    public ControlFlow getControlFlow()
    {
        // TODO $Author: specht $ Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.activity.ActivityRemote#getActivityMeans(java.lang.String)
     */
    @Override
    public MeanArray getActivityMeans(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);
        return meanEntity.getActivityMeans(activityID);
    }
}
