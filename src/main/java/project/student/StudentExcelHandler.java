package project.student;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentExcelHandler {
    private static final String FILE_PATH = "Student_data.xlsx";
    private static final Logger logger = Logger.getLogger(StudentExcelHandler.class.getName());

    // 열 인덱스 상수 선언
    private static final int COLUMN_STUDENT_NUMBER = 0;       // 학번 (int -> long으로 처리)
    private static final int COLUMN_NAME = 1;                 // 이름 (문자열)
    private static final int COLUMN_DEPARTMENT = 2;           // 학과 (문자열)
    private static final int COLUMN_SSN = 3;                  // 주민번호 (int -> long으로 처리)
    private static final int COLUMN_CREDITS = 4;              // 학점
    private static final int COLUMN_REGISTERED_COURSES = 5;   // 등록 강좌
    private static final int COLUMN_TAKEN_COURSES = 6;        // 수강된 강좌
    private static final int COLUMN_TUITION = 7;              // 수강료
    private static final int COLUMN_SCORES = 8;               // 등록 성적

    public boolean registerStudent(String studentNumberStr, String studentName, String department, String ssnStr) {
        return modifySheet(sheet -> {
            Row row = createNewRow(sheet);
            setCellValues(row, studentNumberStr, studentName, department, ssnStr);
            logger.info("학생 등록 완료: " + studentNumberStr + ", " + studentName);
        });
    }

    public boolean updateStudent(String studentNumberStr, String studentName, String department, String ssnStr) {
        return modifySheet(sheet -> {
            Row row = findRowByStudentNumber(sheet, studentNumberStr);
            if (row != null) {
                setCellValues(row, studentNumberStr, studentName, department, ssnStr);
                logger.info("학생 정보 업데이트 완료: " + studentNumberStr + ", " + studentName);
            } else {
                logger.warning("업데이트 대상 학생을 찾을 수 없음: " + studentNumberStr);
                throw new IllegalStateException("학생을 찾을 수 없습니다.");
            }
        });
    }

    public boolean deleteStudent(String studentNumberStr) {
        return modifySheet(sheet -> {
            Row row = findRowByStudentNumber(sheet, studentNumberStr);
            if (row != null) {
                removeRow(sheet, row);
                logger.info("학생 삭제 완료: " + studentNumberStr);
            } else {
                logger.warning("삭제 대상 학생을 찾을 수 없음: " + studentNumberStr);
                throw new IllegalStateException("학생을 찾을 수 없습니다.");
            }
        });
    }

    public String searchStudent(String studentNumber, String studentName) {
        return executeWorkbookOperation(workbook -> {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                String cellStudentNumber = getCellValue(row, COLUMN_STUDENT_NUMBER);
                String cellStudentName = getCellValue(row, COLUMN_NAME);

                if ((studentNumber.isEmpty() || studentNumber.equals(cellStudentNumber)) &&
                        (studentName.isEmpty() || studentName.equals(cellStudentName))) {
                    String result = rowToString(row);
                    logger.info("학생 검색 성공: " + result);
                    return result;
                }
            }
            logger.info("학생을 찾을 수 없음: " + studentNumber + ", " + studentName);
            return null;
        });
    }

    public boolean addOrUpdateScore(String studentNumber, String courseName, String grade) {
        // 성적 검증 및 변환
        Double numericGrade = convertGradeToNumeric(grade);
        if (numericGrade == null) {
            return false; // 잘못된 성적 입력
        }

        return modifySheet(sheet -> {
            Row row = findRowByStudentNumber(sheet, studentNumber);
            if (row == null) {
                logger.warning("점수를 추가/수정하려는 학생을 찾을 수 없음: " + studentNumber);
                throw new IllegalStateException("학생을 찾을 수 없습니다.");
            }

            Cell scoreCell = row.getCell(COLUMN_SCORES, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            String existingScores = getCellValue(scoreCell);

            // 기존 성적 파싱
            Map<String, Double> scoreMap = parseScores(existingScores);

            // 새로운 성적으로 업데이트
            scoreMap.put(courseName, numericGrade);

            // 다시 문자열로 변환하여 저장
            String updatedScores = scoresToString(scoreMap);
            scoreCell.setCellValue(updatedScores);

            logger.info("점수 입력/수정 완료: " + studentNumber + " - " + courseName + "/" + numericGrade);
        });
    }

    private Double convertGradeToNumeric(String grade) {
        if (grade == null) {
            return null;
        }
        switch (grade.toUpperCase()) {
            case "A":
                return 4.0;
            case "B":
                return 3.0;
            case "C":
                return 2.0;
            case "D":
                return 1.0;
            case "F":
                return 0.0;
            default:
                return null;
        }
    }

    private Map<String, Double> parseScores(String scores) {
        Map<String, Double> scoreMap = new HashMap<>();
        if (scores == null || scores.trim().isEmpty()) {
            return scoreMap;
        }

        String[] entries = scores.split(",");
        for (String entry : entries) {
            String[] parts = entry.trim().split("/");
            if (parts.length == 2) {
                String course = parts[0].trim();
                String gradeStr = parts[1].trim();

                Double numericGrade = null;

                try {
                    double rawNumericGrade = Double.parseDouble(gradeStr);
                    if (isValidNumericGrade(rawNumericGrade)) {
                        numericGrade = rawNumericGrade;
                    } else if (rawNumericGrade >= 0 && rawNumericGrade <= 100) {
                        String letterGrade = convertNumericScoreToLetter(rawNumericGrade);
                        numericGrade = convertGradeToNumeric(letterGrade);
                    } else {
                        numericGrade = null;
                    }
                } catch (NumberFormatException e) {
                    numericGrade = convertGradeToNumeric(gradeStr);
                }

                if (numericGrade != null) {
                    scoreMap.put(course, numericGrade);
                }
            }
        }
        return scoreMap;
    }

    private String convertNumericScoreToLetter(double score) {
        if (score >= 90 && score <= 100) {
            return "A";
        } else if (score >= 80 && score < 90) {
            return "B";
        } else if (score >= 70 && score < 80) {
            return "C";
        } else if (score >= 60 && score < 70) {
            return "D";
        } else if (score >= 0 && score < 60) {
            return "F";
        } else {
            return null;
        }
    }

    private boolean isValidNumericGrade(Double grade) {
        return grade.equals(4.0) || grade.equals(3.0) || grade.equals(2.0)
                || grade.equals(1.0) || grade.equals(0.0);
    }

    private String scoresToString(Map<String, Double> scoreMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : scoreMap.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append("/").append(entry.getValue());
        }
        return sb.toString();
    }

    private boolean modifySheet(SheetOperation operation) {
        Workbook workbook = null;
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                try (FileInputStream fileIn = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fileIn);
                }
            } else {
                workbook = new XSSFWorkbook();
                workbook.createSheet("Sheet1");
            }
            Sheet sheet = workbook.getSheetAt(0);
            operation.execute(sheet);
            saveWorkbook(workbook);
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "파일 접근 중 I/O 오류 발생: " + FILE_PATH, e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "modifySheet 중 오류 발생", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "워크북 닫는 중 오류 발생", e);
                }
            }
        }
        return false;
    }

    private <T> T executeWorkbookOperation(WorkbookOperation<T> operation) {
        Workbook workbook = null;
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                try (FileInputStream fileIn = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fileIn);
                }
                return operation.execute(workbook);
            } else {
                logger.log(Level.WARNING, "파일을 찾을 수 없습니다: " + FILE_PATH);
                return null;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "파일 접근 중 I/O 오류 발생: " + FILE_PATH, e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "워크북 닫는 중 오류 발생", e);
                }
            }
        }
        return null;
    }

    private Row createNewRow(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        return sheet.createRow(lastRowNum + 1);
    }

    private void setCellValues(Row row, String studentNumberStr, String studentName, String department, String ssnStr) {
        long studentNumber = parseLongOrWarn(studentNumberStr, "학생번호");
        long ssn = parseLongOrWarn(ssnStr, "주민번호");

        Cell studentNumberCell = row.createCell(COLUMN_STUDENT_NUMBER, CellType.NUMERIC);
        studentNumberCell.setCellValue(studentNumber);

        Cell nameCell = row.createCell(COLUMN_NAME, CellType.STRING);
        nameCell.setCellValue(studentName);

        Cell deptCell = row.createCell(COLUMN_DEPARTMENT, CellType.STRING);
        deptCell.setCellValue(department);

        Cell ssnCell = row.createCell(COLUMN_SSN, CellType.NUMERIC);
        ssnCell.setCellValue(ssn);
        // 필요한 경우 다른 컬럼도 설정
    }

    private long parseLongOrWarn(String valueStr, String fieldName) {
        try {
            return Long.parseLong(valueStr);
        } catch (NumberFormatException e) {
            logger.warning("경고: " + fieldName + "를 숫자로 변환할 수 없음: " + valueStr + " 기본값 0 사용");
            return 0;
        }
    }

    private Row findRowByStudentNumber(Sheet sheet, String studentNumberStr) {
        // 주어진 studentNumberStr를 숫자로 변환 후 검색
        long studentNumber;
        try {
            studentNumber = Long.parseLong(studentNumberStr);
        } catch (NumberFormatException e) {
            logger.warning("경고: 학생번호를 숫자로 변환할 수 없음: " + studentNumberStr);
            return null;
        }

        for (Row row : sheet) {
            // 비교 시 셀 타입이 NUMERIC이라고 가정
            Cell cell = row.getCell(COLUMN_STUDENT_NUMBER, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell.getCellType() == CellType.NUMERIC) {
                long cellValue = (long)cell.getNumericCellValue();
                if (cellValue == studentNumber) {
                    return row;
                }
            } else {
                // 만약 이전 데이터가 문자열로 저장되어 있다면 이 부분에서 문자열 변환 시도
                String cellValStr = getCellValue(cell);
                try {
                    long cellValNum = Long.parseLong(cellValStr);
                    if (cellValNum == studentNumber) {
                        return row;
                    }
                } catch (NumberFormatException ignore) {
                }
            }
        }
        return null;
    }

    private void removeRow(Sheet sheet, Row rowToRemove) {
        int rowIndex = rowToRemove.getRowNum();
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
            logger.info("행 삭제 완료. 삭제된 행 인덱스: " + rowIndex);
        } else if (rowIndex == lastRowNum) {
            sheet.removeRow(rowToRemove);
            logger.info("마지막 행 삭제 완료. 삭제된 행 인덱스: " + rowIndex);
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return getCellValue(cell);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        // 숫자 셀의 경우 소수점 없애고 정수형으로 표현
                        double numericVal = cell.getNumericCellValue();
                        long longVal = (long) numericVal;
                        return String.valueOf(longVal).trim();
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue()).trim();
                case FORMULA:
                    return cell.getCellFormula().trim();
                case BLANK:
                    return "";
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private String rowToString(Row row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= COLUMN_SCORES; i++) {
            sb.append(getCellValue(row, i)).append(" ");
        }
        return sb.toString().trim();
    }

    private void saveWorkbook(Workbook workbook) {
        try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
            workbook.write(fileOut);
            logger.info("엑셀 파일 저장 완료.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "엑셀 파일 저장 실패: " + FILE_PATH, e);
        }
    }

    @FunctionalInterface
    private interface WorkbookOperation<T> {
        T execute(Workbook workbook) throws IOException;
    }

    @FunctionalInterface
    private interface SheetOperation {
        void execute(Sheet sheet) throws IOException;
    }
}
