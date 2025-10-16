package com.ipdisplay;

import net.runelite.client.ui.PluginPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class IpDisplayPanel extends PluginPanel
{
    private final JLabel ipLabel;
    private final JLabel statusLabel;
    private final JButton refreshButton;
    private Runnable onRefresh;

    public IpDisplayPanel()
    {
        super(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("Current IP Address");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // IP Display
        ipLabel = new JLabel("Checking...");
        ipLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ipLabel.setForeground(new Color(0, 200, 0));
        contentPanel.add(ipLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Status
        statusLabel = new JLabel("Loading...");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Refresh Button
        refreshButton = new JButton("Refresh IP");
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.addActionListener(e -> {
            ipLabel.setText("Checking...");
            statusLabel.setText("Refreshing...");
            if (onRefresh != null)
            {
                onRefresh.run();
            }
        });
        contentPanel.add(refreshButton);

        add(contentPanel, BorderLayout.NORTH);
    }

    public void setOnRefresh(Runnable callback)
    {
        this.onRefresh = callback;
    }

    public void updateIp(String ip)
    {
        SwingUtilities.invokeLater(() -> {
            ipLabel.setText(ip);
            if (ip.startsWith("Error"))
            {
                ipLabel.setForeground(Color.RED);
                statusLabel.setText("Failed to fetch IP");
            }
            else
            {
                ipLabel.setForeground(new Color(0, 200, 0));
                statusLabel.setText("Last updated: " + new java.util.Date().toString());
            }
        });
    }
}
