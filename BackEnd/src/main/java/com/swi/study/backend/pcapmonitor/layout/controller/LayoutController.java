package com.swi.study.backend.pcapmonitor.layout.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.swi.study.backend.pcapmonitor.common.model.NetInterface;
import com.swi.study.backend.pcapmonitor.common.service.JNetPcapService;

@RestController
@RequestMapping(value = "/api/layout")
public class LayoutController {
	@Autowired
	private JNetPcapService jNetPcapService;

	@RequestMapping(value = "/net/interface", method = RequestMethod.GET)
	protected List<NetInterface> getNetInterface() throws Exception {
		return jNetPcapService.getNetInterfaceList();
	}
}
