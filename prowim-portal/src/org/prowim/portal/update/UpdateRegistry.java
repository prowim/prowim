/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.update;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang.Validate;
import org.eclipse.rwt.lifecycle.PhaseEvent;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.rwt.lifecycle.PhaseListener;
import org.eclipse.swt.widgets.Display;
import org.prowim.portal.ApplicationEntryPoint;
import org.prowim.portal.view.DefaultView;

import de.ebcot.tools.logging.Logger;


/**
 * This singleton stores information about updated entities. Each update information will be stored for a limited period of time and will then be discarded from the registry.<br/>
 * Clients may poll for events occured after a given timestamp
 * 
 * For details of the concept look in <a href="http://mediawiki.ebcot.info/index.php/Aktualisierungen#Verteilung_der_Aktualisierungsnachrichten">Verteilung der Aktualisierungsnachrichten</a>
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0a8
 */
@SuppressWarnings("serial")
public final class UpdateRegistry implements PhaseListener
{

    private static final Logger                                 LOG                     = Logger.getLogger(UpdateRegistry.class);

    /**
     * The time to live for update events in nanoSeconds. All {@link UpdateEvent}s older than {@link #TTL} will be removed from the registry.
     */
    private static final long                                   TTL                     = ApplicationEntryPoint.SESSION_TIMEOUT * 1000L * 1000L;

    /**
     * The time interval in nanoSeconds when cleanup runs to remove old {@link UpdateEvent}s
     */
    private static final long                                   CLEANUP_INTERVAL        = 5L * 1000L * 1000L;

    private static final UpdateRegistry                         INSTANCE                = new UpdateRegistry();

    /**
     * For each {@link EntityType} there is a set of {@link UpdateEvent}s sorted ascending by incoming timestamp.
     */
    private final Map<EntityType, SortedSet<UpdateEvent>>       registry                = new HashMap<EntityType, SortedSet<UpdateEvent>>(
                                                                                                                                          EntityType
                                                                                                                                                  .values().length);

    /**
     * The timestamp of the last cleanup run
     */
    private long                                                lastCleanupTime         = System.nanoTime();

    /**
     * {@link Semaphore} to control access to the cleanup process.
     */
    private final Semaphore                                     isCleanupInProgress     = new Semaphore(1);

    /**
     * This {@link Semaphore} is used to block adding new {@link UpdateEvent}s while a client is in the process of getting updates since the last timestamp and setting the next timestamp<br/>
     * 
     * That is, between getting the last timestamp from {@link #lastPollTimeForView} and setting the next one, no {@link UpdateEvent}s with and intermediate timestamp must be created. If there were any {@link UpdateEvent}s created, the said client would miss the event on the next poll.
     */
    private final Semaphore                                     eventTimestampSemaphore = new Semaphore(1);

    /**
     * The storage for all registered listeners, sorted by {@link EntityType}
     */
    private final Map<UpdateEventListener, EnumSet<EntityType>> listeners               = new HashMap<UpdateEventListener, EnumSet<EntityType>>();

    /**
     * For each registered {@link DefaultView} we track the time of the last update
     */
    private final Map<UpdateEventListener, Long>                lastPollTimeForView     = Collections
                                                                                                .synchronizedMap(new HashMap<UpdateEventListener, Long>());

    /**
     * Create a new {@link UpdateRegistry}
     */
    private UpdateRegistry()
    {
        for (EntityType type : EntityType.values())
        {
            registry.put(type, new TreeSet<UpdateEvent>(new Comparator<UpdateEvent>()
            {
                /**
                 * 
                 * UpdateEvents need to be sorted by timestamp ascending<br/>
                 * All attributes used in the comparator must also be implemented adequately in DummyUpdateLister
                 * 
                 * @param o1
                 * @param o2
                 * @return the comparison
                 */
                @Override
                public int compare(UpdateEvent o1, UpdateEvent o2)
                {
                    return new Long(o1.getTimestamp()).compareTo(o2.getTimestamp());
                }
            }));
        }
    }

