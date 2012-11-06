/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-19 17:01:15 +0200 (Mon, 19 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/dms/DMSConstants.java $
 * $LastChangedRevision: 4323 $
 *------------------------------------------------------------------------------
 * (c) 14.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.dms;

/**
 * Constants for the DMS Webservice.
 * 
 * @author Saad Wardi
 * @version $Revision: 4323 $
 */
public final class DMSConstants
{
    /** the parent home name. */
    public final static String  COMPANY_HOME              = "/app:company_home";
    /** the prowim space name. */
    public final static String  PROWIM_SPACE_NAME         = "ProWim";
    /** Under line. */
    public final static String  UNDER_LINE                = "_";
    /** Company home prefix. */
    public final static String  CM                        = "cm:";
    /** Prowim content child name. */
    public final static String  PROWIM_CONTENT_CHILD_NAME = "cm_x003a_";
    /** Where id. */
    public final static String  WHERE_ID                  = "1";
    /** Encoding. */
    public final static String  ENCODING                  = "UTF-8";
    /** Constants for the mime types. */
    public final static String  POINT                     = ".";
    /** The first version: "1.0" */
    public final static String  INITIAL_VERSION_LABEL     = "1.0";

    /** The key for customer id property. */
    public final static String  PROWIM_CUSTOMER_ID        = "prowim.customer.id";
    /** The key for customer folder property. */
    private static final String PROWIM_CUSTOMER_FOLDER    = "prowim.customer.folder";

    private DMSConstants()
    {

    }

    /**
     * 
     * Retrieves the complete search path for accessing to an DMS content.
     * 
     * @param space if null, the default space is used
     * @return non null search path
     * @see #PROWIM_SPACE_NAME
     */
    public static String getSearchSpacePath(String space)
    {
        String defaultSpace = space;
        if (space == null)
            defaultSpace = PROWIM_SPACE_NAME;
        return "PATH:\"" + COMPANY_HOME + "/cm:" + defaultSpace + "/*\"";
    }

    /**
     * Constants for content properties.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 4323 $
     */
    public final class Content
    {
        /** The content qualified name space. */
        public final static String NAME_SPACE         = "{http://www.alfresco.org/model/content/1.0}";
        /** The content qualified name space. */
        public final static String NAME_SPACE2        = "{http://www.alfresco.org/model/dictionary/1.0}";

        /** The content name property. */
        public final static String NAME_PROP          = NAME_SPACE + "name";
        /** The content mime type property. */
        public final static String MIMETYPE_PROP      = NAME_SPACE + "content";
        /** The content title. */
        public final static String TITLE_PROP         = NAME_SPACE + "title";
        /** The modified timestamp prop. */
        public final static String MODIFIED_PROP      = NAME_SPACE + "modified";
        /** The created timestamp property. */
        public final static String CREATED_PROP       = NAME_SPACE + "created";
        /** The description prop. */
        public final static String DESCRIPTION_PROP   = NAME_SPACE + "description ";
        /** The author constant. */
        public final static String AUTHOR_CONST       = "author";
        /** The author property. */
        public final static String AUTHOR_PROP        = NAME_SPACE + AUTHOR_CONST;
        /** The creator property. */
        public final static String CREATOR_PROP       = NAME_SPACE + "creator";
        /** The modifier property. */
        public final static String MODIFIER_PROP      = NAME_SPACE + "modifier";
        /** The versionLabel property. */
        public final static String VERSION_LABEL_PROP = NAME_SPACE + "versionLabel";
        /** The creator property. */
        public final static String USER_ID_PATTERN    = "Person_";

        private Content()
        {

        }
    }

    /**
     * Gets the customer ID.
     * 
     * @return not null customer ID.
     */
    public static String getCustomerFolderID()
    {
        String customerID = System.getProperty(PROWIM_CUSTOMER_ID);
        if (customerID != null)
        {
            return customerID;
        }
        else
        {
            throw new IllegalStateException("No Customer ID configuration was found: " + customerID);
        }
    }

    /**
     * 
     * Returns the name of the root folder for the current customer in DMS.
     * 
     * @return the name, can not be <code>NULL</code>
     */
    public static String getCustomerFolder()
    {
        String customerFolder = System.getProperty(PROWIM_CUSTOMER_FOLDER);
        if (customerFolder != null)
        {
            return customerFolder;
        }
        else
        {
            throw new IllegalStateException("No Customer Folder configuration was found: " + customerFolder);
        }

    }

}
