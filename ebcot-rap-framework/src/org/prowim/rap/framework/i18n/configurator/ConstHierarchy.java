/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-06-19 18:07:21 +0200 (Fr, 19 Jun 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/portal/src/de/ebcot/prowim/portal/i18n/configurator/ConstHierarchy.java $
 * $LastChangedRevision: 1785 $
 *------------------------------------------------------------------------------
 * (c) 14.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.i18n.configurator;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.prowim.rap.framework.i18n.Resources;
import org.prowim.rap.framework.i18n.consts.Const;
import org.prowim.rap.framework.i18n.consts.impl.ConfigureException;



/**
 * This class is not complete and should adapted
 * 
 * @author Maziar Khodaei
 * @version $Revision: 1785 $
 */
public final class ConstHierarchy
{

    // ~ Static fields/initializers -------------------------------------------------------------------------------------------------------------------

    /**
     * Using this option causes the configuration code to ignore missing xml elements for constants defined in the const hierarchy class (in other words, the case were some or all constants are not configured is ignored).
     */
    public static final int IGNORE_MISSING_ELEMENTS  = 1;

    /**
     * Using this option causes the configuration code to ignore missing constants defined in the const hierarchy (in other words, the case were unknown elements are found in the xml file is ignored).
     */
    public static final int IGNORE_MISSING_CONSTANTS = 2;

    /** DOCUMENT ME! */
    public static final int IGNORE_UNKNOWN_MEMBERS   = 4;

    /**
     * GROUP_ID_ATTR_NAME
     */
    static final String     GROUP_ID_ATTR_NAME       = "id";

    /**
     * GROUP_TAG_NAME
     */
    static final String     GROUP_TAG_NAME           = "group";

    /**
     * CLASS_NAME_SPLITTER
     */
    static final Pattern    CLASS_NAME_SPLITTER      = Pattern.compile("([a-z])([A-Z])");

    private ConstHierarchy()
    {

    }

    // log-mechanism
    // private static final Logger LOG = Logger.getLogger(ConstHierarchy.class); // log out

    // ~ Methods --------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Configures a const hierarchy class. Options can be used to disable certain kind of exceptions (logging will happen anyhow). This version of configure is useful for gradually developing the XML file and the constant hierarchy class. Without a good reason one should always use the two argument form of configure.
     * 
     * @param constHierarchy Class following the const hierarchy pattern.
     * @param xmlFileName Name of configuration file for the const hierarchy class.
     * @param options ORed options to disable certain kind of exceptions.
     * @param bundle Name of plug in bundle. Is normally define as Activator ID, e.g. "de.ebcot.rap.framework".
     * @throws ConfigureException If anything regarding the configuration wents wrong.
     */
    public static void configure(@SuppressWarnings("rawtypes") Class constHierarchy, URL xmlFileName, int options, String bundle)
                                                                                                                                 throws ConfigureException
    {
        /*
         * if (LOG.isDebugEnabled()) { LOG.debug("CALL configure\tconstHierarchy:" + constHierarchy + " xmlFileName:" + xmlFileName + " ignore missing elements:" + (options & IGNORE_MISSING_ELEMENTS) + " ignore missing constants:" + (options & IGNORE_MISSING_CONSTANTS) + " ignore unknown members:" + (options & IGNORE_UNKNOWN_MEMBERS)); }
         */

        // programming error, therefore runtime exception
        if ((constHierarchy == null) || (xmlFileName == null))
        {
            throw new IllegalArgumentException("constHierarchy:" + constHierarchy + " xmlFileName:" + xmlFileName);
        }

        // read hierarchy map from const hierarchy class
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Map<String, Object>>> hierarchyMap = createHierarchyMap(constHierarchy, (options & IGNORE_UNKNOWN_MEMBERS) != 0);

        // configure constants from xml file with the help of parse tree
        try
        {
            Document doc = new SAXBuilder().build(xmlFileName);
            configureFromXML(hierarchyMap, doc.getRootElement(), (options & IGNORE_MISSING_ELEMENTS) != 0, (options & IGNORE_MISSING_CONSTANTS) != 0,
                             bundle);
        }
        catch (java.io.IOException e)
        {
            throw new ConfigureException("Could not read configuration file " + xmlFileName + ":", e);
        }
        catch (org.jdom.JDOMException e)
        {
            throw new ConfigureException("Could not parse configuration file " + xmlFileName + ":", e);
        }
    }

    /**
     * Configures a const hierarchy class.
     * 
     * @param constHierarchy Class following the const hierarchy pattern.
     * @param xmlFileName Name of configuration file for the const hierarchy class.
     * @param bundle Name of plug in bundle. Is normally define as Activator ID, e.g. "de.ebcot.rap.framework".
     * @throws ConfigureException If anything regarding the configuration wents wrong.
     */
    public static void configure(@SuppressWarnings("rawtypes") Class constHierarchy, URL xmlFileName, String bundle) throws ConfigureException
    {
        /*
         * if (LOG.isDebugEnabled()) { LOG.debug("CALL configure\tconstHierarchy:" + constHierarchy + " xmlFileName:" + xmlFileName); }
         */
        configure(constHierarchy, xmlFileName, 0, bundle);
    }

    /**
     * Description
     * 
     * @param clazz
     * @return Class[] array of inner classes
     */
    @SuppressWarnings("rawtypes")
    private static Class[] getInnerClasses(Class<Resources> clazz)
    {
        /*
         * if (LOG.isDebugEnabled()) { LOG.debug("getInnerClasses\tclazz:" + clazz); }
         */

        // list of nested inner classes
        Class[] nestedInnerClasses = clazz.getDeclaredClasses();

        // create mapping from class names to classes
        Map<String, Class> namesClasses = new HashMap<String, Class>();

        for (int i = 0; i < nestedInnerClasses.length; i++)
        {
            namesClasses.put(nestedInnerClasses[i].getName(), nestedInnerClasses[i]);
        }

        // now remove all inner classes from the mapping
        // this will result in a mapping containing only classes that are themselfes not inner classes = top level inner classes
        for (int i = 0; i < nestedInnerClasses.length; i++)
        {
            if (nestedInnerClasses[i].getClasses().length > 0)
            {
                Class[] innerInnerClasses = nestedInnerClasses[i].getClasses();

                for (int j = 0; j < innerInnerClasses.length; j++)
                {
                    namesClasses.remove(innerInnerClasses[j].getName());
                }
            }
        }

        return namesClasses.values().toArray(new Class[] {});
    }

    /**
     * Recursive configurations of a constant hierarchy from a hierarchy map and a XML document.
     * 
     * @param hierarchyMap Hierarchy map for the current part of the const hierarchy (see {@link createHierarchyMap}).
     * @param element XML element for the current element in the XML document.
     * @param ignoreMissingElements See {@link IGNORE_MISSING_ELEMENTS}option.
     * @param ignoreMissingConstants See {@link IGNORE_MISSING_CONSTANTS}option.
     * @throws ConfigureException If anything regarding the configuration wents wrong.
     * @throws IllegalStateException DOCUMENT ME!
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void configureFromXML(Map hierarchyMap, Element element, boolean ignoreMissingElements, boolean ignoreMissingConstants,
                                         String bundle) throws ConfigureException
    {
        /*
         * if (LOG.isDebugEnabled()) { LOG.debug("CALL configureFromXML\thierarchyMap:" + hierarchyMap + " element:" + element + " ignoreMissingElements:" + ignoreMissingElements + " ignoreMissingConstants:" + ignoreMissingConstants); }
         */
        List<Object> children = element.getChildren();

        for (Iterator<Object> i = children.iterator(); i.hasNext();)
        {
            Element child = (Element) i.next();

            if ( !hierarchyMap.containsKey(child.getName()))
            {
                String message = "Could not find a group or constant for xml element " + child + " with name: " + child.getName() + " and id: "
                        + child.getAttribute("id");

                // LOG.debug(message);
                if ( !ignoreMissingConstants)
                {
                    throw new ConfigureException(message);
                }

                // process next xml element
                continue;
            }

            // get map with possible id attributes
            Map idAttributeMap = (Map) hierarchyMap.get(child.getName());
            List<Object> attributes = child.getAttributes();

            // search attributes of xml element for id. if matching attribute name for id is found
            // try to fetch object
            boolean foundIdAttrib = false;

            for (Iterator<Object> j = attributes.iterator(); j.hasNext();)
            {
                Attribute attribute = (Attribute) j.next();

                if (idAttributeMap.containsKey(attribute.getName()))
                {
                    Map idValueMap = (Map) idAttributeMap.get(attribute.getName());

                    // note: it is possible that this code fails in rare (or illegal?) cases. it will fail in the following case:
                    // 1. the element has multiple attributes
                    // 2. at least two of the attributes have the same name as id attributes for equal named xml elements at the same level/depth,
                    // but only one of them (of course) is the real id attribute
                    // 3. the wrong id attribute is the first one being read
                    if ( !idValueMap.containsKey(attribute.getValue()))
                    {
                        // this case is equal to not finding the id, so get out of search loop
                        break;
                    }

                    Object value = idValueMap.get(attribute.getValue());

                    // configure constant
                    if (value instanceof Const)
                    {
                        ((Const) value).configure(child, bundle);
                    }

                    // configure group (recursively)
                    else if (value instanceof Map)
                    {
                        // remove comment, when errors occur during parsing to see where they occur
                        configureFromXML((Map) value, child, ignoreMissingElements, ignoreMissingConstants, bundle);
                    }
                    else
                    {
                        // should never happen
                        throw new IllegalStateException("Found value in hierarchy map that is neither const nor map:" + value);
                    }

                    // remove value from parse tree (remove keys as well if values are empty; this is needed to check
                    // whether all constants have been configured)
                    idValueMap.remove(attribute.getValue());

                    if (idValueMap.isEmpty())
                    {
                        idAttributeMap.remove(attribute.getName());
                    }

                    if (idAttributeMap.isEmpty())
                    {
                        hierarchyMap.remove(child.getName());
                    }

                    foundIdAttrib = true;

                    // id attribute is found, so get out of search loop
                    break;
                }
            }

            if ( !foundIdAttrib)
            {
                String message = "Could not find a group or constant for xml element " + child + " with name: " + child.getName() + " and id: "
                        + child.getAttribute("id");

                // LOG.debug(message);
                if ( !ignoreMissingConstants)
                {
                    throw new ConfigureException(message);
                }
            }
        }

        // check if all constants have been configured
        if ( !hierarchyMap.isEmpty())
        {
            String message = "Some elements have not been configured in XML: " + hierarchyMap.toString();

            for (Iterator<Object> i = hierarchyMap.keySet().iterator(); i.hasNext();)
            {
                Map idAttributeMap = (Map) hierarchyMap.get(i.next());

                for (Iterator<Object> j = idAttributeMap.keySet().iterator(); j.hasNext();)
                {
                    message += ("\n\t" + idAttributeMap.get(j.next()));
                }
            }

            // LOG.debug(message);
            if ( !ignoreMissingElements)
            {
                throw new IllegalStateException(message);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Map<String, Map<String, Object>>> createHierarchyMap(Class<Resources> constHierarchy, boolean ignoreUnknownMembers)
                                                                                                                                                  throws ConfigureException
    {
        @SuppressWarnings("rawtypes")
        Class[] groups = getInnerClasses(constHierarchy); // i18n constant groups
        Field[] consts = constHierarchy.getFields(); // i18n constants

        Map<String, Map<String, Map<String, Object>>> tagNames = new HashMap<String, Map<String, Map<String, Object>>>(); // mapping from tag names to id attribute names

        for (int i = 0; i < groups.length; i++)
        {
            String[] path = groups[i].getName().split("\\$", 0);
            String className = path[path.length - 1];
            String groupId = CLASS_NAME_SPLITTER.matcher(className).replaceAll("$1_$2").toUpperCase();

            Map<String, Map<String, Map<String, Object>>> value = createHierarchyMap(groups[i], ignoreUnknownMembers);

            // build map entry
            if ( !tagNames.containsKey(GROUP_TAG_NAME))
            {
                tagNames.put(GROUP_TAG_NAME, new HashMap<String, Map<String, Object>>());
            }

            Map<String, Map<String, Object>> idMap = tagNames.get(GROUP_TAG_NAME);

            if ( !idMap.containsKey(GROUP_ID_ATTR_NAME))
            {
                idMap.put(GROUP_ID_ATTR_NAME, new HashMap<String, Object>());
            }

            idMap.get(GROUP_ID_ATTR_NAME).put(groupId, value);
        }

        // ++++++++++++++++++++++++
        for (int i = 0; i < consts.length; i++)
        {
            // is it a constant group or constant ?
            // if (XMLMapper.class.isAssignableFrom(fields[i].getType()))
            Object object;

            try
            {
                object = consts[i].get(null);
            }
            catch (IllegalAccessException e)
            {
                throw new ConfigureException("Could not access member of const hierarchy class:", e);
            }

            if (Const.class.isInstance(object))
            {
                String id = consts[i].getName(); // get name of static member
                Const mapper; // get the static member object

                // all static members within the class should be public
                try
                {
                    mapper = (Const) consts[i].get(null);
                }
                catch (IllegalAccessException e)
                {
                    throw new ConfigureException("Could not access member of const hierarchy class:", e);
                }

                // build map entry
                String tagName = mapper.getTagName();
                String idAttrName = mapper.getIdAttrName();

                // LOG.debug("id:" + id + " tagName:" + tagName + " idAttrName:" + idAttrName + " value:" + mapper);
                if ( !tagNames.containsKey(tagName))
                {
                    // LOG.debug("Creating map for element name.");
                    tagNames.put(tagName, new HashMap<String, Map<String, Object>>());
                }

                Map<String, Map<String, Object>> idMap = tagNames.get(tagName);

                if ( !idMap.containsKey(idAttrName))
                {
                    // LOG.debug("Creating map for id attribute name.");
                    idMap.put(idAttrName, new HashMap<String, Object>());
                }

                idMap.get(idAttrName).put(id, mapper);
            }
            else
            {
                String message = "Found static member in hierarchy that is not of type ConstHierarchy.Group or ConstHierarchy.Const ."
                        + " Const hierarchy class:" + constHierarchy.getName() + " Static member name:" + consts[i].getName();

                // LOG.debug(message);
                if ( !ignoreUnknownMembers)
                {
                    throw new ConfigureException(message);
                }
            }
        }

        return tagNames;
    }

}
