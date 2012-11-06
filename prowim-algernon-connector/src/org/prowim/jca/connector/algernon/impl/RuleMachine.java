/*
 * RuleMachine.java
 *
 * Created on 28. Juni 2005, 17:36
 * 
 * This file is part of ProWim.

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

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.algernon.Algernon;
import org.algernon.datatype.BindingList;
import org.algernon.exception.AlgernonException;
import org.algernon.kb.AlgernonStorageException;
import org.algernon.kb.okbc.protege.AlgernonProtegeKB;
import org.algernon.util.ErrorSet;
import org.jatha.dynatype.LispValue;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.Field;
import org.prowim.datamodel.algernon.Record;
import org.prowim.datamodel.algernon.RecordKV;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.algernon.Result;
import org.prowim.jca.connector.algernon.AlgernonRuntimeException;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.jca.connector.algernon.OntologyFault;

import de.ebcot.tools.logging.Logger;


/**
 * RuleMachine class
 * 
 * @author BS
 */
public final class RuleMachine
{
    private final static Logger LOG          = Logger.getLogger(RuleMachine.class);
    /** the protege file. */
    private static String       projectName  = null;

    /**
     * A singleton instance
     */
    private static RuleMachine  ruleMachine;

    /** Creates a new instance of RuleMachine */
    private Algernon            algernon     = null;

    /** the Knowledge protege file. */
    private AlgernonProtegeKB   algProtegeKB = null;

    /**
     * 
     * Constructs this rule machine.
     * 
     * @param protegeProject
     */
    private RuleMachine(String protegeProject)
    {

        init(protegeProject);
    }

    /**
     * 
     * Returns a singleton instance of this rule machine.
     * 
     * @param protegeProject the absolute path to the protege project file
     * 
     * @return can not be <code>NULL</code>
     */
    public static RuleMachine getInstance(String protegeProject)
    {
        if (ruleMachine == null)
        {
            ruleMachine = new RuleMachine(protegeProject);
        }
        return ruleMachine;
    }

    /**
     * Asks the algernon rule rule_machine.
     * 
     * @param query the query.
     * @param errors the errors.
     * @return lisp value.
     * @throws AlgernonRuntimeException if runtime occurs in org.algernon
     */
    private synchronized LispValue ask(String query, ErrorSet errors) throws AlgernonRuntimeException
    {
        LispValue ask = null;
        try
        {
            ask = algernon.ask(query, errors);
        }
        catch (RuntimeException e)
        {
            String message = "A runtime exception occurs for query : " + query;
            LOG.error(message, e);
            throw new AlgernonRuntimeException(message, e);
        }
        return ask;
    }

    /**
     * Tells the algernon rule rule_machine.
     * 
     * @param query the query.
     * @param errors the errors.
     * @return the lisp value.
     * @throws AlgernonRuntimeException if runtime exception in org.algernon occurs
     */
    private synchronized LispValue tell(String query, ErrorSet errors) throws AlgernonRuntimeException
    {
        LispValue tell = null;
        try
        {
            tell = algernon.tell(query, errors);
        }
        catch (RuntimeException e)
        {
            String message = "A runtime exception occurs for query  " + query;
            LOG.error(message, e);
            throw new AlgernonRuntimeException(message, e);
        }
        return tell;
    }

    /**
     * Description.
     * 
     * @param protegeProject the protege project file
     * @return status
     */
    public synchronized String init(String protegeProject)
    {
        long start = System.currentTimeMillis();
        LOG.debug("Initialize RuleBase with : " + protegeProject);
        try
        {
            algernon = new Algernon();
            algProtegeKB = new AlgernonProtegeKB(algernon, protegeProject);
            algernon.addKB(algProtegeKB);
            projectName = protegeProject;
            LOG.debug("Rule machine init in " + (System.currentTimeMillis() - start) + " ms");
            return AlgernonConstants.OK;
        }
        catch (AlgernonException e)
        {
            if (algProtegeKB != null)
                algProtegeKB.close();
            algProtegeKB = null;
            algernon = null;

            LOG.error(AlgernonConstants.ERROR + " Unable to open the '" + protegeProject + "' KB." + e);
            return AlgernonConstants.ERROR + " Unable to open the '" + protegeProject + "' KB.";
        }

    }

    /**
     * Gets the KB.
     * 
     * @return the KB.
     */
    public synchronized String getKB()
    {
        if (algProtegeKB == null)
        {
            return "";
        }
        else
        {
            return projectName;
        }
    }

