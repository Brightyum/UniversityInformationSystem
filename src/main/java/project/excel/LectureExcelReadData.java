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
            maxCredit, creditIndex, classNumIndex, confirmLectureIndex, nameIndex,
            scoreIndex, departmentIndex;
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
        maxStudentIndex = scoreIndex = 8;
        professorName = confirmLectureIndex = 6;
        maxCredit = 18;
        creditIndex = 4;
        departmentIndex = 2;
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
                
                if (studentCell != null && minStudentCell != null && (classNumCell.toString().equals("0") || (classNumCell.getNumericCellValue() == 0))) {
                    double studentValue = parseCellValue(studentCell);
                    double minStudentValue = parseCellValue(minStudentCell);

                    // 비교 조건
                    if (studentValue >= minStudentValue) {
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
    public CopyOnWriteArrayList<String> getPossibleLecture(String name) throws IOException {
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
                Cell nameCell = row.getCell(nameIndex);
                if (nameCell != null && nameCell.toString().equals(name)) {
                    Cell lectureCell = row.getCell(studentLectureIndex);
                    Cell creditCell = row.getCell(creditIndex);
                    int currentCredit = 0;

                    if (creditCell != null && creditCell.getNumericCellValue() >= maxCredit) {
                        System.out.println(creditCell.toString());
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
                        if (i.equals("")) {
                            continue;
                        }
                        data.add(i);
                    }
                }
            }
        }
        return data;
    }
    
    public Map<String, String> getScore(String name) throws IOException {
        Map<String, String> data = new HashMap<>();
        
        FileInputStream file = new FileInputStream(studentFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);
        
        for (int rowIndex = nextRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell nameCell = row.getCell(nameIndex);
                
                if (nameCell != null && nameCell.toString().equals(name)) {
                    Cell lectureScoreCell = row.getCell(scoreIndex);
                    if (lectureScoreCell != null) {
                        String[] currentValue = lectureScoreCell.toString().split(",");

                        for (String i : currentValue) {
                            String[] map = i.split("/");
                            data.put(map[0], map[1]);
                        }
                    } else {
                        return null;
                    }
                    break;
                }
            }
        }
        return data;
    }
    
    public Map<String, List<String>> getLectureStudent(String name, String select) throws IOException {
        Map<String, List<String>> data = new HashMap<>();
        String[] currentValue = {};

        // 강좌 파일 읽기
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);

        // 강좌에서 교수 이름을 기준으로 학생 명단 추출
        for (Row row : sheet) {
            Cell lectureCell = row.getCell(classIndex);
            if (lectureCell != null && lectureCell.toString().equals(select)) {
                Cell professorCell = row.getCell(professorName);
                if (professorCell != null && professorCell.toString().equals(name)) {
                    Cell studentsCell = row.getCell(studentNameIndex);
                    if (studentsCell != null) {
                        currentValue = studentsCell.toString().split(",");
                    }
                    break;
                }
            }
            
            
        }
        file.close();

        // 학생 파일 읽기
        FileInputStream studentFile = new FileInputStream(studentFilePath);
        Workbook studentWorkbook = new XSSFWorkbook(studentFile);
        Sheet studentSheet = studentWorkbook.getSheetAt(startIndex);

        // 학생 명단과 매칭
        for (int rowIndex = nextRow; rowIndex <= studentSheet.getLastRowNum(); rowIndex++) {
            Row row = studentSheet.getRow(rowIndex);
            if (row != null) {
                Cell studentCell = row.getCell(nameIndex);
                if (studentCell != null) {
                    for (String i : currentValue) {
                        if (studentCell.toString().equals(i)) {
                            Cell departmentCell = row.getCell(departmentIndex);
                            String department = departmentCell.toString();
                            String studentName = studentCell.toString();

                            // 동일 학과의 학생을 리스트로 저장
                            data.computeIfAbsent(department, k -> new ArrayList<>()).add(studentName);
                        }
                    }
                }
            }
        }
        studentFile.close();
        return data;
    }
    
    public Map<String, List<String>> getStudentInformation(String name, String select) throws IOException {
        Map<String, List<String>> data = new HashMap<>();
        String[] currentStudents = {};
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);
        
        for (Row row : sheet) {
            Cell lectureCell = row.getCell(classIndex);
            Cell professorCell = row.getCell(professorName);
            if (lectureCell != null && professorCell != null 
               && lectureCell.toString().equals(select) && professorCell.toString().equals(name)) {
               Cell studentsCell = row.getCell(studentNameIndex);
               if (studentsCell != null) {
                   currentStudents = studentsCell.toString().split(",");
               }
               break;
            }
        }
        file.close();
        
        FileInputStream studentFile = new FileInputStream(studentFilePath);
        Workbook studentWorkbook = new XSSFWorkbook(studentFile);
        Sheet studentSheet = studentWorkbook.getSheetAt(startIndex);
        
        for (int rowIndex = nextRow; rowIndex <= studentSheet.getLastRowNum(); rowIndex++) {
            Row row = studentSheet.getRow(rowIndex);
            if (row != null) {
                Cell nameCell = row.getCell(nameIndex);
                if (nameCell != null) {
                    for (String i : currentStudents) {
                        if (nameCell.toString().equals(i)) {
                            Cell numCell = row.getCell(startIndex);
                            Cell creditCell = row.getCell(creditIndex);
                            
                            if (numCell != null && creditCell != null) {
                                // numCell과 creditCell 값을 리스트로 저장
                                List<String> values = new ArrayList<>();
                                values.add(numCell.toString());
                                values.add(creditCell.toString());

                                // i를 키로 하고 리스트를 값으로 설정
                                data.put(i, values);
                            }
                        }
                    }
                }
            }
        }
        studentFile.close();
        return data;
    }
    
    public CopyOnWriteArrayList<String> lectureName(String name) throws IOException {
        CopyOnWriteArrayList<String> lectureName = new CopyOnWriteArrayList<>();
        
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(startIndex);
        
        for (Row row : sheet) {
            Cell professorCell = row.getCell(professorName);
            if (professorCell != null && professorCell.toString().equals(name)) {
                Cell lectureCell = row.getCell(classIndex);
                lectureName.add(lectureCell.toString());
            }
        }
        return lectureName;
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

