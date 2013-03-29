package org.hbird.business.systemmonitoring.bean;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;

import org.hbird.exchange.util.LocalHostNameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostInfo {

    private static final Logger LOG = LoggerFactory.getLogger(HostInfo.class);

    private static final String FALLBACK_HOST_IDENTIFIER = UUID.randomUUID().toString();

    private static String hostName;

    private static String hostInfo;

    public static String getHostName() {
        if (hostName == null) {
            hostName = LocalHostNameResolver.getLocalHostName();
            if (LocalHostNameResolver.DEFAULT_LOCAL_HOST_NAME.equals(hostName)) {
                LOG.warn("Host name not available from InetAddress; falling back to native \"hostname\" command");
                try {
                    hostName = getHostNameFromSystem();
                }
                catch (IOException ioe) {
                    LOG.error("Failed to execute native \"hostname\" command; falling back to random UUID: {}",
                            FALLBACK_HOST_IDENTIFIER);
                    hostName = FALLBACK_HOST_IDENTIFIER;
                }
            }
        }
        return hostName;
    }

    static String getHostNameFromInetAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    static String getHostNameFromSystem() throws IOException {
        String result;
        InputStream is = null;
        try {
            Process p = Runtime.getRuntime().exec("hostname");
            is = p.getInputStream();
            Scanner scanner = new Scanner(is);
            result = scanner.nextLine();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException ignore) {
                }
            }
        }
        return result;
    }

    public static String getHostInfo() {
        if (hostInfo == null) {
            hostInfo = getHostInfoFromProperties();
        }
        return hostInfo;
    }

    static String getHostInfoFromProperties() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS: ")
                .append(System.getProperty("os.name"))
                .append(" ")
                .append(System.getProperty("os.version"))
                .append(" ")
                .append(System.getProperty("os.arch"))
                .append("; user: ")
                .append(System.getProperty("user.name"));
        return sb.toString();
    }
}
