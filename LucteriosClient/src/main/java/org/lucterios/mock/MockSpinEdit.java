package org.lucterios.mock;

import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUISpinEdit;

public class MockSpinEdit extends MockComponent implements GUISpinEdit {

	private long number;

	private long upperLimit;

	private long bottomLimit;

	private boolean reverse;

	public MockSpinEdit(GUIComponent aOwner){
    	super(aOwner);
		init(0, 0, Integer.MAX_VALUE);
	}

	public void init(long num, long bottomL, long upperL) {
		upperLimit = upperL;
		bottomLimit = bottomL;
		if (num <= upperLimit && num >= bottomLimit)
			number = num;
		else
			number = bottomLimit;
		reverse = false;
	}

	public void setNumber(long num) {
		if (number <= upperLimit && number >= bottomLimit)
			number = num;
	}

	public long getNumber() {
		return number;
	}

	public void setReverse(boolean r) {
		reverse = r;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setUpperLimit(int limit) {
		upperLimit = limit;
		if (number > upperLimit)
			setNumber(upperLimit);
	}

	public long getUpperLimit() {
		return upperLimit;
	}

	public void setBottomLimit(long limit) {
		bottomLimit = limit;
		if (number < bottomLimit)
			setNumber(bottomLimit);
	}

	public long getBottomLimit() {
		return bottomLimit;
	}
	
}