    /**
     * 
     * Add a new {@link UpdateEvent} and performs cleanup if necessary
     * 
     * @param entityType The type of entities affected by the update
     * @param entityIds The ids of the affected entities
     */
    public void addUpdate(EntityType entityType, String... entityIds)
    {
        addUpdate(EnumSet.of(entityType), entityIds);
    }

    /**
     * 
     * Add a new {@link UpdateEvent} and performs cleanup if necessary
     * 
     * @param entityTypes The types of entities affected by the update
     * @param entityIds The ids of the affected entities
     */
    public void addUpdate(EnumSet<EntityType> entityTypes, String... entityIds)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("adding update for types [" + entityTypes + "] with changed ids [" + Arrays.toString(entityIds) + "]");
        }

        for (EntityType entityType : entityTypes)
        {
            SortedSet<UpdateEvent> updatesForType = registry.get(entityType);

            // synchronized with performCleanup(). do not add updates while cleanup is running
            synchronized (updatesForType)
            {
                try
                {
                    this.eventTimestampSemaphore.acquire();
                    UpdateEvent event = new UpdateEvent(entityType, entityIds);
                    this.eventTimestampSemaphore.release();

                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("Adding update with timestamp [" + event.getTimestamp() + "]");
                    }

                    updatesForType.add(event);
                }
                catch (InterruptedException e)
                {
                    throw new IllegalStateException("Was interrupted while waiting to create an updateEvent", e);
                }

            }
        }

        performCleanup();
    }

    /**
     * Perform Cleanup (remove events older than {@link #TTL}) if necessary
     */
    private void performCleanup()
    {
        // Don't block waiting for the semaphore if one thread is already checking for cleanup
        if (isCleanupInProgress.tryAcquire())
        {
            try
            {
                // check if cleanup run is necessary
                boolean isCleanupOverdue = System.nanoTime() > (lastCleanupTime + CLEANUP_INTERVAL);
                if (isCleanupOverdue)
                {
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug("performing cleanup");
                    }

                    // UpdateEvents before this referenceTimeStamp need to be removed
                    long referenceTimeStamp = System.nanoTime() - TTL;

                    for (SortedSet<UpdateEvent> eventsForType : registry.values())
                    {
                        // synchronized with addUpdate. perform cleanup while update is added
                        synchronized (eventsForType)
                        {
                            Iterator<UpdateEvent> updateEventIterator = eventsForType.iterator();
                            while (updateEventIterator.hasNext())
                            {
                                final UpdateEvent updateEvent = updateEventIterator.next();
                                if (updateEvent.getTimestamp() < referenceTimeStamp)
                                {
                                    if (LOG.isDebugEnabled())
                                    {
                                        LOG.debug("Removing outdated updateEvent [" + updateEvent + "]");
                                    }
                                    updateEventIterator.remove();
                                }
                                else
                                {
                                    // since elements are ordered by timestamp, we can stop now
                                    break;
                                }
                            }
                        }
                    }

                    lastCleanupTime = System.nanoTime();
                }
                else if (LOG.isDebugEnabled())
                {
                    LOG.debug("No cleanup necessary");
                }
            }
            finally
            {
                isCleanupInProgress.release();
            }
        }
    }

    /**
     * 
     * Retrieve an {@link UpdateEvent} containing all ids of entities updated after the given timestamp.
     * 
     * @param entityType The type of the entities to get updates for
     * @param startTimeStamp use <code>null</code> to retrieve all available updates. if not null, only get events after timestamp
     * @return never null
     */
    public UpdateNotification getUpdateEvents(EntityType entityType, Long startTimeStamp)
    {
        SortedSet<UpdateEvent> allUpdates = registry.get(entityType);
        synchronized (allUpdates)
        {
            Set<String> updatedIds = new HashSet<String>(allUpdates.size());

            boolean containsForeignEvents = false;

            for (UpdateEvent event : allUpdates)
            {
                if (startTimeStamp == null || event.getTimestamp() > startTimeStamp)
                {
                    updatedIds.addAll(event.getEntityIds());

                    containsForeignEvents |= event.getCreator() == null || Display.getCurrent() == null || Display.getCurrent() != event.getCreator();
                }
            }

            return new UpdateNotification(entityType, updatedIds.toArray(new String[updatedIds.size()]), containsForeignEvents);
        }

    }

    /**
     * getInstance
     * 
     * @return the instance
     */
    public static UpdateRegistry getInstance()
    {
        return INSTANCE;
    }

    /**
     * 
     * Possible types to generate {@link UpdateEvent}s for
     * 
     * @author Philipp Leusmann, Oliver Wolff, Maziar Khodaei
     * @version $Revision$
     * @since 2.0.0a9
     */
    public static enum EntityType
    {
        /** A process entity */
        PROCESS,
        /** A knowledge object entity (auf deutsch: "Wissensobjekt") */
        KNOWLEDGE,
        /** An organization entity */
        ORGANIZATION,
        /** A workflow entity */
        WORKFLOW,
        /** An activity entity */
        ACTIVITY,
        /** A role entity */
        ROLE,
        /** A resource entity (auf deutsch: "Mittel") */
        MEAN,
        /** A process category entity */
        PROCESSCATEGORY,
        /** A knowledge link entity (auf deutsch: "Wissensverweis") */
        KNOWLEDGELINK,
        /** A knowledge domain entity (auf deutsch: "Wissensbereich") */
        KNOWLEDGEDOMAIN,
        /** A person entity */
        PERSON,
        /** A physically depot to save files (auf deutsch: "Ablage") */
        RESULTSMEMORY;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.rwt.lifecycle.PhaseListener#afterPhase(org.eclipse.rwt.lifecycle.PhaseEvent)
     */
    @Override
    public void afterPhase(PhaseEvent event)
    {

    }

    /**
     * 
     * This method forwards the {@link UpdateEvent}s to the client. Because of it's nature being a {@link PhaseListener}, this method is called whenever _a_ client performs a GUI update.
     * 
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.rwt.lifecycle.PhaseListener#beforePhase(org.eclipse.rwt.lifecycle.PhaseEvent)
     */
    @Override
    public void beforePhase(PhaseEvent event)
    {

        // Notify the current listener about available updates

        for (UpdateEventListener currentListener : getUpdateListenerForDisplay(Display.getCurrent()))
        {

            if (currentListener != null)
            {
                synchronized (currentListener)
                {
                    Long lastPollTimeForCurrentListener = null;
                    try
                    {
                        eventTimestampSemaphore.acquire();
                        lastPollTimeForCurrentListener = this.lastPollTimeForView.get(currentListener);

                        long nextPollTimestamp = System.nanoTime();
                        this.lastPollTimeForView.put(currentListener, nextPollTimestamp);

                        eventTimestampSemaphore.release();
                    }
                    catch (InterruptedException e)
                    {
                        LOG.error("Aquiring the semaphore failed. Cancelling request not forwarding any requests ", e);
                        return;
                    }

                    final UpdateNotificationCollection updateEvents = new UpdateNotificationCollection();

                    if (this.listeners.containsKey(currentListener))
                    {
                        for (EntityType entityType : this.listeners.get(currentListener))
                        {
                            UpdateNotification updateEvent = getUpdateEvents(entityType, lastPollTimeForCurrentListener);
                            if ( !updateEvent.getEntityIds().isEmpty())
                            {
                                updateEvents.add(updateEvent);
                            }
                        }
                    }

                    if ( !updateEvents.isEmpty())
                    {
                        currentListener.getView().handleUpdateEvents(updateEvents);
                    }
                }
            }
        }
    }

    /**
     * 
     * Find an {@link UpdateEventListener} using the given {@link Display}
     * 
     * @param display
     * @return <code>null</code> If no corresponding {@link UpdateEventListener} is found
     */
    private Set<UpdateEventListener> getUpdateListenerForDisplay(final Display display)
    {
        Set<UpdateEventListener> listeners = new HashSet<UpdateRegistry.UpdateEventListener>();

        for (UpdateEventListener listener : this.lastPollTimeForView.keySet())
        {
            if (listener.getDisplay() == display)
            {
                listeners.add(listener);
            }
        }

        if (LOG.isDebugEnabled())
        {
            LOG.debug("Found listeners for display: " + listeners);
        }

        return listeners;
    }

    /**
     * 
     * Find an {@link UpdateEventListener} using the given {@link DefaultView}
     * 
     * @param display
     * @return <code>null</code> If no corresponding {@link UpdateEventListener} is found
     */
    private UpdateEventListener getUpdateListenerForView(final DefaultView view)
    {
        for (UpdateEventListener listener : this.lastPollTimeForView.keySet())
        {
            if (listener.getView() == view)
            {
                return listener;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.rwt.lifecycle.PhaseListener#getPhaseId()
     */
    @Override
    public PhaseId getPhaseId()
    {
        return PhaseId.RENDER;
    }

    /**
     * 
     * Add a {@link DefaultView} as a new Update Listener for the given type
     * 
     * @param type The {@link EntityType} the {@link DefaultView} should be notified for
     * @param view The {@link DefaultView} to be notified
     * @param display The {@link Display} the {@link DefaultView} is initialized with
     */
    public void addListener(EntityType type, DefaultView view, Display display)
    {
        EnumSet<EntityType> registeredTypesForListener = EnumSet.of(type);

        UpdateEventListener listener = getUpdateListenerForView(view);

        if (listener == null)
        {
            listener = new UpdateEventListener(view, display);
        }
        else
        {

            Validate.isTrue(listener.getDisplay() == display, "View already registered with different Display");

            registeredTypesForListener.addAll(this.listeners.get(listener));
        }

        if (LOG.isDebugEnabled())
        {
            LOG.debug("Adding listener [" + listener + "]");
        }

        this.listeners.put(listener, registeredTypesForListener);

        if ( !this.lastPollTimeForView.containsKey(view))
        {
            this.lastPollTimeForView.put(listener, System.nanoTime());
        }
    }

    /**
     * Remove a {@link DefaultView} as an Update Listener for all types
     * 
     * @param view view for which the listener should delete
     */
    public void removeListener(DefaultView view)
    {
        if (LOG.isDebugEnabled())
        {
            LOG.debug("removing listening view [" + view + "]");
        }

        UpdateEventListener listener = getUpdateListenerForView(view);

        if (listener != null)
        {
            this.listeners.remove(listener);
            this.lastPollTimeForView.remove(listener);
        }
    }

    /**
     * 
     * Combines a {@link DefaultView} with it's used {@link Display}
     * 
     * @author Philipp Leusmann
     * @version $Revision$
     * @since 2.0.0a8
     */
    private class UpdateEventListener
    {

        /**
         * The listener instance
         */
        private final DefaultView view;

        /**
         * The {@link Display} the listener was initialized with
         */
        private final Display     display;

        /**
         * Create a {@link DefaultView}, {@link Display} - tuple. Make sure you use the {@link Display} the {@link DefaultView} was initialized with
         * 
         * @param listener
         * @param display
         */
        public UpdateEventListener(DefaultView listener, Display display)
        {
            super();

            Validate.notNull(listener);
            Validate.notNull(display);

            this.view = listener;
            this.display = display;
        }

        /**
         * getView
         * 
         * @return the listener
         */
        public DefaultView getView()
        {
            return view;
        }

        /**
         * getDisplay
         * 
         * @return the display
         */
        public Display getDisplay()
        {
            return display;
        }
    }
}
