
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameJoiner {

    // URL of the Node.js server - dynamically assigned based on local IP
    private static String SERVER_URL;

    // Method to get the local IP address
    private static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // For macOS, it might be en1, en0, etc., for Windows wlan0
                if (networkInterface.getName().startsWith("en") || networkInterface.getName().equals("wlan0")) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        // Ignore loopback address and non-IPv4 addresses
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof java.net.Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Method to get a specific variable by key (GET request)
    // Method to get a specific variable by key (GET request)
    // Method to get a specific variable by key (GET request)

    private static String getFirstThreeOctets(String ipAddress) {
        // Regex pattern to match the first three octets of an IP address
        String regex = "^((\\d{1,3})\\.){2}(\\d{1,3})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddress);

        if (matcher.find()) {
            return matcher.group()+"."; // Return the matched part
        }

        // Return null or an empty string if no match is found
        return "";
    }

    public static int connect(String host) throws IOException {
        System.out.println(getFirstThreeOctets(getLocalIPAddress()));

        ServerIO.SERVER_URL = "http://" + getFirstThreeOctets(getLocalIPAddress()) + host + ":3000/"; // Using the local IP address for server communication
        System.out.println("Local IP: " + getLocalIPAddress());
        System.out.println("Server URL: " + ServerIO.SERVER_URL);
        int pNum = ServerIO.getData("manyPlayers")+1;
        ServerIO.sendData("manyPlayers", pNum);
        ServerIO.connectedPlayers.add(new Player("player.png",40, 40,20, 20));
        return pNum;

    }
}