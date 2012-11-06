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

package org.prowim.services.ejb.admin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.security.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OntologyVersionArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RightsRoleArray;
import org.prowim.datamodel.collections.UpdateFrameArray;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.datamodel.prowim.UpdateFrame;
import org.prowim.datamodel.prowim.UpdateItem;
import org.prowim.datamodel.prowim.UpdateWork;
import org.prowim.datamodel.prowim.UpdatesLog;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.resources.ResourcesLocator;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.UpdateHelper;
import org.prowim.services.coreengine.entities.KnowledgeEntity;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.services.coreengine.entities.impl.DefaultSecurityManager;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.admin.AdminRemote;
import org.prowim.services.interceptors.BeansSecurityInterceptor;
import org.prowim.services.interceptors.ChangeProcessInterceptor;
import org.prowim.tools.mail.MailMessengerService;

import de.ebcot.tools.crypt.Encrypter;
import de.ebcot.tools.logging.Logger;


/**
 * The bean class provides the web service implementation for the prowim system info webservice.
 * 
 * @author Saad Wardi
 * @version $Revision: 5071 $
 */
@Stateless(name = ServiceConstants.PROWIM_ADMIN_BEAN)
@WebService(name = ServiceConstants.PROWIM_ADMIN_SERVICE_NAME, serviceName = ServiceConstants.PROWIM_ADMIN_SERVICE_NAME, endpointInterface = ServiceConstants.PROWIM_ADMIN_SEI)
@WebContext(contextRoot = "/" + ServiceConstants.CONTEXT_ROOT, urlPattern = "/*", authMethod = "BASIC", transportGuarantee = "NONE", secureWSDLAccess = false)
@SecurityDomain("JBossWS3")
public class AdminBean implements AdminRemote
{
    /** the logger. */
    private static final Logger  LOG = Logger.getLogger(AdminBean.class);

    @IgnoreDependency
    @EJB
    private CommonHelper         common;

    @IgnoreDependency
    @EJB
    private UpdateHelper         update;

    @IgnoreDependency
    @EJB
    private OrganizationEntity   organizationEntity;

    @IgnoreDependency
    @EJB
    private ProcessHelper        processHelper;

    @IgnoreDependency
    @EJB
    private KnowledgeEntity      knowledgeEntity;

