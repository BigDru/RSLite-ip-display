# IP Display Plugin for RuneLite

A RuneLite plugin that displays your current external IP address, perfect for verifying proxy connections and monitoring your network identity while playing Old School RuneScape.

## Features

- **Real-time IP Display**: Shows your current external IP address in a convenient sidebar panel
- **Multiple IP Services**: Automatically tries multiple IP detection services (api.ipify.org, icanhazip.com, ifconfig.me) for reliability
- **Auto-refresh**: Automatically updates your IP every 60 seconds
- **Manual Refresh**: Click the "Refresh IP" button to check your IP on demand
- **Visual Feedback**: Color-coded display (green for success, red for errors)
- **Proxy Verification**: Ideal for users running RuneLite through SOCKS5 proxies or VPNs

## Why Use This Plugin?

If you're using a proxy or VPN with RuneLite, this plugin provides instant verification that your connection is properly routed through the expected IP address. No need to tab out to check your IP in a browser!

## Installation

### Method 1: Download Pre-built JAR (Recommended)

1. Download the latest `ip-display-1.0.0.jar` from the [Releases](../../releases) page
2. Place the JAR file in your RuneLite sideloaded plugins directory:
   - **Linux/Mac**: `~/.runelite/sideloaded-plugins/`
   - **Windows**: `%USERPROFILE%\.runelite\sideloaded-plugins\`
3. Launch RuneLite in developer mode:
   ```bash
   java -ea -jar RuneLite.jar --developer-mode
   ```
4. Enable the "IP Display" plugin in the RuneLite plugin panel

### Method 2: Build from Source

#### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Gradle (or use the Gradle wrapper included with the project)

#### Build Steps

1. Clone this repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/runelite-ip-display.git
   cd runelite-ip-display
   ```

2. Build the plugin:
   ```bash
   gradle clean build
   ```

3. The compiled JAR will be in `build/libs/ip-display-1.0.0.jar`

4. Copy to your RuneLite sideloaded plugins directory:
   ```bash
   cp build/libs/ip-display-1.0.0.jar ~/.runelite/sideloaded-plugins/
   ```

5. Launch RuneLite in developer mode with assertions enabled:
   ```bash
   java -ea -jar RuneLite.jar --developer-mode
   ```

## Usage

1. After installation, look for the green "IP" icon in the RuneLite sidebar
2. Click the icon to open the IP Display panel
3. Your current external IP address will be displayed
4. The IP automatically refreshes every 60 seconds
5. Click "Refresh IP" button to manually check your current IP

## Screenshots

The plugin displays:
- **Current IP Address**: Large, easy-to-read display of your external IP
- **Status**: Shows last update time or error messages
- **Refresh Button**: Manually trigger IP check

## Running with Proxies

This plugin is especially useful when running RuneLite through proxychains or other proxy solutions:

```bash
# Example: Running RuneLite with proxychains
proxychains4 -f /path/to/proxychains.conf java -ea -jar RuneLite.jar --developer-mode
```

The IP Display will show the IP of your proxy server, confirming your connection is properly routed.

## Technical Details

- Checks your IP using public IP detection APIs
- Validates responses to ensure HTML pages aren't mistakenly displayed as IPs
- Implements fallback mechanism across multiple services for reliability
- Uses background executor service to avoid blocking the UI thread
- Automatic retry with multiple services if one fails

## Troubleshooting

### Plugin doesn't appear in sidebar

- Make sure you're running RuneLite with `-ea` (enable assertions) flag
- Verify the JAR is in the correct sideloaded-plugins directory
- Check RuneLite logs for any errors during plugin loading

### Shows "Error: Connection timeout"

- Check your internet connection
- If using a proxy, verify the proxy is working correctly
- Some proxies may block IP detection services

### Shows HTML instead of IP

- This has been fixed in version 1.0.0+
- Update to the latest version
- The plugin now validates responses and tries multiple services

## Development

### Project Structure

```
ip-display/
├── build.gradle           # Gradle build configuration
├── settings.gradle        # Gradle settings
└── src/
    └── main/
        └── java/
            └── com/
                └── ipdisplay/
                    ├── IpDisplayPlugin.java      # Main plugin class
                    ├── IpDisplayPanel.java       # UI panel
                    └── IpDisplayConfig.java      # Configuration (future use)
```

### Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Built for [RuneLite](https://runelite.net/), the open-source Old School RuneScape client
- Uses public IP detection services: ipify.org, icanhazip.com, ifconfig.me

## Support

If you encounter any issues or have suggestions:
- Open an issue on GitHub
- Check existing issues for solutions
- Provide detailed information about your setup and the problem

## Changelog

### Version 1.0.0
- Initial release
- Basic IP display functionality
- Multiple IP service fallback
- Auto-refresh every 60 seconds
- Manual refresh button
- HTML response validation
- Green icon with "IP" text in sidebar
