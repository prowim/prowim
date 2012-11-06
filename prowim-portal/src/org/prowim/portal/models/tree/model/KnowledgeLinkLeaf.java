/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-05-10 14:14:31 +0200 (Di, 10 Mai 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/models/tree/model/KnowledgeLinkLeaf.java $
 * $LastChangedRevision: 5083 $
 *------------------------------------------------------------------------------
 * (c) 03.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.models.tree.model;

import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.graphics.Image;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.utils.GlobalConstants;



/**
 * This is a model for KnwoledgeLinkModel. KnwoledgeLinkModel has not any children at time. <br>
 * It can has a list of {@link KnowledgeLink}.
 * 
 * For future we can re-implement this class, if this should be necessary
 * 
 * @author Maziar Khdoaei
 * @version $Revision: 5083 $
 */
public class KnowledgeLinkLeaf extends DefaultLeaf
{
    // ************************* Variable *******************
    private String              link;
    private String              repository;
    private final KnowledgeLink knowledgeLink;
    private String              knowledgeObjectID;

    /**
     * Constructor. Each KnwoledgeLinkModel should has an unique ID and a name
     * 
     * @param knowLink KnowledgeLink
     * 
     */
    public KnowledgeLinkLeaf(KnowledgeLink knowLink)
    {
        super(knowLink.getID(), knowLink.getName());
        setLink(knowLink.getHyperlink());
        setRepository(knowLink.getRepository());
        setImage(getLinkImage(knowLink));
        setKnowledgeObjectID(knowLink.getKnowledgeObject());
        this.knowledgeLink = knowLink;
    }

    /**
     * Set the link of this KnwoledgeLink
     * 
     * @param link String
     */
    public void setLink(String link)
    {
        Validate.notNull(link);
        this.link = link;
    }

    /**
     * Give the link of this KnwoledgeLink
     * 
     * @return String
     */
    public String getLink()
    {
        return link;
    }

    /**
     * Set the link of this KnwoledgeLink
     * 
     * @param repository String
     */
    public void setRepository(String repository)
    {
        Validate.notNull(repository);
        this.repository = repository;
    }

    /**
     * Give the link of this KnwoledgeLink
     * 
     * @return String
     */
    public String getRepository()
    {
        return repository;
    }

    /**
     * 
     * Get the image of the knowledge link
     * 
     * @param repository Type of link
     * @return Image
     */
    private Image getLinkImage(final KnowledgeLink knowLink)
    {
        Image imageKey;
        String repositoryName = "";
        KnowledgeRepositoryArray knowRepArray = MainController.getInstance().getKnowledgeRepositories();
        Iterator<KnowledgeRepository> knowRepIt = knowRepArray.iterator();

        while (knowRepIt.hasNext())
        {
            KnowledgeRepository knowRep = knowRepIt.next();
            if (knowRep.getID().equals(getRepository()))
            {
                repositoryName = knowRep.getName();
                break;
            }
        }

        if (repositoryName.equals(GlobalConstants.INTERNET))
            imageKey = Resources.Frames.Knowledge.Images.URL_IMAGE.getImage();
        else if (repositoryName.equals(GlobalConstants.NETWORK))
            imageKey = Resources.Frames.Knowledge.Images.NETWORK_IMAGE.getImage();
        else if (repositoryName.equals(GlobalConstants.PROWIM_DMS))
            imageKey = Resources.Frames.Knowledge.Images.DMS_IMAGE.getImage();
        else if (repositoryName.equals(GlobalConstants.PROWIM_WIKI))
            imageKey = Resources.Frames.Knowledge.Images.WIKI_IMAGE.getImage();
        else
            imageKey = Resources.Frames.Tree.Images.TREE_FOLDER.getImage();

        return imageKey;
    }

    /**
     * Gets the knowledgelink.
     * 
     * @return knowledgelink.
     */
    public KnowledgeLink getKnowledgeLink()
    {
        return this.knowledgeLink;
    }

    /**
     * Set the of Knowledge object of this knowledge link
     * 
     * @param knowledgeObjectID the knowledgeObjectID to set
     */
    public void setKnowledgeObjectID(String knowledgeObjectID)
    {
        this.knowledgeObjectID = knowledgeObjectID;
    }

    /**
     * Get the of Knowledge object of this knowledge link
     * 
     * @return the knowledgeObjectID
     */
    public String getKnowledgeObjectID()
    {
        return knowledgeObjectID;
    }

}
