package utils;

public class ExcelutilTest {
    public static void main(String[] args) {
        String excelPath="C:\\Users\\vuchander\\Api_testing_maven\\src\\main\\DataFromExcel\\userData.xlsx";
        String sheetName="userData";
        Excelutils excel=new Excelutils(excelPath,sheetName);
        excel.getRowCount();
        excel.getCellData(1,0);
        excel.getCellData(1,1);
        excel.getCellData(1,2);
    }
}
