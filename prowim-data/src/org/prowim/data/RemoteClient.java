/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 05.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
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
package org.prowim.data;

import java.net.URL;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.commons.lang.Validate;
import org.prowim.jaas.CustomUserPasswordHandler;
import org.prowim.proxy.ActivityService;
import org.prowim.proxy.AdminService;
import org.prowim.proxy.CommonService;
import org.prowim.proxy.DocumentService;
import org.prowim.proxy.EditorService;
import org.prowim.proxy.KnowledgeService;
import org.prowim.proxy.OntologyWSAccessService;
import org.prowim.proxy.OrganizationService;
import org.prowim.proxy.ProcessService;
import org.prowim.proxy.WorkflowService;
import org.prowim.services.ejb.activity.ActivityRemote;
import org.prowim.services.ejb.admin.AdminRemote;
import org.prowim.services.ejb.commons.CommonRemote;
import org.prowim.services.ejb.documents.DocumentRemote;
import org.prowim.services.ejb.editor.EditorRemote;
import org.prowim.services.ejb.knowledge.KnowledgeRemote;
import org.prowim.services.ejb.ontology.OntologyWSAccessRemote;
import org.prowim.services.ejb.organization.OrganizationRemote;
import org.prowim.services.ejb.process.ProcessRemote;
import org.prowim.services.ejb.workflow.WorkflowRemote;

import de.ebcot.tools.crypt.Encrypter;
import de.ebcot.tools.logging.Logger;


/**
 * The client to communicate with the server instance. All communication is bundled here.
 * 
 * @author Saad Wardi
 * @version $Revision$
 * @since 2.0
 */
public class RemoteClient
{
    /**
     * The Property key to the URL of the JAAS_Config
     */
    private static final String       JAAS_CONFIG_LOCATION_KEY = "java.security.auth.login.config";

    /**
     * Location of the JAAS-Config file in the Classpath
     */
    private static final String       AUTH_CONF                = "/auth.conf";

    private static final Logger       LOG                      = Logger.getLogger(RemoteClient.class);

    private static final RemoteClient INSTANCE                 = new RemoteClient();

    private final static String       DEFAULT_CONFIGURATION    = "client-login";
    private AdminRemote               adminService             = null;
    private DocumentRemote            documentService          = null;
    private ProcessRemote             processService           = null;
    private KnowledgeRemote           knowledgeService         = null;
    private ActivityRemote            activityService          = null;
    private OrganizationRemote        organizationService      = null;
    private CommonRemote              commonService            = null;
    private EditorRemote              editorService            = null;
    private OntologyWSAccessRemote    algernonService          = null;
    private WorkflowRemote            workflowService          = null;

    static
    {
        URL resource = RemoteClient.class.getResource(AUTH_CONF);
        LOG.debug("Put Security-Config from: " + resource);
        if (System.getProperty(JAAS_CONFIG_LOCATION_KEY) == null)
        {
            System.setProperty(JAAS_CONFIG_LOCATION_KEY, resource.toString());
        }
    }

    /**
     * Get the instance of RemoteClient. Please verify first, if you can use a method from the clients Cache directly, before using it directly via RMI.
     * 
     * @return this
     */
    public static RemoteClient getInstance()
    {
        return INSTANCE;
    }

    /**
     * Creates first a connection and authenticate to the server. With creating the connection the remoteManager is set.
     * 
     * @param username to login
     * @param password the password.
     * @throws LoginException the login exception.
     */

    public void createConnection(String username, String password) throws LoginException
    {
        String encodedPass = Encrypter.encryptMessage(password.toCharArray());
        char[] pwd = passwordStringToChar(encodedPass);

        LOG.debug("RemoteClient creates connection to server for user  ...  " + username);

        // verifying clients login module
        CustomUserPasswordHandler handler = new CustomUserPasswordHandler(username, pwd);

        LoginContext loginContext;
        try
        {
            loginContext = new LoginContext(DEFAULT_CONFIGURATION, handler);
            loginContext.login();
            LOG.debug("RemoteClient createConnection:  ");
        }
        catch (Exception e)
        {
            LOG.error("LoginException:  Could not Create connection to the server ", e);
            throw new LoginException("LoginException:  Could not authenticate user " + username);
        }

        /** init the admin service. */
        adminService = new AdminService().getProwimAdminServicePort();
        documentService = new DocumentService().getProwimDocumentServicePort();
        processService = new ProcessService().getProcessServicePort();
        activityService = new ActivityService().getProwimActivityServicePort();
        commonService = new CommonService().getProwimCommonServicePort();
        knowledgeService = new KnowledgeService().getProwimKnowledgeServicePort();
        organizationService = new OrganizationService().getOrganizationServicePort();
        editorService = new EditorService().getEditorServicePort();
        algernonService = new OntologyWSAccessService().getOntologyWSAccessServicePort();
        workflowService = new WorkflowService().getWorkflowServicePort();

        // register the services.
        Object[] services = { adminService, documentService, processService, activityService, commonService, knowledgeService, organizationService,
                editorService, algernonService, workflowService };
        initJNDIBindingProvider(services, username, pwd);
    }

    /**
     * Creates first a connection and authenticates to the server. With creating the connection the remoteManager is set.
     * 
     * @param username to login
     * @param password the password.
     * @param url the service url.
     * @param qname the service QName.
     * @throws LoginException the login exception.
     */

    public void createAdminConnection(String username, String password, URL url, QName qname) throws LoginException
    {
        char[] pwd = login(username, password);

        /** init the admin service. */
        adminService = new AdminService(url, qname).getProwimAdminServicePort();

        // register the services.
        Object[] services = { adminService };
        initJNDIBindingProvider(services, username, pwd);
    }

