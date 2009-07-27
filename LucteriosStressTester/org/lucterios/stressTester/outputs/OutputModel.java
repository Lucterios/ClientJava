package org.lucterios.stressTester.outputs;

import org.lucterios.utils.LucteriosException;

public interface OutputModel {

	public void addResult(int aId,String aActionName,int aTime,String aObservation) throws LucteriosException;

}
