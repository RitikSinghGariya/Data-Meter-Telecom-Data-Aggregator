import java.io.*;
import java.util.*;

// Represents usage data per mobile number
class DataUsage {
    long fourG = 0;
    long fiveG = 0;
    long fourGRoaming = 0;
    long fiveGRoaming = 0;

    // Aggregates usage based on roaming flag
    public void addUsage(boolean isRoaming, long g4, long g5) {
        if (isRoaming) {
            this.fourGRoaming += g4;
            this.fiveGRoaming += g5;
        } else {
            this.fourG += g4;
            this.fiveG += g5;
        }
    }

    // Calculates total cost based on usage and threshold
    public double computeCost(double threshold) {
        double cost = 0;
        cost += fourG * 0.05;
        cost += fiveG * 0.10;
        cost += fourGRoaming * 0.055;
        cost += fiveGRoaming * 0.115;

        if (cost > threshold) {
            cost *= 1.05; // Apply 5% surcharge if threshold exceeded
        }
        return cost;
    }
}

public class DataMeter {

    private static final double COST_THRESHOLD = 500.0;

    public static void main(String[] args) {

        // Define output file paths for valid and invalid data
        String validCSV = "E:/DataMeter Dataset/output.csv";
        String badCSV = "E:/DataMeter Dataset/bad_data.csv";

        // Collect malformed or invalid rows
        List<String> badLines = new ArrayList<>();

        // Map to store and aggregate data per mobile number
        Map<String, DataUsage> usageMap = new HashMap<>();

        // Define the folder path containing input dataset files
        String folderPath = "E:/DataMeter Dataset";
        File folder = new File(folderPath);

        // Validate folder existence and type
        if (folder.exists() && folder.isDirectory()) {

            // Retrieve all files from the directory
            File[] files = folder.listFiles();

            // Process each .txt file
            if (files != null) {
                for (File file : files) {
                    if (!file.getName().endsWith(".txt")) {
                        continue; // Skip non-text files
                    }

                    // Read file line by line
                    try (BufferedReader fis = new BufferedReader(new FileReader(file))) {
                        String line;
                        int lineNum = 0;

                        while ((line = fis.readLine()) != null) {
                            lineNum++;
                            String[] parts = line.split("\\|");

                            // Validate format and data types
                            if (parts.length != 5 ||
                                    !parts[0].trim().matches("\\d{10}") ||  // Validate mobile number
                                    !parts[2].trim().matches("\\d+") ||     // Validate 4G usage
                                    !parts[3].trim().matches("\\d+")) {     // Validate 5G usage
                                badLines.add("Bad Line (" + file.getName() + " L" + lineNum + "): " + line);
                                continue;
                            }

                            String mobile = parts[0].trim();
                            long g4 = Long.parseLong(parts[2].trim());
                            long g5 = Long.parseLong(parts[3].trim());
                            boolean isRoaming = parts[4].trim().equalsIgnoreCase("Yes");

                            // Initialize usage if mobile number is new
                            usageMap.putIfAbsent(mobile, new DataUsage());

                            // Accumulate usage data
                            usageMap.get(mobile).addUsage(isRoaming, g4, g5);
                        }

                    } catch (IOException e) {
                        System.out.println("Error reading file: " + file.getName() + " - " + e.getMessage());
                    }
                }

                // Write aggregated valid data to CSV
                try (FileWriter writer = new FileWriter(validCSV)) {
                    writer.write("Mobile Number,4G,5G,4G Roaming,5G Roaming,Cost\n");
                    for (Map.Entry<String, DataUsage> entry : usageMap.entrySet()) {
                        String mobile = entry.getKey();
                        DataUsage d = entry.getValue();
                        double cost = d.computeCost(COST_THRESHOLD);

                        writer.write(String.format("%s,%d,%d,%d,%d,%.2f\n",
                                mobile, d.fourG, d.fiveG, d.fourGRoaming, d.fiveGRoaming, cost));
                    }
                    System.out.println("Valid data saved to: " + validCSV);
                } catch (IOException e) {
                    System.err.println("Error writing valid data: " + e.getMessage());
                }

                // Write invalid (bad) lines to separate CSV
                try (FileWriter badWriter = new FileWriter(badCSV)) {
                    badWriter.write("Bad Records\n");
                    for (String bad : badLines) {
                        badWriter.write(bad + "\n");
                    }
                    System.out.println("Bad data saved to: " + badCSV);
                } catch (IOException e) {
                    System.err.println("Error writing bad data: " + e.getMessage());
                }

                // Display result on console for quick verification
                System.out.println("Mobile Number|4G|5G|4G Roaming|5G Roaming|Cost");

                for (Map.Entry<String, DataUsage> entry : usageMap.entrySet()) {
                    String mobile = entry.getKey();
                    DataUsage d = entry.getValue();
                    double cost = d.computeCost(COST_THRESHOLD);

                    System.out.printf("%s|%d|%d|%d|%d|%.2f\n",
                            mobile, d.fourG, d.fiveG, d.fourGRoaming, d.fiveGRoaming, cost);
                }

            } else {
                System.out.println("No files found in the folder.");
            }

        } else {
            System.out.println("Folder doesn't exist or is not a directory.");
        }
    }
}
