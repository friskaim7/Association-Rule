package CSVTool;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

public class CSVTool {
    String file;

    public static void main(String[] args) {
        CSVTool csvTool = new CSVTool("transactions.csv");
        csvTool.readDataOnly();
    }

    public CSVTool(String fileName) {
        this.file = fileName;
    }

    public void readDataWithHeader() {
        try {

            FileReader filereader = new FileReader(this.file);

            CSVReader csvReader = new CSVReader(filereader);
            String[] currentRecord;

            // we are going to read data line by line
            while ((currentRecord = csvReader.readNext()) != null) {
                for (String cell : currentRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String[]> readDataOnly() {
        List<String[]> allData;
        try {

            FileReader filereader = new FileReader(this.file);

            // create csvReader object
            // and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            allData = csvReader.readAll();

        } catch (Exception e) {
            e.printStackTrace();
            allData = null;
        }
        return allData;
    }

    public void readDataFromCustomSeperator(char separator) {
        try {

            FileReader filereader = new FileReader(file);

            // create csvParser object with
            // custom seperator semi-colon
            CSVParser customSeparatorParser = new CSVParserBuilder().withSeparator(separator).build();

            CSVReader csvReader = new CSVReaderBuilder(filereader).withCSVParser(customSeparatorParser).build();

            // Read data only (without header/first line)
            List<String[]> allData = csvReader.readAll();

            // print Data
            for (String[] row : allData) {
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}