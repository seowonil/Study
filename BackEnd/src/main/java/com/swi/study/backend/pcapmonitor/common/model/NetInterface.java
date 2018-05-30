package com.swi.study.backend.pcapmonitor.common.model;

public class NetInterface {
	private Integer number;
	private String name;

	public NetInterface(Integer number, String name) {
		this.number = number;
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
