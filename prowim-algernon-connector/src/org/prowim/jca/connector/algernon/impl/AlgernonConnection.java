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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;

import org.algernon.Algernon;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.Record;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.algernon.Result;
import org.prowim.jca.connector.algernon.AlgernonRuntimeException;
import org.prowim.jca.connector.algernon.Connection;
import org.prowim.jca.connector.algernon.DataSource;
import org.prowim.jca.connector.algernon.OntologyErrorException;

import de.ebcot.tools.logging.Logger;


/**
 * The implementation of a {@link Connection connection}, which are provided by the {@link DataSource data source}.<br>
 * 
 * It provides execution of {@link Algernon algeron} rules. For that it initializes a {@link RuleMachine rule machine}, which communicates with a protege server.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 */
public class AlgernonConnection implements Connection, LocalTransaction
{
    /**
     * The instance for the logger
     */
    private final static Logger            LOG            = Logger.getLogger(AlgernonConnection.class);

    /**
     * The name of protege project on which rules are executed
     */
    private String                         protegeProject = null;

    /**
     * The {@link ManagedConnection managed connection} which interacts with this connection
     */
    private final GenericManagedConnection genericManagedConnection;

    /**
     * Additional request informations for this connection
     */
    private final ConnectionRequestInfo    connectionRequestInfo;

    /**
     * A machine which provides the access to {@link Algernon algernon} and a {@link AlgernonProtegeMultiUserKB knowledgebase}
     */
    private final RuleMachine              machine;

    /**
     * A cache for business rules
     */
    private Hashtable<Object, Object>      businessRules;

    /**
     * A cache for normal rules
     */
    private Hashtable<Object, Object>      normalRule;

