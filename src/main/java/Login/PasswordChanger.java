package Login;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.regex.Pattern;

public class PasswordChanger {

    // 비밀번호 변경 함수
    public static boolean changePassword(String id, String oldPassword, String newPassword) {
        if (!isValidPassword(newPassword)) {
            JOptionPane.showMessageDialog(null, "비밀번호는 7자리의 영문자와 숫자로만 구성되어야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String fileName;
        int idIndex;
        int ssnIndex;

        // 파일 및 열 인덱스 설정
        if (id.startsWith("S")) {
            fileName = "Student_data.xlsx";
            idIndex = 0;
            ssnIndex = 3;
        } else if (id.startsWith("P")) {
            fileName = "Professor_data.xlsx";
            idIndex = 0;
            ssnIndex = 3;
        } else if (id.startsWith("H")) {
            fileName = "Academic_data.xlsx";
            idIndex = 0;
            ssnIndex = 1;
        } else if (id.startsWith("G")) {
            fileName = "LectureStaff_data.xlsx";
            idIndex = 9;
            ssnIndex = 10;
        } else {
            JOptionPane.showMessageDialog(null, "올바르지 않은 ID 형식입니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 엑셀 파일에서 비밀번호 변경
        try (FileInputStream fis = new FileInputStream(new File(fileName));
            Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell idCell = row.getCell(idIndex);
                Cell ssnCell = row.getCell(ssnIndex);

                if (idCell != null && ssnCell != null) {
                    String storedId = getCellValue(idCell);
                    String storedPwd = getLast7Digits(getCellValue(ssnCell));

                    // ID 및 현재 비밀번호 확인
                    if (storedId.equals(id) && storedPwd.equals(oldPassword)) {
                        // 비밀번호 변경
                        if (ssnCell.getCellType() == CellType.NUMERIC) {
                            ssnCell.setCellValue(Long.parseLong(storedPwd.substring(0, storedPwd.length() - 7) + newPassword));
                        } else if (ssnCell.getCellType() == CellType.STRING) {
                            String oldSsn = ssnCell.getStringCellValue().trim();
                            ssnCell.setCellValue(oldSsn.substring(0, oldSsn.length() - 7) + newPassword);
                        }

                        // 엑셀 파일 저장
                        try (FileOutputStream fos = new FileOutputStream(new File(fileName))) {
                            workbook.write(fos);
                        }
                        JOptionPane.showMessageDialog(null, "비밀번호가 성공적으로 변경되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "ID 또는 현재 비밀번호가 올바르지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, fileName + " 파일을 처리하는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // 비밀번호 유효성 검사
    private static boolean isValidPassword(String password) {
        return password.length() == 7 && Pattern.matches("^[a-zA-Z0-9]+$", password);
    }

    // 셀 값 가져오기
    private static String getCellValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }
        return "";
    }

    // 주민등록번호 뒤 7자리 가져오기
    private static String getLast7Digits(String fullSsn) {
        if (fullSsn.length() >= 7) {
            return fullSsn.substring(fullSsn.length() - 7);
        }
        return "";
    }
}

