/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 20.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

/**
 * This is an Interface for classes performing modifying operations on the XML Process Model.<br/>
 * <br/>
 * It is required that only one instance at a time may modify the XML.<br/>
 * <br/>
 * To guarantee this, it is necessary for modifying classes to
 * <ol>
 * <li>Implement this Interface</li>
 * <li>While locking on {@link ModelEditorLockingManager#getInstance()}:
 * <ol>
 * <li>Check if modelEditor is already locked by other instance by checking if {@link ModelEditorLockingManager#getLockingEditorInfoForProcess(String)} returns a non-null object</li>
 * <li>If already locked a locking-object was returned, use {@link LockingEditorInfo#getView()} to call {@link HijackModelEditorView#hijack(LockingEditorInfo)}
 * <li>If hijacking was successful, or hijacking wasn't necessary register using {@link ModelEditorLockingManager#registerLockerForProcess(String, HijackModelEditorView)}</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0RC1
 */
public interface HijackModelEditorView
{

    /**
     * 
     * This method must provide an implementation for a supporting view to get hijacked. It is supposed to be used with views modifying the process model XML, which only may happen exclusively.
     * 
     * That is, the implementation is required to provide a determined state of the model and return <code>true</code> iff the model is ready to be hijacked.
     * 
     * @param lockingEditorInfo The non-null meta-info of the hijacked view. Make sure that {@link LockingEditorInfo#getView()} returns the instance of the implementing class
     * @return true, iff the model can be hijacked
     */
    boolean hijack(final LockingEditorInfo lockingEditorInfo);
}
