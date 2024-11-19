package project.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author duatm
 */
public class ClassLectureReadData {
    private String lectureFilePath;

    public ClassLectureReadData() {
        lectureFilePath = "LectureStaff_data.xlsx";
    }

    public CopyOnWriteArrayList<String> readClassData() throws IOException {
        CopyOnWriteArrayList<String> data = new CopyOnWriteArrayList<>();
        FileInputStream file = new FileInputStream(lectureFilePath);
        Workbook workbook = new XSSFWorkbook(file);

        // 첫 번째 시트 가져오기
        Sheet sheet = workbook.getSheetAt(0); // 0번 시트를 선택

        // 출력할 열의 인덱스 설정 (예: 1번째 열 = index 0)
        int targetColumnIndex = 1;
        int rowIndex = 0;
        // 모든 행 순회
        for (Row row : sheet) {
            if (rowIndex == 0) {
                rowIndex++;
                continue;
            }
            Cell cell = row.getCell(targetColumnIndex);
            if (cell != null) {
                data.add(cell.toString());
            }
        }

        return data;
    }

    public static void main(String[] args) throws IOException {
        ClassLectureReadData reader = new ClassLectureReadData();
        CopyOnWriteArrayList<String> columnData = reader.readClassData();
        System.out.println(columnData);
    }
}