    /**
     * Creates first a connection and authenticates to the server. With creating the connection the remoteManager is set.
     * 
     * @param username to login
     * @param password the password.
     * @param url the service url.
     * @param qname the service QName.
     * @throws LoginException the login exception.
     */
    public void createAlgernonConnection(String username, String password, URL url, QName qname) throws LoginException
    {
        char[] pwd = login(username, password);

        /** init the algernonservice. */
        algernonService = new OntologyWSAccessService(url, qname).getOntologyWSAccessServicePort();

        // register the services.
        Object[] services = { algernonService };
        initJNDIBindingProvider(services, username, pwd);
    }

    /**
     * Description.
     * 
     * @param username
     * @param password
     * @return
     * @throws LoginException
     */
    private char[] login(String username, String password) throws LoginException
    {
        String encodedPass = Encrypter.encryptMessage(password.toCharArray());
        char[] pwd = passwordStringToChar(encodedPass);

        LOG.debug("RemoteClient creates connection to server for user  ...  " + username);
        // verifying clients login module
        CustomUserPasswordHandler handler;
        handler = new CustomUserPasswordHandler(username, pwd);

        LoginContext loginContext;
        try
        {

            loginContext = new LoginContext(DEFAULT_CONFIGURATION, handler);
            loginContext.login();
            LOG.debug("RemoteClient createConnection:  ");
        }
        catch (Exception e)
        {
            LOG.error("LoginException:  Could not Create connection to the server ", e);
            throw new LoginException("LoginException:  Could not authenticate user " + username);
        }
        return pwd;
    }

    /**
     * Convert the password as String to char.
     * 
     * @param password the password.
     * @return password as char[].
     */
    private char[] passwordStringToChar(String password)
    {
        Validate.notNull(password);
        char[] result = new char[password.length()];
        for (int i = 0; i < password.length(); i++)
        {
            result[i] = password.charAt(i);
        }
        return result;
    }

    /**
     * Initialize the JNDI RequestContext.
     * 
     * @param remoteEndPoint the service remote end point.
     * @param username the username.
     * @param pwd the password.
     */
    private void initJNDIBindingProvider(Object[] remoteEndPoints, String username, char[] pwd)
    {
        LOG.debug("Initialize JNDI RequestContext  for user:   " + username);

        for (int i = 0; i < remoteEndPoints.length; i++)
        {
            BindingProvider bp = (BindingProvider) remoteEndPoints[i];
            bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, username);
            bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, new String(pwd));
        }
    }

    /**
     * Returns the {@link AdminRemote} service endpoint.
     * 
     * @return {@link AdminRemote}.
     */
    public AdminRemote getAdminService()
    {
        if (adminService != null)
            return adminService;
        else
        {
            throw new IllegalStateException("Could not create AdminService . ");
        }
    }

    /**
     * Returns the {@link DocumentRemote} service endpoint.
     * 
     * @return {@link DocumentRemote}.
     */
    public DocumentRemote getDocumentService()
    {
        if (documentService != null)
            return documentService;
        else
        {
            throw new IllegalStateException("Could not create DocumentService . ");
        }
    }

    /**
     * Returns the {@link ProcessRemote} service endpoint.
     * 
     * @return {@link ProcessRemote}.
     */
    public ProcessRemote getProcessService()
    {
        if (processService != null)
            return processService;
        else
        {
            throw new IllegalStateException("Could not create ProcessService . ");
        }
    }

    /**
     * Returns the {@link ActivityRemote} service endpoint.
     * 
     * @return {@link ActivityRemote}.
     */
    public ActivityRemote getActivityService()
    {
        if (activityService != null)
            return activityService;
        else
        {
            throw new IllegalStateException("Could not create ActivityService . ");
        }
    }

    /**
     * Returns the {@link CommonRemote} service endpoint.
     * 
     * @return {@link CommonRemote}.
     */
    public CommonRemote getCommonService()
    {
        if (commonService != null)
            return commonService;
        else
        {
            throw new IllegalStateException("Could not create CommonService . ");
        }
    }

    /**
     * Returns the {@link KnowledgeRemote} service endpoint.
     * 
     * @return {@link KnowledgeRemote}.
     */
    public KnowledgeRemote getKnowledgeService()
    {
        if (knowledgeService != null)
            return knowledgeService;
        else
        {
            throw new IllegalStateException("Could not create KnowledgeService . ");
        }
    }

    /**
     * Returns the {@link OrganizationRemote} service endpoint.
     * 
     * @return {@link OrganizationRemote}.
     */
    public OrganizationRemote getOrganizationService()
    {
        if (organizationService != null)
            return organizationService;
        else
        {
            throw new IllegalStateException("Could not create OrganizationService . ");
        }
    }

    /**
     * Returns the {@link EditorRemote} service endpoint.
     * 
     * @return {@link EditorRemote}.
     */
    public EditorRemote getEditorService()
    {
        if (editorService != null)
            return editorService;
        else
        {
            throw new IllegalStateException("Could not create EditorService . ");
        }
    }

    /**
     * Returns the {@link OntologyWSAccessRemote} service endpoint.
     * 
     * @return {@link OntologyWSAccessRemote}.
     */
    public OntologyWSAccessRemote getAlgernonService()
    {
        if (algernonService != null)
            return algernonService;
        else
        {
            throw new IllegalStateException("Could not create AlgernonService . ");
        }
    }

    /**
     * Returns the {@link WorkflowRemote} service endpoint.
     * 
     * @return {@link WorkflowRemote}.
     */
    public WorkflowRemote getWorkflowService()
    {
        if (workflowService != null)
            return workflowService;
        else
        {
            throw new IllegalStateException("Could not create workflowService . ");
        }
    }
}
