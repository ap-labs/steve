package de.rwth.idsg.steve;

import de.rwth.idsg.steve.web.dto.EndpointInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static de.rwth.idsg.steve.SteveConfiguration.CONFIG;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 12.12.2014
 */
@Slf4j
public class JettyServer {

    private Server server;

    private static final int MIN_THREADS = 4;
    private static final int MAX_THREADS = 50;

    private static final long STOP_TIMEOUT = TimeUnit.SECONDS.toMillis(5);
    private static final long IDLE_TIMEOUT = TimeUnit.MINUTES.toMillis(1);

    /**
     * A fully configured Jetty Server instance
     */
    private void prepare() {

        // === jetty.xml ===
        // Setup Threadpool
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(MIN_THREADS);
        threadPool.setMaxThreads(MAX_THREADS);

        // Server
        server = new Server(threadPool);

        // Scheduler
        server.addBean(new ScheduledExecutorScheduler());

        // HTTP Configuration
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSecureScheme(HttpScheme.HTTPS.asString());
        httpConfig.setSecurePort(CONFIG.getJetty().getHttpsPort());
        httpConfig.setOutputBufferSize(32768);
        httpConfig.setRequestHeaderSize(8192);
        httpConfig.setResponseHeaderSize(8192);
        httpConfig.setSendServerVersion(false);
        httpConfig.setSendDateHeader(false);
        httpConfig.setSendXPoweredBy(false);

        // Extra options
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);
        server.setStopAtShutdown(true);
        server.setStopTimeout(STOP_TIMEOUT);

        if (CONFIG.getJetty().isHttpEnabled()) {
            server.addConnector(httpConnector(httpConfig));
        }

        if (CONFIG.getJetty().isHttpsEnabled()) {
            server.addConnector(httpsConnector(httpConfig));
        }

        SteveAppContext steveAppContext = new SteveAppContext();
        server.setHandler(steveAppContext.getHandlers());
    }

    private ServerConnector httpConnector(HttpConfiguration httpConfig) {
        // === jetty-http.xml ===
        ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        http.setHost(CONFIG.getJetty().getServerHost());
        http.setPort(CONFIG.getJetty().getHttpPort());
        http.setIdleTimeout(IDLE_TIMEOUT);
        return http;
    }

    private ServerConnector httpsConnector(HttpConfiguration httpConfig) {
        // === jetty-https.xml ===
        // SSL Context Factory
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(CONFIG.getJetty().getKeyStorePath());
        sslContextFactory.setKeyStorePassword(CONFIG.getJetty().getKeyStorePassword());
        sslContextFactory.setKeyManagerPassword(CONFIG.getJetty().getKeyStorePassword());
        sslContextFactory.setExcludeCipherSuites(
                "SSL_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_RSA_WITH_DES_CBC_SHA",
                "SSL_DHE_DSS_WITH_DES_CBC_SHA",
                "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
                "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
                "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA");

        // SSL HTTP Configuration
        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        // SSL Connector
        ServerConnector https = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(httpsConfig));
        https.setHost(CONFIG.getJetty().getServerHost());
        https.setPort(CONFIG.getJetty().getHttpsPort());
        https.setIdleTimeout(IDLE_TIMEOUT);
        return https;
    }

    /**
     * Starts the Jetty Server instance
     */
    public void start() throws Exception {
        prepare();

        if (server != null) {
            server.start();
            populateEndpointInfo();
        }
    }

    /**
     * Join the server thread with the current thread
     */
    public void join() throws Exception {
        if (server != null) {
            server.join();
        }
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    public boolean isStarted() {
        return server != null && server.isStarted();
    }

    public void populateEndpointInfo() {
        List<String> list = getConnectorPathList();

        EndpointInfo info = EndpointInfo.INSTANCE;

        info.getWebInterface().setData(buildList(list, false));
        info.getOcppSoap().setData(buildList(list, false));
        info.getOcppWebSocket().setData(buildList(list, true));
    }

    private List<String> getConnectorPathList() {
        if (server == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(server.getConnectors())
                     .map(JettyServer::getConnectorPath)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }

    private static List<String> getConnectorPath(Connector c) {
        ServerConnector sc = (ServerConnector) c;

        final String prefix;
        if (sc.getDefaultConnectionFactory() instanceof SslConnectionFactory) {
            prefix = "https";
        } else {
            prefix = "http";
        }

        Set<String> ips = new HashSet<>();
        String host = sc.getHost();
        if (host == null || host.equals("0.0.0.0")) {
            ips.addAll(getPossibleIpAddresses());
        } else {
            ips.add(host);
        }

        String layout = "%s://%s:%d" + CONFIG.getContextPath();

        return ips.stream()
                  .map(k -> String.format(layout, prefix, k, sc.getPort()))
                  .collect(Collectors.toList());
    }

    private List<String> buildList(List<String> list, boolean replaceHttp) {
        return list.stream()
                   .map(s -> getElementPrefix(s, replaceHttp))
                   .collect(Collectors.toList());
    }

    private String getElementPrefix(String str, boolean replaceHttp) {
        if (replaceHttp) {
            return str.replaceFirst("http", "ws");
        } else {
            return str;
        }
    }

    /**
     * Uses different APIs to find out the IP of this machine.
     */
    private static List<String> getPossibleIpAddresses() {
        final String host = "treibhaus.informatik.rwth-aachen.de";
        final List<String> ips = new ArrayList<>();

        try {
            ips.add(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            // fail silently
        }

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, 80));
            ips.add(socket.getLocalAddress().getHostAddress());
        } catch (Exception e) {
            // fail silently
        }

        // https://stackoverflow.com/a/38342964
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ips.add(socket.getLocalAddress().getHostAddress());
        } catch (Exception e) {
            // fail silently
        }

        ips.removeIf("0.0.0.0"::equals);

        if (ips.isEmpty()) {
            // Well, we failed to read from system, fall back to main.properties.
            // Better than nothing
            ips.add(CONFIG.getJetty().getServerHost());
        }

        return ips;
    }
}
