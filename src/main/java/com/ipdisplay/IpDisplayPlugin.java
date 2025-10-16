package com.ipdisplay;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
    name = "IP Display",
    description = "Displays your current external IP address",
    tags = {"ip", "proxy", "network"}
)
public class IpDisplayPlugin extends Plugin
{
    private static final Logger log = LoggerFactory.getLogger(IpDisplayPlugin.class);
    @Inject
    private Client client;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ScheduledExecutorService executorService;

    private IpDisplayPanel panel;
    private NavigationButton navButton;

    @Override
    protected void startUp() throws Exception
    {
        panel = new IpDisplayPanel();
        panel.setOnRefresh(() -> executorService.submit(this::updateIpAddress));

        // Create a visible icon for the sidebar
        final BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = icon.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0, 200, 0)); // Green color
        g.fillRoundRect(0, 0, 16, 16, 4, 4);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("IP", 2, 12);
        g.dispose();

        navButton = NavigationButton.builder()
            .tooltip("IP Display")
            .icon(icon)
            .priority(100)
            .panel(panel)
            .build();

        clientToolbar.addNavigation(navButton);

        // Start IP check immediately
        executorService.submit(this::updateIpAddress);

        // Schedule periodic updates every 60 seconds
        executorService.scheduleAtFixedRate(this::updateIpAddress, 60, 60, TimeUnit.SECONDS);
    }

    @Override
    protected void shutDown() throws Exception
    {
        clientToolbar.removeNavigation(navButton);
    }

    private void updateIpAddress()
    {
        try
        {
            String ip = fetchExternalIp();
            panel.updateIp(ip);
            log.info("Current IP: " + ip);
        }
        catch (Exception e)
        {
            panel.updateIp("Error: " + e.getMessage());
            log.error("Failed to fetch IP", e);
        }
    }

    private String fetchExternalIp() throws Exception
    {
        // Try multiple services for reliability
        String[] services = {
            "https://api.ipify.org",
            "https://icanhazip.com",
            "https://ifconfig.me/ip"
        };

        Exception lastException = null;

        for (String serviceUrl : services)
        {
            try
            {
                URL url = new URL(serviceUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("User-Agent", "curl/7.68.0");
                conn.setRequestProperty("Accept", "text/plain");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200)
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response = in.readLine();
                    in.close();

                    // Validate it's actually an IP address (not HTML)
                    if (response != null && response.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$"))
                    {
                        return response.trim();
                    }
                    else if (response != null && response.contains("<"))
                    {
                        // HTML response detected, try next service
                        log.warn("Service {} returned HTML instead of IP", serviceUrl);
                        continue;
                    }
                }
            }
            catch (Exception e)
            {
                lastException = e;
                log.warn("Failed to fetch IP from {}: {}", serviceUrl, e.getMessage());
            }
        }

        // All services failed
        if (lastException != null)
        {
            throw lastException;
        }
        else
        {
            throw new Exception("All IP services returned invalid responses");
        }
    }

    @Provides
    IpDisplayConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(IpDisplayConfig.class);
    }
}
