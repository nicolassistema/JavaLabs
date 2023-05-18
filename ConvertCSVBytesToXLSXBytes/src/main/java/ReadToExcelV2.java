import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import com.opencsv.CSVReader;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;


public class ReadToExcelV2 {

    private final static Logger logger = Logger.getLogger("com.ibm.bpm.custom.ReadExcel");

    public ReadToExcelV2() {
    }

    /**
     * Reads the input a base64 excel xlsx file and converts to JSON
     * This will read multiple sheets.
     * It returns a cells structure thus
     * {"colIndex":0,"type":"string","value":"#"}
     * the types that are returned mapped to Excel types thus
     * STRING string,
     * BOOLEANboolean,
     * BLANK null
     * _NONE null
     * ERROR null
     * NUMERIC   Date, int, or decimal.
     * Detailed numerics not handled would need updates for more robust needs
     * FORMULA formulas are evaluated and value returned TYPES (STRING, NUMERIC, BOOLEAN, ERROR)
     * Dates are in following format "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
     *
     * @param base64ExcelData
     * @return the file as JSON format or null in case of error
     */
    public String read(String base64ExcelData) {


/******************************************************************************/
        String base64CSV = base64ExcelData;
        byte[] csvData = Base64.getDecoder().decode(base64CSV);
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(csvData), StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(isr);
        String[] headers = reader.readNext();

        // Crear un objeto XSSFWorkbook y llenarlo con los datos del archivo CSV
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Hoja1");
        int rownum = 0;
        Row headerRow = sheet.createRow(rownum++);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        String[] line;
        while ((line = reader.readNext()) != null) {
            Row row = sheet.createRow(rownum++);
            for (int i = 0; i < line.length; i++) {
                row.createCell(i).setCellValue(line[i]);
            }
        }
        reader.close();

        // Escribir el objeto XSSFWorkbook en un ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);

        // Codificar el contenido del ByteArrayOutputStream en base64
        String base64XLSX = Base64.getEncoder().encodeToString(baos.toByteArray());

        // Usar la cadena base64 del archivo XLSX como se desee
        System.out.println(base64XLSX);


        String base64ExcelDataNuevo = base64XLSX;

/******************************************************************************/




        StringBuilder sb = new StringBuilder();

        if ((base64ExcelDataNuevo == null) || (base64ExcelDataNuevo.isEmpty())) {

            logger.logp(Level.OFF, "com.ibm.bpm.custom.ReadExcel", "read", "ReadExcel(read) - The Excel data passed is either missing or bad.");

            throw new RuntimeException("ReadExcel(read) - The Excel data passed is either missing or bad.");

        }

        byte[] data = Base64.getDecoder().decode(base64ExcelDataNuevo);
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);

            // Creating a Workbook from an Excel file (.xls or .xlsx)
            Workbook workbook = WorkbookFactory.create(bais);

            /*
             * =============================================================
             * Iterating over all the sheets and build JSON Objects
             * =============================================================
             */

            // Create a JSONObject to store table data.
            JSONObject wbJSON = new JSONObject();
            JSONObject sheetJSON = null;
            JSONObject rowJSON = null;
            JSONObject cellJSON = null;

            List<JSONObject> sheetJSONList = new ArrayList<JSONObject>();
            List<JSONObject> rowJSONList = new ArrayList<JSONObject>();
            List<JSONObject> cellJSONList = new ArrayList<JSONObject>();


            // Getting the Sheet at index zero
            String type = null;

            DataFormatter dataFormatter = new DataFormatter();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date cellDate = null;

