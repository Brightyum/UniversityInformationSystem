/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
/**
 *
 * @author user
 */
public class Execel {
      public static void main(String[] args) throws IOException {
        FileInputStream file = new FileInputStream(new File("Student_data.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        System.out.print(cell.getStringCellValue() + "\t");
                        break;
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t");
                        break;
                    default:
                        break;
                }
            }
            System.out.println();
        }
        workbook.close();
        file.close();
    }
}
