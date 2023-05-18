//package com.ibm.bcba.services.rest;

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
public class ConvertCSVBase64ToXLSXBase64 {

    // public static void main(String[] args) {
    // }

    public ConvertCSVBase64ToXLSXBase64() {

    }


    public String read(String base64CSVData) {

        String xlsxBase64 = "";

        Workbook libro2 = new XSSFWorkbook();
        Sheet sheet = libro2.createSheet("Hoja1");

        try {
            String[] lines = base64CSVData.split("\\r?\\n");
            System.out.println("lineas : "+ lines[0]);


            int rowNum = 0;
            for (String line : lines) {
                String[] fields = line.split(",");
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (String field : fields) {
                    row.createCell(colNum++).setCellValue(field);
                }
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            libro2.write(bos);
            byte[] bytess = bos.toByteArray();
            xlsxBase64 = Base64.getEncoder().encodeToString(bytess);
            System.out.println("Base 64 xlsx : "+ xlsxBase64);

        } catch (Exception ex) {
            System.out.println("RestAPI.java. Error inesperado: " + ex.getMessage());
        }
        return xlsxBase64;
    }
}

