package project.student;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentExcelHandler {
    private static final String FILE_PATH = "Student_data.xlsx";
    private static final Logger logger = Logger.getLogger(StudentExcelHandler.class.getName());

    public void registerStudent(String studentNumber, String studentName, String department, String ssn) {
        modifySheet(sheet -> {
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

    private boolean modifySheet(SheetOperation operation) {
        return Boolean.TRUE.equals(executeWorkbookOperation(workbook -> {
            Sheet sheet = workbook.getSheetAt(0);
            operation.execute(sheet);
            saveWorkbook(workbook);
            return true;
        }));
    }

    private <T> T executeWorkbookOperation(WorkbookOperation<T> operation) {
        try (FileInputStream fileIn = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fileIn)) {

            return operation.execute(workbook);

        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "파일을 찾을 수 없습니다: " + FILE_PATH, e);
            return null;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "파일 접근 중 I/O 오류 발생: " + FILE_PATH, e);
            return null;
        }
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
        if (rowToRemove == null) {
            throw new IllegalArgumentException("삭제할 행은 null일 수 없습니다.");
        }

        int rowIndex = rowToRemove.getRowNum();
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1); // 아래 행을 위로 이동
            logger.info("행 삭제 완료. 삭제된 행 인덱스: " + rowIndex);
        } else if (rowIndex == lastRowNum) {
            sheet.removeRow(rowToRemove); // 마지막 행 삭제
            logger.info("마지막 행 삭제 완료. 삭제된 행 인덱스: " + rowIndex);
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }

    private String rowToString(Row row) {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : row) {
            sb.append(cell.toString()).append(" ");
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