    /**
     * Stops the rule rule_machine.
     * 
     * @return the status.
     */
    public synchronized String stop()
    {

        try
        {
            if (algProtegeKB != null)
                algProtegeKB.close();
            algProtegeKB = null;
            algernon = null;
            return AlgernonConstants.OK;

        }
        catch (Exception e)
        {
            return AlgernonConstants.ERROR + " Unable to close the KB.";

        }
    }

    /**
     * reinitialize.
     * 
     * @return status
     */
    public synchronized String reInit()
    {

        LOG.info("Reinitializing the knowledgebase");
        try
        {
            this.saveKB();
            LOG.debug(AlgernonConstants.OK);
            return AlgernonConstants.OK;
        }
        catch (Exception e)
        {
            LOG.info("ERROR");
            return AlgernonConstants.ERROR + " Unable to reinit the KB.";

        }
    }

    /**
     * Saves changes to the knowledge base file.
     * 
     * @return the status.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public synchronized String saveKB() throws OntologyErrorException
    {
        try
        {
            if (algProtegeKB != null)
                algProtegeKB.save();
            return AlgernonConstants.OK;
        }
        catch (AlgernonStorageException e)
        {
            String message = " Unable to save the KB";
            LOG.error(message, e);

            OntologyFault ontologyFault = new OntologyFault();
            ontologyFault.setMessage(message);

            throw new OntologyErrorException(message, ontologyFault, e);
        }
    }

    /**
     * Starts a query.
     * 
     * @param query the query.
     * @param askTell ask or tell.
     * @return an algernon result set.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public synchronized Result start(String query, String askTell) throws OntologyErrorException
    {
        LispValue lispResult;
        String resultString = new String();
        Result result = null;
        try
        {
            ErrorSet errors = new ErrorSet();
            if (askTell.equals(AlgernonConstants.ASK))
            {
                lispResult = this.ask(query, errors);
            }
            else
            {
                lispResult = this.tell(query, errors);
            }
            String errorMessage;
            if (errors.size() > 0)
            {
                errorMessage = errorMessage(query, errors);
                OntologyFault ontologyFault = new OntologyFault();
                ontologyFault.setMessage(errorMessage);

                throw new OntologyErrorException(errorMessage, ontologyFault);
            }
            else if (lispResult == null)
            {
                errorMessage = errorMessage(query, errors);
                OntologyFault ontologyFault = new OntologyFault();
                ontologyFault.setMessage(errorMessage);

                throw new OntologyErrorException(errorMessage, ontologyFault);
            }
            else if (lispResult.basic_null())
            {
                resultString = AlgernonConstants.PATH_FAILED;
                result = new Result();
                result.setResultString(resultString);
                result.setResult(AlgernonConstants.FAILED);
                return result;
            }
            else
            {
                result = this.analyzeResult(lispResult);
                resultString = AlgernonConstants.PATH_SUCCEEDED;
                result.setResultString(resultString);
                result.setResult(AlgernonConstants.OK);
            }
        }
        catch (AlgernonRuntimeException e)
        {
            String message = "A algernon runtime exception occurs while executing 'start' : ";
            LOG.error(message, e);
            OntologyFault ontologyFault = new OntologyFault();
            ontologyFault.setMessage(message);
            throw new OntologyErrorException(message, ontologyFault, e);
        }
        return result;
    }

    /**
     * Starts a rule.
     * 
     * @param query the query.
     * @param askTell the ask or tell.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public synchronized RecordSet startRS(String query, String askTell) throws OntologyErrorException
    {
        LispValue lispResult;
        String resultString = new String();
        RecordSet result = null;
        try
        {
            ErrorSet errors = new ErrorSet();
            if (askTell.equals(AlgernonConstants.ASK))
            {
                lispResult = this.ask(query, errors);
            }
            else
            {
                lispResult = this.tell(query, errors);
            }
            String errorMessage;
            if (errors.size() > 0)
            {
                errorMessage = errorMessage(query, errors);
                OntologyFault ontologyFault = new OntologyFault();
                ontologyFault.setMessage(errorMessage);

                throw new OntologyErrorException(errorMessage, ontologyFault);
            }
            else if (lispResult == null)
            {
                errorMessage = errorMessage(query, errors);
                OntologyFault ontologyFault = new OntologyFault();
                ontologyFault.setMessage(errorMessage);

                throw new OntologyErrorException(errorMessage, ontologyFault);
            }
            else if (lispResult.basic_null())
            {
                resultString = AlgernonConstants.PATH_FAILED;
                result = new RecordSet();
                result.setResultString(resultString);
                result.setResult(AlgernonConstants.FAILED);
                return result;

            }
            else
            {
                result = this.analyzeResultRS(lispResult);
                resultString = AlgernonConstants.PATH_SUCCEEDED;
                result.setResultString(resultString);
                result.setResult(AlgernonConstants.OK);
            }
        }
        catch (AlgernonRuntimeException e)
        {
            String message = "A algernon runtime exception occurs while excecuting 'startRs'";
            LOG.error(message, e);
            OntologyFault ontologyFault = new OntologyFault();
            ontologyFault.setMessage(message);
            throw new OntologyErrorException(message, ontologyFault, e);
        }
        return result;
    }

    /**
     * Description.
     * 
     * @param query the query
     * @param askTell ask or tell
     * @return {@link RecordsetKV}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public synchronized RecordsetKV startKV(String query, String askTell) throws OntologyErrorException
    {
        LispValue lispResult;
        String resultString = new String();
        RecordsetKV result = null;
        try
        {
            ErrorSet errors = new ErrorSet();
            if (askTell.equals(AlgernonConstants.ASK))
            {
                lispResult = this.ask(query, errors);
            }
            else
            {
                lispResult = this.tell(query, errors);
            }
            String errorMessage;
            if (errors.size() > 0)
            {
                errorMessage = errorMessage(query, errors);
                OntologyFault ontologyFault = new OntologyFault();
                ontologyFault.setMessage(errorMessage);

                throw new OntologyErrorException(errorMessage, ontologyFault);
            }
            else if (lispResult == null)
            {
                errorMessage = errorMessage(query, errors);
                OntologyFault ontologyFault = new OntologyFault();
                ontologyFault.setMessage(errorMessage);

                throw new OntologyErrorException(errorMessage, ontologyFault);
            }
            else if (lispResult.basic_null())
            {
                resultString = AlgernonConstants.PATH_FAILED;
                result = new RecordsetKV();
                result.setResultString(resultString);
                result.setResult(AlgernonConstants.FAILED);
                return result;
            }
            else
            {
                result = this.analyzeResultKV(lispResult);
                resultString = AlgernonConstants.PATH_SUCCEEDED;
                result.setResultString(resultString);
                result.setResult(AlgernonConstants.OK);
            }
        }
        catch (AlgernonRuntimeException e)
        {
            String message = "A alergnon runtime exception occurs while executing 'startKV': ";
            LOG.error(message, e);
            OntologyFault ontologyFault = new OntologyFault();
            ontologyFault.setMessage(message);
            throw new OntologyErrorException(message, ontologyFault, e);
        }
        return result;
    }

    /**
     * analyzes the result.
     * 
     * @param value the value.
     * @return an algernon result object.
     */
    private Result analyzeResult(LispValue value)
    {

        int noOfRecords = 0;
        int noOfColumns = 0;
        boolean first = true;
        String[] fieldNames = null;
        String[] fields = null;

        Iterator<? > iterator = value.iterator();
        while (iterator.hasNext())
        {
            BindingList bl = (BindingList) iterator.next();
            if (first)
            {

                noOfColumns = bl.size();
                Iterator<? > it2 = bl.iterator();
                fieldNames = new String[noOfColumns];
                int i = 0;
                while (it2.hasNext())
                {
                    Object obj = it2.next();
                    fieldNames[i++] = new String(obj.toString());
                }
                first = false;
            }
            noOfRecords++;
        }

        List<String> fieldnames = Arrays.asList(fieldNames);

        Result result = new Result();
        result.setNumberOfRecords(noOfRecords);
        result.setNumberOfColumns(noOfColumns);

        result.setFieldNames(fieldnames);// .setFieldNames(m_fieldNames);

        Record[] records = new Record[noOfRecords];

        iterator = value.iterator();
        int recordno = 0;
        while (iterator.hasNext())
        {

            // Result contains some BindingList objects.
            Record record = new Record();
            record.setNumberOfFields(noOfColumns);
            BindingList bl = (BindingList) iterator.next();
            Set<? > keyset = bl.keySet();
            Iterator<? > iterator2 = keyset.iterator();
            int i = 0;
            fields = new String[noOfColumns];
            while (iterator2.hasNext())
            {
                Object key = iterator2.next();
                LispValue tmp = bl.get(key);
                String tmp2 = tmp.toString();
                fields[i++] = tmp2;
            }
            record.setFields(Arrays.asList(fields));
            records[recordno++] = record;
        }
        result.setRecords(Arrays.asList(records));
        return result;

    }

