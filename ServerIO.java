import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

public class ServerIO {
    public static String SERVER_URL;

    public static Stack<Player> connectedPlayers = new Stack<>();
    public static void sendData(String key, int value) throws IOException {
        URL url = new URL(SERVER_URL + "data");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // JSON data to send
        String jsonInputString = String.format("{\"key\":\"%s\",\"value\":%d}", key, value);


        // Send the data
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }catch(Exception e){
            System.out.println("Hello");
        }

        // Check response code
        int responseCode = connection.getResponseCode();
        //System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            //System.out.println("Value updated successfully.");
        } else {
            System.out.println("Error: " + connection.getResponseMessage());
        }
    }

    public static int getData(String key) throws IOException {
        // Fix the URL to match the server route (/data/:key)
        URL url = new URL(SERVER_URL + "data/" + key);  // Use "data/" before the key
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int data = 0;
        // Get the response
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                String r = responseLine.split(":")[1];
                data = Integer.parseInt(r.substring(0, r.length()-1));
                //System.out.println("Response from server: " + data);
            }
        }
        return data;
    }
}
