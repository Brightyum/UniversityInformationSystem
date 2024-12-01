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
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author user
 */

public class LectureExcelReadData {
    private int count, startIndex, targetIndex,
            classIndex, studentIndex, minStudentIndex,studentNameIndex;
    private String lectureFilePath;
    
    public LectureExcelReadData(){
        lectureFilePath = "LectureStaff_data.xlsx";
        count = startIndex = 0;
        targetIndex = 5;
        classIndex = 1;
        studentIndex = 9;
        minStudentIndex = 7;
        studentNameIndex = 10;
    }
    
    public CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> readLectureStaffExcel() throws IOException {
    CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = new CopyOnWriteArrayList<>();
    try (FileInputStream fileIn = new FileInputStream(new File(lectureFilePath));
         Workbook workbook = new XSSFWorkbook(fileIn)) {
        Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트를 가져옴
        for (Row row : sheet) {
            CopyOnWriteArrayList<Object> rowData = new CopyOnWriteArrayList<>();
            for (int i = 0; i <= 4; i++) { // 인덱스 0~4까지만 읽음
                Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // 셀이 없으면 빈 셀로 처리
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            rowData.add(cell.getDateCellValue().toString());
                        } else {
                            rowData.add(cell.getNumericCellValue());
                        }
                        break;
                    default:
                        rowData.add(""); // 셀이 빈 경우 빈 문자열 추가
                        break;
                }
            }
            data.add(rowData);
        }
    }
    return data;
}

    
    public CopyOnWriteArrayList<String> getColumn(
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data) {
                CopyOnWriteArrayList<String> column = new CopyOnWriteArrayList<>();
                if(!data.isEmpty()) {
                    CopyOnWriteArrayList<Object> firstRow = data.get(count);
                    for (Object cell : firstRow) {
                        column.add(cell.toString());
                    }
                    /*
                    for(int i = 0; i < firstRow.size(); i++) {
                        column.add("" + (i + 1));
                    }
                    */
                }
                return column;
    }
    
    public CopyOnWriteArrayList<String> getClasses() throws IOException {
        CopyOnWriteArrayList<String> classData = new CopyOnWriteArrayList<>();
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);
        
        for (Row row : sheet) {
            Cell cell = row.getCell(targetIndex);
            if (cell != null && cell.toString().equals("0")) {
                Cell dataCell = row.getCell(classIndex);
                classData.add(dataCell.toString());
            }
        }
        return classData;
    }
    
    public CopyOnWriteArrayList<String> getLecture() throws IOException {
        CopyOnWriteArrayList<String> data = new CopyOnWriteArrayList<>();
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);

        // 첫 번째 행을 건너뛰고 나머지 데이터 처리
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // rowIndex = 1부터 시작
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell studentCell = row.getCell(studentIndex);
                Cell minStudentCell = row.getCell(minStudentIndex);

                if (studentCell != null && minStudentCell != null) {
                    double studentValue = parseCellValue(studentCell);
                    double minStudentValue = parseCellValue(minStudentCell);

                    // 비교 조건
                    if (studentValue > minStudentValue) {
                        Cell cell = row.getCell(classIndex);
                        if (cell != null) {
                            data.add(cell.toString());
                        }
                    }
                }
            }
        }

        workbook.close();
        file.close();
        return data;
    }

    
    public CopyOnWriteArrayList<String> getStudent(String classSelect) throws IOException {
        CopyOnWriteArrayList<String> data = new CopyOnWriteArrayList<>();
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);
        
        for (Row row : sheet) {
            Cell classCell = row.getCell(classIndex);
            if (classCell != null && classCell.toString().equals(classSelect)) {
                Cell cell = row.getCell(studentNameIndex);
                if (cell != null) {
                    String cellValue = cell.toString().replaceAll("\\s+", "");
                    String[] studentData = cellValue.split(",");
                    for (String i : studentData) {
                        data.add(i);
                    }
                }
            }
        }
        workbook.close();
        file.close();
        return data;
    }
        // 셀 값을 파싱하여 double로 반환하는 메서드
    private double parseCellValue(Cell cell) {
        String cellValue = cell.toString().trim();

        // String 타입이면 Integer로 변환 후 double로 반환
        if (isInteger(cellValue)) {
            return Integer.parseInt(cellValue);
        }

        // Double 타입인지 확인 후 double로 반환
        if (isDouble(cellValue)) {
            return Double.parseDouble(cellValue);
        }

        throw new NumberFormatException("셀 값이 숫자가 아닙니다: " + cellValue);
    }

    // 문자열이 Integer 형식인지 확인
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 문자열이 Double 형식인지 확인
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

