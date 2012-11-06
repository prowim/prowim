/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.process.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.prowim.portal.MainController;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * Helper Object to provide exclusive modifying access to the {@link ModelEditor}
 * 
 * @see LockingEditorInfo
 * @see HijackModelEditorView
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0RC1
 */
public final class ModelEditorLockingManager
{
    private static final ModelEditorLockingManager INSTANCE          = new ModelEditorLockingManager();

    /**
     * Locking instances for model editor instances
     * 
     * @see HijackModelEditorView HijackModelEditorView for further documentation
     */
    private final Map<String, LockingEditorInfo>   modelEditorLocker = new HashMap<String, LockingEditorInfo>();

    /**
     * Description.
     */
    private ModelEditorLockingManager()
    {
    }

    /**
     * get the singleton instance
     * 
     * @return the instance
     */
    public static ModelEditorLockingManager getInstance()
    {
        return INSTANCE;
    }

    /**
     * Retrieve the {@link LockingEditorInfo} for the current locking view of processId
     * 
     * @param processId the process ID to return the locking editor info for, may not be null
     * @return <code>null</code> if the process is not locked
     */
    public LockingEditorInfo getLockingEditorInfoForProcess(String processId)
    {
        Validate.notNull(processId);
        Validate.isTrue(Thread.holdsLock(INSTANCE), "Accessing the manager must be performed while synchronizing on the instance");

        return modelEditorLocker.get(getUuid(processId));
    }

    /**
     * 
     * Retrieve the {@link LockingEditorInfo} for the current locking view of processId
     * 
     * @param uuID the alfresco UUID to return the locking editor info for, may not be null
     * @return <code>null</code> if the process is not locked
     */
    public LockingEditorInfo getLockingEditorInfoForUUID(String uuID)
    {
        Validate.notNull(uuID);
        Validate.isTrue(Thread.holdsLock(INSTANCE), "Accessing the manager must be performed while synchronizing on the instance");

        return modelEditorLocker.get(uuID);
    }

    /**
     * Get a unique ID for all versions of a process.
     * 
     * @param processId
     * @return never null
     */
    private String getUuid(String processId)
    {
        return MainController.getInstance().getAlfrescoProcessModelUuid(processId);
    }

    /**
     * 
     * Register the {@link HijackModelEditorView} as the current locker for the process.<br/>
     * Make sure, you call {@link HijackModelEditorView#hijack(LockingEditorInfo)} before registering, if there was already a locker registered.
     * 
     * @param processId must not be null
     * @param view the locking view. Not null.
     * @return never null. The created {@link LockingEditorInfo}
     */
    public LockingEditorInfo registerLockerForProcess(final String processId, final HijackModelEditorView view)
    {
        Validate.notNull(processId);
        Validate.notNull(view);
        Validate.isTrue(Thread.holdsLock(INSTANCE), "Accessing the manager must be performed while synchronizing on the instance");

        return modelEditorLocker.put(getUuid(processId), new LockingEditorInfo(view));
    }

    /**
     * Deregister the {@link HijackModelEditorView} for processId.<br/>
     * Does NOT fail, if no locker was registered for the processId, but DOES throw a Runtime Exception if a different view for registered as the locker.
     * 
     * @param processId the process ID to deregister, may not be null
     * @param view the {@link HijackModelEditorView} to deregister from, may not be null
     */
    public void deregisterLockerForProcess(final String processId, final HijackModelEditorView view)
    {
        Validate.notNull(processId);
        Validate.notNull(view);
        Validate.isTrue(Thread.holdsLock(INSTANCE), "Accessing the manager must be performed while synchronizing on the instance");
        Validate.isTrue(getLockingEditorInfoForProcess(processId) == null || view == getLockingEditorInfoForProcess(processId).getView(),
                        "You tried to deregister an unregistered view");

        modelEditorLocker.remove(getUuid(processId));
    }

    /**
     * Deregister the {@link HijackModelEditorView} for the given UUID, which is the ID of the alfresco storage point.<br/>
     * Does NOT fail, if no locker was registered for the processId, but DOES throw a Runtime Exception if a different view for registered as the locker.
     * 
     * @param uuID the alfresco UUID to deregister the locker for, may not be null
     * @param view the {@link HijackModelEditorView} to deregister
     */
    public void deregisterLockerForUUID(final String uuID, final HijackModelEditorView view)
    {
        Validate.notNull(uuID);
        Validate.notNull(view);
        Validate.isTrue(Thread.holdsLock(INSTANCE), "Accessing the manager must be performed while synchronizing on the instance");
        Validate.isTrue(getLockingEditorInfoForUUID(uuID) == null || view == getLockingEditorInfoForUUID(uuID).getView(),
                        "You tried to deregister an unregistered view");

        modelEditorLocker.remove(uuID);
    }
}
