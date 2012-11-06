/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 02.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.browserfunctions;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.Process;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.ProcessVersionsDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.view.process.ModelEditorView;
import org.prowim.rap.modeleditor.ModelEditor;
import org.prowim.rap.modeleditor.ModelEditor.EditorMode;



/**
 * Browser function to show the process versions dialog and to enter a new name for the new version to save.
 * 
 * @author Oliver Specht
 * @version $Revision$
 * @since 2.0.0alpha9
 */
public class BFSaveProcessAsNewVersion extends AbstractModelEditorFunction
{
    private Process process;

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFSaveProcessAsNewVersion(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] arguments)
    {
        process = DefaultDataObjectFactory.createProwimProcessTemplate(getModelEditor().getModelId(), getModelEditor().getModelName());
        ProcessVersionsDialog dialog = new ProcessVersionsDialog(null, Resources.Frames.Toolbar.Actions.SHOW_VERSIONS.getAction(), "", process, true);
        dialog.open();

        // only save as new version if return code is OK
        if (dialog.getReturnCode() == Window.OK)
        {
            ModelEditor modelEditor = (ModelEditor) getBrowser();

            if (modelEditor.getMode() == EditorMode.EDITOR)
            {
                MainController.getInstance().saveProcessAsXML(modelEditor.getModelId(), modelEditor.getModelXML(), true, dialog.getVersionName());

                modelEditor.setSaveOnClose(false);

                for (IViewReference reference : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences())
                {
                    if (reference.getId().equals(ModelEditorView.ID) && modelEditor.getModelId().equals(reference.getSecondaryId()))
                    {
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(reference);
                        break;
                    }
                }
            }
            else
            {
                throw new RuntimeException("Cannot save model. Editor in not in editor mode");
            }
        }

        return null;
    }
}
