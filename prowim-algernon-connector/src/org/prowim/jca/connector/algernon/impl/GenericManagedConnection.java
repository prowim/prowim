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
import java.util.LinkedList;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import de.ebcot.tools.logging.Logger;


/**
 * A implementation of {@link ManagedConnection managed connection} which provides {@link LocalTransaction local transaction}.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 */
public class GenericManagedConnection implements ManagedConnection, LocalTransaction
{
    /**
     * Instance of the logger
     */
    private final static Logger                       LOG = Logger.getLogger(GenericManagedConnection.class);

    /**
     * The managed connection factory
     */
    private final ManagedConnectionFactory            managedConnectionFactory;

    /**
     * Additional request information about the connection
     */
    private final ConnectionRequestInfo               connectionRequestInfo;

    /**
     * A list of {@link ConnectionEventListener listeners } for connection events
     */
    private final LinkedList<ConnectionEventListener> connectionEventListeners;

    /**
     * The connection which are related to this managed connection
     */
    private AlgernonConnection                        algernonConnection;

    /**
     * 
     * Constructs the managed connection.
     * 
     * @param managedConnectionFactory the factory to construct the connection
     * @param connectionRequestInfo additional information about the request for a connection
     */
    public GenericManagedConnection(ManagedConnectionFactory managedConnectionFactory, ConnectionRequestInfo connectionRequestInfo)
    {
        this.managedConnectionFactory = managedConnectionFactory;
        this.connectionRequestInfo = connectionRequestInfo;
        connectionEventListeners = new LinkedList<ConnectionEventListener>();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#addConnectionEventListener(javax.resource.spi.ConnectionEventListener)
     */
    @Override
    public void addConnectionEventListener(ConnectionEventListener connEventListener)
    {
        LOG.debug("Add connection eventlistener " + connEventListener);
        this.connectionEventListeners.add(connEventListener);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#associateConnection(java.lang.Object)
     */
    @Override
    public void associateConnection(Object connection) throws ResourceException
    {
        LOG.debug("Associate connection " + connection);
        this.algernonConnection = (AlgernonConnection) connection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#cleanup()
     */
    @Override
    public void cleanup() throws ResourceException
    {
        LOG.debug("Clean up");
        if (algernonConnection != null)
        {
            algernonConnection.destroy();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#destroy()
     */
    @Override
    public void destroy() throws ResourceException
    {
        LOG.debug("Destroy");

        if (algernonConnection != null)
        {
            algernonConnection.destroy();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#getConnection(javax.security.auth.Subject, javax.resource.spi.ConnectionRequestInfo)
     */
    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException
    {
        LOG.debug("Get connection for " + connectionRequestInfo + " factory: " + managedConnectionFactory);
        algernonConnection = new AlgernonConnection(this, this.connectionRequestInfo,
                                                    ((GenericManagedConnectionFactory) managedConnectionFactory).getProtegeProject(),
                                                    ((GenericManagedConnectionFactory) managedConnectionFactory).getProtegeUser(),
                                                    ((GenericManagedConnectionFactory) managedConnectionFactory).getProtegePassword());
        return algernonConnection;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#getLocalTransaction()
     */
    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException
    {
        LOG.debug("Return local transaction: " + this);
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#getLogWriter()
     */
    @Override
    public PrintWriter getLogWriter() throws ResourceException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#getMetaData()
     */
    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException
    {
        return new ManagedConnectionMetaData()
        {

            @Override
            public String getUserName() throws ResourceException
            {
                return null;
            }

            @Override
            public int getMaxConnections() throws ResourceException
            {
                return 5;
            }

            @Override
            public String getEISProductVersion() throws ResourceException
            {
                return "1.0";
            }

            @Override
            public String getEISProductName() throws ResourceException
            {
                return "Algernon-Connector";
            }
        };
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#getXAResource()
     */
    @Override
    public XAResource getXAResource() throws ResourceException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#removeConnectionEventListener(javax.resource.spi.ConnectionEventListener)
     */
    @Override
    public void removeConnectionEventListener(ConnectionEventListener connEventListener)
    {
        this.connectionEventListeners.remove(connEventListener);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.ManagedConnection#setLogWriter(java.io.PrintWriter)
     */
    @Override
    public void setLogWriter(PrintWriter printWriter) throws ResourceException
    {
    }

    /**
     * 
     * Returns the request information for this connection.
     * 
     * @return can not be <code>NULL</code>
     */
    public ConnectionRequestInfo getConnectionRequestInfo()
    {
        return connectionRequestInfo;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.cci.LocalTransaction#begin()
     */
    @Override
    public void begin() throws ResourceException
    {
        LOG.debug("Begin transaction");
        this.algernonConnection.begin();
        this.fireConnectionEvent(ConnectionEvent.LOCAL_TRANSACTION_STARTED);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.cci.LocalTransaction#commit()
     */
    @Override
    public void commit() throws ResourceException
    {
        LOG.debug("Commit transaction");
        this.algernonConnection.commit();
        this.fireConnectionEvent(ConnectionEvent.LOCAL_TRANSACTION_COMMITTED);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.cci.LocalTransaction#rollback()
     */
    @Override
    public void rollback() throws ResourceException
    {
        LOG.debug("Rollback transaction");
        this.algernonConnection.rollback();
        this.fireConnectionEvent(ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK);
    }

    /**
     * 
     * Description.
     */
    public void close()
    {
        LOG.debug("Close Connection");
        this.fireConnectionEvent(ConnectionEvent.CONNECTION_CLOSED);
    }

    /**
     * 
     * Fire's the given event.
     * 
     * @param event a event constant from {@link ConnectionEvent connection event}
     */
    public void fireConnectionEvent(int event)
    {
        ConnectionEvent connectionEvent = new ConnectionEvent(this, event);
        connectionEvent.setConnectionHandle(this.algernonConnection);

        for (ConnectionEventListener connectionEventListener : this.connectionEventListeners)
        {
            switch (event)
            {
                case ConnectionEvent.LOCAL_TRANSACTION_STARTED:
                    connectionEventListener.localTransactionStarted(connectionEvent);
                    break;
                case ConnectionEvent.LOCAL_TRANSACTION_COMMITTED:
                    connectionEventListener.localTransactionCommitted(connectionEvent);
                    break;

                case ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK:
                    connectionEventListener.localTransactionRolledback(connectionEvent);
                    break;
                case ConnectionEvent.CONNECTION_CLOSED:
                    connectionEventListener.connectionClosed(connectionEvent);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Connection event" + event);
            }
        }
    }

}
