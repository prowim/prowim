/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 27.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.dialogs.knowledge;

import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.portal.MainController;
import org.prowim.portal.utils.GlobalConstants;



/**
 * This temporary class help us to save Document and the ID of repository by adding or editing of knowledge objects <br>
 * and knowledge links. This is necessary, because we do not want to create this objects before the user push OK button.<br>
 * So, this class helps to save this elements temporary and saves this, when user push OK button
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class KnowledgeLinkTemp extends KnowledgeLink
{
    private Document                  document;
    private String                    repositoryName;
    private DocumentContentProperties documentContentProperties = null;
    private Version                   version                   = null;
    private boolean                   newDocumentUploaded       = false;
    private boolean                   hasDocumentInitially      = false;

    /**
     * Creates a new {@link KnowledgeLinkTemp} object.
     * 
     * @param knowLink original object of knowledge link, may not be null
     */
    public KnowledgeLinkTemp(KnowledgeLink knowLink)
    {
        super(knowLink.getID(), knowLink.getName(), knowLink.getCreateTime());

        this.setHyperlink(knowLink.getHyperlink());

        KnowledgeRepositoryArray knowRepArray = MainController.getInstance().getKnowledgeRepositories();
        Iterator<KnowledgeRepository> knowRepIt = knowRepArray.iterator();

        while (knowRepIt.hasNext())
        {
            KnowledgeRepository knowRep = knowRepIt.next();
            if (knowLink.getRepository().equals(knowRep.getID()))
            {
                this.setRepository(knowRep.getID());
                this.setRepositoryName(knowRep.getName());
            }
        }

        if (getRepositoryName().equals(GlobalConstants.PROWIM_DMS) && !getHyperlink().equals(""))
        {
            hasDocumentInitially = true;
        }
    }

    /**
     * Constructor to create a KnowledgeLinkTemp and adds the properties later.
     * 
     * @param id ID should be empty string "", if you create a new KnowledgeLinkTemp.
     * @param name Will be "new Knowledge link"
     * @param createTime empty String
     */
    public KnowledgeLinkTemp(String id, String name, String createTime)
    {
        super(id, name, createTime);

    }

    /**
     * setDocument
     * 
     * @param document the document to set
     */
    public void setDocument(Document document)
    {
        this.document = document;
    }

    /**
     * getDocument
     * 
     * @return the document
     */
    public Document getDocument()
    {
        return document;
    }

    /**
     * Sets the repository name.
     * 
     * @param repositoryName the name of the repository to set, may not be null
     */
    public void setRepositoryName(String repositoryName)
    {
        Validate.notNull(repositoryName);
        this.repositoryName = repositoryName;
    }

    /**
     * Returns the name of the repository.
     * 
     * @return the repository name, never null
     */
    public String getRepositoryName()
    {
        return repositoryName;
    }

    /**
     * Returns the {@link DocumentContentProperties} of this knowledge link.
     * 
     * @return the documentContentProperties, can be null
     */
    public DocumentContentProperties getDocumentContentProperties()
    {
        return documentContentProperties;
    }

    /**
     * Sets the {@link DocumentContentProperties} of this knowledge link.
     * 
     * @param documentContentProperties the documentContentProperties to set, can be null
     */
    public void setDocumentContentProperties(DocumentContentProperties documentContentProperties)
    {
        this.documentContentProperties = documentContentProperties;
    }

    /**
     * Returns the {@link Version} of this knowledge link.
     * 
     * @return the version, can be null
     */
    public Version getVersion()
    {
        return version;
    }

    /**
     * Sets the {@link Version} of this knowledge link.
     * 
     * @param version the version to set, can be null
     */
    public void setVersion(Version version)
    {
        this.version = version;
    }

    /**
     * Set to true, if a new document has been uploaded and is stored in this object.
     * 
     * @param newDocumentUploaded true, if a new document has been uploaded, false otherwise
     */
    public void setNewDocumentUploaded(boolean newDocumentUploaded)
    {
        this.newDocumentUploaded = newDocumentUploaded;
    }

    /**
     * Returns true, if a new document has been uploaded for this object.
     * 
     * @return the newDocumentUploaded true, if a new document has been uploaded
     */
    public boolean isNewDocumentUploaded()
    {
        return newDocumentUploaded;
    }

    /**
     * Returns true, if the knowledge link had a document linked.
     * 
     * @return true, if the knowledge link had a document initially
     */
    public boolean hasDocumentInitially()
    {
        return hasDocumentInitially;
    }
}
