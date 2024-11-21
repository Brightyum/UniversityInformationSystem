package project.student;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class StudentExcelHandler {
    private static final String FILE_PATH = "Student_data.xlsx";

    public void registerStudent(String studentNumber, String studentName, String department, String ssn) {
        modifySheet(sheet -> {
            Row row = createNewRow(sheet);
            setCellValues(row, studentNumber, studentName, department, ssn);
        });
    }

    public boolean updateStudent(String studentNumber, String studentName, String department, String ssn) {
        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, 0, studentNumber);
            if (row != null) {
                setCellValues(row, studentNumber, studentName, department, ssn);
            } else {
                throw new IllegalStateException("학생 정보를 찾을 수 없습니다.");
            }
        });
    }

    public boolean deleteStudent(String studentNumber) {
        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, 0, studentNumber);
            if (row != null) {
                removeRow(sheet, row);
            } else {
                throw new IllegalStateException("학생 정보를 찾을 수 없습니다.");
            }
        });
    }

    public String searchStudent(String studentNumber, String studentName) {
        return executeWorkbookOperation(workbook -> {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if ((studentNumber.isEmpty() || getCellValue(row, 0).equals(studentNumber)) &&
                        (studentName.isEmpty() || getCellValue(row, 1).equals(studentName))) {
                    return rowToString(row);
                }
            }
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
        try (FileInputStream fileIn = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fileIn)) {

            return operation.execute(workbook);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Row createNewRow(Sheet sheet) {
        return sheet.createRow(sheet.getLastRowNum() + 1);
    }

    private void setCellValues(Row row, String... values) {
        for (int i = 0; i < values.length; i++) {
            row.createCell(i).setCellValue(values[i]);
        }
    }

    private Row findRowByValue(Sheet sheet, int cellIndex, String value) {
        for (Row row : sheet) {
            if (getCellValue(row, cellIndex).equals(value)) {
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
        } else if (rowIndex == lastRowNum) {
            sheet.removeRow(rowToRemove);
        }
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell != null ? cell.getStringCellValue() : "";
    }

    private String rowToString(Row row) {
        StringBuilder sb = new StringBuilder();
        for (Cell cell : row) {
            sb.append(cell.toString()).append(" ");
        }
        return sb.toString().trim();
    }

    private void saveWorkbook(Workbook workbook) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(new File(FILE_PATH))) {
            workbook.write(fileOut);
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
