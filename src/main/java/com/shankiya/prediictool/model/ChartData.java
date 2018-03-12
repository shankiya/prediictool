package com.shankiya.prediictool.model;

import java.math.BigDecimal;

public class ChartData {
	
	private String xaxis;
	private String yaxis;
	private BigDecimal value;
	
	public String getXaxis() {
		return xaxis;
	}
	public void setXaxis(String xaxis) {
		this.xaxis = xaxis;
	}
	public String getYaxis() {
		return yaxis;
	}
	public void setYaxis(String yaxis) {
		this.yaxis = yaxis;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	

}
