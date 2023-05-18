import org.apache.commons.compress.utils.IOUtils;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class importXlsxFileConverToBase64Xlsx {

/*  public static void main(String[] args) throws IOException {
        System.out.println("base 64: "+importFile());

    }*/

    public static String importFile() throws IOException {
        String result = "";
        FileDialog fileDialog = null;
        try {
            // Crea un objeto FileDialog
            Frame frame = new Frame();
            fileDialog = new FileDialog(frame, "Seleccionar archivo xlsx", FileDialog.LOAD);

            // Establece un filtro para seleccionar sólo archivos xlsx
            fileDialog.setFile("*.xlsx");

            // Muestra el cuadro de diálogo para seleccionar un archivo
            fileDialog.setVisible(true);

            // Si el usuario seleccionó un archivo, lee su contenido , obtiene su filepad, tomar el archivo y lo convierte en base 64
            if (fileDialog.getFile() != null) {

                //aca obtiene el filepad del archivo seleccionado
                File file = new File(fileDialog.getDirectory() + fileDialog.getFile());
                String filePath = file.getAbsolutePath();
                File fileFinal = new File(filePath);

                // Lee el archivo xlsx
                FileInputStream fileInputStream = new FileInputStream(fileFinal);
                byte[] bytes = IOUtils.toByteArray(fileInputStream);
                fileInputStream.close();

                // Convierte el archivo en base64
                String base64String = Base64.getEncoder().encodeToString(bytes);

                // Imprime la representación en base64 del archivo xlsx
               // System.out.println(base64String);

                result = base64String;

                //cierra el FileDialog
                fileDialog.dispose();
            } else {
                result = "0";
                //System.out.println("No se seleccionó ningún archivo.");
            }
        }catch (Exception ex){
            result = "Salio catch exception";
        } finally {
            if (fileDialog != null) {
                fileDialog.dispose();
            }
            Frame[] frames = Frame.getFrames();
            for (Frame f : frames) {
                f.dispose();
            }
            return result;
        }


       //

    }
}
