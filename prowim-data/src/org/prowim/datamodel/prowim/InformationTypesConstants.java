/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-21 14:29:53 +0200 (Wed, 21 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/InformationTypesConstants.java $
 * $LastChangedRevision: 4377 $
 *------------------------------------------------------------------------------
 * (c) 15.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

/**
 * Constants for InformationTypes. Each InformationType is a class in the ontology.
 * 
 * Warning: If you add, delete or change InformationTypes, you have to adjust the ontology. Also
 * 
 * @author Saad Wardi
 * @version $Revision: 4377 $
 */
public final class InformationTypesConstants
{
    /** the shorttext. */
    public static final String SHORTTEXT              = "ShortText";
    /** the long text. */
    public static final String LONGTEXT               = "LongText";
    /** the float. */
    public static final String FLOAT                  = "Float";
    /** the personen. */
    public static final String PERSONEN               = "Personen";
    /** the organizationalUnit. */
    public static final String ORGANIZATIONALUNIT     = "OrganizationalUnit";
    /** Integer. */
    public static final String INTEGER                = "Integer";
    /** SingleList. */
    public static final String SINGLELIST             = "SingleList";
    /** MultiList. */
    public static final String MULTILIST              = "MultiList";
    /** ComboBox. */
    public static final String COMBOBOX               = "ComboBox";
    /** Document. */
    public static final String DOCUMENT               = "Document";
    /** Link. */
    public static final String LINK                   = "Link";
    /** Date. */
    public static final String DATE                   = "Date";
    /** TimeStamp. */
    public static final String TIMESTAMP              = "TimeStamp";
    /** ComboBoxControlFlow. */
    public static final String COMBOBOX_CONTROL_FLOW  = "ComboBoxControlFlow";
    /** MultiListControlFlow. */
    public static final String MULTILIST_CONTROL_FLOW = "MultiListControlFlow";

    /**
     * Each Informationtype contains a slot where the value is stored. Thoose are the possible slots..
     * 
     */
    public static final class ContentSlots
    {
        /** the Inhalt_Relation slot. */
        public final static String CONTENT_RELATION = "Inhalt_Relation";
        /** the Inhalt_String slot */
        public final static String CONTENT_STRING   = "Inhalt_String";
        /** the Inhalt_String slot */
        public final static String CONTENT_FLOAT    = "Inhalt_Festkommazahl";
        /** the Inhalt_String slot */
        public final static String CONTENT_INTEGER  = "Inhalt_Ganzzahl";
        /** the Inhalt_String slot */
        public final static String CONTENT_LIST     = "Inhalt_List";

        private ContentSlots()
        {

        }

    }

    private InformationTypesConstants()
    {

    }
}
