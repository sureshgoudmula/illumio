# Flow Log Parser

## Description

This project is a Java-based solution for parsing a flow log file and mapping each flow log entry to a tag based on a lookup table. The lookup table is a CSV file containing three columns: `dstport`, `protocol`, and `tag`. The program uses the combination of destination port (`dstport`) and protocol to assign a tag, and it generates two output files:

1. **Tag Count**: The count of occurrences for each tag in the flow log.
2. **Port/Protocol Combination Count**: The count of occurrences for each unique port/protocol combination from the flow log, including both **tagged** and **untagged** combinations.

## Requirements

1. **Input File Format**: 
   - The input files, both the flow log file and the lookup table, are plain text (ASCII) files.
   - The lookup table (`lookup.csv`) should contain three columns: `dstport`, `protocol`, and `tag`.
   - The flow log file (`flowlog.txt`) contains log entries with information about network flows, including fields like source and destination IPs, ports, and protocols.

2. **File Size and Limits**: 
   - The flow log file size can be up to 10 MB.
   - The lookup table can have up to 10,000 mappings.

3. **Case-Insensitive Matching**: 
   - The matching of port and protocol combinations between the flow log and the lookup table is case-insensitive.

4. **Multiple Tag Mapping**: 
   - Tags can map to more than one port/protocol combination. For instance, multiple rows in the lookup table can share the same tag.

5. **Handling of Untagged Entries**: 
   - If no matching tag is found for a flow log entry, it is marked as **Untagged** and counted as such in both the tag count and the port/protocol combination count.

## Program Workflow

1. **Lookup Table Parsing**: 
   - The program reads the lookup table from a CSV file and creates a mapping between the destination port, protocol, and the corresponding tag.
   - Case-insensitive matching is ensured when reading the `protocol` column.

2. **Flow Log Parsing**: 
   - The program reads the flow log file and extracts relevant fields such as `dstport` and `protocol`.
   - For each flow log entry, the destination port and protocol are combined to check for a match in the lookup table.
   - If a match is found, the corresponding tag is applied; otherwise, the entry is marked as **Untagged**.

3. **Output Generation**:
   - **Tag Count Output**: The program generates a file that contains the count of occurrences for each tag, including the count of untagged entries.
   - **Port/Protocol Combination Output**: The program also generates a file that contains the count of occurrences for each unique port/protocol combination, including both tagged and untagged combinations.

## Program Execution

1. **Input Files**: 
   - The flow log and lookup table should be provided as `flowlog.txt` and `lookup.csv`, respectively, and placed in the same directory as the program.

2. **Compilation**: 
   - Compile the Java program using the following command:
     ```bash
     javac FlowLogParser.java
     ```

3. **Execution**: 
   - Run the program using the following command:
     ```bash
     java FlowLogParser
     ```

4. **Output Files**: 
   - After execution, two output files will be generated:
     - `tag_output.txt`: Contains the count of each tag and untagged entries.
     - `port_protocol_output.txt`: Contains the count of each port/protocol combination, including both tagged and untagged combinations.

## Additional Details

### Assumptions:

- The program assumes the flow log file is in the default log format (with a specific structure as described in the problem statement).
- Only the default protocols (`tcp` and `udp`) are supported in the current version. Other protocols will be marked as **unknown** if found in the flow log.

### Performance Considerations:

- The program is designed to handle files up to 10 MB in size efficiently using buffered reading.
- Case-insensitive matching ensures that protocol names in the flow logs and lookup table are compared correctly, regardless of letter case.

### Future Improvements:

- Extend support for custom flow log formats.
- Add the ability to handle additional protocols beyond `tcp` and `udp`.
- Enhance performance for even larger files by optimizing memory usage and data structures.

