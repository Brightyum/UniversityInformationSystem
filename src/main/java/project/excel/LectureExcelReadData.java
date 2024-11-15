/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author user
 */

public class LectureExcelReadData {
    private int count;
    private String lectureFilePath;
    
    public LectureExcelReadData(){
        lectureFilePath = "LectureStaff_data.xlsx";
        count = 0;
    }
    
    public CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> readLectureStaffExcel() throws IOException {
        CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data = new CopyOnWriteArrayList<>();
        FileInputStream fileIn = new FileInputStream(new File(lectureFilePath));
        Workbook workbook = new XSSFWorkbook(fileIn);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            CopyOnWriteArrayList<Object> rowData = new CopyOnWriteArrayList<>();
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.add(cell.getStringCellValue());
                        break;
                    default:
                        rowData.add("");
                        break;
                    
                }
            }
            data.add(rowData);
        }
        return data;
    }
    
    public CopyOnWriteArrayList<String> getColumn(
            CopyOnWriteArrayList<CopyOnWriteArrayList<Object>> data) {
        CopyOnWriteArrayList<String> column = new CopyOnWriteArrayList<>();
        if(!data.isEmpty()) {
            CopyOnWriteArrayList<Object> firstRow = data.get(count);
            for(int i = 0; i < firstRow.size(); i++) {
                column.add("" + (i + 1));
            }
        }
        return column;
    }
    public static void main(String[] args) {
        int s =0;
    }
}
