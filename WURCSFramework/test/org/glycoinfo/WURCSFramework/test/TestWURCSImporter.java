package org.glycoinfo.WURCSFramework.test;

import static org.junit.Assert.*;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.junit.Test;

public class TestWURCSImporter {

	@Test(expected=WURCSFormatException.class)
	public void testException() throws WURCSFormatException {
		WURCSImporter t_oImporter = new WURCSImporter();
		System.out.println( System.getProperty("user.dir") );
		// WURCS format test
		// Regular expression: "WURCS=(.+)/(\d+),(\d+),(\d+)(\+)?/\[(.+)\]/([\d\-<>]+)/(.*)"
		String t_strWURCS = "WURCS=2.0/1,1,0/[u2122h]/1"; // no slash at last
		t_oImporter.extractWURCSArray(t_strWURCS, true);
	}

	@Test
	public void testExceptionMessage() {
		WURCSImporter t_oImporter = new WURCSImporter();
		// WURCS format test
		// Regular expression: "WURCS=(.+)/(\d+),(\d+),(\d+)(\+)?/\[(.+)\]/([\d\-<>]+)/(.*)"
		String t_strWURCS = "WURCS=2.0/1,1,0/[u2122h]/1"; // no slash at last
		try {
			t_oImporter.extractWURCSArray(t_strWURCS);
		} catch (WURCSFormatException e) {
			assertEquals( "Not match as WURCS.", e.getErrorMessage() );
			assertEquals( t_strWURCS, e.getInputString() );
		}
	}


}
