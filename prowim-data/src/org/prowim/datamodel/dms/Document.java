/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-05-07 14:37:57 +0200 (Fr, 07 Mai 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/dms/Document.java $
 * $LastChangedRevision: 3801 $
 *------------------------------------------------------------------------------
 * (c) 14.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
 *This file is part of ProWim.

ProWim is free soimport javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;
 your option) any later version.

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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents document. A {@link Document} is a file with byte content stored in DMS.
 * 
 * @author Saad Wardi
 * @version $Revision: 3801 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = { "name", "contentType", "size", "content" })
public class Document
{
    private String name;
    private String contentType;
    private long   size;
    private byte[] content;

    /**
     * Creates a document data-object that can be transmit to the prowim-server.
     * 
     * @param name {@link Document#setName(String)}
     * @param contentType {@link Document#setContentType(String)}
     * @param content {@link Document#setContent(byte[])}.
     */
    public Document(String name, String contentType, byte[] content)
    {
        setName(name);
        setContentType(contentType);
        setSize(content.length);
        setContent(content);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Document()
    {
    }

    /**
     * {@link Document#setName(String)}
     * 
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the content that will be stored in DMS.<br>
     * The name of the content can be the filename with extension.<br>
     * Example: Create a Document from the file on the file system at this path: c:/home/mydoc.pdf<br>
     * Document doc = new Document("mydoc.pdf", size, bytes)
     * 
     * @param name the name to set
     */
    public void setName(String name)
    {
        Validate.notNull(name);
        this.name = name;
    }

    /**
     * {@link Document#setContentType(String)}
     * 
     * @return the contentType
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * Sets the file contentType.
     * 
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType)
    {
        Validate.notNull(contentType);
        this.contentType = contentType;
    }

    /**
     * {@link Document#setSize(long)}
     * 
     * @return the size
     */
    public long getSize()
    {
        return size;
    }

    /**
     * Sets the file size. The size is the number of bytes of a binary file.
     * 
     * @param size the size to set
     */
    public void setSize(long size)
    {
        Validate.isTrue(size > 0);
        this.size = size;
    }

    /**
     * {@link Document#setContent(byte[])}
     * 
     * @return the content
     */
    public byte[] getContent()
    {
        return content;
    }

    /**
     * Sets the content bytes.
     * 
     * @param content the content to set
     */
    public void setContent(byte[] content)
    {
        Validate.notNull(content);
        this.content = content;
    }
}
