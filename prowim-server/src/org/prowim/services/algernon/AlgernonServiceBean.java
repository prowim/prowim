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

package org.prowim.services.algernon;

import java.util.Hashtable;

import javax.annotation.Resource;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.algernon.Result;
import org.prowim.jca.connector.algernon.Connection;
import org.prowim.jca.connector.algernon.DataSource;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * The implementation of the {@link AlgernonService algernon service}.
 * 
 * @author Saad Wardi, Thomas Wiesner
 * @version $Revision: 4826 $
 */
@Stateless
public class AlgernonServiceBean implements AlgernonService
{
    @Resource(mappedName = "java:/algernon.DataSource")
    private DataSource dataSource;

    /**
     * Returns a Connection from the injected data source.
     * 
     * @return can not be <code>NULL</code>
     */
    private Connection getConnection()
    {
        Validate.notNull(dataSource, "The data source for algernon connector must not be null");
        return dataSource.getConnection();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#saveKB()
     */
    public String saveKB() throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        String saveKB;
        try
        {
            saveKB = currentConnection.saveKB();
        }
        finally
        {
            currentConnection.close();
        }

        return saveKB;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#start(java.lang.String, java.lang.String)
     */
    public Result start(String query, String askTell) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        Result start;
        try
        {
            start = currentConnection.start(query, askTell);
        }
        finally
        {
            currentConnection.close();
        }
        return start;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#startRS(java.lang.String, java.lang.String)
     */
    public RecordSet startRS(String query, String askTell) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        RecordSet startRS;
        try
        {
            startRS = currentConnection.startRS(query, askTell);
        }
        finally
        {
            currentConnection.close();
        }

        return startRS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#startKV(java.lang.String, java.lang.String)
     */
    public RecordsetKV startKV(String query, String askTell) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        RecordsetKV startKV;
        try
        {
            startKV = currentConnection.startKV(query, askTell);
        }
        finally
        {
            currentConnection.close();
        }
        return startKV;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#getKB()
     */
    public String getKB()
    {
        Connection currentConnection = getConnection();
        String kb = currentConnection.getKB();
        currentConnection.close();

        return kb;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#loadRule(java.lang.String)
     */
    public String loadRule(String ruleName) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        String loadRule;
        try
        {
            loadRule = currentConnection.loadRule(ruleName);
        }
        finally
        {
            currentConnection.close();
        }
        return loadRule;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#executeBusinessRule(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public RecordSet executeBusinessRule(String ruleName, String n1, String v1, String n2, String v2) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        RecordSet executeBusinessRule;
        try
        {
            executeBusinessRule = currentConnection.executeBusinessRule(ruleName, n1, v1, n2, v2);
        }
        finally
        {
            currentConnection.close();
        }

        return executeBusinessRule;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#executeBusinessRule(java.lang.String, java.util.Hashtable, java.lang.String)
     */
    public RecordSet executeBusinessRule(String ruleName, Hashtable<? , ? > parameters, String askTell) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        RecordSet executeBusinessRule;
        try
        {
            executeBusinessRule = currentConnection.executeBusinessRule(ruleName, parameters, askTell);
        }
        finally
        {
            currentConnection.close();
        }
        return executeBusinessRule;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#executeBusinessRule(java.lang.String, java.util.Hashtable)
     */
    public RecordSet executeBusinessRule(String ruleName, Hashtable<? , ? > parameters) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        RecordSet executeBusinessRule;
        try
        {
            executeBusinessRule = currentConnection.executeBusinessRule(ruleName, parameters);
        }
        finally
        {
            currentConnection.close();
        }
        return executeBusinessRule;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#executeRule(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public RecordSet executeRule(String ruleName, String n1, String v1, String n2, String v2) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        RecordSet executeRule;
        try
        {
            executeRule = currentConnection.executeRule(ruleName, n1, v1, n2, v2);
        }
        finally
        {
            currentConnection.close();
        }

        return executeRule;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#executeRule(java.lang.String, java.util.Hashtable, java.lang.String)
     */
    public RecordSet executeRule(String ruleName, Hashtable<? , ? > parameters, String askTell) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        try
        {
            return currentConnection.executeRule(ruleName, parameters, askTell);
        }
        finally
        {
            currentConnection.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#executeActivityRule(java.lang.String, java.util.Hashtable)
     */
    public RecordSet executeActivityRule(String ruleName, Hashtable<? , ? > parameters) throws OntologyErrorException
    {
        Connection currentConnection = getConnection();
        try
        {
            return currentConnection.executeActivityRule(ruleName, parameters);
        }
        finally
        {
            currentConnection.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#getBusinessRules()
     */
    public Hashtable<Object, Object> getBusinessRules()
    {
        Connection currentConnection = getConnection();
        Hashtable<Object, Object> businessRules = currentConnection.getBusinessRules();
        currentConnection.close();
        return businessRules;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#setBusinessRules(java.util.Hashtable)
     */
    public void setBusinessRules(Hashtable<Object, Object> businessRules)
    {
        Connection currentConnection = getConnection();
        currentConnection.setBusinessRules(businessRules);
        currentConnection.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#getNormalRule()
     */
    public Hashtable<Object, Object> getNormalRule()
    {
        Connection currentConnection = getConnection();
        Hashtable<Object, Object> normalRule = currentConnection.getNormalRule();
        currentConnection.close();
        return normalRule;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.algernon.AlgernonService#setNormalRule(java.util.Hashtable)
     */
    public void setNormalRule(Hashtable<Object, Object> normalRule)
    {
        Connection currentConnection = getConnection();
        currentConnection.setNormalRule(normalRule);
        currentConnection.close();
    }
}
