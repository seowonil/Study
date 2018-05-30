package com.swi.study.backend.pcapmonitor.common.service;

import java.util.List;

import com.swi.study.backend.pcapmonitor.common.model.NetInterface;

public interface JNetPcapService {
	public List<NetInterface> getNetInterfaceList() throws Exception;
}
