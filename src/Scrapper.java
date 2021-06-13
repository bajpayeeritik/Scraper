
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.select.Elements;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


class Scraper {
    public Scraper() {
    }
    private static Workbook wb;
    private static Sheet sh;
    private static Row row;
    private static Cell cell;
    private static FileInputStream fis;
    private static FileOutputStream fos;
    private static File xlFile;



    private static ArrayList<ArrayList<String>> crData(String Site){
        Scraper Oj=new Scraper();
        Document doc = null;

        try {
            doc = Jsoup.connect(Site).get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Elements table = doc.getElementsByTag("tbody");
        Elements links = doc.select("a[href]");
        ArrayList<ArrayList<String>> fin=new ArrayList<ArrayList<String>>();
        int i=1;
        for (Element x:links) {
            ArrayList<String> a=new ArrayList<>();
            a.add(String.valueOf(i));
            a.add(x.getElementsByTag("a").eachText().get(0));
            a.add(x.attr("href"));
            a.add("<->");
            i++;
            fin.add(a);
        }

        return fin;
    }
    public static void main (String args[]) throws IOException {
        Scanner sc=new Scanner(System.in);
        String url=sc.next();
        wb=new XSSFWorkbook();
        sh=wb.createSheet("A2OJ");
        String [] colHead={"No.","Name","Link","Done?"};
        row=sh.createRow(0);
        for(int i=0;i<colHead.length;i++){
            cell=row.createCell(i);
            cell.setCellValue(colHead[i]);
        }
        ArrayList<ArrayList<String>> a=crData(url);
        CreationHelper creationHelper=wb.getCreationHelper();
        int rownum =1;
        for(ArrayList i : a) {
            //System.out.println("rownum-before"+(rownum));
            Row row = sh.createRow(rownum++);
            //System.out.println("rownum-after"+(rownum));
            row.createCell(0).setCellValue((String) i.get(0));
            row.createCell(1).setCellValue((String) i.get(1));
            row.createCell(2).setCellValue((String) i.get(2));
            row.createCell(3).setCellValue((String) i.get(3));

        }
        for(int i=0;i<colHead.length;i++) {
            sh.autoSizeColumn(i);
        }
        FileOutputStream f=new FileOutputStream("./A2OJ.xlsx");
        wb.write(f);
        f.close();
        wb.close();
    }

}
