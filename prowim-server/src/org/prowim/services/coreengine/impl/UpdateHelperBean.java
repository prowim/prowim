/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-22 16:50:50 +0200 (Mi, 22 Sep 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/UpdateHelperBean.java $
 * $LastChangedRevision: 4817 $
 *------------------------------------------------------------------------------
 * (c) 22.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OntologyVersionArray;
import org.prowim.datamodel.collections.UpdateFrameArray;
import org.prowim.datamodel.collections.UpdateFrameComprator;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.OntologyVersion;
import org.prowim.datamodel.prowim.UpdateFrame;
import org.prowim.datamodel.prowim.UpdateItem;
import org.prowim.datamodel.prowim.UpdateWork;
import org.prowim.datamodel.prowim.UpdatesLog;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.UpdateEngine;
import org.prowim.services.coreengine.UpdateEngineConstants;
import org.prowim.services.coreengine.UpdateHelper;
import org.prowim.utils.StringConverter;

import de.ebcot.tools.logging.Logger;


/**
 * Implements {@link UpdateHelper}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4817 $
 * @since !!VERSION!!
 */
@Stateless
public class UpdateHelperBean implements UpdateHelper
{
    private static final Logger LOG      = Logger.getLogger(UpdateHelperBean.class);
    private final static String CLASS    = "Class";
    private final static String SLOT     = "Slot";
    private final static String INSTANCE = "Instance";

    @IgnoreDependency
    @EJB
    private UpdateEngine        updateEngine;

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#getUpdateFrames(java.lang.String)
     */
    @Override
    public UpdateFrameArray getUpdateFrames(String versionID) throws OntologyErrorException
    {
        final RecordSet records = updateEngine.getUpdateFrames(versionID);
        final UpdateFrameArray result = new UpdateFrameArray();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                final String frameID = records.getRecords()[i].get(UpdateEngineConstants.Variables.ID);
                final String count = records.getRecords()[i].get(UpdateEngineConstants.Variables.COUNT);
                final String name = records.getRecords()[i].get(UpdateEngineConstants.Variables.NAME);
                final String type = this.getFrameType(frameID);
                Integer countI = StringConverter.strToInteger(count);
                boolean isNewFrame = true;
                if (countI.intValue() > 1)
                {
                    isNewFrame = false;
                }
                final UpdateFrame frame = DefaultDataObjectFactory.createUpdateFrame(frameID, name, type, isNewFrame);
                result.add(frame);
            }
        }
        return result;
    }

    private String getFrameType(String frameID) throws OntologyErrorException
    {
        final RecordSet records = updateEngine.isFrameClass(frameID);
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            return CLASS;
        }
        else if (updateEngine.isFrameSlot(frameID).getResult().equals(AlgernonConstants.OK))
        {
            return SLOT;
        }
        else if (updateEngine.isFrameInstance(frameID).getResult().equals(AlgernonConstants.OK))
        {
            return INSTANCE;
        }
        else
        {
            throw new IllegalStateException("Frame type not known . ");
        }
    }

    private boolean isFrameClass(String frameID) throws OntologyErrorException
    {
        String frameType = this.getFrameType(frameID);
        return frameType.equals(CLASS);
    }

    private boolean isFrameSlot(String frameID) throws OntologyErrorException
    {
        String frameType = this.getFrameType(frameID);
        return frameType.equals(SLOT);
    }

    private boolean isFrameInstance(String frameID) throws OntologyErrorException
    {
        String frameType = this.getFrameType(frameID);
        return frameType.equals(INSTANCE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#getVersions()
     */
    @Override
    public OntologyVersionArray getVersions() throws OntologyErrorException
    {
        final RecordSet records = updateEngine.getAllVersions();
        final OntologyVersionArray result = new OntologyVersionArray();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String versionID = records.getRecords()[i].get(UpdateEngineConstants.Variables.ID);
                String versionLabel = records.getRecords()[i].get(UpdateEngineConstants.Variables.LABEL);
                String versionCreateTime = records.getRecords()[i].get(UpdateEngineConstants.Variables.CREATE_TIME);
                String versionCreator = records.getRecords()[i].get(UpdateEngineConstants.Variables.CREATOR);

                final OntologyVersion version = DefaultDataObjectFactory.createOntologyVersion(versionID, versionLabel, versionCreateTime,
                                                                                               versionCreator);
                result.add(version);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#getUpdateWork(java.lang.String)
     */
    public UpdateWork getUpdateWork(String versionID) throws OntologyErrorException
    {
        UpdateFrameArray updateFrames = this.getUpdateFrames(versionID);
        RecordSet versionRecord = updateEngine.getVersionProperties(versionID);
        String versionName = "NO_VERSION";
        if (versionRecord.getResult().equals(AlgernonConstants.OK))
        {
            versionName = versionRecord.getRecords()[0].get("?label");
        }

        String txt = "";
        Collections.sort(updateFrames.getItem(), new UpdateFrameComprator());
        Iterator<UpdateFrame> it = updateFrames.iterator();
        final ObjectArray updateItems = new ObjectArray();

        while (it.hasNext())
        {

            UpdateFrame frame = it.next();
            String query = generateUpdateRule(frame.getID(), frame.getType(), frame.isNewFrame());

            query = ";;" + frame.getName() + "\n" + query;
            final UpdateItem updateItem = DefaultDataObjectFactory.createUpdateItem(frame.getName(), query);
            updateItems.add(updateItem);
            txt = txt + query + "<EOR>\n";
        }

        // // TODO GET the delete items.
        UpdateWork updateWork = DefaultDataObjectFactory.createUpdateWork(txt, updateItems, versionName);
        return updateWork;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#executeUpdate(org.prowim.datamodel.prowim.UpdateWork)
     */
    @Override
    public ObjectArray executeUpdate(UpdateWork updateWork) throws OntologyErrorException
    {
        LOG.info("Executing update");
        final ObjectArray resultAsString = new ObjectArray();
        final Iterator<Object> updatesIt = updateWork.getUpdateItems().iterator();
        final ObjectArray result = new ObjectArray();
        boolean continueUpdate = true;
        while (updatesIt.hasNext())
        {
            UpdateItem item = (UpdateItem) updatesIt.next();
            String rule = item.getUpdateRule();
            String status = executeUpdate(rule);
            LOG.info("Update rule: " + rule);
            LOG.info("Update status: " + status);
            resultAsString.add(status);
            boolean statusAsBool = StringConverter.algStatusStrToBool(status);
            if ( !statusAsBool)
            {
                final UpdatesLog log = DefaultDataObjectFactory.createUpdatesLog(item.getUpdateFrame(), rule, statusAsBool);
                result.add(log);
                continueUpdate = false;
                break;

            }
        }

        /** iterate the delete frames and delete them. */
        final ObjectArray deleteItems = new ObjectArray();
        // updateWork.getDeleteItems();
        Iterator<Object> deleteFrameIT = deleteItems.iterator();
        String deleteScript = "";
        while (deleteFrameIT.hasNext())
        {
            if ( !continueUpdate)
            {
                break;
            }
            String frameID = (String) deleteFrameIT.next();

            if (isFrameClass(frameID))
            {
                deleteScript = "((:DELETE-CLASS " + frameID + " ?a))";
            }
            else if (isFrameSlot(frameID))
            {
                deleteScript = "((:DELETE-RELATION " + frameID + " ?a))";
            }
            else if (isFrameInstance(frameID))
            {
                deleteScript = "((:DELETE-INSTANCE " + frameID + " ?a))";
            }
            else
            {
                throw new IllegalStateException("Type does not supported  " + frameID);
            }
            LOG.info("Deleting frame ID " + frameID + " with script: " + deleteScript);
            executeUpdate(deleteScript);
        }

        return resultAsString;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#executeUpdateFromScript(java.lang.String)
     */
    public boolean executeUpdateFromScript(String updateScript) throws OntologyErrorException
    {
        /** parse the update script and create rules that have to be executed from the method executeUpdate(String rule). */
        Pattern pattern = Pattern.compile("<EOR>+");
        String[] rules = pattern.split(updateScript);

        LOG.debug("\n\n  UPDATE SCRIPT:   ");
        boolean result = false;
        if (rules != null)
        {
            for (int i = 0; i < rules.length; i++)
            {
                if (rules[i] != null && !rules[i].equals(" "))
                {
                    result = executeUpdate(rules[i]).equals(AlgernonConstants.OK);
                    if ( !result)
                    {
                        LOG.debug("\n\n  UPDATE SCRIPT ERROR:   " + rules[i]);
                    }
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#clean()
     */
    @Override
    public void clean() throws OntologyErrorException
    {
        updateEngine.clean();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#initKnowledgeBase()
     */
    @Override
    public void initKnowledgeBase()
    {
    }

    /**
     * Generates the update rule for the slected update frame.
     * 
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    private String generateUpdateRule(String frameID, String frameType, boolean isNew) throws OntologyErrorException
    {
        final RecordSet records = updateEngine.getUpdateFrameProperties(frameID);
        String txt = "", txtAdd = "", del = "", defin = "", clazz = "", verbi = "", statement = "", delnew = "", verbalt = "";
        String updateScript = "";
        String header = "";
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String clause = records.getRecords()[i].get("?ValueID");
                if ( !clause.equals("") || isNew)
                {
                    String verb = records.getRecords()[i].get("?SlotID");
                    String object = records.getRecords()[i].get("?ValueID");
                    if (verb.equals(":DIRECT-SUPERCLASSES"))
                    {
                        clazz = object;
                    }
                    else
                    {
                        if (object.equals(frameID) && ( !verb.equals(":NAME")))
                        {
                            object = "?x";
                        }
                        else
                        {
                            if (object.length() > 0 && object.charAt(0) != ':')

                                if (records.getRecords()[i].get("?Type").equals("Integer") || records.getRecords()[i].get("?Type").equals("Float"))
                                {
                                    this.toString();
                                }
                                else
                                {
                                    object = "\"" + object + "\"";
                                }
                        }
                        defin = "";
                        if (verb.length() > 0 && verb.charAt(0) != ':')
                        {
                            defin = "(:BIND ?a " + verb + ")";
                            verbi = "?a";
                        }
                        else
                        {
                            verbi = verb;
                        }
                        if ( !verbi.equals(":DIRECT-INSTANCES"))
                        {
                            statement = "(" + verbi + " ?x " + object + ")";
                            delnew = "(:CLEAR-RELATION ?x " + verbi + ")";
                            if (verbalt.equals(verb))
                            {
                                del = "";
                            }
                            else
                            {
                                del = delnew;
                                verbalt = verb;
                            }
                            if (verb.equals(":NAME"))
                            {
                                txtAdd = txtAdd + statement + "\n";
                            }
                            else
                            {
                                if ( !clause.equals(""))
                                {

                                    txt = txt + defin + del + "(:OR (" + statement + ")((:BIND ?F 1)))" + "\n";
                                }
                                else
                                {
                                    txt = txt + defin + del + "\n";
                                }
                            }

                        }

                    }
                }

            }
            if (isNew)
            {
                if (frameType.equals("Slot"))
                {
                    header = "(:BIND ?x " + frameID + ")(:ADD-RELATION ?x (PROWIM-CLASS PROWIM-CLASS))";
                    updateScript = updateScript + "(" + header + "\n" + txt;
                }
                else
                {
                    updateScript = updateScript + "((:ADD-CLASS (?x " + clazz + ")" + "\n" + txtAdd + "\n" + txt + ")";
                }
            }
            else
            {
                updateScript = updateScript + "((:BIND ?x " + frameID + ")" + "\n" + txt;
            }
            updateScript = updateScript + "\n" + ")" + "\n";
        }
        return updateScript;
    }

    /**
     * Executes the update query.
     * 
     * @param query not null query.
     * @return SUCCESSFULL or FAILED.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private String executeUpdate(String query) throws OntologyErrorException
    {
        return updateEngine.executeUpdateQuery(query).getResult();
    }

    /**
     * Gets the ontology version from a starting a query.
     * 
     * @return not null version label.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public String getOntologyVersionFromQuery() throws OntologyErrorException
    {
        RecordSet records = updateEngine.executeQuery("((:BIND ?slot \"VERSION\")(?slot ProWimVersion ?Version))");
        String res = "not defined";
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            res = records.getRecords()[0].get("?Version");
        }
        return res;
    }

    /**
     * Adds a new web service.
     * 
     * @param name the web service name.
     * @param description the web service description.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public void addWebservice(String name, String description) throws OntologyErrorException
    {
        Validate.notNull(name);
        Validate.notNull(description);
        updateEngine.addWebservice(name, description);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#setVersionInvalid()
     */
    public void setVersionInvalid() throws OntologyErrorException
    {
        updateEngine.setVersionInvalid();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#setVersionValid()
     */
    @Override
    public void setVersionValid() throws OntologyErrorException
    {
        updateEngine.setVersionValid();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateHelper#isVersionValid()
     */
    @Override
    public boolean isVersionValid() throws OntologyErrorException
    {
        boolean result = false;
        RecordSet records = updateEngine.isVersionValid();
        if (records != null)
        {
            if (records.getResult().equals(AlgernonConstants.FAILED))
            {
                result = true;
            }

        }
        return result;
    }

}
