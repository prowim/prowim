/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 25.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.tables.labelprovider;

import org.prowim.datamodel.search.KnowledgeSearchLinkResult;
import org.prowim.datamodel.search.KnowledgeSearchResult;
import org.prowim.datamodel.search.KnowledgeSearchResult.KnowledgeSource;
import org.prowim.datamodel.search.SearchProcessResult;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.table.DefaultTableLabelProvider;



/**
 * A label provider for {@link KnowledgeSearchResult knowledge search results}.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a10
 */
public class KnowlegdeSearchLabelProvider extends DefaultTableLabelProvider
{

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.table.DefaultTableLabelProvider#getColumnText(java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex)
    {
        String title = null;
        String source = null;
        String parent = null;

        KnowledgeSearchResult searchResult = (KnowledgeSearchResult) element;
        title = searchResult.getTitle();

        KnowledgeSource knowledgeSource = searchResult.getSource();

        switch (knowledgeSource)
        {
            case DMS:
                source = Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_DMS.getText();
                break;
            case WIKI:
                source = Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_WIKI.getText();
                break;
            case KNOWLEDGE_OBJECT:
                source = Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_KNOWLEDGE_OBJECT.getText();
                break;
            case KNOWLEDGE_LINK:
                source = Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_KNOWLEDGE_LINK.getText();
                parent = Resources.Frames.Knowledge.Texts.KNOW_OBJECT.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE1
                        + MainController.getInstance().getKnowlegdeObj(((KnowledgeSearchLinkResult) searchResult).getKnowledgeObject()).getName();
                break;
            case KNOWLEDGE_DOAMAIN:
                source = Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_KNOWLEDGE_DOMAIN.getText();
                break;
            case PROCESS:
                source = Resources.Frames.Process.Texts.PROCESS.getText();
                parent = Resources.Frames.Process.Texts.PROCESS_TYPE.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE1
                        + ((SearchProcessResult) searchResult).getProcess().getProcessType();

                break;

            default:
                throw new IllegalArgumentException("The knowledge source " + knowledgeSource + " is not supported");
        }

        switch (columnIndex)
        {
            case 0:
                return source;
            case 1:
                return title;
            case 2:
                return parent;
            default:
                return "";
        }
    }
}