    @IgnoreDependency
    @EJB
    private MailMessengerService mailMessengerService;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getOntologyVersion()
     */
    @Override
    public String getOntologyVersion() throws OntologyErrorException
    {
        return common.getOntologyVersion();

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getModelEditorVersion()
     */
    @Override
    public String getModelEditorVersion()
    {
        return ResourcesLocator.getModelEditorVersion();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getServerVersion()
     */
    @Override
    public String getServerVersion()
    {
        return ResourcesLocator.getServerVersion();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getVersions()
     */
    @Override
    public OntologyVersionArray getVersions() throws OntologyErrorException
    {
        return update.getVersions();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getUpdateFrames(java.lang.String)
     */
    @Override
    public UpdateFrameArray getUpdateFrames(String versionID) throws OntologyErrorException
    {
        return update.getUpdateFrames(versionID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getUpdateWork(java.lang.String)
     */
    @Override
    public UpdateWork getUpdateWork(String versionID) throws OntologyErrorException
    {
        return update.getUpdateWork(versionID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#executeUpdate(org.prowim.datamodel.prowim.UpdateWork)
     */
    @Override
    public ObjectArray executeUpdate(UpdateWork updateWork) throws OntologyErrorException
    {
        return update.executeUpdate(updateWork);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#executeUpdate(java.lang.String)
     */
    @Override
    public boolean executeUpdate(String updateScript) throws OntologyErrorException
    {
        return update.executeUpdateFromScript(updateScript);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#clean()
     */
    @Override
    public void clean() throws OntologyErrorException
    {
        update.clean();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#initKnowledgeBase()
     */
    @Override
    public void initKnowledgeBase()
    {
        update.initKnowledgeBase();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#searchPerson(java.lang.String)
     */
    @Override
    public PersonArray searchPerson(String keyWord) throws OntologyErrorException
    {
        return organizationEntity.searchPersons(keyWord);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#changePassword(java.lang.String, java.lang.String)
     */
    @Override
    public void changePassword(String userID, String newPassword) throws OntologyErrorException
    {
        organizationEntity.changePassword(userID, newPassword);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#login(java.lang.String, java.lang.String)
     */
    @Interceptors(BeansSecurityInterceptor.class)
    @RolesAllowed({ "prowim_role" })
    @Override
    public boolean login(String userName, String password) throws OntologyErrorException
    {
        return DefaultSecurityManager.getInstance().login(userName, password);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getUserWithName(java.lang.String)
     */
    @Override
    public Person getUserWithName(String userName) throws OntologyErrorException
    {
        Person result = organizationEntity.getUser(userName);
        if (result != null)
        {
            result.setAdmin(DefaultSecurityManager.getInstance().isPersonAdminUser(userName));
            result.setModeler(DefaultSecurityManager.getInstance().isPersonModelerUser(userName));
            result.setReader(DefaultSecurityManager.getInstance().isPersonReaderUser(result.getUserName()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getUserWithID(java.lang.String)
     */
    @Override
    public Person getUserWithID(String userID) throws OntologyErrorException
    {
        Person result = organizationEntity.getPerson(userID);
        if (result != null)
        {
            result.setAdmin(DefaultSecurityManager.getInstance().isPersonAdminUser(result.getUserName()));
            result.setModeler(DefaultSecurityManager.getInstance().isPersonModelerUser(result.getUserName()));
            result.setReader(DefaultSecurityManager.getInstance().isPersonReaderWithID(result.getID()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getRightsRoles(java.lang.String)
     */
    @Override
    public RightsRoleArray getRightsRoles(String userID) throws OntologyErrorException
    {
        return organizationEntity.getRightsRoles(userID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getExistingRightsRoles()
     */
    @Override
    public RightsRoleArray getExistingRightsRoles() throws OntologyErrorException
    {
        return organizationEntity.getRightsRoles();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#assigneRightsRolesToUser(java.lang.String, org.prowim.datamodel.collections.RightsRoleArray)
     */
    @Override
    public void assigneRightsRolesToUser(String userID, RightsRoleArray rightsRoles) throws OntologyErrorException
    {
        organizationEntity.assigneRightsRolesToUser(userID, rightsRoles);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#setElementCanModifyEntity(java.lang.String, org.prowim.datamodel.collections.ObjectArray)
     */
    @Override
    @Interceptors(ChangeProcessInterceptor.class)
    public void setElementCanModifyEntity(String entityID, ObjectArray elements) throws OntologyErrorException
    {

        Validate.notNull(elements);

        String className = "";

        final ObjectArray commitList = new ObjectArray();
        final HashMap<String, String> uniqueMap = new HashMap<String, String>();

        final PersonArray oldpersonsValues = this.getPersonCanModifyEntity(entityID);
        final OrganisationArray oldOrgasValues = this.getOrganizationsCanModifyEntity(entityID);

        final Iterator<Object> usersIt = elements.iterator();
        if (elements.size() > 0)
        {
            className = common.getDirectClassOfInstance((String) elements.getItem().get(0));
        }

        final Iterator<Person> personIt = oldpersonsValues.iterator();
        final Iterator<Organization> orgaIt = oldOrgasValues.iterator();

        while (usersIt.hasNext())
        {
            String userID = (String) usersIt.next();

            // DefaultSecurityManager.getInstance().setElementCanModifyEntity(entityID, userID);
            if ( !uniqueMap.containsKey(userID))
            {
                commitList.add(userID);
                uniqueMap.put(userID, userID);
            }
        }

        LOG.debug("MODIFIER CLASSNAME:  " + className);
        if (className.equals(Relation.Classes.ORGANIZATION))
        {
            while (personIt.hasNext())
            {
                Person person = personIt.next();
                if ( !uniqueMap.containsKey(person.getID()))
                {
                    commitList.add(person.getID());
                    uniqueMap.put(person.getID(), person.getID());
                }
                // DefaultSecurityManager.getInstance().setElementCanModifyEntity(entityID, userID);
            }
        }
        else if (className.equals(Relation.Classes.PERSON))
        {

            while (orgaIt.hasNext())
            {
                Organization orga = orgaIt.next();
                if ( !uniqueMap.containsKey(orga.getID()))
                {
                    commitList.add(orga.getID());
                    uniqueMap.put(orga.getID(), orga.getID());

                }
            }
        }

        /** 1. clear the existing values. */
        common.clearRelationValue(entityID, Relation.Slots.CAN_BE_CHANGED_FROM);

        /** 2. Iterate the collected unique values and commit them */
        Iterator<Object> valuesIt = commitList.iterator();

        while (valuesIt.hasNext())
        {
            String id = (String) valuesIt.next();
            LOG.debug("COMMIT ELEMENT:  " + id);

            DefaultSecurityManager.getInstance().setUserCanModifyEntity(entityID, id);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getPersonCanModifyEntity(java.lang.String)
     */
    @Override
    public PersonArray getPersonCanModifyEntity(String entityID) throws OntologyErrorException
    {
        return DefaultSecurityManager.getInstance().getPersonCanModifyEntity(entityID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getOrganizationsCanModifyEntity(java.lang.String)
     */
    @Override
    public OrganisationArray getOrganizationsCanModifyEntity(String entityID) throws OntologyErrorException
    {
        return DefaultSecurityManager.getInstance().getOrganizationsCanModifyEntity(entityID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getWikiURL()
     */
    @Override
    public String getWikiURL() throws OntologyErrorException
    {
        String result = "";
        KnowledgeRepositoryArray repositories = knowledgeEntity.getKnowledgeRepositories();
        Iterator<KnowledgeRepository> it = repositories.iterator();
        while (it.hasNext())
        {
            KnowledgeRepository repository = it.next();
            if (repository.getName().equals("ProWim Wiki"))
            {
                result = repository.getStorage();
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#addWebservice(java.lang.String, java.lang.String)
     */
    @Override
    @Deprecated
    public void addWebservice(String name, String description) throws OntologyErrorException
    {
        update.addWebservice(name, description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#canPersonModifyEntity(java.lang.String, java.lang.String)
     */
    @Override
    public boolean canPersonModifyEntity(String entityID, String personID) throws OntologyErrorException
    {
        return DefaultSecurityManager.getInstance().checkPersonCanModifyEntity(entityID, personID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#encryptPasswords()
     */
    @Override
    public boolean encryptPasswords() throws OntologyErrorException
    {
        PersonArray res = new PersonArray();
        Person[] personArray = processHelper.getUsers();
        if (personArray != null)
        {
            res.setItem(Arrays.asList(personArray));
        }
        Iterator<Person> it = res.iterator();
        while (it.hasNext())
        {
            Person p = it.next();
            String plainText = p.getPassword();
            common.clearRelationValue(p.getID(), Relation.Slots.PASSWORD);
            String encrypt = Encrypter.encryptMessage(plainText.toCharArray());
            common.setSlotValue(p.getID(), Relation.Slots.PASSWORD, encrypt);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#setVersionInvalid()
     */
    @Override
    public void setVersionInvalid() throws OntologyErrorException
    {
        update.setVersionInvalid();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#isVersionValid()
     */
    @Override
    public boolean isVersionValid()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#setVersionValid()
     */
    @Override
    public void setVersionValid() throws OntologyErrorException
    {
        update.setVersionValid();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getUpdates()
     */
    @Override
    @Deprecated
    public UpdateFrame getUpdates()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getWork()
     */
    @Override
    @Deprecated
    public UpdateWork getWork()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getItem()
     */
    @Override
    @Deprecated
    public UpdateItem getItem()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getLog()
     */
    @Override
    @Deprecated
    public UpdatesLog getLog()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getPermissions()
     */
    @Override
    @Deprecated
    public RightsRole getPermissions()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#sendMessage(java.lang.String, java.lang.String[], String, String)
     */
    @Override
    public void sendMessage(String content, String[] receiver, String subject, String footer)
    {
        mailMessengerService.addMessages(content, receiver, subject, footer);
        mailMessengerService.sendAllMessages();

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getPersonsOfRightRoles(java.lang.String)
     */
    @Override
    public PersonArray getPersonsOfRightRoles(String rightsRoleName) throws OntologyErrorException
    {
        return organizationEntity.getPersonsOfRightRoles(rightsRoleName);
    }

}
