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
    private int rowIndex;
    private int startDataIndex;
    private int departmentIndex;

    public LectureExcelSaveData() {
        lectureFilePath = "LectureStaff_data.xlsx";
        rowIndex = 0; // Sheet index
        startDataIndex = 6; // Data column start index
        departmentIndex = 1; // Department column index
    }

    public void saveData(ArrayList<String> lectureStaffData) {
        try {
            FileInputStream fileIn = new FileInputStream(new File(lectureFilePath));
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(rowIndex);

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
            System.err.println("엑셀파일 저장에 실패했습니다: " + e.getMessage());
        }
    }

    public boolean finalConfirm(String minInput, String maxInput, String professorSelect, String classSelect) throws IOException {
        FileInputStream fileLecture = new FileInputStream(lectureFilePath);
        Workbook workbookLecture = new XSSFWorkbook(fileLecture);
        Sheet sheetLecture = workbookLecture.getSheetAt(rowIndex);
        boolean check = false;

        String[] inputData = {professorSelect, minInput, maxInput};

        for (Row row : sheetLecture) {
            Cell cell = row.getCell(departmentIndex);
            if (cell != null && cell.toString().trim().equals(classSelect)) {
                for (int i = 0; i < inputData.length; i++) {
                    Cell saveCell = row.createCell(startDataIndex + i);
                    saveCell.setCellValue(inputData[i]);
                }
                check = true;
                break;
            }
        }

        fileLecture.close();

        FileOutputStream outputStream = new FileOutputStream(lectureFilePath);
        workbookLecture.write(outputStream);
        outputStream.close();
        workbookLecture.close();
        return check;
    }
}
