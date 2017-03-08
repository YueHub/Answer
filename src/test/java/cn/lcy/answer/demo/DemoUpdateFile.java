package cn.lcy.answer.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class DemoUpdateFile {
 
    public static void main(String[] args) {
        int count=0;
        String s = null;
         
        try {
            FileReader fr = new FileReader("G:/GraduationProject/HanLP/DataAndModel/data/dictionary/custom/Answer_Dict.txt");//源文件
            BufferedReader in = new BufferedReader(fr);
            FileWriter fw = new FileWriter("G:/GraduationProject/HanLP/DataAndModel/data/dictionary/custom/Answer_Dict.txt.temp");//缓存文件
            PrintWriter pw = new PrintWriter(fw);
            while((s=in.readLine())!=null){
                count++;
                if(count==5){
                    s="anc";
                    pw.println(s);
                    pw.flush();
                }else{
                    pw.println(s);
                    pw.flush();
                }
            }
            pw.close();
            fw.close();
            in.close();
            fr.close();
            FileReader f = new FileReader("G:/GraduationProject/HanLP/DataAndModel/data/dictionary/custom/Answer_Dict.txt.temp");
            BufferedReader i = new BufferedReader(f);
            FileWriter w = new FileWriter("G:/GraduationProject/HanLP/DataAndModel/data/dictionary/custom/Answer_Dict.txt");
            PrintWriter p = new PrintWriter(w);
            while((s=i.readLine())!=null){
                p.println(s);
                p.flush();
            }
            p.close();
            w.close();
            i.close();
            f.close();
            new File("G:/GraduationProject/HanLP/DataAndModel/data/dictionary/custom/Answer_Dict.txt.temp").delete();
            System.out.println("结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
}