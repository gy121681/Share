package com.shareshenghuo.app.shop.network.request;

public class HandPayRequse extends BaseRequest{
	
	public String TERMTYPE;
	public String ACCOUNT;
	public String CTXNAT;
	public String POINTSID;
	public String CUSTERMTEL;
	public String TERMINALNUMBER;
	public String CRDNO;
	public String ENCTRACKS;
	public String TRACK2;
	public String TRACK3;
	public String TPINBLK;
	public String TEXPDAT;
	public String INMOD;
	public String CRDSQN;
	public String ICDAT;
	public String RANDOMNUMBER;
	@Override
	public String toString() {
		return "HandPayRequse [TERMTYPE=" + TERMTYPE + ", ACCOUNT=" + ACCOUNT
				+ ", CTXNAT=" + CTXNAT + ", POINTSID=" + POINTSID
				+ ", CUSTERMTEL=" + CUSTERMTEL + ", TERMINALNUMBER="
				+ TERMINALNUMBER + ", CRDNO=" + CRDNO + ", ENCTRACKS="
				+ ENCTRACKS + ", TRACK2=" + TRACK2 + ", TRACK3=" + TRACK3
				+ ", TPINBLK=" + TPINBLK + ", TEXPDAT=" + TEXPDAT + ", INMOD="
				+ INMOD + ", CRDSQN=" + CRDSQN + ", ICDAT=" + ICDAT
				+ ", RANDOMNUMBER=" + RANDOMNUMBER + "]";
	}
}
