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

package org.prowim.services.client;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.prowim.jaas.CustomUserPasswordHandler;
import org.prowim.jaas.SamplePrincipal;

import de.ebcot.tools.logging.Logger;


/**
 * A customized LoginModule for JAAS-Login.<br>
 * It initializes a customer client login module.
 * 
 * @author Saad Wardi
 * @version $Revision:$
 */
public class CustomClientLoginModule implements LoginModule
{

    private static final Logger LOG = Logger.getLogger(CustomClientLoginModule.class); // Creates a instance of a logger class
    private Subject             subject;
    private CallbackHandler     callbackHandler;                                      // container for callbacks which handle username, password and mandator
    private Principal           loginPrincipal;                                       // The principal set during login()
    private Object              loginCredential;                                      // The credential set during login()
    private Map<String, Object> sharedState;                                          // Shared state between login modules
    private boolean             loginFailed;                                          // A flag indicating if the login failed
    private String              username;                                             // the username to authenticate

    /**
     * {@inheritDoc}
     */
    public boolean abort() throws LoginException
    {
        username = null;
        loginCredential = null;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public boolean commit() throws LoginException
    {
        if (loginFailed)
            return false;

        // Add the login principal to the subject if is not there
        Set<Principal> principals = subject.getPrincipals();
        if ( !principals.contains(loginPrincipal))
            principals.add(loginPrincipal);
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
     */
    @SuppressWarnings("unchecked")
    public void initialize(Subject subject, CallbackHandler callbackHandler, @SuppressWarnings("rawtypes") Map sharedState,
                           @SuppressWarnings("rawtypes") Map options)
    {
        LOG.debug("INITILIZING THE CLIENT LOGIN MODULE:  ");
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;

    }

    /**
     * {@inheritDoc}
     */
    public boolean login() throws LoginException
    {
        loginFailed = true;
        getUserInfo();
        loginPrincipal = new SamplePrincipal(username);
        sharedState.put("javax.security.auth.login.name", loginPrincipal);
        sharedState.put("javax.security.auth.login.password", loginCredential);
        loginFailed = false;
        return true;
    }

    /**
     * Gather the credentials (username, password and mandator) from the Callbackhandler and creates the custom principal object
     */
    private void getUserInfo() throws LoginException
    {

        // There is no password sharing or we are the first login module. Get the username and password from the callback hander.
        if (callbackHandler == null)
            throw new LoginException("Error: no CallbackHandler available " + "to garner authentication information from the user");

        PasswordCallback pc = new PasswordCallback("thefrog", true);
        NameCallback nc = new NameCallback("kermit", "kermit");
        Callback[] callbacks = { nc, pc };

        char[] sp = { 't', 'h', 'e', 'f', 'r', 'o', 'g' };
        callbackHandler = new CustomUserPasswordHandler("kermit", sp);

        try
        {
            char[] password = { 't', 'h', 'e', 'f', 'r', 'o', 'g' };
            char[] tmpPassword;

            callbackHandler.handle(callbacks);
            username = nc.getName();

            tmpPassword = pc.getPassword();
            if (tmpPassword != null)
            {
                tmpPassword.getClass();
            }
            loginCredential = password;
        }
        catch (IOException ioe)
        {
            LoginException ex = new LoginException(ioe.toString());
            ex.initCause(ioe);
            throw ex;
        }
        catch (UnsupportedCallbackException uce)
        {
            LoginException ex = new LoginException("Error: " + uce.getCallback().toString() + ", not able to use this callback for username/password");
            ex.initCause(uce);
            throw ex;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean logout() throws LoginException
    {
        LOG.debug("Client logging off ...");
        @SuppressWarnings("rawtypes")
        Set principals = subject.getPrincipals();
        principals.remove(loginPrincipal);
        return true;
    }

}