            String cellValue = null;
            wbJSON.put("numsheets", workbook.getNumberOfSheets());
            // Iterate over work sheets from work book
            for (Sheet sheet : workbook) {
                sheetJSON = new JSONObject();

                sheetJSON.put("name", sheet.getSheetName());
                sheetJSON.put("numrows", sheet.getPhysicalNumberOfRows());

                rowJSONList.clear();
                // Iterate over rows of worksheet
                for (Row row : sheet) {
                    //
                    rowJSON = new JSONObject();
                    rowJSON.put("rownum", row.getRowNum());
                    rowJSON.put("numcells", row.getPhysicalNumberOfCells());
                    rowJSONList.add(rowJSON);

                    // Clear the last row of cells
                    cellJSONList.clear();


                    //Iterate cells for this row
                    for (Cell cell : row) {

                        cellValue = dataFormatter.formatCellValue(cell);

                        // Log the basic cell info if Logging leves fine
                        if (logger.isLoggable(Level.FINE)) {

                            sb.append("SHEETNAME ").append(sheet.getSheetName()).append("= ROWNUM =").append(row.getRowNum()).append(" ,CELL INFO TYPE=[").append(cell.getCellType());
                            sb.append("] VALUE=[").append(dataFormatter.formatCellValue(cell));


                            logger.logp(Level.FINE, "com.ibm.bpm.custom.ReadExcel", "read", sb.substring(0));
                            sb.setLength(0);
                        }

                        switch (cell.getCellType()) {
                            case NUMERIC:

                                if (DateUtil.isCellDateFormatted(cell)) {

                                    cellDate = cell.getDateCellValue();
                                    cellValue = sdf.format(cellDate);
                                    type = "date";

                                } else {
                                    // Keep it simple at first if Integer use integer otehrwise Decimal
                                    if (isInt(cellValue)) {

                                        type = "integer";
                                    }
                                    else {
                                        type = "decimal";
                                    }
                                }
                                break;

                            case STRING:
                                type = "string";
                                break;

                            case BOOLEAN:
                                cellValue = cellValue.toLowerCase();
                                type = "boolean";
                                break;

                            case BLANK:
                                type = "null";
                                break;

                            case _NONE:
                                type = "null";
                                break;

                            case ERROR:
                                type = "error";
                                break;

                            case FORMULA:

                                cellValue = dataFormatter.formatCellValue(cell, evaluator);
                                CellType fct = evaluator.evaluateFormulaCell(cell);

                                if (logger.isLoggable(Level.FINE)) {

                                    sb.append("FORMULA INFO, FORMULA=[").append(dataFormatter.formatCellValue(cell));
                                    sb.append("], FORMULA_VALUE=[").append(cellValue).append("], FORMULA_TYPE=[");
                                    sb.append(evaluator.evaluate(cell).getCellType().toString()).append("]");

                                    logger.logp(Level.FINE, "com.ibm.bpm.custom.ReadExcel", "read", sb.substring(0));
                                    sb.setLength(0);
                                }


                                switch (fct) {
                                    case STRING:
                                        type = "string";
                                        break;

                                    case BOOLEAN:
                                        cellValue = cellValue.toLowerCase();
                                        type = "boolean";
                                        break;

                                    case NUMERIC:
                                        if (DateUtil.isCellDateFormatted(evaluator.evaluateInCell(cell))) {
                                            cellDate = evaluator.evaluateInCell(cell).getDateCellValue();
                                            cellValue = sdf.format(cellDate);
                                            type = "date";

                                        } else {
                                            // Keep it simple at first if Integer use integer otehrwise Decimal
                                            if (isInt(cellValue)) {

                                                type = "integer";
                                            }
                                            else {
                                                type = "decimal";
                                            }
                                        }

                                        break;
                                    case ERROR:
                                        type = "error";
                                        break;

                                    default:
                                        type = "unknown";
                                        break;
                                }

                                break;

                            default:
                                type = "unknown";
                                break;
                        }
                        cellJSON = new JSONObject();
                        cellJSON.put("colIndex", cell.getColumnIndex());
                        cellJSON.put("value", cellValue);
                        cellJSON.put("type", type);
                        cellJSONList.add(cellJSON);
                    } // For cell
                    rowJSON.put("Cells", cellJSONList);

                } // for row

                sheetJSON.put("Rows", rowJSONList);
                sheetJSONList.add(sheetJSON);
            } // for sheet
            // Closing the workbook

            wbJSON.put("Sheets", sheetJSONList);
            workbook.close();

            if (logger.isLoggable(Level.FINE)) {


                logger.logp(Level.FINER, "com.ibm.bpm.custom.ReadExcel", "read", wbJSON.toString());
            }


            return wbJSON.toString();

        } catch (IOException e) {

            logger.logp(Level.OFF, "com.ibm.bpm.custom.ReadExcel", "read", "error reading byte stream of Excel file", e);
        } finally {

            // do nothing

        }

        return null;

    }

    private boolean isInt(String val) {

        boolean isInt = false;
        try {

            Integer.parseInt(val);
            isInt = true;

        } catch (NumberFormatException e) {

            // do Nothing
        }

        return isInt;
    }



}
