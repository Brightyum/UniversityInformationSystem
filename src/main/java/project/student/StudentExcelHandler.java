package project.student;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentExcelHandler {
    private static final String FILE_PATH = "Student_data.xlsx";
    private static final Logger logger = Logger.getLogger(StudentExcelHandler.class.getName());

    public boolean registerStudent(String studentNumber, String studentName, String department, String ssn) {
        return modifySheet(sheet -> {
            Row row = createNewRow(sheet);
            setCellValues(row, studentNumber, studentName, department, ssn);
            logger.info("학생 등록 완료: " + studentNumber + ", " + studentName);
        });
    }

    public boolean updateStudent(String studentNumber, String studentName, String department, String ssn) {
        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, studentNumber);
            if (row != null) {
                setCellValues(row, studentNumber, studentName, department, ssn);
                logger.info("학생 정보 업데이트 완료: " + studentNumber + ", " + studentName);
            } else {
                logger.warning("업데이트 대상 학생을 찾을 수 없음: " + studentNumber);
                throw new IllegalStateException("학생을 찾을 수 없습니다.");
            }
        });
    }

    public boolean deleteStudent(String studentNumber) {
        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, studentNumber);
            if (row != null) {
                removeRow(sheet, row);
                logger.info("학생 삭제 완료: " + studentNumber);
            } else {
                logger.warning("삭제 대상 학생을 찾을 수 없음: " + studentNumber);
                throw new IllegalStateException("학생을 찾을 수 없습니다.");
            }
        });
    }

    public String searchStudent(String studentNumber, String studentName) {
        return executeWorkbookOperation(workbook -> {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if ((studentNumber.isEmpty() || getCellValue(row, 0).equals(studentNumber)) &&
                        (studentName.isEmpty() || getCellValue(row, 1).equals(studentName))) {
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
        // 성적 변환 및 검증
        Double numericGrade = convertGradeToNumeric(grade);
        if (numericGrade == null) {
            return false; // 잘못된 성적 입력
        }

        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, studentNumber);
            if (row == null) {
                logger.warning("점수를 추가/수정하려는 학생을 찾을 수 없음: " + studentNumber);
                throw new IllegalStateException("학생을 찾을 수 없습니다.");
            }

            Cell scoreCell = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // 인덱스 8
            if (scoreCell.getCellType() != CellType.STRING) {
                scoreCell.setCellType(CellType.STRING);
            }

            String existingScores = getCellValue(scoreCell);

            String newEntry = courseName + "/" + numericGrade;

            if (existingScores.isEmpty()) {
                scoreCell.setCellValue(newEntry);
            } else {
                // 기존 코스가 있는지 확인하고 업데이트 또는 추가
                String[] entries = existingScores.split(",");
                boolean courseFound = false;
                for (int i = 0; i < entries.length; i++) {
                    String entry = entries[i].trim();
                    if (entry.startsWith(courseName + "/")) {
                        entries[i] = newEntry; // 점수 업데이트
                        courseFound = true;
                        break;
                    }
                }
                if (!courseFound) {
                    existingScores += "," + newEntry;
                    scoreCell.setCellValue(existingScores);
                } else {
                    scoreCell.setCellValue(String.join(",", entries));
                }
            }
            logger.info("점수 입력/수정 완료: " + studentNumber + " - " + newEntry);
        });
    }

    // 성적 변환 메서드 추가
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
                T result = operation.execute(workbook);
                return result;
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
        return sheet.createRow(sheet.getLastRowNum() + 1);
    }

    private void setCellValues(Row row, String... values) {
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(values[i]);
        }
    }

    private Row findRowByValue(Sheet sheet, String value) {
        for (Row row : sheet) {
            if (value.equals(getCellValue(row, 0))) {
                return row;
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

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                case BLANK:
                    return "";
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return getCellValue(cell);
    }

    private String rowToString(Row row) {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : row) {
            sb.append(getCellValue(cell)).append(" ");
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
