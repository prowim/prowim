/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-19 17:01:15 +0200 (Mon, 19 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/Relation.java $
 * $LastChangedRevision: 4323 $
 *------------------------------------------------------------------------------
 * (c) 30.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.prowim;

import java.util.HashMap;


/**
 * This class declares the slots, classes and instances constants used in prowim-services project.
 * 
 * @author Saad Wardi
 * @version $Revision: 4323 $
 */
public class Relation
{
    /**
     * Contains slot names that are defined for classes in the ontology.
     * 
     * @author Saad Wardi
     * @version $Revision: 4323 $
     */
    public final class Slots
    {
        /** The slot "name" of a relation. */
        public final static String NAME                            = "Bezeichnung";

        /** The slot "name" of a relation. */
        public final static String DENOTATION                      = "DENOTATION";

        /** The slot "wird_besetzt_durch" of a Role. */
        public final static String ASSIGNED_TO                     = "wird_besetzt_durch";

        /** The slot ""wird_beherrscht_von"". */
        public final static String DOMINATED_FROM                  = "wird_beherrscht_von";

        /** The slot "verweist_ueber". */
        public final static String REFERS_TO                       = "verweist_ueber";

        /** The slot "verweist_auf". */
        public final static String REFER_OF                        = "verweist_auf";

        /** The slot "hat_Vorgangsregel". */
        public final static String HAS_RULE                        = "hat_Vorgangsregel";

        /** The slot "Hyperlink". */
        public final static String HYPERLINK                       = "Hyperlink";

        /** The information type. */
        public final static String INFORMATION_TYPE                = "ist_vom_Informationstyp";

        // Slots for the model
        /** The slot "besteht_aus". */
        public final static String BESTEHT_AUS                     = "besteht_aus";

        /** The slot "besteht_aus_TOPOElement". */
        public final static String BESTEHT_AUS_TOPELEMENT          = "besteht_aus_TOPOElement";

        /** The slot "hat_Risikoelement". */
        public final static String HAT_RISIKOELEMENT               = "hat_Risikoelement";

        /** The slot "hat_Anwendungselement". */
        public final static String HAT_ANWENDUNGSELEMENT           = "hat_Anwendungselement";

        /** The slot "besteht_aus_Anforderungselement". */
        public final static String BESTHET_AUS_ANFORDERUNGSELEMENT = "besteht_aus_Anforderungselement";

        /** The Attribute Beschreibung. */
        public static final String DESCRIPTION                     = "Beschreibung";

        /** The start slot. */
        public static final String START                           = "Start";

        /** The setzt_aktiv slot. */
        public static final String SET_ACTIVE                      = "setzt_aktiv";

        /** The slot address. */
        public static final String ADDRESS                         = "Adresse";
        /** The slot email. */
        public static final String EMAIL                           = "Email";
        /** The telefon. */
        public static final String TELEFON                         = "Telefon";
        /** The firstname . */
        public static final String FIRSTNAME                       = "Vorname";
        /** The lastname . */
        public static final String LASTNAME                        = "Nachname";
        /** the "ist_Mitglied_von" slot. */
        public static final String IS_MEMBER                       = "ist_Mitglied_von";
        /** the slot "darf_besetzt_werden_von" slot. */
        public static final String COULD_BE_OCCUPIED_FROM          = "darf_besetzt_werden_von";
        /** the slot "besteht_aus_Domaene". */
        public static final String SUB_DOMAINS                     = "besteht_aus_Domaene";
        /** the slot "ist_aufgeteilt_in". */
        public static final String SUB_ORGA                        = "ist_aufgeteilt_in";
        /** the slot RuleBody. */
        public static final String RULE_BODY                       = "RuleBody";
        /** the slot "hat_Verkn√ºpfungsregel". */
        public static final String HAS_COMBINATION_RULE            = "hat_Verknüpfungsregel";
        /** the slot "Passwort". */
        public static final String PASSWORD                        = "Passwort";
        /** the slot "hat_Rechtrolle". */
        public static final String RIGHTS_ROLES                    = "hat_Rechtrolle";
        /** the "darf_geaendert_werden_von" slot. */
        public static final String CAN_BE_CHANGED_FROM             = "darf_geaendert_werden_von";
        /** the slot "hat_Wissensbedarf_an". */
        public static final String HAS_KNOWLEDGEOBJECTS            = "hat_Wissensbedarf_an";
        /** the slot "Hat_Rollen". */
        public static final String HAS_ROLES                       = "Hat_Rollen";
        /** the slot "gehoert_zu". */
        public static final String BELONGS_TO                      = "gehoert_zu";
        /** The document version label slot. */
        public final static String DOCUMENT_VERSION                = "Dokument_Version";
        /** The hat_Prozessinformation slot. */
        public final static String HAS_PROCESS_INFORMATION         = "hat_Prozessinformation";

        private Slots()
        {

        }

    }

    /**
     * Constants for instances.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 4323 $
     */
    public final class Instances
    {
        /**
         * Constants for repositories.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4323 $
         */
        public final class Repository
        {
            /** Repositories name. */
            public final static String DMS = "ProWim DMS";

            private Repository()
            {

            }
        }

        private Instances()
        {

        }

    }

    /**
     * Constants for instances.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 4323 $
     */
    public final class Models
    {
        /** Process model name. */
        public final static String PROZESS           = "Prozess";
        /** Process model name. */
        public final static String TOPOPORTFOLIO     = "TOPO-Portfolio";
        /** Process model name. */
        public final static String RISIKOBEREICH     = "Risikobereich";
        /** Process model name. */
        public final static String PRODUCTFAMILIE    = "Produktfamilie";
        /** Process model name. */
        public final static String ANWENDUNGSBEREICH = "Anforderungsbereich";

        private Models()
        {

        }

    }

    /**
     * Constants for instances.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 4323 $
     */
    public final class Classes
    {
        /** Person class name. */
        public final static String PERSON             = "Person";
        /** KnowledgeDomain class name. */
        public static final String KNOWLEDGE_DOMAIN   = "Wissensdomaene";
        /** OrganizationalUnit class name. */
        public static final String ORGANIZATION       = "Organisationseinheit";
        /** Activity. */
        public static final String ACTIVITY           = "Aktivität";
        /** Mean. */
        public static final String MEAN               = "Mittel";
        /** Role. */
        public static final String ROLE               = "Rolle";
        /** ProcessInformation. */
        public static final String PROCESSINFORMATION = "Prozessinformation";
        /** Default Information type. */
        public static final String SHORT_TEXT         = "ShortText";
        /** ProductWay. */
        public static final String PRODUCTWAY         = "Produktweg";
        /** ControlFlow. */
        public static final String CONTROLFLOW        = "Kontrollfluss";
        /** ResultsMemory. */
        public static final String RESULTSMEMORY      = "Ablage";
        /** Work. */
        public static final String WORK               = "Tätigkeit";
        /** Function. */
        public static final String FUNCTION           = "Funktion";
        /** Product. */
        public static final String PRODUCT            = "Produkt";
        /** conjunction */
        public final static String CONJUNCTION        = "Verknüpfung";
        /** Decision */
        public final static String DECISION           = "Entscheidung";
        /** KnowledgeLink. */
        public final static String KNOWLEDGE_LINK     = "Wissensverweis";

        private Classes()
        {

        }

    }

    /**
     * A static class to store the allowed classes needed to authorize user.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 4323 $
     * @since !!VERSION!!
     */
    public final static class AuthorizationAllowedClasses
    {
        private final static HashMap<String, String> ALLOWED_CLASSES_MAP = new HashMap<String, String>();
        static
        {
            ALLOWED_CLASSES_MAP.put(Classes.PERSON, Classes.PERSON);
            ALLOWED_CLASSES_MAP.put(Classes.ROLE, Classes.ROLE);
            ALLOWED_CLASSES_MAP.put(Classes.ORGANIZATION, Classes.ORGANIZATION);
        }

        private AuthorizationAllowedClasses()
        {

        }

    }

}
