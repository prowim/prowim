/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 09.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.Validate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Simple XML parser taken from {@link <a href="http://blog.datajelly.com/company/blog/50-parsing-an-xml-document-in-java-6.html">Datajelly</a>}.
 * 
 * @author Oliver Specht, Unknown
 * @version $Revision$
 * @since 2.0.0alpha9
 */
public final class XMLParser
{

    private static XMLParser instance = new XMLParser();

    private XMLParser()
    {

    }

    /**
     * Returns the parser instance
     * 
     * @return {@link XMLParser}, never null
     */
    public static XMLParser getInstance()
    {
        return instance;
    }

    /**
     * Parses the given XML string.
     * 
     * @param xml the string to parse, may not be null
     * @return {@link Document}
     */
    public Document parse(String xml)
    {
        Validate.notNull(xml);
        return parse(new InputSource(new StringReader(xml)));
    }

    /**
     * Parses the given input stream.
     * 
     * @param stream may not be null
     * @return {@link Document}, never null
     */
    public Document parse(InputStream stream)
    {
        Validate.notNull(stream);
        return parse(new InputSource(stream));
    }

    private Document parse(InputSource source)
    {
        Validate.notNull(source);

        try
        {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(source);
            return document;
        }
        catch (ParserConfigurationException e)
        {
            throw new IllegalStateException(e);
        }
        catch (SAXException e)
        {
            throw new IllegalStateException(e);
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }
    }
}
