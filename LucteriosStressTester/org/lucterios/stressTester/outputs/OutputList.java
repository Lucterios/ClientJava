package org.lucterios.stressTester.outputs;

import java.util.ArrayList;

import org.lucterios.utils.LucteriosException;

public class OutputList extends ArrayList<OutputModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void addResult(int aId,String aActionName, int aTime, String aObservation) throws LucteriosException {
		for(OutputModel item:this)
			item.addResult(aId,aActionName, aTime, aObservation);		
	}

}
