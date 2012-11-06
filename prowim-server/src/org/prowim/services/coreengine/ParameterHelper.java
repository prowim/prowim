/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.InformationType;
import org.prowim.datamodel.prowim.InformationTypesConstants;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.ParameterConstraint;
import org.prowim.datamodel.prowim.Person;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * Helper used to get the parameter selection list and possible selection list.<br/>
 * see {@link Parameter}.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a8
 */
@Local
public interface ParameterHelper
{

    /**
     * Converts an {@link Object} to {@link String}.
     * 
     * @param values the original values collected from Inhalt_String field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of {@link String}.
     */
    ObjectArray convertShortText(ObjectArray values);

    /**
     * Converts an {@link Object} to {@link String}.
     * 
     * @param values the original values collected from Inhalt_String field defined on the PROCESSINFORMATION class.
     * @return a String. not null list of {@link String}.
     */
    ObjectArray convertLongText(ObjectArray values);

    /**
     * Converts an Object to {@link Float}.
     * 
     * @param values the original values collected from Inhalt_Fliesskomazahl field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of {@link Float}.
     */
    ObjectArray convertFloat(ObjectArray values);

    /**
     * Converts an Object to {@link Person}.
     * 
     * @param values the original values collected from Inhalt_Relation field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of {@link Person}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray convertPersonen(ObjectArray values) throws OntologyErrorException;

    /**
     * Converts an Object to an {@link Organization}.
     * 
     * @param values the original values collected from Inhalt_Relation field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of {@link Organization} instances.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray convertOrganizationalUnit(ObjectArray values) throws OntologyErrorException;

    /**
     * Converts an Object to Integer.
     * 
     * @param values the original values collected from Inhalt_Ganzzahl field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of Integer values.
     */
    ObjectArray convertInteger(ObjectArray values);

    /**
     * Converts an Object to string.
     * 
     * @param values the original values collected from Inhalt_String field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of values.
     */
    ObjectArray convertSingleList(ObjectArray values);

    /**
     * Converts an Object list to string String list.
     * 
     * @param values the original values collected from Inhalt_List field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of values.
     */
    ObjectArray convertMultiList(ObjectArray values);

    /**
     * Converts an Object to string.
     * 
     * @param values the original values collected from Inhalt_String field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of values.
     */
    ObjectArray convertComboBox(ObjectArray values);

    /**
     * Converts an Object to string.
     * 
     * @see InformationTypesConstants#DOCUMENT
     * 
     * @param values the original values collected from Inhalt_String field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of values.
     */
    ObjectArray convertDocument(ObjectArray values);

    /**
     * Converts a date as String to a formatted MidnightDate as String<br/>
     * the used format from DateMidnight is the ISO8601. See http://joda-time.sourceforge.net/cal_iso.html
     * 
     * @param values the original values as String collected from the ontology.
     * @return a formated values-list.
     */
    ObjectArray convertDate(ObjectArray values);

    /**
     * Converts a timestamp as String to a formatted MidnightDate as String<br/>
     * the used format from DateMidnight is the ISO8601. See http://joda-time.sourceforge.net/cal_iso.html
     * 
     * @param values the original values as String collected from the ontology.
     * @return a formated values-list.
     */
    ObjectArray convertTimeStamp(ObjectArray values);

    /**
     * Converts an Object to Link. A Link is just an URL that refers to a document on the web.
     * 
     * @param values the original values collected from Inhalt_String field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of values.
     */
    ObjectArray convertLink(ObjectArray values);

    /**
     * Converts an Object to {@link ControlFlow}.
     * 
     * @param values the original values collected from Inhalt_String field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of values.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray convertComboBoxControlFlow(ObjectArray values) throws OntologyErrorException;

    /**
     * Converts an Object to {@link ControlFlow}.
     * 
     * @param values the original values collected from Inhalt_String field defined on the PROCESSINFORMATION ontology class.
     * @return a String. not null list of values.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray convertMultiListControlFlow(ObjectArray values) throws OntologyErrorException;

    /**
     * Gets the {@link ParameterConstraint} object for a {@link Parameter}.<br/>
     * The date of a {@link ParameterConstraint} are readed from the PROCESSINFORMATION ontology frame, if there is no data<br>
     * The values of the min, max and required are readed from the {@link InformationType}.
     * 
     * @param parameterID the parameter id.
     * @param infoType the informationtype id. {@link InformationType}
     * @return the {@link ParameterConstraint} or null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ParameterConstraint getConstraint(String parameterID, String infoType) throws OntologyErrorException;

    /**
     * Gets the selection list for a parameter.
     * 
     * @param processInformationID the ID of the processinformation. not null.
     * @param informationTypeID the ID of the informationtype. not null.
     * @return the not null selectionlist. an empty list is returned, if no items exists.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getParameterSelectList(String processInformationID, String informationTypeID) throws OntologyErrorException;

    /**
     * Gets the value of a paramter.
     * 
     * @param parameterTemplateID the not null ID of the parameter.
     * @return not null parameter value.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getParameterValue(String parameterTemplateID) throws OntologyErrorException;

    /**
     * Gets the parameter value by a given parameter ID and content type.
     * 
     * @param contentType the content type.
     * @param parameterID the not null ID of the parameter.
     * @return not null parameter value.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getParameterValue(String contentType, String parameterID) throws OntologyErrorException;

}