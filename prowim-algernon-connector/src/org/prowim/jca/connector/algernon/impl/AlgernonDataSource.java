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

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionFactory;

import org.prowim.jca.connector.algernon.Connection;
import org.prowim.jca.connector.algernon.DataSource;

import de.ebcot.tools.logging.Logger;


/**
 * The implementation of the {@link DataSource data source} to provide {@link Connection connections}, which are managed by a {@link ConnectionManager connection manager}.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 */
public class AlgernonDataSource implements DataSource
{

    private static final long              serialVersionUID = 4000296557514830679L;

    /**
     * A instance for the logger
     */
    private static final Logger            LOG              = Logger.getLogger(AlgernonDataSource.class);

    /**
     * A connection factory used by the {@link ConnectionManager connection manager}
     */
    private final ManagedConnectionFactory connectionFactory;

    /**
     * A connection manager for allocating a {@link Connection connection} from the {@link ManagedConnectionFactory factory}
     */
    private final ConnectionManager        connectionManager;
    private Reference                      reference;

    /**
     * 
     * Constructs the data source with given factory and manager.
     * 
     * @param connectionFactory a managed connection factory
     * @param connectionManager a connection manager
     */
    public AlgernonDataSource(ManagedConnectionFactory connectionFactory, ConnectionManager connectionManager)
    {
        this.connectionFactory = connectionFactory;
        this.connectionManager = connectionManager;

        LOG.debug("Algernon DataSource with factory " + connectionFactory + " and manager " + connectionManager + " created");

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.DataSource#getConnection()
     */
    @Override
    public Connection getConnection()
    {
        try
        {
            LOG.debug("Allocate Algernon Connection in Algernon DataSource from factory " + connectionFactory);
            return (AlgernonConnection) connectionManager.allocateConnection(connectionFactory, getConnectionRequestInfo());
        }
        catch (ResourceException e)
        {
            LOG.error("Could not get Connection from ConnectionManager: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 
     * Constructs additional request informations.
     * 
     * @return can not be <code>NULL</code>
     */
    private ConnectionRequestInfo getConnectionRequestInfo()
    {
        return new ConnectionRequestInfo()
        {

            @Override
            public boolean equals(Object obj)
            {
                return true;
            }

            @Override
            public int hashCode()
            {
                return 1;
            }
        };
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.naming.Referenceable#getReference()
     */
    @Override
    public Reference getReference() throws NamingException
    {
        return reference;
    }

    /**
     * 
     * Sets the given reference.
     * 
     * @param reference the reference to set
     * @see Referenceable#getReference()
     */
    public void setReference(Reference reference)
    {
        this.reference = reference;
    }

}
