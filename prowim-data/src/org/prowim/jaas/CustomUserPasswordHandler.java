/*==============================================================================
 * File $$Id: CustomUserPasswordHandler.java 4377 2010-07-21 12:29:53Z wardi $$
 * Project: BSC-Tool LISA
 *
 * $$LastChangedDate: 2010-07-21 14:29:53 +0200 (Wed, 21 Jul 2010) $$
 * $$LastChangedBy: wardi $$
 * $$HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/aspects/security/CustomUserPasswordHandler.java $$
 * $$LastChangedRevision: 4377 $$
 *------------------------------------------------------------------------------
 * (c) 2006 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;


/**
 * A customized {@link javax.security.auth.callback.CallbackHandler Callbackhandler} which handles the additional mandator attribute
 * 
 * @author Thomas Wiesner
 * @version $Revision: 4377 $
 * @since 25.09.2006 Last reviewed on: 25.09.2006
 */
public class CustomUserPasswordHandler implements CallbackHandler
{

    private transient final String username;
    private transient final char[] password;

    /**
     * Constructs the call back handler with given attributes
     * 
     * @param username the user to authenticate
     * @param password the users password
     */
    public CustomUserPasswordHandler(String username, char[] password)
    {
        this.username = username;
        this.password = password;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        for (int i = 0; i < callbacks.length; i++)
        {
            Callback c = callbacks[i];
            if (c instanceof NameCallback)
            {
                NameCallback nc = (NameCallback) c;
                nc.setName(username);
            }
            else if (c instanceof PasswordCallback)
            {
                PasswordCallback pc = (PasswordCallback) c;
                pc.setPassword(password);
            }
            else if (c instanceof TextInputCallback)
            {
                TextInputCallback tc = (TextInputCallback) c;
                tc.getClass();
                // String callbackPrompt = tc.getPrompt();

            }
            else
            {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback: " + c);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ");
        sb.append(this.getClass().getName() + " \n");
        sb.append("User: ");
        sb.append(username + " \n");
        sb.append("Password: ");
        sb.append(password.toString() + " \n");

        return sb.toString();
    }
}
