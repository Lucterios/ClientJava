package org.lucterios.gui;

public class GUIParam {
	public enum FillMode {FM_NONE,FM_BOTH,FM_HORIZONTAL,FM_VERTICAL}
	public enum ReSizeMode {RSM_NONE,RSM_BOTH,RSM_HORIZONTAL,RSM_VERTICAL}
	
	private int x;
	private int y;
	private int w;
	private int h;
	private ReSizeMode reSize;
	private FillMode fill;
	private int pad;

	private int prefSizeX=-1;
	private int prefSizeY=-1;
	
	public GUIParam(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill,int prefSizeX,int prefSizeY){
		this.setX(x);
		this.setY(y);
		this.setW(w);
		this.setH(h);
		this.setReSize(reSize);
		this.setFill(fill);
		this.prefSizeX=prefSizeX;
		this.prefSizeY=prefSizeY;
		this.setPad(1);
	}

	public GUIParam(int x, int y, int w, int h, ReSizeMode reSize,FillMode fill){
		this(x, y, w, h, reSize, fill, -1, -1);
	}

	public GUIParam(int x, int y, int w, int h){
		this(x, y, w, h, ReSizeMode.RSM_BOTH,FillMode.FM_BOTH);
	}

	public GUIParam(int x, int y){
		this(x, y, 1, 1);
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getW() {
		return w;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getH() {
		return h;
	}

	public void setReSize(ReSizeMode reSize) {
		this.reSize = reSize;
	}

	public ReSizeMode getReSize() {
		return reSize;
	}

	public void setFill(FillMode fill) {
		this.fill = fill;
	}

	public FillMode getFill() {
		return fill;
	}

	public void setPrefSizeX(int prefSizeX) {
		this.prefSizeX = prefSizeX;
	}

	public int getPrefSizeX() {
		return prefSizeX;
	}

	public void setPrefSizeY(int prefSizeY) {
		this.prefSizeY = prefSizeY;
	}

	public int getPrefSizeY() {
		return prefSizeY;
	}

	public void setPad(int pad) {
		this.pad = pad;
	}

	public int getPad() {
		return pad;
	}

	public void setWeight(double weightx, double weighty) {
		this.reSize = ReSizeMode.RSM_NONE;
		if ((weightx<0.5) && (weighty>=0.5))
			this.reSize = ReSizeMode.RSM_VERTICAL;
		else if ((weightx>=0.5) && (weighty<0.5))
			this.reSize = ReSizeMode.RSM_HORIZONTAL;
		else if ((weightx>=0.5) && (weighty>=0.5))
			this.reSize = ReSizeMode.RSM_BOTH;
	}
}
