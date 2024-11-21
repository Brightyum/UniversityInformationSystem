package project.professor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ProfessorExcelHandler {
    private static final String FILE_PATH = "Professor_data.xlsx";

    public boolean registerProfessor(String professorNumber, String professorName, String department, String ssn) {
        return modifySheet(sheet -> {
            Row row = createNewRow(sheet);
            setCellValues(row, professorNumber, professorName, department, ssn);
        });
    }

    public boolean updateProfessor(String professorNumber, String professorName, String department, String ssn) {
        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, professorNumber);
            if (row != null) {
                setCellValues(row, professorNumber, professorName, department, ssn);
            } else {
                throw new IllegalStateException("교수 정보를 찾을 수 없습니다.");
            }
        });
    }

    public boolean deleteProfessor(String professorNumber) {
        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, professorNumber);
            if (row != null) {
                removeRow(sheet, row);
            } else {
                throw new IllegalStateException("교수 정보를 찾을 수 없습니다.");
            }
        });
    }

    public String searchProfessor(String professorNumber, String professorName) {
        return executeWorkbookOperation(workbook -> {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if ((professorNumber.isEmpty() || getCellValue(row, 0).equals(professorNumber)) &&
                        (professorName.isEmpty() || getCellValue(row, 1).equals(professorName))) {
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

    private Row findRowByValue(Sheet sheet, String value) {
        for (Row row : sheet) {
            if (getCellValue(row, 0).equals(value)) {
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
