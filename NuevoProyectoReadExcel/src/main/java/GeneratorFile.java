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

public class GeneratorFile {

    public GeneratorFile() {
    }
    /*
     * genera un xslx auxiliar el algun directorio -- dejo comentado por que en este caso no lo necesito
     * luego toma el CSV y lo convierte en CSV base 64
     * */
    public String Base64CVSGenerator() throws IOException {
        ConvertCSVBase64ToXLSXBase64 prueba = new ConvertCSVBase64ToXLSXBase64();

        //Creacion de un .xlsx
    /*   String filePath= "C:\\Users\\Usuario\\Desktop\\t1.xslx";
        File file = new File(filePath);
        filePath= file.getAbsolutePath();
        File xlFile = new File(filePath);
        if(xlFile.exists() && !xlFile.isDirectory())
            xlFile.delete(); //delete if file already exists.
        xlFile.createNewFile();*/


        //obtengo objeto de un .csv
        String filePath2 = "C:\\Users\\Usuario\\Desktop\\Pruebas_Excel\\ReportePrueba_CSV.csv";
        File file3 = new File(filePath2);
        byte[] bytes = new byte[(int) file3.length()];
        FileInputStream fis = new FileInputStream(file3);
        fis.read(bytes);
        fis.close();
        String base64String = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Base 64 del CSV: "+ base64String);
        String base64CSVData = new String(bytes, StandardCharsets.UTF_8);//

        //return prueba.read(csvContent);
        return base64CSVData;
    }
}