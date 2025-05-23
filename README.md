# Data-Meter-Telecom-Data-Aggregator

This project processes large telecom data logs to compute data usage and cost per mobile number. It reads multiple `.txt` files containing raw usage, validates them, aggregates usage, calculates cost, and generates clean CSV reports.

---

## Folder Structure

DataMeter/
├── src/
│   └── DataMeter.java              # Main Java program
├── E:/DataMeter Dataset/           # Input/output folder (create manually)
│   ├── file1.txt                   # Example input file
│   ├── file2.txt                   # More input files...
│   ├── output.csv                  # Auto-generated valid result
│   └── bad_data.csv                # Auto-generated invalid records
└── README.md


**Note:** `output.csv` and `bad_data.csv` will be **automatically generated**. You don't need to create them manually.
## Features
- Validates input lines (10-digit phone numbers, numeric 4G/5G usage)
- Aggregates data per mobile number
- Categorizes usage:
  - 4G Home
  - 5G Home
  - 4G Roaming
  - 5G Roaming
- Calculates cost with:
  - 4G: ₹0.05/KB
  - 5G: ₹0.10/KB
  - 4G Roaming: ₹0.055/KB
  - 5G Roaming: ₹0.115/KB
  - +5% surcharge if total exceeds ₹500
- Generates:
  - output.csv – Valid clean result
  - bad_data.csv – Invalid or malformed input rows
## Setup Folder
E:/DataMeter Dataset/ -- can vary in your pc
## Compile and Run
cd src
javac DataMeter.java
java DataMeter
