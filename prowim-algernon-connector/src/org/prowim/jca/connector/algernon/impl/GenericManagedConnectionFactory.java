/*==============================================================================
 * File $Id$
 * Project: LISA
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 10.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.jca.connector.algernon.impl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;


/**
 * A implementation for a managed connection factory.<br>
 * It is responsible to create the {@link AlgernonDataSource data source}
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 */
public class GenericManagedConnectionFactory implements ManagedConnectionFactory, Serializable
{

    private static final long serialVersionUID = -7491619166812283373L;

    /**
     * The protege project name
     */
    private String            protegeProject;

    /**
     * The protege user for the project
     */
    private String            protegeUser;

    /**
     * The password for the protege user
     */
    private String            protegePassword;

    /**
     * 
     * Constructs this factory.
     */
    public GenericManagedConnectionFactory()
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory()
     */
    @Override
    public Object createConnectionFactory() throws ResourceException
    {
        return new AlgernonDataSource(this, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory(javax.resource.spi.ConnectionManager)
     */
    @Override
    public Object createConnectionFactory(ConnectionManager connectionManager) throws ResourceException
    {
        return new AlgernonDataSource(this, connectionManager);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject, javax.resource.spi.ConnectionRequestInfo)
     */
    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException
    {
        return new GenericManagedConnection(this, connectionRequestInfo);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#getLogWriter()
     */
    @Override
    public PrintWriter getLogWriter() throws ResourceException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#matchManagedConnections(java.util.Set, javax.security.auth.Subject, javax.resource.spi.ConnectionRequestInfo)
     */
    @Override
    public ManagedConnection matchManagedConnections(@SuppressWarnings("rawtypes") Set connectionSet, Subject subject,
                                                     ConnectionRequestInfo connectionRequestInfo) throws ResourceException
    {
        for (@SuppressWarnings("rawtypes")
        Iterator connectionIterator = connectionSet.iterator(); connectionIterator.hasNext();)
        {
            GenericManagedConnection managedConnection = (GenericManagedConnection) connectionIterator.next();
            ConnectionRequestInfo requestInfo = managedConnection.getConnectionRequestInfo();

            if ((connectionRequestInfo == null) || requestInfo.equals(connectionRequestInfo))
            {
                return managedConnection;
            }
            throw new ResourceException("Cannot find connection ");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#setLogWriter(java.io.PrintWriter)
     */
    @Override
    public void setLogWriter(PrintWriter printWriter) throws ResourceException
    {
    }

    /**
     * Sets the name of the protege project
     * 
     * @param protegeProject the name of protege project to set
     */
    public void setProtegeProject(String protegeProject)
    {
        this.protegeProject = protegeProject;
    }

    /**
     * Returns the name of the protege project
     * 
     * @return the,name, can not be <code>NULL</code>
     */
    public String getProtegeProject()
    {
        return protegeProject;
    }

    /**
     * Sets the protege user for the protege project
     * 
     * @param protegeUser the protegeUser to set
     */
    public void setProtegeUser(String protegeUser)
    {
        this.protegeUser = protegeUser;
    }

    /**
     * Returns the protege user for the protege project
     * 
     * @return the protegeUser, can not be <code>NULL</code>
     */
    public String getProtegeUser()
    {
        return protegeUser;
    }

    /**
     * Sets the password for the proetege user
     * 
     * @param protegePassword the protegePassword to set
     */
    public void setProtegePassword(String protegePassword)
    {
        this.protegePassword = protegePassword;
    }

    /**
     * Returns the password for the protege user
     * 
     * @return the protegePassword, can not be <code>NULL</code>
     */
    public String getProtegePassword()
    {
        return protegePassword;
    }

}
