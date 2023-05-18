import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.String;
import org.apache.commons.lang.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PruebaXlsxBase64ToExcel {

    public PruebaXlsxBase64ToExcel() {
    }

    public String read(String xlsxBase64) {

        String jsonExcel = "";

        // Decode the Base64 string to a byte array
        //byte[] decodedBytes = Base64.decodeBase64(base64String);
        byte[] decodedBytes = Base64.decodeBase64(xlsxBase64.getBytes());

        try (InputStream inputStream = new ByteArrayInputStream(decodedBytes)) {

            // Create a new workbook object
            Workbook workbook = new XSSFWorkbook(inputStream);

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Define a JSON array to store the rows
            JSONArray rows = new JSONArray();

            // Iterate over the rows
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Define a JSON object to store the cells
                JSONObject cells = new JSONObject();

                // Iterate over the cells
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String key = String.valueOf(cell.getColumnIndex());
                    String value = cell.getStringCellValue();
                    cells.put(key, value);
                }

                // Add the cells to the row object
                rows.put(cells);
            }

            // Convert the rows JSON array to a JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rows", rows);

            // Print the JSON object
            System.out.println(jsonObject.toString());

            jsonExcel = jsonObject.toString();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return jsonExcel;
    }

}