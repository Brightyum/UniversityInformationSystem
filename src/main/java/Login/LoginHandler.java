package Login;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginHandler {

    // 로그인 검증 및 역할 반환
    public static Map<String, String> checkLogin(String id, String password) {
        Map<String, String> result = new HashMap<>();

        if (id.startsWith("S")) {
            Map<String, Object> loginResult = checkStudentLogin(id, password);
            if ((boolean) loginResult.get("success")) {
                result.put("role", "Student");
                result.put("name", (String) loginResult.get("name")); // 이름 추가
                return result;
            }
        } else if (id.startsWith("P")) {
            Map<String, Object> loginResult = checkProfessorLogin(id, password);
            if ((boolean) loginResult.get("success")) {
                result.put("role", "Professor");
                result.put("name", (String) loginResult.get("name")); // 이름 추가
                return result;
            }
        } else if (id.startsWith("H") && checkAcademicLogin(id, password)) {
            // 기존 String 반환 방식
            result.put("role", "Academic");
            result.put("name", null); // 이름은 반환하지 않음
            return result;
        } else if (id.startsWith("G") && checkLectureStaffLogin(id, password)) {
            // 기존 String 반환 방식
            result.put("role", "LectureStaff");
            result.put("name", null); // 이름은 반환하지 않음
            return result;
        }

        // 로그인 실패 시 null 반환
        JOptionPane.showMessageDialog(null, "로그인 실패! 아이디 또는 비밀번호를 확인하세요.", "실패", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    // 학생 데이터 검증
    private static Map<String, Object> checkStudentLogin(String id, String password) {
        return checkExcelLogin("Student_data.xlsx", id, password, "S", 0, 3, 9, 1);
    }

    // 교수 데이터 검증
    private static Map<String, Object> checkProfessorLogin(String id, String password) {
        return checkExcelLogin("Professor_data.xlsx", id, password, "P", 0, 3, 5, 1);
    }

    // 학사 담당자 데이터 검증
    private static boolean checkAcademicLogin(String id, String password) {
        return checkExcelLogin("Academic_data.xlsx", id, password, "H", 0, 1, 3);
    }

    // 수업 담당자 데이터 검증
    private static boolean checkLectureStaffLogin(String id, String password) {
        return checkExcelLogin("LectureStaff_data.xlsx", id, password, "G", 11, 12, 14);
    }

        private static Map<String, Object> checkExcelLogin(String fileName, String id, String password, String idPrefix, int idIndex, int defaultPasswordIndex, int savedPasswordIndex, int nameIndex) {
        Map<String, Object> result = new HashMap<>(); // 결과를 저장할 Map 생성

        try (FileInputStream fis = new FileInputStream(new File(fileName));
             Workbook workbook = new XSSFWorkbook(fis)) { // 엑셀 파일 열기
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기

            // 시트의 모든 행 반복
            for (Row row : sheet) {
                Cell idCell = row.getCell(idIndex); // 지정된 열에서 ID 가져오기
                Cell defaultPasswordCell = row.getCell(defaultPasswordIndex); // 기본 주민등록번호 파생 비밀번호 열
                Cell savedPasswordCell = savedPasswordIndex >= 0 ? row.getCell(savedPasswordIndex) : null; // 저장된 비밀번호 열
                Cell nameCell = nameIndex >= 0 ? row.getCell(nameIndex) : null; // 이름 데이터 가져오기

                if (idCell != null) {
                    String storedId = "";  // 엑셀에서 읽어온 ID 값
                    String storedPwd = ""; // 기존 파생 비밀번호 값
                    String savedPwd = ""; // 저장된 비밀번호 값
                    String name = ""; // 이름 데이터

                    // ID 처리
                    if (idCell.getCellType() == CellType.NUMERIC) { // ID가 숫자 : 접두사를 추가하여 저장
                        storedId = idPrefix + String.valueOf((long) idCell.getNumericCellValue());
                    } else if (idCell.getCellType() == CellType.STRING) { // ID가 문자열 : 문자열을 읽고 공백을 제거
                        storedId = idCell.getStringCellValue().trim();
                    }

                    // 저장된 비밀번호 처리
                    if (savedPasswordCell != null && savedPasswordCell.getCellType() == CellType.STRING) {
                        savedPwd = savedPasswordCell.getStringCellValue().trim();
                    }

                    // 기본 주민등록번호 비밀번호 처리
                    if (defaultPasswordCell != null) {
                        if (defaultPasswordCell.getCellType() == CellType.NUMERIC) {
                            String fullSsn = String.valueOf((long) defaultPasswordCell.getNumericCellValue());
                            if (fullSsn.length() >= 7) {
                                storedPwd = fullSsn.substring(fullSsn.length() - 7);
                            }
                        } else if (defaultPasswordCell.getCellType() == CellType.STRING) {
                            String fullSsn = defaultPasswordCell.getStringCellValue().trim();
                            if (fullSsn.length() >= 7) {
                                storedPwd = fullSsn.substring(fullSsn.length() - 7);
                            }
                        }
                    }

                    // 이름 데이터 처리
                    if (nameCell != null) {
                        if (nameCell.getCellType() == CellType.STRING) {
                            name = nameCell.getStringCellValue().trim();
                        } else if (nameCell.getCellType() == CellType.NUMERIC) {
                            name = String.valueOf((long) nameCell.getNumericCellValue());
                        }
                    }

                    // 입력된 ID와 비밀번호를 비교
                    if (storedId.equals(id) && (savedPwd.equals(password) || storedPwd.equals(password))) {
                        if (!savedPwd.equals(password)) {
                            // 새 비밀번호 저장
                            saveNewPassword(fileName, row, savedPasswordIndex, password);
                        }

                        // 성공 시 결과에 데이터 저장
                        result.put("success", true);
                        result.put("name", name);
                        return result; // 성공 결과 반환
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

        // 실패 시 반환값 설정
        result.put("success", false);
        return result; // 실패 결과 반환
    }
    
    private static boolean checkExcelLogin(String fileName, String id, String password, String idPrefix, int idIndex, int defaultPasswordIndex, int savedPasswordIndex) {
        try (FileInputStream fis = new FileInputStream(new File(fileName));
             Workbook workbook = new XSSFWorkbook(fis)) { // 엑셀 파일 열기
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기

            // 시트의 모든 행 반복
            for (Row row : sheet) {
                Cell idCell = row.getCell(idIndex); // 지정된 열에서 ID 가져오기
                Cell defaultPasswordCell = row.getCell(defaultPasswordIndex); // 기본 주민등록번호 파생 비밀번호 열
                Cell savedPasswordCell = savedPasswordIndex >= 0 ? row.getCell(savedPasswordIndex) : null; // 저장된 비밀번호 열

                if (idCell != null) {
                    String storedId = "";  // 엑셀에서 읽어온 ID 값
                    String storedPwd = ""; // 기존 파생 비밀번호 값
                    String savedPwd = ""; // 저장된 비밀번호 값

                    // ID 처리
                    if (idCell.getCellType() == CellType.NUMERIC) { // ID가 숫자 : 접두사를 추가하여 저장
                        storedId = idPrefix + String.valueOf((long) idCell.getNumericCellValue());
                    } else if (idCell.getCellType() == CellType.STRING) { // ID가 문자열 : 문자열을 읽고 공백을 제거
                        storedId = idCell.getStringCellValue().trim();
                    }

                    // 저장된 비밀번호 처리
                    if (savedPasswordCell != null && savedPasswordCell.getCellType() == CellType.STRING) {
                        savedPwd = savedPasswordCell.getStringCellValue().trim();
                    }

                    // 기본 주민등록번호 비밀번호 처리
                    if (defaultPasswordCell != null) {
                        if (defaultPasswordCell.getCellType() == CellType.NUMERIC) {
                            String fullSsn = String.valueOf((long) defaultPasswordCell.getNumericCellValue());
                            if (fullSsn.length() >= 7) {
                                storedPwd = fullSsn.substring(fullSsn.length() - 7);
                            }
                        } else if (defaultPasswordCell.getCellType() == CellType.STRING) {
                            String fullSsn = defaultPasswordCell.getStringCellValue().trim();
                            if (fullSsn.length() >= 7) {
                                storedPwd = fullSsn.substring(fullSsn.length() - 7);
                            }
                        }
                    }

                    // 입력된 ID와 비밀번호를 비교
                    if (storedId.equals(id) && (savedPwd.equals(password) || storedPwd.equals(password))) {
                        if (!savedPwd.equals(password)) {
                            // 새 비밀번호 저장
                            saveNewPassword(fileName, row, savedPasswordIndex, password);
                        }
                        return true;
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

    private static void saveNewPassword(String fileName, Row row, int passwordIndex, String newPassword) {
        if (passwordIndex < 0) return;

        try (FileInputStream fis = new FileInputStream(new File(fileName));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row currentRow = sheet.getRow(row.getRowNum());
            Cell passwordCell = currentRow.createCell(passwordIndex, CellType.STRING);
            passwordCell.setCellValue(newPassword);

            try (FileOutputStream fos = new FileOutputStream(new File(fileName))) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "새 비밀번호를 저장하는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
