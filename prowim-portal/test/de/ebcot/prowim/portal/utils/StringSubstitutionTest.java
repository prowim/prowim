/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 10.05.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 */
package de.ebcot.prowim.portal.utils;

import junit.framework.Assert;
import junit.framework.TestCase;
import de.ebcot.tools.string.EscapeFunctions;


/**
 * Test to set " to \".
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class StringSubstitutionTest extends TestCase
{
    /**
     * Tests the replacement of double quotes.
     */
    public void testSubstitutionOfDoubleQuote()
    {
        String input = "Hallo ich bin \"Heinz Schmitz\"";
        String expected = "Hallo ich bin \\\"Heinz Schmitz\\\"";
        String output = EscapeFunctions.toJSString(input);
        System.out.println("Input: " + input);
        System.out.println("Expected: " + expected);
        System.out.println("Output: " + output);
        Assert.assertEquals(expected, output);
    }

    /**
     * Tests the replacement of backslashes.
     */
    public void testSubstitutionOfBackslash()
    {
        // one linefeed
        String input = "Hallo \\Du\\ich\\bin\\erlaubt.";
        String expected = "Hallo \\\\Du\\\\ich\\\\bin\\\\erlaubt.";
        // String output = EscapeFunctions.replaceBackslash(input);
        String output = replaceApostrophe(input);
        System.out.println("Input: " + input);
        System.out.println("Expected: " + expected);
        System.out.println("Output: " + output);
        Assert.assertEquals(expected, output);
    }

    /**
     * Tests the replacement line feeds.
     */
    public void testSubstitutionOfLineFeed()
    {
        /**
         * // one linefeed String input = "Hallo \"Umbruch\" \nHier"; String expected = "Hallo \\\"Umbruch\\\" \\nHier"; String output = GlobalFunctions.toJSString(input); System.out.println("Input: " + input); System.out.println("Expected: " + expected); System.out.println("Output: " + output); Assert.assertEquals(expected, output);
         * 
         * // three linefeeds input = "Hallo \"Umbruch\n\" \n\nHier"; expected = "Hallo \\\"Umbruch\\\\n\\\" \\\\n\\\\nHier"; output = GlobalFunctions.replaceApostrophe(input); Assert.assertEquals(expected, output);
         */
        // 4427: Die Name der Prozesskategorie wird Fehlerhaft gespeichert '"öäü
        String input = "'\"öäü http:\\www.ebcot.de\\test.html \n hhh";
        String expected = "'\\\"öäü http:\\\\www.ebcot.de\\\\test.html \n hhh";
        // String output = GlobalFunctions.replaceApostrophe(input);
        String output = replaceBackslash(input);
        System.out.println("Input: " + input);
        System.out.println("Expected: " + expected);
        System.out.println("Output: " + output);

        Assert.assertEquals(expected, output);
    }

    // public void testSubstitutionOfBackslashAndApostorphe()
    // {
    //
    // }

    private static String replaceApostrophe(String string)
    {
        if (string != null && !string.equals(""))
        {
            String regEx = "\"";
            String replacement = "\\\\\\\"";
            return string.replaceAll(regEx, replacement);
        }
        else
            return "";
    }

    private static String replaceBackslash(String string)
    {
        if (string != null && !string.equals(""))
        {
            String regEx = "\\\\";
            String replacement = "\\\\\\\\";
            return string.replaceAll(regEx, replacement);
        }
        else
            return "";
    }

}
