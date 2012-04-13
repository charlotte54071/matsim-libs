/* *********************************************************************** *
 * project: org.matsim.*
 * LeaveLinkIdentifierFactory.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2010 by the members listed in the COPYING,        *
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

package org.matsim.withinday.replanning.identifiers;

import java.util.Set;

import org.matsim.withinday.replanning.identifiers.interfaces.DuringLegIdentifier;
import org.matsim.withinday.replanning.identifiers.interfaces.DuringLegIdentifierFactory;
import org.matsim.withinday.replanning.identifiers.tools.LinkReplanningMap;

public class LeaveLinkIdentifierFactory extends DuringLegIdentifierFactory {

	private final LinkReplanningMap linkReplanningMap;
	private final Set<String> transportModes;
	
	public LeaveLinkIdentifierFactory(LinkReplanningMap linkReplanningMap) {
		this.linkReplanningMap = linkReplanningMap;
		this.transportModes = null;
	}

	public LeaveLinkIdentifierFactory(LinkReplanningMap linkReplanningMap, Set<String> transportModes) {
		this.linkReplanningMap = linkReplanningMap;
		this.transportModes = transportModes;
	}
	
	@Override
	public DuringLegIdentifier createIdentifier() {
		DuringLegIdentifier identifier = new LeaveLinkIdentifier(linkReplanningMap, transportModes);
		identifier.setIdentifierFactory(this);
		this.addAgentFiltersToIdentifier(identifier);
		return identifier;
	}

}
