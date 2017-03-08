package cn.lcy.answer.io.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.List;

public class FileIOUtil {
	
	/**
	 * 向某一个文件追加内容（单个字符串）
	 * @param filePath
	 * @param appendContent
	 * @return
	 */
	public static boolean appendContent(String filePath, String appendContent) {
		// 随机文件访问对象
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式     
			randomFile = new RandomAccessFile(filePath, "rw");     
	        // 文件长度，字节数
	        long fileLength = randomFile.length();
			// TODO 测试阶段为覆盖写入
			//long fileLength = 0;
	        // 将写文件指针移到文件尾
	        randomFile.seek(fileLength);
	        // 追加内容
        	String content = new String(appendContent.getBytes("UTF-8"),"ISO8859_1");
        	randomFile.writeBytes(content);
	        randomFile.writeBytes("\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(randomFile != null) {
				try {
					randomFile.close();
				} catch(IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 向某一个文件追加内容（字符串列表）
	 * @return
	 */
	public static boolean appendContent(String filePath, List<String> appendContents) {
		// 随机文件访问对象
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式     
			randomFile = new RandomAccessFile(filePath, "rw");     
	        // 文件长度，字节数
	        long fileLength = randomFile.length();
			// TODO 测试阶段为覆盖写入
			//long fileLength = 0;
	        // 将写文件指针移到文件尾
	        randomFile.seek(fileLength);
	        // 追加内容
	        for(String appendContent : appendContents) {
	        	System.out.println("appendContent"+appendContent);
	        	String content = new String(appendContent.getBytes("UTF-8"),"ISO8859_1");
	        	randomFile.writeBytes(content);
	        	
		        randomFile.writeBytes("\r\n");
	        }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(randomFile != null) {
				try {
					randomFile.close();
				} catch(IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	// 修改文件某一行内容
	public static boolean updateContent(String filePath, Long rowNum, String rowUpdate) {
		int count = 0;
        String row = null;
         
        try {
            FileReader fileReader = new FileReader(filePath);//源文件
            BufferedReader in = new BufferedReader(fileReader);
            FileWriter fileWriter = new FileWriter(filePath + ".temp");//缓存文件
            PrintWriter pw = new PrintWriter(fileWriter);
            while((row = in.readLine()) != null){
                count++;
                if(count == rowNum){
                	row = rowUpdate;
                    pw.println(row);
                    pw.flush();
                }else{
                    pw.println(row);
                    pw.flush();
                }
            }
            pw.close();
            fileWriter.close();
            in.close();
            fileReader.close();
            FileReader f = new FileReader(filePath + ".temp");
            BufferedReader i = new BufferedReader(f);
            FileWriter w = new FileWriter(filePath);
            PrintWriter p = new PrintWriter(w);
            while((row = i.readLine()) != null){
                p.println(row);
                p.flush();
            }
            p.close();
            w.close();
            i.close();
            f.close();
            new File(filePath + ".temp").delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