    @SuppressWarnings("unchecked")
    private RecordSet analyzeResultRS(LispValue value)
    {

        Hashtable<String, String> fieldNames = new Hashtable<String, String>();
        Vector<Hashtable<String, String>> cache = new Vector<Hashtable<String, String>>();

        RecordSet result = new RecordSet();

        // iterate over all the m_records
        Iterator<? > iterator = value.iterator();
        while (iterator.hasNext())
        {

            // Result contains some BindingList objects.
            Hashtable<String, String> record = new Hashtable<String, String>();
            BindingList bl = (BindingList) iterator.next();
            Set<? > keyset = bl.keySet();
            Iterator<? > iterator2 = keyset.iterator();

            // iterate over all the m_fields
            while (iterator2.hasNext())
            {
                Object key = iterator2.next();
                LispValue tmp = bl.get(key);
                String tmp2 = tmp.toString();
                // remove quotes from value
                if (tmp2.indexOf('"') == 0)
                {
                    String tmp3 = tmp2.substring(1, tmp2.length() - 1);
                    record.put(key.toString(), tmp3);
                }
                else
                {
                    record.put(key.toString(), tmp2);
                }
                // insert key name within field name list
                this.insertFieldName(fieldNames, key.toString());
            }
            cache.add(record);
        }

        int noOfRecords = cache.size();
        int noOfColumns = fieldNames.size();

        String[] theFields = new String[noOfColumns];
        Set<String> keyset = fieldNames.keySet();
        theFields = keyset.toArray(theFields);

        Hashtable<String, String>[] records = new Hashtable[noOfRecords];

        records = cache.toArray(records);
        result.setNoOfRecords(noOfRecords);
        result.setNoOfColumns(noOfColumns);
        result.setFieldNames(theFields);
        result.setRecords(records);
        return result;

    }

