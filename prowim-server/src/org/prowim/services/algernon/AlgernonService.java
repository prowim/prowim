/*==============================================================================
 * File $Id: AlgernonService.java 4510 2010-08-02 10:37:49Z wiesner $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/algernon/AlgernonService.java $
 * $LastChangedRevision: 4510 $
 *------------------------------------------------------------------------------
 * (c) 21.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.algernon;

import java.util.Hashtable;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.algernon.Result;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * The service to access ontology backend with algernon.
 * 
 * @author Thomas Wiesner
 * @version $Revision: 4510 $
 * @since 2.0.0a8
 */
@Local
public interface AlgernonService
{

    /**
     * save the knowledge base.
     * 
     * @return status
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    String saveKB() throws OntologyErrorException;

    /**
     * start the service.
     * 
     * @param query the query
     * @param askTell ask or tell
     * @return {@link Result}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    Result start(String query, String askTell) throws OntologyErrorException;

    /**
     * start the RecordSet.
     * 
     * @param query the query
     * @param askTell ask or tell
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet startRS(String query, String askTell) throws OntologyErrorException;

    /**
     * startKV.
     * 
     * @param query the query
     * @param askTell ask or tell
     * @return {@link RecordsetKV}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordsetKV startKV(String query, String askTell) throws OntologyErrorException;

    /**
     * get the knowledge base.
     * 
     * @return knowledge base file
     */
    String getKB();

    /**
     * load a rule.
     * 
     * @param ruleName the rule name
     * @return algernon result
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    String loadRule(String ruleName) throws OntologyErrorException;

    /**
     * execute a rule.
     * 
     * @param ruleName the rule name
     * @param n1 param1
     * @param v1 param2
     * @param n2 param3
     * @param v2 param4
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet executeBusinessRule(String ruleName, String n1, String v1, String n2, String v2) throws OntologyErrorException;

    /**
     * execute a rule.
     * 
     * @param ruleName the rule name
     * @param parameters the parameters in a HashMap
     * @param askTell ask or tell
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet executeBusinessRule(String ruleName, Hashtable<? , ? > parameters, String askTell) throws OntologyErrorException;

    /**
     * execute a business rule. The decision for ASK or TELL would be got from rule own.
     * 
     * @param ruleName the rule name
     * @param parameters the parameters in a HashMap
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet executeBusinessRule(String ruleName, Hashtable<? , ? > parameters) throws OntologyErrorException;

    /**
     * execute a rule.
     * 
     * @param ruleName the rule name
     * @param n1 param1
     * @param v1 param2
     * @param n2 param3
     * @param v2 param4
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet executeRule(String ruleName, String n1, String v1, String n2, String v2) throws OntologyErrorException;

    /**
     * execute a rule.
     * 
     * @param ruleName the rule name
     * @param parameters parameters in HashMap
     * @param askTell ask or tell
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet executeRule(String ruleName, Hashtable<? , ? > parameters, String askTell) throws OntologyErrorException;

    /**
     * executeActivityRule.
     * 
     * @param ruleName the rule name
     * @param parameters the parameters
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet executeActivityRule(String ruleName, Hashtable<? , ? > parameters) throws OntologyErrorException;

    /**
     * get the business rule.
     * 
     * @return business rules
     */
    Hashtable<Object, Object> getBusinessRules();

    /**
     * set the business rule.
     * 
     * @param businessRules the business rules
     */
    void setBusinessRules(Hashtable<Object, Object> businessRules);

    /**
     * get normal rule.
     * 
     * @return normal rules
     */
    Hashtable<Object, Object> getNormalRule();

    /**
     * set the normal rule.
     * 
     * @param normalRule the normal rule
     */
    void setNormalRule(Hashtable<Object, Object> normalRule);

}