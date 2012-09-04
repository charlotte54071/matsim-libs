/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2012 by the members listed in the COPYING,        *
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

package playground.andreas.P2.stats.abtractPAnalysisModules;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.core.events.PersonEntersVehicleEvent;
import org.matsim.core.events.PersonLeavesVehicleEvent;
import org.matsim.core.events.TransitDriverStartsEvent;
import org.matsim.core.events.VehicleDepartsAtFacilityEvent;
import org.matsim.core.events.handler.PersonEntersVehicleEventHandler;
import org.matsim.core.events.handler.PersonLeavesVehicleEventHandler;
import org.matsim.core.events.handler.TransitDriverStartsEventHandler;
import org.matsim.core.events.handler.VehicleDepartsAtFacilityEventHandler;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.Vehicles;

import playground.andreas.utils.stats.RecursiveStatsContainer;


/**
 * Calculates the average load per departure and ptModes specified.
 * 
 * @author aneumann
 *
 */
public class AverageLoadPerDeparturePerMode extends AbstractPAnalyisModule implements TransitDriverStartsEventHandler, PersonEntersVehicleEventHandler, PersonLeavesVehicleEventHandler, VehicleDepartsAtFacilityEventHandler{
	
	private final static Logger log = Logger.getLogger(AverageLoadPerDeparturePerMode.class);
	
	private HashMap<Id, Double> vehId2VehicleCapacity = new HashMap<Id, Double>();
	
	private HashMap<Id, String> vehId2ptModeMap;
	private HashMap<Id, Integer> vehId2PaxMap = new HashMap<Id, Integer>();
	private HashMap<String, RecursiveStatsContainer> ptMode2Stats = new HashMap<String, RecursiveStatsContainer>();

	
	public AverageLoadPerDeparturePerMode(String ptDriverPrefix){
		super(AverageLoadPerDeparturePerMode.class.getSimpleName(), ptDriverPrefix);
		log.info("enabled");
	}

	@Override
	public String getResult() {
		StringBuffer strB = new StringBuffer();
		for (String ptMode : this.ptModes) {
			strB.append(", " + (this.ptMode2Stats.get(ptMode).getMean()));
		}
		return strB.toString();
	}
	
	@Override
	public void updateVehicles(Vehicles vehicles) {
		this.vehId2VehicleCapacity = new HashMap<Id, Double>();
		for (Vehicle veh : vehicles.getVehicles().values()) {
			this.vehId2VehicleCapacity.put(veh.getId(), new Double(veh.getType().getCapacity().getSeats() + veh.getType().getCapacity().getStandingRoom() - 1.0));
		}
	}
	
	@Override
	public void reset(int iteration) {
		this.vehId2ptModeMap = new HashMap<Id, String>();
		this.vehId2PaxMap = new HashMap<Id, Integer>();
		this.ptMode2Stats = new HashMap<String, RecursiveStatsContainer>();
	}

	@Override
	public void handleEvent(TransitDriverStartsEvent event) {
		String ptMode = this.lineIds2ptModeMap.get(event.getTransitLineId());
		if (ptMode == null) {
			log.warn("Should not happen");
			ptMode = "no valid pt mode found";
		}
		this.vehId2ptModeMap.put(event.getVehicleId(), ptMode);
		
		if (this.vehId2PaxMap.get(event.getVehicleId()) != null) {
			if (this.vehId2PaxMap.get(event.getVehicleId()).intValue() != 0) {
				log.warn(event.getVehicleId() + " has still " + this.vehId2PaxMap.get(event.getVehicleId()) + " passengers onboard. Should be zero by now.");
			}
		}
	}

	@Override
	public void handleEvent(PersonEntersVehicleEvent event) {
		if(!event.getPersonId().toString().startsWith(ptDriverPrefix)){
			if (this.vehId2PaxMap.get(event.getVehicleId()) == null) {
				this.vehId2PaxMap.put(event.getVehicleId(), new Integer(0));
			}
			
			this.vehId2PaxMap.put(event.getVehicleId(), new Integer(this.vehId2PaxMap.get(event.getVehicleId()) + 1));
		}
	}
	
	@Override
	public void handleEvent(PersonLeavesVehicleEvent event) {
		if(!event.getPersonId().toString().startsWith(ptDriverPrefix)){
			this.vehId2PaxMap.put(event.getVehicleId(), new Integer(this.vehId2PaxMap.get(event.getVehicleId()) - 1));
		}
	}

	@Override
	public void handleEvent(VehicleDepartsAtFacilityEvent event) {
		String ptMode = this.vehId2ptModeMap.get(event.getVehicleId());
		if (ptMode == null) {
			ptMode = "nonPtMode";
		}
		if (this.ptMode2Stats.get(ptMode) == null) {
			this.ptMode2Stats.put(ptMode, new RecursiveStatsContainer());
		}
		
		double currentLoad = 0.0;
		if (this.vehId2PaxMap.get(event.getVehicleId()) != null) {
			currentLoad = this.vehId2PaxMap.get(event.getVehicleId()).doubleValue();
		}
		double capacity = this.vehId2VehicleCapacity.get(event.getVehicleId()).doubleValue();
		this.ptMode2Stats.get(ptMode).handleNewEntry(currentLoad / capacity);
	}
}
