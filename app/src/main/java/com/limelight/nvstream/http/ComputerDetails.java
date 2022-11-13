package com.limelight.nvstream.http;

import com.limelight.LimeLog;

import java.security.cert.X509Certificate;


public class ComputerDetails {
    public enum State {
        ONLINE,
        OFFLINE,
        UNKNOWN
    }

    // Persistent attributes
    public String uuid;
    public String name;
    public String localAddress;
    public String remoteAddress;
    public String manualAddress;
    public String ipv6Address;
    public String macAddress;
    public X509Certificate serverCert;

    // Transient attributes
    public State state;
    public String activeAddress;
    public PairingManager.PairState pairState;
    public int runningGameId;
    public String rawAppList;

    public ComputerDetails() {
        // Use defaults
        state = State.UNKNOWN;
    }

    public ComputerDetails(String uuid, String name, String localAddress, String remoteAddress, String manualAddress, String macAddress, X509Certificate serverCert, String activeAddress, int runningGameId) {
        this.uuid = uuid;
        this.name = name;
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
        this.manualAddress = manualAddress;
        this.macAddress = macAddress;
        this.serverCert = serverCert;
        this.activeAddress = activeAddress;
        this.runningGameId = runningGameId;
        this.pairState = PairingManager.PairState.PAIRED;
        this.state = State.ONLINE;
    }

    public ComputerDetails(ComputerDetails details) {
        // Copy details from the other computer
        update(details);
    }

    public void update(ComputerDetails details) {
        this.state = details.state;
        this.name = details.name;
        this.uuid = details.uuid;
        if (details.activeAddress != null) {
            this.activeAddress = details.activeAddress;
        }
        // We can get IPv4 loopback addresses with GS IPv6 Forwarder
        if (details.localAddress != null && !details.localAddress.startsWith("127.")) {
            this.localAddress = details.localAddress;
        }
        if (details.remoteAddress != null) {
            this.remoteAddress = details.remoteAddress;
        }
        if (details.manualAddress != null) {
            this.manualAddress = details.manualAddress;
        }
        if (details.ipv6Address != null) {
            this.ipv6Address = details.ipv6Address;
        }
        if (details.macAddress != null && !details.macAddress.equals("00:00:00:00:00:00")) {
            this.macAddress = details.macAddress;
        }
        if (details.serverCert != null) {
            this.serverCert = details.serverCert;
        }
        this.pairState = details.pairState;
        LimeLog.info("polled pair state:" + this.pairState);
        this.runningGameId = details.runningGameId;
        this.rawAppList = details.rawAppList;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Name: ").append(name).append("\n");
        str.append("State: ").append(state).append("\n");
        str.append("Active Address: ").append(activeAddress).append("\n");
        str.append("UUID: ").append(uuid).append("\n");
        str.append("Local Address: ").append(localAddress).append("\n");
        str.append("Remote Address: ").append(remoteAddress).append("\n");
        str.append("IPv6 Address: ").append(ipv6Address).append("\n");
        str.append("Manual Address: ").append(manualAddress).append("\n");
        str.append("MAC Address: ").append(macAddress).append("\n");
        str.append("Pair State: ").append(pairState).append("\n");
        str.append("Running Game ID: ").append(runningGameId).append("\n");
        return str.toString();
    }
}
