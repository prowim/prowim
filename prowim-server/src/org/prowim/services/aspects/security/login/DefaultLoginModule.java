/*==============================================================================
 * File $Id: DefaultLoginModule.java 4510 2010-08-02 10:37:49Z wiesner $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/aspects/security/login/DefaultLoginModule.java $
 * $LastChangedRevision: 4510 $
 *------------------------------------------------------------------------------
 * (c) 05.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.aspects.security.login;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.jboss.security.NestableGroup;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.callback.SecurityAssociationCallback;
import org.jboss.security.auth.spi.AbstractServerLoginModule;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.entities.impl.DefaultSecurityManager;

import de.ebcot.tools.logging.Logger;


/**
 * Title and description.
 * 
 * @author Saad Wardi
 * @version $Revision: 4510 $
 * @since 2.0
 */
public class DefaultLoginModule extends AbstractServerLoginModule
{
    /** the logger. */
    private static final Logger     LOG         = Logger.getLogger(DefaultLoginModule.class);
    private final static String     PROWIM_ROLE = "prowim_role";
    private final static String     ROLES       = "Roles";
    /** Password. */
    protected char[]                credential;
    /** the loged in principal. */
    protected Principal             identity;
    /** the roles. */
    private HashMap<String, char[]> userpasswordsMap;

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getIdentity()
     */
    @Override
    protected Principal getIdentity()
    {
        return identity;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#getRoleSets()
     */
    @Override
    protected Group[] getRoleSets() throws LoginException
    {
        HashMap<String, Group> setsMap = new HashMap<String, Group>();
        Group group = setsMap.get(ROLES);
        if (group == null)
        {
            group = new SimpleGroup(ROLES);
            setsMap.put(ROLES, group);
        }
        String name = PROWIM_ROLE;

        if (name != null)
            group.addMember(new SimplePrincipal(name));

        Group[] roleSets = new Group[setsMap.size()];
        setsMap.values().toArray(roleSets);

        return roleSets;

    }

    /**
     * Gets the username and password.
     * 
     * @return Object[0] = username and Object[1] = password.
     * @throws LoginException thrown on login.
     */
    protected Object[] getUsernameAndPassword() throws LoginException
    {
        Object[] info = { null, null };
        // prompt for a username and password
        if (callbackHandler == null)
        {
            throw new LoginException("Error: no CallbackHandler available " + "to collect authentication information");
        }
        LOG.debug("Current callbackHandler " + callbackHandler.toString());

        SecurityAssociationCallback callback = new SecurityAssociationCallback();
        Callback[] callbacks = { callback };
        String username = null;
        try
        {
            callbackHandler.handle(callbacks);

            Principal principal = callback.getPrincipal();
            LOG.debug("'getUsernameAndPassword()' found Principal " + principal);
            if (principal != null)
            {
                identity = principal;
                username = identity.getName();
            }
            LOG.debug("CALLBACK#getCredential  :  " + callback.getCredential().getClass() + "    value    " + callback.getCredential());

            char[] tmpPassword = new char[((String) callback.getCredential()).length()];
            // (char[]) callback.getCredential();
            for (int i = 0; i < ((String) callback.getCredential()).length(); i++)
            {
                tmpPassword[i] = ((String) callback.getCredential()).charAt(i);
            }
            if (tmpPassword != null)
            {
                credential = new char[tmpPassword.length];
                System.arraycopy(tmpPassword, 0, credential, 0, tmpPassword.length);
                callback.clearCredential();
            }
        }
        catch (IOException e)
        {
            LoginException le = new LoginException("Failed to get username/password");
            le.initCause(e);
            throw le;
        }
        catch (UnsupportedCallbackException e)
        {
            LoginException le = new LoginException("CallbackHandler does not support: " + e.getCallback());
            le.initCause(e);
            throw le;
        }
        info[0] = username;
        info[1] = credential;
        LOG.debug("Found username: " + username);
        return info;
    }

    /**
     * Gets the caller username.
     * 
     * @return caller username.
     */
    protected String getUsername()
    {
        String username = null;
        if (getIdentity() != null)
            username = getIdentity().getName();
        return username;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, @SuppressWarnings("rawtypes") Map sharedState,
                           @SuppressWarnings("rawtypes") Map options)
    {
        super.initialize(subject, callbackHandler, sharedState, options);
        LOG.debug("INITILAIZING THE JAAS SECURITY ASPECT!  ");
        initUserPasswordsMap();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#login()
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean login() throws LoginException
    {
        LOG.info("Using " + this.getClass().getName() + " for authentication.");
        // See if shared credentials exist
        if (super.login())
        {
            LOG.debug("DefaultLoginModule#login:   " + super.loginOk);
            setCredentials();
            return true;
        }

        super.loginOk = false;
        Object[] info = getUsernameAndPassword();
        String username = (String) info[0];
        char[] password = (char[]) info[1];

        if (username == null && password == null)
        {
            identity = unauthenticatedIdentity;
            LOG.debug("Authenticating as unauthenticatedIdentity=" + identity);
        }

        if (identity != unauthenticatedIdentity)
        {
            LOG.debug("Principal   identity != unauthenticatedIdentity");
            // LOG.debug("Principal " + identity.getClass() + "(" + identity.hashCode() + ") created " + identity);
            char[] expectedPassword = getUsersPassword();

            // Allow the storeDigestCallback to hash the expected password
            if ( !validatePassword(password, expectedPassword))
            {
                Throwable ex = new Throwable("Login not successful : ");
                FailedLoginException fle = new FailedLoginException("Password Incorrect/Password Required");
                LOG.debug("Bad password for username=" + username, ex);
                fle.initCause(ex);
                throw fle;
            }
        }

        if (getUseFirstPass())
        {
            LOG.debug("DefaultLoginModule#login:  UseFirstPass  " + super.loginOk);
            // Add the username and password to the shared state map
            sharedState.put("javax.security.auth.login.name", username);
            sharedState.put("javax.security.auth.login.password", credential);
        }
        super.loginOk = true;
        LOG.debug("User '" + identity + "' authenticated, loginOk=" + loginOk);
        return true;
    }

    /**
     * Sets the the login data.
     * 
     * @throws LoginException thrown if Failed to create principal.
     */
    protected void setCredentials() throws LoginException
    {
        // Setup our view of the user
        Object username = sharedState.get("javax.security.auth.login.name");
        LOG.debug("'login()' Loading shared credentials ..." + username);
        if (username instanceof Principal)
            identity = (Principal) username;
        else
        {
            String name = username.toString();
            try
            {
                identity = createIdentity(name);
            }
            catch (Exception e)
            {
                LOG.debug("Failed to create principal", e);
                throw new LoginException("Failed to create principal: " + e.getMessage());
            }
        }
        Object password = sharedState.get("javax.security.auth.login.password");
        if (password instanceof char[])
            credential = (char[]) password;
        else if (password != null)
        {
            String tmp = password.toString();
            credential = tmp.toCharArray();
        }
    }

    /**
     * Validates the inputPassword against the expectedPassword
     * 
     * @param password
     * @param expectedPassword
     * @return see main description
     */
    private boolean validatePassword(char[] password, char[] expectedPassword)
    {
        LOG.debug("Validate password " + String.copyValueOf(password) + " with expected " + String.copyValueOf(expectedPassword));
        if (password == null || expectedPassword == null)
            return false;
        boolean valid = String.copyValueOf(password).equals(String.copyValueOf(expectedPassword));
        LOG.debug("Validate password " + String.copyValueOf(password) + " with expected " + String.copyValueOf(expectedPassword) + "  -> " + valid);
        return valid;
    }

    /**
     * Gets the credential.
     * 
     * @return the creadential object.
     */
    protected Object getCredentials()
    {
        return credential;
    }

    /**
     * Gathers and validate the password from database for the given user with password and verifys the given mandator
     * 
     * @return user password found in database
     * @throws LoginException the login exception
     */
    protected char[] getUsersPassword() throws LoginException
    {
        String username = getUsername();
        credential = userpasswordsMap.get(username);
        LOG.debug("Get password for user " + username + " from Security manager");
        String password;
        try
        {
            password = DefaultSecurityManager.getInstance().getUserPassword(username);
        }
        catch (OntologyErrorException e)
        {
            LOG.error("Could not get user password for  " + username, e);
            throw new LoginException("Could not get user password for  " + username + " " + e.getMessage());
        }

        LOG.debug("Password from Security Manager found " + password);
        return password.toCharArray();
    }

    private void initUserPasswordsMap()
    {
        char[] usersPasswordKermit = { 't', 'h', 'e', 'f', 'r', 'o', 'g' };
        char[] usersPasswordSaad = { '1', '2', '3', 'a', 'b', 'd' };

        userpasswordsMap = new HashMap<String, char[]>();
        userpasswordsMap.put("kermit", usersPasswordKermit);
        userpasswordsMap.put("saad", usersPasswordSaad);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.jboss.security.auth.spi.AbstractServerLoginModule#commit()
     */
    @Override
    public boolean commit() throws LoginException
    {

        LOG.debug("commit, loginOk=" + loginOk);
        if ( !loginOk)
            return false;

        Set<Principal> principals = subject.getPrincipals();
        Principal identity = getIdentity();

        principals.add(identity);
        LOG.debug("Add Principal in commit-Phase: " + identity + " to " + principals);
        Group[] roleSets = getRoleSets();
        for (int g = 0; g < roleSets.length; g++)
        {
            Group group = roleSets[g];
            String name = group.getName();
            Group subjectGroup = createGroup(name, principals);
            if (subjectGroup instanceof NestableGroup)
            {
                /*
                 * A NestableGroup only allows Groups to be added to it so we need to add a SimpleGroup to subjectRoles to contain the roles
                 */
                SimpleGroup tmp = new SimpleGroup(ROLES);
                subjectGroup.addMember(tmp);
                subjectGroup = tmp;
            }
            // Copy the group members to the Subject group

            Enumeration<? extends Principal> members = group.members();
            while (members.hasMoreElements())
            {
                Principal role = members.nextElement();
                subjectGroup.addMember(role);
            }
        }
        return true;
    }
}