    private void insertFieldName(Hashtable<String, String> table, String fieldname)
    {
        // insert the fieldname in case it is not already within table
        if (table.get(fieldname) == null)
        {
            table.put(fieldname, fieldname);
        }
    }

    private RecordsetKV analyzeResultKV(LispValue value)
    {

        Hashtable<String, String> fieldNames = new Hashtable<String, String>();
        Vector<RecordKV> cache = new Vector<RecordKV>();

        RecordsetKV result = new RecordsetKV();

        // iterate over all the m_records
        Iterator<? > iterator = value.iterator();
        while (iterator.hasNext())
        {

            // Result contains some BindingList objects.
            RecordKV record = new RecordKV();
            BindingList bl = (BindingList) iterator.next();
            Set<? > keyset = bl.keySet();
            int fieldsCount = keyset.size();
            record.setNumberOfFields(fieldsCount);
            Field[] fields = new Field[fieldsCount];
            Iterator<? > iterator2 = keyset.iterator();
            int i = 0;

            // iterate over all the m_fields
            while (iterator2.hasNext())
            {
                Field field = new Field();
                Object key = iterator2.next();
                LispValue tmp = bl.get(key);
                String tmp2 = tmp.toString();
                // remove quotes from value
                if (tmp2.indexOf('"') == 0)
                {
                    String tmp3 = tmp2.substring(1, tmp2.length() - 1);
                    field.setKey(key.toString());
                    field.setValue(tmp3);
                }
                else
                {
                    field.setKey(key.toString());
                    field.setValue(tmp2);
                }
                // insert key name within field name list
                this.insertFieldName(fieldNames, key.toString());
                fields[i] = field;
                i++;
            }
            record.setFields(Arrays.asList(fields));
            cache.add(record);
        }

        int noOfRecords = cache.size();
        int noOfColumns = fieldNames.size();

        String[] theFields = new String[noOfColumns];
        Set<String> keyset = fieldNames.keySet();
        theFields = keyset.toArray(theFields);

        Arrays.sort(theFields);

        RecordKV[] records = new RecordKV[noOfRecords];

        records = cache.toArray(records);
        result.setNumberOfRecords(noOfRecords);
        result.setNumberOfColumns(noOfColumns);
        result.setFieldNames(Arrays.asList(theFields));
        result.setRecords(Arrays.asList(records));
        // result.records2 = records;
        return result;

    }

    /**
     * prints error message.
     * 
     * @param query a query
     * @param errors an error set from algernon
     * @return errors message
     */
    private String errorMessage(String query, ErrorSet errors)
    {
        return AlgernonConstants.ERROR + AlgernonConstants.ERROR_MESSAGE + query + "'" + Algernon.printErrorsToString(errors);
    }
}
