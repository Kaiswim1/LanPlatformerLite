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

public class GameHoster {

    // URL of the Node.js server - dynamically assigned based on local IP
    //private static String SERVER_URL;
    // Method to get the local IP address
    public static String getLocalIPAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // Check if the IP is not the loopback address (127.0.0.1) and is IPv4
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof java.net.Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getLastOctet(String ipAddress) {
        // Regex pattern to match the last octet of an IP address
        String regex = "\\d{1,3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddress);

        if (matcher.find()) {
            return matcher.group(); // Return the matched part
        }

        // Return null or an empty string if no match is found
        return "";
    }



    // Method to connect to the server and demonstrate sending/retrieving data
    public static int connect() throws IOException {
        String localIP = getLocalIPAddress();
        ServerIO.SERVER_URL = "http://" + localIP + ":3000/";
        ServerIO.sendData("manyPlayers", 1); //First player to connect
        return 1;
    }
}









