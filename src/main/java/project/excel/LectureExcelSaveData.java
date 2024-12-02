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
    private int rowIndex, departmentIndex, startDataIndex, modifyClassIndex;

    public LectureExcelSaveData() {
        lectureFilePath = "LectureStaff_data.xlsx";
        rowIndex = 0; // Sheet index
        startDataIndex = 5; // Data column start index
        departmentIndex = 1; // Department column index
        modifyClassIndex = 1;
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

        String[] inputData = {"0", professorSelect, minInput, maxInput};

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
    public boolean modifyClass(String className, String modifyData) throws IOException {
        boolean check = false;
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(rowIndex);
        int count = 0;
        
        for (Row row : sheet) {
            Cell modifyCell = row.getCell(modifyClassIndex);
            if (modifyCell != null && modifyCell.toString().equals(className)) {
                String[] data = modifyData.trim().replaceAll("\\s+", "").split(",");
                
                for (int i = modifyClassIndex; i <= data.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(data[count]);
                    count++;
                }
                check = true;
                break;
            }
        }
        
        file.close();
        FileOutputStream outputStream = new FileOutputStream(lectureFilePath);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
        
        return check;
    }
    
    public boolean deleteClass(String className) throws IOException {
        boolean check = false;
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(rowIndex);
        
        for (Row row : sheet) {
            Cell deleteCell = row.getCell(modifyClassIndex);
            if (deleteCell != null && deleteCell.toString().equals(className)) {
                sheet.removeRow(row);
                check = true;
                break;
            }
        }
        file.close();
        FileOutputStream outputStream = new FileOutputStream(lectureFilePath);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
        
        return check;
    }
}
