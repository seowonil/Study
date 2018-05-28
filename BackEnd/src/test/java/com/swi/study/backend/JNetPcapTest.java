package com.swi.study.backend;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JRegistry;
import org.jnetpcap.packet.Payload;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.application.Html;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.junit.Test;

public class JNetPcapTest {

	// 정적으로 jnetpcap.dll 파일 로드
	static {
		try {
			// native Library Load
			System.load(new File("./lib/jnetpcap.dll").getAbsolutePath());
			System.out.println(new File("./lib/jnetpcap.dll").getAbsolutePath());
		} catch (UnsatisfiedLinkError e) {
			System.out.println("Native code library failed to load.\n" + e);
			System.exit(1);
		}
	}

	@Test
	public void netpcap2() {
		List<PcapIf> alldevs = new ArrayList<PcapIf>();
		StringBuilder errbuf = new StringBuilder();
		int r = Pcap.findAllDevs(alldevs, errbuf);
		if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
			return;
		}
		System.out.println("Network devices found:");
		int i = 0;
		for (PcapIf device : alldevs) {
			String description = (device.getDescription() != null) ? device.getDescription()
					: "No description available";
			System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
		}

		PcapIf device = alldevs.get(0);
		System.out.printf("\nChoosing '%s' on your behalf:\n",
				(device.getDescription() != null) ? device.getDescription() : device.getName());

		int snaplen = 64 * 1024;
		int flags = Pcap.MODE_PROMISCUOUS;
		int timeout = 10 * 1000;
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
		if (pcap == null) {
			System.err.printf("Error while opening device for capture: " + errbuf.toString());
			return;
		}

		Ethernet eth = new Ethernet();
		Ip4 ip = new Ip4();
		Tcp tcp = new Tcp();
		Udp udp = new Udp();
		Payload payload = new Payload();

		PcapHeader header = new PcapHeader(JMemory.POINTER);
		JBuffer buf = new JBuffer(JMemory.POINTER);

		int id = JRegistry.mapDLTToId(pcap.datalink());
		for (;;) {

			if (pcap.nextEx(header, buf) == Pcap.NEXT_EX_OK) {
			PcapPacket packet = new PcapPacket(header, buf);
				packet.scan(id);
				System.out.println(packet.getFrameNumber());
				System.out.println("###Packet###");
				if (packet.hasHeader(eth)) {
					System.out.println("출발지 MAC주소 : " + FormatUtils.mac(eth.source()));
					System.out.println("도착지 MAC주소 : " + FormatUtils.mac(eth.destination()));
				}

				if (packet.hasHeader(ip)) {
					System.out.println("출발지 IP주소 : " + FormatUtils.ip(ip.source()));
					System.out.println("도착지 IP주소 : " + FormatUtils.ip(ip.destination()));
				}

				if (packet.hasHeader(tcp)) {
					System.out.println("출발지 TCP주소 : " + tcp.source());
					System.out.println("도착지 TCP주소 : " + tcp.destination());
				}

				if (packet.hasHeader(udp)) {
					System.out.println("출발지 UDP주소 : " + udp.source());
					System.out.println("도착지 UDP주소 : " + udp.destination());
				}

				if (packet.hasHeader(payload)) {
					System.out.println("페이로드의 길이 : " + payload.getLength());
					// System.out.println("와이어샤크 : " + payload.toHexdump());
				}
			}
		}
	}
}
