package Login;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PasswordChanger {

    public static boolean changePassword(String role, String idWithoutPrefix, String oldPassword, String newPassword) throws IOException {
        String filePath = RoleConfig.getFilePath(role);
        int idColumn = RoleConfig.getIdColumn(role);
        int passwordColumn = RoleConfig.getPasswordColumn(role);

        if (filePath == null) {
            throw new IllegalArgumentException("잘못된 역할입니다.");
        }

        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        boolean isUpdated = false;

        for (Row row : sheet) {
            Cell idCell = row.getCell(idColumn);
            if (idCell != null && getCellValueAsString(idCell).endsWith(idWithoutPrefix)) { // 접두사 제거된 ID 비교
                Cell passwordCell = row.getCell(passwordColumn);
                if (passwordCell != null && getCellValueAsString(passwordCell).equals(oldPassword)) {
                    // 이전 비밀번호 검증 성공 시 새 비밀번호 설정
                    passwordCell.setCellValue(newPassword);
                    isUpdated = true;
                }
                break;
            }
        }

        fis.close();

        if (isUpdated) {
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
        }

        workbook.close();
        return isUpdated;
    }

    // 셀 데이터를 문자열로 변환하는 유틸리티 메서드
    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // 숫자를 문자열로 변환
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
