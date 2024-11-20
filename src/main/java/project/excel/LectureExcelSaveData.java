/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author user
 */
public class LectureExcelSaveData {
    private String lectureFilePath;

    public LectureExcelSaveData() {
        lectureFilePath = "LectureStaff_data.xlsx";
    }

    public void saveData(ArrayList<String> lectureStaffData) {
        try {
            FileInputStream fileIn = new FileInputStream(new File(lectureFilePath));
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            int newRow = sheet.getLastRowNum() + 1;

            Row row = sheet.createRow(newRow);
            int newCell = 0;
            for (String cellData : lectureStaffData) {
                Cell cell = row.createCell(newCell++);
                cell.setCellValue(cellData);
            }
            fileIn.close();
            FileOutputStream fileOut = new FileOutputStream(new File(lectureFilePath));
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("엑셀파일 저장 완료");
        } catch (IOException e) {
            System.err.println("엑셀파일 저장에 실패했습니다." + e.getMessage());
        }
    }




    /*
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
*/
}