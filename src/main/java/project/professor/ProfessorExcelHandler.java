package project.professor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfessorExcelHandler {
    private static final String FILE_PATH = "Professor_data.xlsx";
    private static final Logger logger = Logger.getLogger(ProfessorExcelHandler.class.getName());

    public boolean registerProfessor(String professorNumber, String professorName, String department, String ssn) {
        return modifySheet(sheet -> {
            Row row = createNewRow(sheet);
            setCellValues(row, professorNumber, professorName, department, ssn);
            logger.info("교수 등록 완료: " + professorNumber + ", " + professorName);
        });
    }

    public boolean updateProfessor(String professorNumber, String professorName, String department, String ssn) {
        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, professorNumber);
            if (row != null) {
                setCellValues(row, professorNumber, professorName, department, ssn);
                logger.info("교수 정보 업데이트 완료: " + professorNumber + ", " + professorName);
            } else {
                logger.warning("업데이트 대상 교수를 찾을 수 없음: " + professorNumber);
                throw new IllegalStateException("교수를 찾을 수 없습니다.");
            }
        });
    }

    public boolean deleteProfessor(String professorNumber) {
        return modifySheet(sheet -> {
            Row row = findRowByValue(sheet, professorNumber);
            if (row != null) {
                removeRow(sheet, row);
                logger.info("교수 삭제 완료: " + professorNumber);
            } else {
                logger.warning("삭제 대상 교수를 찾을 수 없음: " + professorNumber);
                throw new IllegalStateException("교수를 찾을 수 없습니다.");
            }
        });
    }

    public String searchProfessor(String professorNumber, String professorName) {
        return executeWorkbookOperation(workbook -> {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if ((professorNumber.isEmpty() || getCellValue(row, 0).equals(professorNumber)) &&
                        (professorName.isEmpty() || getCellValue(row, 1).equals(professorName))) {
                    String result = rowToString(row);
                    logger.info("교수 검색 성공: " + result);
                    return result;
                }
            }
            logger.info("교수를 찾을 수 없음: " + professorNumber + ", " + professorName);
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
        } catch (IOException e) {
            logger.log(Level.SEVERE, "파일 접근 중 I/O 오류 발생: " + FILE_PATH, e);
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
            logger.info("행 삭제 완료. 삭제된 행 인덱스: " + rowIndex);
        } else if (rowIndex == lastRowNum) {
            sheet.removeRow(rowToRemove);
            logger.info("마지막 행 삭제 완료. 삭제된 행 인덱스: " + rowIndex);
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
