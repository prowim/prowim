/*==============================================================================
 * File $Id: MainPerspective.java 2271 2009-08-28 14:17:57Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2009-08-28 16:17:57 +0200 (Fr, 28 Aug 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/MainPerspective.java $
 * $LastChangedRevision: 2271 $
 *------------------------------------------------------------------------------
 * (c) 13.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *This file is part of ProWim.

ProWim is freeimport org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import org.prowim.portal.view.BlankView;
import org.prowim.portal.view.HeaderView;
import org.prowim.portal.view.NavigationView;
 will be useful,
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
 *
 */
package org.prowim.portal;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.prowim.portal.view.BlankView;
import org.prowim.portal.view.HeaderView;
import org.prowim.portal.view.NavigationView;



/**
 * MainPerspective groups the workbench in three main area: Header, Navigation and workframe.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 2271 $
 */
public class MainPerspective implements IPerspectiveFactory
{

    /**
     * ID of View
     */
    public static final String ID = MainPerspective.class.getName();

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    @Override
    public void createInitialLayout(IPageLayout layout)
    {

        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.setFixed(true);

        // Create header view
        layout.addStandaloneView(HeaderView.ID, false, IPageLayout.TOP, 0.085f, editorArea);
        // Create navigation view
        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.20f, editorArea);

        // Create workframe view. By initialization we create only one blank view as place holder. Other views can added, when necessary
        IFolderLayout folder = layout.createFolder("workframe", IPageLayout.RIGHT, 0.5f, editorArea);
        folder.addPlaceholder(BlankView.ID + ":*");
    }
}
