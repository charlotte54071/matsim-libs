
/* *********************************************************************** *
 * project: org.matsim.*
 * ShapeFileReaderTest.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2019 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

 package org.matsim.utils.gis;

import java.io.IOException;

import org.geotools.data.FeatureSource;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.testcases.MatsimTestUtils;

/**
 * @author mrieser / senozon
 */
public class ShapeFileReaderTest {

	@Rule public MatsimTestUtils utils = new MatsimTestUtils(); 
	
	/**
	 * Based on message on users-mailing list from 20Dec2012)
	 * @throws IOException
	 */
	@Test
	public void testPlusInFilename() throws IOException {
		String filename = "src/test/resources/" + utils.getInputDirectory() + "test+test.shp";
		FeatureSource fs = ShapeFileReader.readDataFile(filename);
		Assert.assertEquals(3, fs.getFeatures().size());
	}
}