    /**
     * 
     * Constructs this connection .
     * 
     * @param genericManagedConnection the {@link ManagedConnection managed} connection
     * @param connectionRequestInfo the request informations
     * @param protegeProject the protege project name
     * @param protegeUser the protege user,referred to the protege project
     * @param protegePassword the password fpr the protege user
     */
    public AlgernonConnection(GenericManagedConnection genericManagedConnection, ConnectionRequestInfo connectionRequestInfo, String protegeProject,
            String protegeUser, String protegePassword)
    {
        this.genericManagedConnection = genericManagedConnection;
        this.connectionRequestInfo = connectionRequestInfo;
        this.protegeProject = protegeProject;

        businessRules = new Hashtable<Object, Object>(50, (float) 0.75);
        normalRule = new Hashtable<Object, Object>(50, (float) 0.75);

        machine = RuleMachine.getInstance(protegeProject);

        LOG.debug("Connection created for " + protegeProject);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#executeActivityRule(java.lang.String, java.util.Hashtable)
     */
    @Override
    public RecordSet executeActivityRule(String ruleName, Hashtable<? , ? > parameters) throws OntologyErrorException
    {
        String query = getRuleBodyWithType(ruleName);
        String rule = null;
        String askTell = null;
        // / hier noch arbeiten
        RecordSet result = this.startRS(query, AlgernonConstants.ASK);
        RecordSet resultRS = null;
        if (result.getResult() == AlgernonConstants.OK)
        {
            // only the rule string is expected
            if (result.getNoOfRecords() == 1)
            {
                Hashtable<? , ? > record = result.getRecords()[0];

                String tmp = (String) record.get("?r");
                askTell = (String) record.get("?t");
                if (askTell == null || askTell.equals(""))
                {
                    askTell = AlgernonConstants.ASK;
                }
                char quotes = '"';
                char singleQuote = '\'';
                // remove quotes at start and end of rule
                String tmp2 = removeStartandEndQuotes(tmp);
                // replace single quote by double quotes
                rule = new String(tmp2.replace(singleQuote, quotes));

                String nextquery = AlgernonConstants.NEXT_QUERY_OPEN;
                Set<? > keys = parameters.keySet();
                Iterator<? > it = keys.iterator();
                int noOfEntries = parameters.size();
                for (int i = 0; i < noOfEntries; i++)
                {
                    String key = (String) it.next();
                    Object obj = parameters.get(key);
                    if (obj instanceof String)
                    {
                        String value = (String) obj;
                        nextquery += bindLispVars(key, value, false);
                    }
                    else if (obj instanceof AlgernonValue)
                    {
                        AlgernonValue algernonValue = (AlgernonValue) obj;
                        if (algernonValue.getWithQuotes())
                        {
                            nextquery += bindLispVars(key, algernonValue.getValue(), true);
                        }
                        else
                        {
                            nextquery += bindLispVars(key, algernonValue.getValue(), false);
                        }
                    }

                }

                nextquery += rule + AlgernonConstants.NEXT_QUERY_CLOSE;

                resultRS = this.startRS(nextquery, askTell);
            }
        }
        return resultRS;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.jca.connector.algernon.Connection#executeBusinessRule(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet executeBusinessRule(String ruleName, String n1, String v1, String n2, String v2) throws OntologyErrorException
    {
        RecordSet resultRS = null;

        String rule = this.checkBusinessRule(ruleName);

        String nextquery = AlgernonConstants.NEXT_QUERY_OPEN;
        if (n1 != null && v1 != null)
        {
            nextquery += bindLispVars(n1, v1, false);
        }
        if (n2 != null && v2 != null)
        {
            nextquery += bindLispVars(n2, v2, false);
        }
        nextquery += rule + AlgernonConstants.NEXT_QUERY_CLOSE;

        resultRS = this.startRS(nextquery, AlgernonConstants.ASK);

        return resultRS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#executeBusinessRule(java.lang.String, java.util.Hashtable, java.lang.String)
     */
    @Override
    public RecordSet executeBusinessRule(String ruleName, Hashtable<? , ? > parameters, String askTell) throws OntologyErrorException
    {
        RecordSet resultRS = null;

        String rule = this.checkBusinessRule(ruleName);

        String nextquery = AlgernonConstants.NEXT_QUERY_OPEN;
        Set<? > keys = parameters.keySet();
        Iterator<? > it = keys.iterator();
        int noOfEntries = parameters.size();
        for (int i = 0; i < noOfEntries; i++)
        {
            String key = (String) it.next();
            Object obj = parameters.get(key);
            if (obj instanceof String)
            {
                String value = (String) obj;
                nextquery += bindLispVars(key, value, false);
            }
            else if (obj instanceof AlgernonValue)
            {
                AlgernonValue algernonValue = (AlgernonValue) obj;
                if (algernonValue.getWithQuotes())
                {
                    nextquery += bindLispVars(key, algernonValue.getValue(), true);
                }
                else
                {
                    nextquery += bindLispVars(key, algernonValue.getValue(), false);
                }
            }

        }

        nextquery += rule + AlgernonConstants.NEXT_QUERY_CLOSE;

        resultRS = this.startRS(nextquery, askTell);

        return resultRS;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.jca.connector.algernon.Connection#executeBusinessRule(java.lang.String, java.util.Hashtable)
     */
    @Override
    public RecordSet executeBusinessRule(String ruleName, Hashtable<? , ? > parameters) throws OntologyErrorException
    {
        RecordSet resultRS = null;

        String rule = this.checkBusinessRule(ruleName);

        String nextquery = AlgernonConstants.NEXT_QUERY_OPEN;
        Set<? > keys = parameters.keySet();
        Iterator<? > it = keys.iterator();
        int noOfEntries = parameters.size();
        for (int i = 0; i < noOfEntries; i++)
        {
            String key = (String) it.next();
            Object obj = parameters.get(key);
            if (obj instanceof String)
            {
                String value = (String) obj;
                nextquery += bindLispVars(key, value, false);
            }
            else if (obj instanceof AlgernonValue)
            {
                AlgernonValue algernonValue = (AlgernonValue) obj;
                if (algernonValue.getWithQuotes())
                {
                    nextquery += bindLispVars(key, algernonValue.getValue(), true);
                }
                else
                {
                    nextquery += bindLispVars(key, algernonValue.getValue(), false);
                }
            }

        }

        nextquery += rule + AlgernonConstants.NEXT_QUERY_CLOSE;

        resultRS = this.startRS(nextquery, getRuleType(ruleName));

        return resultRS;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.jca.connector.algernon.Connection#executeRule(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet executeRule(String ruleName, String n1, String v1, String n2, String v2) throws OntologyErrorException
    {
        RecordSet resultRS = null;

        String rule = getRuleBody(ruleName);

        String nextquery = AlgernonConstants.NEXT_QUERY_OPEN;
        if (n1 != null && v1 != null)
        {
            nextquery += bindLispVars(n1, v1, false);
        }
        if (n2 != null && v2 != null)
        {
            nextquery += bindLispVars(n2, v2, false);
        }
        nextquery += rule + AlgernonConstants.NEXT_QUERY_CLOSE;

        resultRS = this.startRS(nextquery, AlgernonConstants.ASK);

        return resultRS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#executeRule(java.lang.String, java.util.Hashtable, java.lang.String)
     */
    @Override
    public RecordSet executeRule(String ruleName, Hashtable<? , ? > parameters, String askTell) throws OntologyErrorException
    {
        RecordSet resultRS = null;

        String rule = getRuleBody(ruleName);

        logRuleExecution(ruleName, rule, parameters);
        String nextquery = AlgernonConstants.NEXT_QUERY_OPEN;
        Set<? > keys = parameters.keySet();

        for (Object object : keys)
        {
            String key = (String) object;
            Object obj = parameters.get(key);
            if (obj instanceof String)
            {
                String value = (String) obj;
                nextquery += bindLispVars(key, value, false);
            }
            else if (obj instanceof AlgernonValue)
            {
                AlgernonValue algernonValue = (AlgernonValue) obj;
                if (algernonValue.getWithQuotes())
                {
                    nextquery += bindLispVars(key, algernonValue.getValue(), true);
                }
                else
                {
                    nextquery += bindLispVars(key, algernonValue.getValue(), false);
                }
            }
        }

        nextquery += rule + AlgernonConstants.NEXT_QUERY_CLOSE;

        LOG.debug("THE QUERY   " + nextquery);
        resultRS = this.startRS(nextquery, askTell);

        return resultRS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#getBusinessRules()
     */
    @Override
    public Hashtable<Object, Object> getBusinessRules()
    {
        return businessRules;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#getKB()
     */
    @Override
    public String getKB()
    {
        LOG.debug("getKB");
        String result = machine.getKB();
        return result;
    }

    /**
     * 
     * Initializes the {@link RuleMachine rule machine}.
     */
    private void initialize()
    {
        machine.init(protegeProject);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#getNormalRule()
     */
    @Override
    public Hashtable<Object, Object> getNormalRule()
    {
        return normalRule;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#loadRule(java.lang.String)
     */
    @Override
    public String loadRule(String ruleName) throws OntologyErrorException
    {
        String query = getRuleBody(ruleName);
        String resultString = null;
        Result result = this.start(query, AlgernonConstants.ASK);
        if (result.getResult().equals(AlgernonConstants.OK))
        {
            // only the rule string is expected
            if (result.getNumberOfRecords() == 1 && result.getNumberOfColumns() == 1)
            {
                Record[] records = (Record[]) result.getRecords().toArray();
                String[] fields = (String[]) records[0].getFields().toArray();
                String tmp = fields[0];
                char quotes = '"';
                char blank = ' ';
                resultString = tmp.replace(quotes, blank);
            }
        }
        return resultString;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#saveKB()
     */
    @Override
    public String saveKB() throws OntologyErrorException
    {
        return machine.saveKB();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#setBusinessRules(java.util.Hashtable)
     */
    @Override
    public void setBusinessRules(Hashtable<Object, Object> businessRules)
    {
        this.businessRules = businessRules;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#setNormalRule(java.util.Hashtable)
     */
    @Override
    public void setNormalRule(Hashtable<Object, Object> normalRule)
    {
        this.normalRule = normalRule;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#start(java.lang.String, java.lang.String)
     */
    @Override
    public Result start(String query, String askTell) throws OntologyErrorException
    {
        LOG.debug("StartQuery query = " + query);
        Result result = machine.start(query, askTell);
        logQueryExecution(query);
        LOG.debug("Start beendet");
        LOG.debug(query);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#startKV(java.lang.String, java.lang.String)
     */
    @Override
    public RecordsetKV startKV(String query, String askTell) throws OntologyErrorException
    {
        LOG.debug("StartQuery query = " + query);
        RecordsetKV result = machine.startKV(query, askTell);
        LOG.debug("StartKV beendet");
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#startRS(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet startRS(String query, String askTell) throws OntologyErrorException
    {
        RecordSet result = machine.startRS(query, askTell);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.LocalTransaction#begin()
     */
    @Override
    public void begin() throws ResourceException
    {
        LOG.debug("'begin': Initialize Connection with RuleMachine");
        initialize();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.LocalTransaction#commit()
     */
    @Override
    public void commit() throws ResourceException
    {
        LOG.debug("'commit': Save KB");
        try
        {
            machine.saveKB();
        }
        catch (OntologyErrorException e)
        {
            LOG.error("An error occurs while saving knowledge base: ", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.resource.spi.LocalTransaction#rollback()
     */
    @Override
    public void rollback() throws ResourceException
    {
        LOG.debug("'rollback': Must be clarified");
    }

    /**
     * remove the start and end quotes.
     * 
     * @param input the input
     * @return formatted input
     */
    private String removeStartandEndQuotes(String input)
    {

        byte[] bytes = input.getBytes();
        for (int i = 0; i < 3; i++)
        {
            if (bytes[i] == '"')
            {
                bytes[i] = ' ';
            }
        }

        for (int i = (bytes.length - 1); i > (bytes.length) - 3; i--)
        {
            if (bytes[i] == '"')
            {
                bytes[i] = ' ';
            }
        }
        return new String(bytes);
    }

    /**
     * Reads the rule-code from the RuleBody slot.
     * 
     * @param ruleName the name of the rule to be loaded.
     * @return the rule code.
     * @throws AlgernonRuntimeException if an runtime exception in algernon occurs
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    private String getRuleBody(String ruleName) throws OntologyErrorException
    {
        String ruleBody = getRule(getTheRuleFromBody(ruleName));
        String rule = prepareRuleFromBody(ruleBody);
        return rule;

    }

    private String getRuleBodyWithType(String ruleName)
    {

        return "((RuleBody \"" + ruleName + "\" ?r)(RuleType \"" + ruleName + "\" ?t))";
    }

    private String getTheRuleFromBody(String ruleName)
    {
        return "((:BIND ?slot  RuleBody)(?slot \"" + ruleName + "\"  ?r))";
    }

    private String getRule(String query) throws OntologyErrorException
    {
        Result record = this.start(query, "ASK");
        if (record.getResult().equals(AlgernonConstants.ERROR))
        {
            LOG.error("Could not find rule for query: " + query);
        }
        Record rec = record.getRecords().get(0);
        List<String> recList = rec.getFields();

        for (String v : recList)
        {
            if (v.charAt(0) == '"')
            {
                return v;
            }
        }

        return rec.getFields().get(0);
    }

    /**
     * a method to prepare the rule.
     * 
     * @param result the result
     * @return
     */
    private String prepareRuleFromBody(String ruleBody)
    {
        String rule = null;

        // only the rule string is expected
        String tmp = ruleBody;
        char quotes = '"';
        char singleQuote = '\'';
        // remove quotes at start and end of rule
        String tmp2 = removeStartandEndQuotes(tmp);
        // replace single quote by double quotes
        rule = new String(tmp2.replace(singleQuote, quotes));

        return rule;
    }

    private String bindLispVars(String left, String right, boolean withQuotes)
    {
        if ( !withQuotes)
        {
            return "(:BIND " + left + " " + right + " )\n";
        }
        else
        {
            return "(:BIND " + left + " \"" + right + "\" )\n";
        }
    }

    /**
     * check.
     * 
     * @param ruleName the rule name
     * @return the result
     * @throws AlgernonRuntimeException if an runtime exception in algernon occurs
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    private String checkBusinessRule(String ruleName) throws OntologyErrorException
    {
        String rule = null;

        rule = (String) this.businessRules.get(ruleName);
        if (rule == null)
        {
            String query = "((RuleBody \"" + ruleName + "\" ?r))";

            Result result = this.start(query, AlgernonConstants.ASK);
            rule = prepareRule(result);
            if (rule != null)
            {
                this.businessRules.put(ruleName, rule);
            }
        }
        return rule;
    }

    /**
     * a method to prepare the rule.
     * 
     * @param result the result
     * @return
     */
    private String prepareRule(Result result)
    {
        String rule = null;
        if (result.getResult().equals(AlgernonConstants.OK))
        {
            // only the rule string is expected
            if (result.getNumberOfRecords() == 1 && result.getNumberOfColumns() == 1)
            {
                String[] fields = (String[]) result.getRecords().get(0).getFields().toArray();
                String tmp = fields[0];
                char quotes = '"';
                char singleQuote = '\'';
                // remove quotes at start and end of rule
                String tmp2 = removeStartandEndQuotes(tmp);
                // replace single quote by double quotes
                rule = new String(tmp2.replace(singleQuote, quotes));
            }
        }
        return rule;
    }

    /**
     * Get the rule type of given business rule name.
     * 
     * @param ruleName the rule name
     * @return the rule type. Can be TELL or ASK
     * @throws AlgernonRuntimeException if an runtime exception in algernon occurs
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    private String getRuleType(String ruleName) throws OntologyErrorException
    {
        String ruleType = null;

        String query = "((RuleType \"" + ruleName + "\" ?r))";

        Result result = this.start(query, AlgernonConstants.ASK);
        ruleType = prepareRule(result);
        LOG.debug("RULE TYPE of " + ruleName + " is " + ruleType);
        return ruleType;
    }

    private void logRuleExecution(String ruleName, String ruleBody, Hashtable<? , ? > parameters)
    {

        Set<? > keySet = parameters.keySet();
        Object[] keys = keySet.toArray();

        LOG.debug("\n\nexecuting rule:     " + ruleName);
        LOG.debug("params:    ");
        String params = "";

        for (int i = 0; i < keys.length; i++)
        {
            Object obj = parameters.get(keys[i]);

            if (obj instanceof String)
            {
                params = (String) obj;
            }
            else if (obj instanceof AlgernonValue)
            {
                AlgernonValue algernonValue = (AlgernonValue) obj;
                params = algernonValue.getValue();
            }
            params = keys[i] + " = " + params;
            if (i < keys.length - 1)
            {
                params += " , ";
            }
        }
        LOG.debug(params + "\n      RuleBody :  " + ruleBody + "\n");
    }

    private void logQueryExecution(String query)
    {
        LOG.debug("\n Executing query :  " + query + "\n");
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.jca.connector.algernon.Connection#close()
     */
    @Override
    public void close()
    {
        LOG.debug("Close connection " + this);
        genericManagedConnection.close();
    }

    /**
     * 
     * Destroy this connection.
     */
    public void destroy()
    {
        businessRules.clear();
        normalRule.clear();
    }

    /**
     * Returns the request information about this connection
     * 
     * @return the connectionRequestInfo, <code>NULL</code>
     */
    public ConnectionRequestInfo getConnectionRequestInfo()
    {
        return connectionRequestInfo;
    }

}
