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
            classIndex, studentIndex, minStudentIndex,studentNameIndex,
            nextRow, maxStudentIndex, professorName, studentLectureIndex, 
            maxCredit, creditIndex, classNumIndex, confirmLectureIndex, nameIndex;
    private String lectureFilePath, studentFilePath;
    
    public LectureExcelReadData(){
        lectureFilePath = "LectureStaff_data.xlsx";
        studentFilePath = "Student_data.xlsx";
        count = startIndex = 0;
        targetIndex = studentLectureIndex = classNumIndex = 5;
        classIndex = nextRow = nameIndex = 1;
        studentIndex = 9;
        minStudentIndex = 7;
        studentNameIndex = 10;
        maxStudentIndex = 8;
        professorName = confirmLectureIndex = 6;
        maxCredit = 18;
        creditIndex = 4;
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
        for (int rowIndex = nextRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // rowIndex = 1부터 시작
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell studentCell = row.getCell(studentIndex);
                Cell minStudentCell = row.getCell(minStudentIndex);
                Cell classNumCell = row.getCell(classNumIndex);
                if (studentCell != null && minStudentCell != null && classNumCell.toString().equals("0")) {
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
    public CopyOnWriteArrayList<String> getPossibleLecture() throws IOException {
        CopyOnWriteArrayList<String> data = new CopyOnWriteArrayList<>();

        // 첫 번째 엑셀 파일 읽기
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);

        // 두 번째 엑셀 파일 읽기 (중복 체크용)
        FileInputStream studentFile = new FileInputStream(studentFilePath);
        Workbook studentWorkbook = new XSSFWorkbook(studentFile);
        Sheet studentSheet = studentWorkbook.getSheetAt(startIndex);

        // 중복 강의 목록 추출
        CopyOnWriteArrayList<String> existingLectures = new CopyOnWriteArrayList<>();
        for (int rowIndex = 1; rowIndex <= studentSheet.getLastRowNum(); rowIndex++) { // 첫 번째 행 건너뜀
            Row row = studentSheet.getRow(rowIndex);
            if (row != null) {
                Cell lectureCell = row.getCell(studentLectureIndex);
                Cell creditCell = row.getCell(creditIndex);
                int currentCredit = 0;
                
                if (creditCell != null && creditCell.getNumericCellValue() >= maxCredit) {
                    data = null;
                    return data;
                }
                
                if (lectureCell != null) {
                    String currentLecture = lectureCell.toString();
                    String[] checkLecture = currentLecture.split(",");
                    existingLectures.addAll(Arrays.asList(checkLecture));
                }
            }
        }

        // 기존 엑셀 파일 처리
        for (int rowIndex = nextRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell studentCell = row.getCell(studentIndex);
                Cell maxStudentCell = row.getCell(maxStudentIndex);

                if (studentCell != null && maxStudentCell != null) {
                    double studentValue = parseCellValue(studentCell);
                    double maxStudentValue = parseCellValue(maxStudentCell);

                    // 비교 조건
                    if (studentValue < maxStudentValue) {
                        Cell classCell = row.getCell(classIndex);
                        Cell professorNameCell = row.getCell(professorName);

                        if (classCell != null && professorNameCell != null) {
                            String className = classCell.toString();

                            // 중복 체크
                            if (!existingLectures.contains(className)) {
                                data.add(className + "/" + professorNameCell.toString());
                            }
                        }
                    }
                }
            }
        }

        // 리소스 정리
        studentFile.close();
        studentWorkbook.close();
        file.close();
        workbook.close();

        return data;
    }
    
    public CopyOnWriteArrayList<String> getConfirmLecture(String name) throws IOException {
        CopyOnWriteArrayList<String> data = new CopyOnWriteArrayList<>();
        
        FileInputStream file = new FileInputStream(studentFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);
        
        for (int rowIndex = nextRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell nameCell = row.getCell(nameIndex);
                if (nameCell != null && nameCell.toString().equals(name)) {
                    Cell confirmLectureCell = row.getCell(confirmLectureIndex);
                    String[] currentValue = confirmLectureCell.toString().split(",");
                    for (String i : currentValue) {
                        data.add(i);
                    }
                }
            }
        }
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

