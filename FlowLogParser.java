import java.io.*;
import java.util.*;

public class FlowLogParser {

    // Map to store the tag mappings from lookup table
    private static Map<String, String> lookupMap = new HashMap<>();

    // Maps to store the counts
    private static Map<String, Integer> tagCountMap = new HashMap<>();
    private static Map<String, Integer> portProtocolCountMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            // Parse the lookup table
            parseLookupTable("lookup.csv");

            // Parse the flow logs
            parseFlowLogs("flowlog.txt");

            // Write output to files
            writeTagCountOutput("tag_output.txt");
            writePortProtocolCountOutput("port_protocol_output.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to parse the lookup table
    private static void parseLookupTable(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            // Skip empty lines
            if (line.trim().isEmpty()) {
                continue;
            }

            // Split the line into parts
            String[] parts = line.split(",");

            // Ensure the line has at least 3 columns (dstport, protocol, tag)
            if (parts.length != 3) {
                System.out.println("Invalid line in lookup table: " + line);
                continue; // Skip this line if it's not valid
            }

            // Combine dstport and protocol as the key (case-insensitive)
            String key = parts[0].trim() + "," + parts[1].trim().toLowerCase(); // Case insensitive

            // Extract the tag
            String tag = parts[2].trim();

            // Put the key and tag into the lookup map
            lookupMap.put(key, tag);
        }
        br.close();
    }

    // Method to parse the flow logs and map them to tags
    private static void parseFlowLogs(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = br.readLine()) != null) {
            // Skip empty or malformed lines
            if (line.trim().isEmpty()) {
                continue;
            }

            // Split the line into parts (assumes space-separated fields)
            String[] parts = line.split("\\s+"); // Use regex to handle multiple spaces

            // Ensure the line has at least the required number of fields (we're interested in fields 5, 7, etc.)
            if (parts.length < 8) {  // Assuming we need at least 8 parts
                System.out.println("Invalid line in flow log: " + line);
                continue; // Skip this line if it's not valid
            }

            // Extract dstport and protocol
            String dstport = parts[5]; // dstport is at index 5
            String protocol = getProtocol(parts[7]); // protocol is at index 7 (6 -> tcp, 17 -> udp)

            // Combine dstport and protocol as the key
            String key = dstport + "," + protocol.toLowerCase(); // Case insensitive matching

            // Lookup the tag for this dstport/protocol combination
            String tag = lookupMap.getOrDefault(key, "Untagged");

            // Update tag count
            tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);

            // Update port/protocol combination count (regardless of whether it's tagged or untagged)
            String portProtocolKey = dstport + "," + protocol;
            portProtocolCountMap.put(portProtocolKey, portProtocolCountMap.getOrDefault(portProtocolKey, 0) + 1);
        }
        br.close();
    }

    // Method to convert the protocol number into a string
    private static String getProtocol(String protocolNumber) {
        switch (protocolNumber) {
            case "6":
                return "tcp";
            case "17":
                return "udp";
            default:
                return "unknown";
        }
    }

    // Method to write the tag counts to a file
    private static void writeTagCountOutput(String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write("Tag,Count\n");
        for (Map.Entry<String, Integer> entry : tagCountMap.entrySet()) {
            bw.write(entry.getKey() + "," + entry.getValue() + "\n");
        }
        bw.close();
    }

    // Method to write the port/protocol combination counts to a file
    private static void writePortProtocolCountOutput(String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write("Port,Protocol,Count\n");
        for (Map.Entry<String, Integer> entry : portProtocolCountMap.entrySet()) {
            bw.write(entry.getKey().split(",")[0] + "," + entry.getKey().split(",")[1] + "," + entry.getValue() + "\n");
        }
        bw.close();
    }
}
