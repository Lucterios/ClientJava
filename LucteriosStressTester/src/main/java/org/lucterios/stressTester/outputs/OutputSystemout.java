package org.lucterios.stressTester.outputs;

import org.lucterios.utils.LucteriosException;

public class OutputSystemout implements OutputModel {

	public void addResult(int aId,String aActionName, int aTime, String aObservation, boolean success) throws LucteriosException {
		System.out.println(String.format("%03d[%s] %8d - %s [%s]", aId,aActionName, aTime, aObservation,success?"Success":"Failed"));
	}

}
