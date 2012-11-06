/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
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
 *
 */

package org.prowim.datamodel.factory.impl;

import javax.xml.bind.annotation.XmlRegistry;

import org.prowim.datamodel.algernon.Field;
import org.prowim.datamodel.algernon.Record;
import org.prowim.datamodel.algernon.RecordKV;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.algernon.Result;



/**
 * This object contains factory methods for each Java content interface and Java element interface Factory methods for each of these are provided in this class.
 * 
 * @author wardi
 * 
 */
@XmlRegistry
public class AlgernonDataObjectFactory implements org.prowim.datamodel.factory.AlgernonDataObjectFactory
{

    /**
     * Create a new AlgernonDataObjectFactory that can be used to create new instances of schema derived classes for package: de.ebcot.prowim.services.algernon
     * 
     */
    public AlgernonDataObjectFactory()
    {
    }

    /**
     * Create an id of {@link RecordsetKV }.
     * 
     * @return {@link RecordsetKV}
     */
    public RecordsetKV createAlgernonRecordsetKV()
    {
        return new RecordsetKV();
    }

    /**
     * Create an id of {@link RecordKV }.
     * 
     * @return {@link RecordKV}
     */
    public RecordKV createAlgernonRecordKV()
    {
        return new RecordKV();
    }

    /**
     * Create an id of {@link Result }
     * 
     * @return {@link Result}
     */
    public Result createAlgernonResult()
    {
        return new Result();
    }

    /**
     * Create an id of {@link Record }
     * 
     * @return {@link Record}
     */
    public Record createAlgernonRecord()
    {
        return new Record();
    }

    /**
     * Create an id of {@link Field }
     * 
     * @return {@link Field}
     */
    public Field createAlgernonField()
    {
        return new Field();
    }

}
