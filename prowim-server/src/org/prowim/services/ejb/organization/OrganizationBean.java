/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-07 14:55:21 +0200 (Di, 07 Sep 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/organization/OrganizationBean.java $
 * $LastChangedRevision: 4772 $
 *------------------------------------------------------------------------------
 * (c) 01.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.ejb.organization;

import java.util.Arrays;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.organization.OrganizationRemote;
import org.prowim.services.interceptors.ChangeOrganizationElementInterceptor;



/**
 * Implements the {@link OrganizationRemote} interface.
 * 
 * @author Saad Wardi
 * @version $Revision: 4772 $
 */

@Stateless(name = ServiceConstants.PROWIM_ORGANIZATION_BEAN)
@WebService(name = ServiceConstants.PROWIM_ORGANIZATION_SERVICE_NAME, serviceName = ServiceConstants.PROWIM_ORGANIZATION_SERVICE_NAME, endpointInterface = ServiceConstants.PROWIM_ORGANIZATION_SEI)
@WebContext(contextRoot = "/" + ServiceConstants.CONTEXT_ROOT)
public class OrganizationBean implements OrganizationRemote
{
    @IgnoreDependency
    @EJB
    private OrganizationEntity organizationEntity;

    @IgnoreDependency
    @EJB
    private ProcessHelper      processHelper;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#getOrganizations()
     */
    public OrganisationArray getOrganizations() throws OntologyErrorException
    {
        return organizationEntity.getOrganizations();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#getTopOrganizations()
     */
    @Override
    public OrganisationArray getTopOrganizations() throws OntologyErrorException
    {
        return organizationEntity.getTopOrganizations();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#getSubOrganizations(java.lang.String)
     */
    @Override
    public OrganisationArray getSubOrganizations(String orgaID) throws OntologyErrorException
    {
        return organizationEntity.getSubOrganizations(orgaID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#getUsers()
     */
    public PersonArray getUsers() throws OntologyErrorException
    {
        PersonArray res = new PersonArray();
        Person[] personArray = processHelper.getUsers();
        if (personArray != null)
        {
            res.setItem(Arrays.asList(personArray));
        }

        return res;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#createOrganization(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Interceptors(ChangeOrganizationElementInterceptor.class)
    @Override
    public Organization createOrganization(String name, String adress, String email, String telefon, String description)
                                                                                                                        throws OntologyErrorException
    {
        return organizationEntity.createOrganization(name, adress, email, telefon, description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#createOrganization(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Interceptors(ChangeOrganizationElementInterceptor.class)
    @Override
    public Organization createOrganization(String name, String adress, String email, String telefon, String description, String parentOrgaID)
                                                                                                                                             throws OntologyErrorException
    {
        return organizationEntity.createOrganization(name, adress, email, telefon, description, parentOrgaID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#deleteOrganization(java.lang.String)
     */
    public void deleteOrganization(String organizationID) throws OntologyErrorException
    {
        organizationEntity.deleteOrganization(organizationID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#updateOrganization(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Interceptors(ChangeOrganizationElementInterceptor.class)
    @Override
    public Organization updateOrganization(String orgaID, String name, String address, String email, String telefon, String description)
                                                                                                                                        throws OntologyErrorException
    {
        return organizationEntity.updateOrgaInfo(orgaID, name, address, email, telefon, description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#getMembers(java.lang.String)
     */
    @Override
    public PersonArray getMembers(String organizationID) throws OntologyErrorException
    {
        return organizationEntity.getMembers(organizationID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Interceptors(ChangeOrganizationElementInterceptor.class)
    @Override
    public Person createUser(String name, String firstName, String lastName, String adress, String email, String telefon, String organizationID,
                             String password) throws OntologyErrorException
    {
        return organizationEntity.createUser(name, firstName, lastName, adress, email, telefon, organizationID, password);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#updateUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */

    @Interceptors(ChangeOrganizationElementInterceptor.class)
    @Override
    public Person updateUser(String userID, String name, String firstName, String lastName, String address, String email, String telefon,
                             String description, String password) throws OntologyErrorException
    {
        return organizationEntity.updateUserInfo(userID, name, firstName, lastName, address, email, telefon, description, password);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#getPreSelection(java.lang.String)
     */
    @Override
    public PersonArray getPreSelection(String roleID) throws OntologyErrorException
    {
        return organizationEntity.getPreSelection(roleID);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#setPreSelection(java.lang.String, org.prowim.datamodel.collections.PersonArray)
     */
    @Override
    public void setPreSelection(String roleID, PersonArray presSelection) throws OntologyErrorException
    {
        organizationEntity.setPreSelection(roleID, presSelection);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#setPersonAsMember(java.lang.String, java.lang.String)
     */

    @Interceptors(ChangeOrganizationElementInterceptor.class)
    @Override
    public void setPersonAsMember(String userID, String organizationID) throws OntologyErrorException
    {
        organizationEntity.setPersonAsMember(userID, organizationID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#addRoleToOrganization(java.lang.String, java.lang.String)
     */
    @Override
    public void addRoleToOrganization(String roleID, String organizationID) throws OntologyErrorException
    {
        organizationEntity.addRoleToOrganization(roleID, organizationID);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#getRolesToOrganization(java.lang.String)
     */
    @Override
    public RoleArray getRolesToOrganization(String organizationID) throws OntologyErrorException
    {
        return organizationEntity.getRolesToOrganization(organizationID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#removeRolesFromOrganization(org.prowim.datamodel.collections.ObjectArray, java.lang.String)
     */
    @Override
    public void removeRolesFromOrganization(ObjectArray roles, String organizationID) throws OntologyErrorException
    {
        organizationEntity.removeRolesFromOrganization(roles, organizationID);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#moveOrganization(java.lang.String, java.lang.String)
     */
    @Override
    public void moveOrganization(String selectedOrganizationID, String parentOrganizationID) throws OntologyErrorException
    {
        organizationEntity.moveOrganization(selectedOrganizationID, parentOrganizationID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.organization.OrganizationRemote#moveOrganizationToTop(java.lang.String)
     */
    @Override
    public void moveOrganizationToTop(String organizationID) throws OntologyErrorException
    {
        organizationEntity.moveOrganizationToTop(organizationID);

    }

}
