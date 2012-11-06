/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-07 14:55:21 +0200 (Di, 07 Sep 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/DefaultUpdateEngine.java $
 * $LastChangedRevision: 4772 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.services.coreengine.impl;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.Result;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.UpdateEngine;
import org.prowim.services.coreengine.UpdateEngineConstants;
import org.prowim.utils.LoggingUtility;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the {@link UpdateEngine}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4772 $
 * @since !!VERSION!!
 */
@Stateless
public class DefaultUpdateEngine implements UpdateEngine
{
    /** the logger. */
    private static final Logger LOG = Logger.getLogger(DefaultUpdateEngine.class);
    /** the algernon service. */
    @EJB
    private AlgernonService     myService;

    @IgnoreDependency
    @EJB
    private CommonHelper        common;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#getUpdateFrames(java.lang.String)
     */
    @Override
    public RecordSet getUpdateFrames(String versionID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(UpdateEngineConstants.Variables.VERSION_ID, new AlgernonValue(versionID, true));
        RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.GET_UPDATE_FRAMES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.GET_UPDATE_FRAMES, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#getAllVersions()
     */
    @Override
    public RecordSet getAllVersions() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.GET_ALL_VERSIONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.GET_ALL_VERSIONS, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#getUpdateFrameProperties(java.lang.String)
     */
    @Override
    public RecordSet getUpdateFrameProperties(String frameID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(UpdateEngineConstants.Variables.FRAME_ID, new AlgernonValue(frameID, false));
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.GET_UPDATE_FRAME_PROPERTIES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.GET_UPDATE_FRAME_PROPERTIES, result, this.getClass());

        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#isFrameClass(java.lang.String)
     */
    @Override
    public RecordSet isFrameClass(String frameID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(UpdateEngineConstants.Variables.FRAME_ID, new AlgernonValue(frameID, false));
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.IS_CLASS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.IS_CLASS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#isFrameInstance(java.lang.String)
     */
    @Override
    public RecordSet isFrameInstance(String frameID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(UpdateEngineConstants.Variables.FRAME_ID, new AlgernonValue(frameID, false));
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.IS_INSTANCE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.IS_INSTANCE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#isFrameSlot(java.lang.String)
     */
    @Override
    public RecordSet isFrameSlot(String frameID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(UpdateEngineConstants.Variables.FRAME_ID, new AlgernonValue(frameID, false));
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.IS_SLOT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.IS_SLOT, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#executeUpdateQuery(java.lang.String)
     */
    @Override
    public Result executeUpdateQuery(String query) throws OntologyErrorException
    {
        return this.myService.start(query, AlgernonConstants.TELL);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#getVersionProperties(java.lang.String)
     */
    @Override
    public RecordSet getVersionProperties(String versionID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(UpdateEngineConstants.Variables.ID, new AlgernonValue(versionID, false));
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.GET_VERSION_PROPERTIES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.GET_VERSION_PROPERTIES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#executeQuery(java.lang.String)
     */
    @Override
    public RecordSet executeQuery(String query) throws OntologyErrorException
    {
        return myService.startRS(query, AlgernonConstants.ASK);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#clean()
     */
    @Override
    public RecordSet clean() throws OntologyErrorException
    {
        String cleanQuery = UpdateEngineConstants.Variables.CLEAN_RULE;
        final RecordSet cleanRecords = myService.startRS(cleanQuery, AlgernonConstants.ASK);
        if (cleanRecords.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < cleanRecords.getNoOfRecords(); i++)
            {
                String ruleID = cleanRecords.getRecords()[i].get(UpdateEngineConstants.Variables.ID);
                String ruleBody = cleanRecords.getRecords()[i].get(UpdateEngineConstants.Variables.RULE_BODY);
                common.clearRelationValue(ruleID, Relation.Slots.RULE_BODY);
                String ruleBodyNew = writeQuoteToSimple(ruleBody);

                common.setSlotValue(ruleID, Relation.Slots.RULE_BODY, ruleBodyNew);
            }
        }
        else
        {
            LOG.debug("clean items is empty");
        }
        LoggingUtility.logResult("clean", cleanRecords, this.getClass());
        return cleanRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#getDeleteFrames(java.lang.String)
     */
    @Override
    public RecordSet getDeleteFrames(String versionID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(UpdateEngineConstants.Variables.ID, new AlgernonValue(versionID, false));
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.GET_DELETE_FRAMES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.GET_DELETE_FRAMES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#addWebservice(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet addWebservice(String name, String description) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(UpdateEngineConstants.Variables.NAME, new AlgernonValue(name, false));
        tab.put("?OID", new AlgernonValue("Webservice", false));
        tab.put("?desc", new AlgernonValue(description, false));
        final RecordSet result = myService.executeRule("addWebservice", tab, AlgernonConstants.TELL);
        LoggingUtility.logResult("addWebservice", result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#setVersionInvalid()
     */
    @Override
    public RecordSet setVersionInvalid() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.SET_VERSION_INVALID, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.SET_VERSION_INVALID, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#setVersionValid()
     */
    @Override
    public RecordSet setVersionValid() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.SET_VERSION_VALID, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.SET_VERSION_VALID, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.UpdateEngine#isVersionValid()
     */
    @Override
    public RecordSet isVersionValid() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        final RecordSet result = myService.executeRule(UpdateEngineConstants.Rules.IS_VERSION_VALID, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(UpdateEngineConstants.Rules.IS_VERSION_VALID, result, this.getClass());
        return result;
    }

    /**
     * Writes the content of a string without '"' char.
     * 
     * @param ruleBody not null rule body
     */
    private String writeQuoteToSimple(String ruleBody)
    {
        String text = ruleBody;
        return text.replace('"', '\'');
    }

}
