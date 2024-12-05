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
import java.util.Arrays;

/**
 *
 * @author user
 */
public class LectureExcelSaveData {
    private String lectureFilePath, studentFilePath;
    private int rowIndex, departmentIndex, startDataIndex, 
            classIndex, studentCountIndex, studentNamesIndex,
            plusCount, creditIndex, studentCreditIndex, studentLectureIndex,
            studentNameIndex, lectureNumIndex, nextRow,
            billingIndex, confirmLectureIndex, lectureFee;

    public LectureExcelSaveData() {
        lectureFilePath = "LectureStaff_data.xlsx";
        studentFilePath = "Student_data.xlsx";
        rowIndex = 0; // Sheet index
        startDataIndex = studentLectureIndex = lectureNumIndex = 5; // Data column start index
        departmentIndex = classIndex = plusCount = studentNameIndex = nextRow = 1; // Department column index
        studentCountIndex = 9;
        studentNamesIndex = 10;
        creditIndex = 3;
        studentCreditIndex = 4;
        confirmLectureIndex = 6;
        billingIndex = 7;
        lectureFee = 50000;
    }

    public void saveData(ArrayList<String> lectureStaffData) {
        try {
            FileInputStream fileIn = new FileInputStream(new File(lectureFilePath));
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(rowIndex);

            int newRowIndex = sheet.getLastRowNum() + 1; // 새 행 인덱스
            Row newRow = sheet.createRow(newRowIndex); // 새 행 생성

            // 0번째 셀 처리
            int newCellIndex = 0; // 셀 인덱스 시작
            Cell firstCell = newRow.createCell(newCellIndex); // 0번째 셀 생성
            if (newRowIndex > 0) { // 이전 행이 있을 경우
                Row previousRow = sheet.getRow(newRowIndex - 1); // 바로 위의 행 가져오기
                if (previousRow != null && previousRow.getCell(newCellIndex) != null) {
                    double previousValue = previousRow.getCell(newCellIndex).getNumericCellValue();
                    firstCell.setCellValue(previousValue + 1); // 바로 위 값에 1 추가
                } else {
                    firstCell.setCellValue(1); // 기본값 1
                }
            } else {
                firstCell.setCellValue(1); // 첫 행의 기본값 1
            }

            // 나머지 데이터 처리
            newCellIndex = 1; // 1번 셀부터 시작
            for (String cellData : lectureStaffData) {
                Cell cell = newRow.createCell(newCellIndex++);
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
                Cell saveStudentCell = row.createCell(studentCountIndex);
                saveStudentCell.setCellValue(0);
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
            Cell modifyCell = row.getCell(classIndex);
            if (modifyCell != null && modifyCell.toString().equals(className)) {
                String[] data = modifyData.trim().replaceAll("\\s+", "").split(",");
                
                for (int i = classIndex; i <= data.length; i++) {
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
            Cell deleteCell = row.getCell(classIndex);
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
    
    public boolean addStudent(String name, String select) throws IOException {
        boolean check = false;
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(rowIndex);
        
        String[] selectClass = select.split("/");
        String className = selectClass[rowIndex];
        System.out.println(className);
        for (Row row : sheet) {
            Cell classNameCell = row.getCell(classIndex);
            if (classNameCell != null && classNameCell.toString().equals(className)) {
                Cell studentCountCell = row.getCell(studentCountIndex);
                Cell studentNamesCell = row.getCell(studentNamesIndex);
                
                if (studentCountCell != null) {
                    int currentValue = (int)studentCountCell.getNumericCellValue();
                    currentValue += plusCount;
                    
                    studentCountCell.setCellValue(currentValue);
                } else {
                    studentCountCell = row.createCell(studentCountIndex);
                    studentCountCell.setCellValue(1);
                }
                
                if (studentNamesCell != null) {
                    String currentNames = studentNamesCell.toString();
                    String updateNames = currentNames + "," + name;
                    
                    studentNamesCell.setCellValue(updateNames);
                } else {
                    studentNamesCell = row.createCell(studentNamesIndex);
                    studentNamesCell.setCellValue(name);
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
    
    public boolean setStudentData(String name, String select) throws IOException {
        boolean check = false;

        try (FileInputStream file = new FileInputStream(lectureFilePath);
             Workbook workbook = new XSSFWorkbook(file);
             FileInputStream studentFile = new FileInputStream(studentFilePath);
             Workbook studentWorkbook = new XSSFWorkbook(studentFile)) {

                Sheet sheet = workbook.getSheetAt(rowIndex);
                Sheet studentSheet = studentWorkbook.getSheetAt(rowIndex);

                // 선택한 클래스 정보 처리
                String[] selectClass = select.split("/");
                String className = selectClass[rowIndex];
                int credit = 0;

                // 강의 파일에서 학점 정보 가져오기
                for (Row row : sheet) {
                    Cell classNameCell = row.getCell(classIndex);
                    if (classNameCell != null && classNameCell.toString().equals(className)) {
                        Cell creditCell = row.getCell(creditIndex);
                        if (creditCell != null) {
                            System.out.println(creditCell.getCellType()+"1");
                            switch (creditCell.getCellType()) {
                                case NUMERIC:
                                    credit = (int) creditCell.getNumericCellValue();
                                    break;
                                case STRING:
                                    try {
                                        credit = Integer.parseInt(creditCell.getStringCellValue());
                                    } catch (NumberFormatException e) {
                                        System.err.println("Invalid numeric value in credit cell: " + creditCell.getStringCellValue());
                                    }
                                    break;
                                default:
                                    System.err.println("Unsupported cell type for credit: " + creditCell.getCellType());
                                    break;
                            }
                        }
                        break;
                    }
                }

            // 학생 파일 수정
            for (Row row : studentSheet) {
                Cell studentNameCell = row.getCell(studentNameIndex);
                if (studentNameCell != null && studentNameCell.toString().equals(name)) {
                    Cell creditCell = row.getCell(studentCreditIndex);
                    Cell lectureCell = row.getCell(studentLectureIndex);

                    // 학점 업데이트
                    if (creditCell != null) {
                        int currentValue = 0;
                        System.out.println(creditCell.getCellType()+"2");
                        switch (creditCell.getCellType()) {
                            case NUMERIC:
                                currentValue = (int) creditCell.getNumericCellValue();
                                break;
                            case STRING:
                                try {
                                    currentValue = Integer.parseInt(creditCell.getStringCellValue());
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid numeric value in student credit cell: " + creditCell.getStringCellValue());
                                }
                                break;
                            default:
                                System.err.println("Unsupported cell type for student credit: " + creditCell.getCellType());
                                break;
                        }
                        currentValue += credit;
                        creditCell.setCellValue(currentValue);
                    } else {
                        creditCell = row.createCell(studentCreditIndex);
                        creditCell.setCellValue(credit);
                    }

                    // 강의 업데이트
                    if (lectureCell != null) {
                        String currentLecture = lectureCell.toString();
                        String[] checkLecture = currentLecture.split(",");
                        boolean isDuplicate = false;
                        for (String i : checkLecture) {
                            if (i.equals(className)) { // 문자열 비교 수정
                                isDuplicate = true;
                                break;
                            }
                        }
                        if (!isDuplicate) {
                            String updateLecture = currentLecture + "," + className;
                            lectureCell.setCellValue(updateLecture);
                        } else {
                            return check; // 중복 강의, false 반환
                        }
                    } else {
                        lectureCell = row.createCell(studentLectureIndex);
                        lectureCell.setCellValue(className);
                    }
                    check = true;
                    break;
                }
            }

            // 수정된 내용을 파일에 저장
            try (FileOutputStream fileOut = new FileOutputStream(lectureFilePath);
                 FileOutputStream studentFileOut = new FileOutputStream(studentFilePath)) {
                workbook.write(fileOut);
                studentWorkbook.write(studentFileOut);
            }
        }

        return check;
    }
    
    public boolean billingLecture(String select) throws IOException {
        boolean check = false;
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(rowIndex);
        
        String[] studentName = {};
        for (int rowIndex = nextRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell lectureNumCell = row.getCell(lectureNumIndex);
                Cell studentNameCell = row.getCell(studentNamesIndex);
                
                if (lectureNumCell != null && lectureNumCell.toString().equals("0")) {
                    lectureNumCell.setCellValue(plusCount);
                }
                if (studentNameCell != null) {
                    studentName = studentNameCell.toString().split(",");
                }
            }
        }
        
        file.close(); // 기존 파일 스트림 닫기
        FileOutputStream fileOut = new FileOutputStream(lectureFilePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close(); 
        
        FileInputStream studentFile = new FileInputStream(studentFilePath);
        Workbook studentWorkbook = new XSSFWorkbook(studentFile);
        Sheet studentSheet = studentWorkbook.getSheetAt(rowIndex);
        
        for (Row row : studentSheet) {
            Cell studentNameCell = row.getCell(studentNameIndex);
            if (studentNameCell != null ) {
                String cellValue = studentNameCell.getStringCellValue();
                if (Arrays.asList(studentName).contains(cellValue)) {
                    Cell lectureCell = row.getCell(studentLectureIndex);
                    String[] lectureList = lectureCell.toString().split(",");
                    
                    if (Arrays.asList(lectureList).contains(select)) {
                        Cell confirmLectureCell = row.getCell(confirmLectureIndex);
                        Cell billingCell = row.getCell(billingIndex);
                        
                        if (confirmLectureCell != null) {
                            String updateValue = confirmLectureCell.toString() + "," + select;
                            
                            confirmLectureCell.setCellValue(updateValue);
                        } else {
                            confirmLectureCell = row.createCell(confirmLectureIndex);
                            confirmLectureCell.setCellValue(select);
                        }
                        
                        if (billingCell != null) {
                            int updateValue = (int)billingCell.getNumericCellValue();
                            updateValue += lectureFee;
                            
                            billingCell.setCellValue(updateValue);
                        } else {
                            billingCell = row.createCell(billingIndex);
                            billingCell.setCellValue(lectureFee);
                        }
                        check = true;
                    }
                }
            }
        }
        studentFile.close(); // 기존 파일 스트림 닫기
        FileOutputStream studentFileOut = new FileOutputStream(studentFilePath);
        studentWorkbook.write(studentFileOut);
        studentFileOut.close();
        studentWorkbook.close(); 
        return check;
    }

    

}
