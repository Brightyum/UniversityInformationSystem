package Login;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LoginHandler {

    // 로그인 검증 및 역할 반환
    public static String checkLogin(String id, String password) {
        if (id.startsWith("S") && checkStudentLogin(id, password)) {
            return "Student"; // 학생 역할 반환
        } else if (id.startsWith("P") && checkProfessorLogin(id, password)) {
            return "Professor"; // 교수 역할 반환
        } else if (id.startsWith("H") && checkAcademicLogin(id, password)) {
            return "Academic"; // 학사 담당자 역할 반환
        } else if (id.startsWith("G") && checkLectureStaffLogin(id, password)) {
            return "LectureStaff"; // 수업 담당자 역할 반환
        }

        // 로그인 실패 시 null 반환
        JOptionPane.showMessageDialog(null, "로그인 실패! 아이디 또는 비밀번호를 확인하세요.", "실패", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    // 학생 데이터 검증
    private static boolean checkStudentLogin(String id, String password) {
        return checkExcelLogin("Student_data.xlsx", id, password, "S", 0, 3, 6);
    }

    // 교수 데이터 검증
    private static boolean checkProfessorLogin(String id, String password) {
        return checkExcelLogin("Professor_data.xlsx", id, password, "P", 0, 3, 5);
    }

    // 학사 담당자 데이터 검증
    private static boolean checkAcademicLogin(String id, String password) {
        return checkExcelLogin("Academic_data.xlsx", id, password, "H", 0, 1, 1);
    }

    // 수업 담당자 데이터 검증
    private static boolean checkLectureStaffLogin(String id, String password) {
        return checkExcelLogin("LectureStaff_data.xlsx", id, password, "G", 9, 10, 10);
    }

    private static boolean checkExcelLogin(String fileName, String id, String password, String idPrefix, int idIndex, int oldPasswordIndex, int newPasswordIndex) {
        try (FileInputStream fis = new FileInputStream(new File(fileName));
             Workbook workbook = new XSSFWorkbook(fis)) { // 엑셀 파일 열기
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기

            // 시트의 모든 행 반복
            for (Row row : sheet) {
                Cell idCell = row.getCell(idIndex); // 지정된 열에서 ID 가져오기
                Cell oldPasswordCell = row.getCell(oldPasswordIndex); // 지정된 열에서 기존 비밀번호 가져오기
                Cell newPasswordCell = row.getCell(newPasswordIndex); // 지정된 열에서 새 비밀번호 가져오기

                if (idCell != null) {
                    String storedId = "";  // 엑셀에서 읽어온 ID 값
                    String storedOldPwd = ""; // 기존 비밀번호 값
                    String storedNewPwd = ""; // 새 비밀번호 값

                    // ID 처리
                    if (idCell.getCellType() == CellType.NUMERIC) { // ID가 숫자
                        storedId = idPrefix + String.valueOf((long) idCell.getNumericCellValue());
                    } else if (idCell.getCellType() == CellType.STRING) { // ID가 문자열
                        storedId = idCell.getStringCellValue().trim();
                    }

                    // 기존 비밀번호 처리
                    if (oldPasswordCell != null) {
                        if (oldPasswordCell.getCellType() == CellType.NUMERIC) {
                            String fullOldPwd = String.valueOf((long) oldPasswordCell.getNumericCellValue());
                            if (fullOldPwd.length() >= 7) {
                                storedOldPwd = fullOldPwd.substring(fullOldPwd.length() - 7);
                            }
                        } else if (oldPasswordCell.getCellType() == CellType.STRING) {
                            storedOldPwd = oldPasswordCell.getStringCellValue().trim();
                        }
                    }

                    // 새 비밀번호 처리
                    if (newPasswordCell != null) {
                        if (newPasswordCell.getCellType() == CellType.STRING) {
                            storedNewPwd = newPasswordCell.getStringCellValue().trim();
                        }
                    }

                    // ID와 비밀번호 검증
                    if (storedId.equals(id)) {
                        // 새 비밀번호가 설정된 경우 우선적으로 검증
                        if (!storedNewPwd.isEmpty() && storedNewPwd.equals(password)) {
                            return true;
                        }

                        // 새 비밀번호가 없는 경우 기존 비밀번호로 검증
                        if (storedNewPwd.isEmpty() && storedOldPwd.equals(password)) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // 파일 읽기 오류 처리
            JOptionPane.showMessageDialog(null, fileName + " 파일을 읽는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            // 숫자 변환 오류 처리
            JOptionPane.showMessageDialog(null, fileName + " 데이터에 문제가 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
        return false; // 로그인 실패
    }
}
