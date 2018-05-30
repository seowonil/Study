package com.swi.study.backend.pcapmonitor.common.serviceimpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.springframework.stereotype.Component;

import com.swi.study.backend.pcapmonitor.common.model.NetInterface;
import com.swi.study.backend.pcapmonitor.common.service.JNetPcapService;

@Component
public class JNetPcapServiceImpl implements JNetPcapService {
	static {
		try {
			System.load(new File("./lib/jnetpcap.dll").getAbsolutePath());
		} catch (UnsatisfiedLinkError e) {
			System.exit(1);
		}
	}

	@SuppressWarnings("deprecation")
	public List<NetInterface> getNetInterfaceList() throws Exception {
		List<NetInterface> returnVal = new ArrayList<NetInterface>();
		try {
			List<PcapIf> alldevs = new ArrayList<PcapIf>();
			StringBuilder errbuf = new StringBuilder();
			if (Pcap.findAllDevs(alldevs, errbuf) == Pcap.NOT_OK || alldevs.isEmpty()) {
				return null;
			}
			for (PcapIf device : alldevs) {
				returnVal.add(new NetInterface(device.getFlags(), device.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}
}
