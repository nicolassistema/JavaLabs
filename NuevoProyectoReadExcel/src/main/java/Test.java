import org.apache.commons.lang.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Test {
    public static void main(String[] args) throws IOException {

        GeneratorFile generatorFile = new GeneratorFile();
        ConvertCSVBase64ToXLSXBase64 convertCSVBase64ToXLSXBase64 = new ConvertCSVBase64ToXLSXBase64();
        PruebaXlsxBase64ToExcel pruebaXlsxBase64ToExcel = new PruebaXlsxBase64ToExcel();

        String  base64CSV = generatorFile.Base64CVSGenerator();

        String base64Xlsx = convertCSVBase64ToXLSXBase64.read(base64CSV);
        String jsonExcel = pruebaXlsxBase64ToExcel.read(base64Xlsx);


        System.out.println("json excel: " +  jsonExcel);

    }
    //     ConvertCSVBase64ToXLSXBase64 prueba = new ConvertCSVBase64ToXLSXBase64();


    //Creacion de un .xlsx
      /*  String filePath= "C:\\Users\\Usuario\\Desktop\\t1.xslx";
        File file = new File(filePath);
        filePath= file.getAbsolutePath();
        File xlFile = new File(filePath);
        if(xlFile.exists() && !xlFile.isDirectory())
            xlFile.delete(); //delete if file already exists.
        xlFile.createNewFile();*/


    //obtengo objeto de un .csv
       /* String filePath2 = "C:\\Users\\Usuario\\Desktop\\Pruebas_Excel\\ReportePrueba_CSV.csv";
        File file3 = new File(filePath2);
        byte[] bytes = new byte[(int) file3.length()];
        FileInputStream fis = new FileInputStream(file3);
        fis.read(bytes);
        fis.close();
        String base64String = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Base 64 del CSV: "+ base64String);

        String csvContent = new String(bytes, StandardCharsets.UTF_8);//

        System.out.println("Base 64 xlsx: "+ prueba.read(csvContent));*/
    //}

}