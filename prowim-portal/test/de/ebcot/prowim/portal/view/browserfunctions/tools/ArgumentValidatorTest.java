/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 19.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 */
package de.ebcot.prowim.portal.view.browserfunctions.tools;

import java.util.List;

import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;

import junit.framework.TestCase;


/**
 * Tests the {@link ArgumentValidator}.
 * 
 * @author Oliver Specht
 * @version $Revision$
 * @since 2.0.alpha9
 */
public class ArgumentValidatorTest extends TestCase
{
    /**
     * Tests the convert method which converts an array of objects to a string list.
     */
    public void testConvert()
    {
        String obj1 = "Objekt 1";
        String obj2 = "Objekt 2";
        String obj3 = "Objekt 3";
        Object[] testObjectArray = new Object[] { obj1, obj2, obj3 };

        // case 1: simply convert objects to strings
        List<String> resultList = ArgumentValidator.convert(testObjectArray);
        assertNotNull(resultList);
        assertTrue(resultList.size() == testObjectArray.length);
        assertEquals(testObjectArray[0], resultList.get(0));
        assertEquals(testObjectArray[1], resultList.get(1));
        assertEquals(testObjectArray[2], resultList.get(2));

        // case 2: passing null
        try
        {
            ArgumentValidator.convert(null);
            fail("Null should not be allowed.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("");
        }

        // case 3: passing empty list
        try
        {
            ArgumentValidator.convert(new Object[] {});
            fail("Empty array should not be allowed.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("");
        }

        // case 4: passing empty string
        try
        {
            ArgumentValidator.convert(new Object[] { "" });
            fail("Empty string should not be allowed.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("");
        }

    }
}
