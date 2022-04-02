package utils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;

public class Excelutils {
    static XSSFWorkbook workbook;
    static XSSFSheet sheet;

    public Excelutils(String excelPath,String sheetName)
    {
        try {
             workbook=new XSSFWorkbook(excelPath);
             sheet=workbook.getSheet(sheetName);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }

    }


    public static String getCellData(int rowNum,int colNum)
    {

        //DataFormatter formatter=new DataFormatter();
        XSSFCell cell = sheet.getRow(rowNum).getCell(colNum);
        DataFormatter formatter = new DataFormatter();
        System.out.println(formatter.formatCellValue(cell));
        return formatter.formatCellValue(cell);
        //Object value=formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));
        //System.out.println(value);
        //return (value);
    }
    public static  int getRowCount()
    {
            int rowCount=sheet.getPhysicalNumberOfRows();
            return rowCount;
    }



}
