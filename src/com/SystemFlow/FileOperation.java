package com.SystemFlow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class FileOperation {

	public static boolean createFile(File fileName) throws Exception {
		boolean flag = false;
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 读TXT文件内容
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readTxtFile(File fileName) throws Exception {
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read = null;
				while ((read = bufferedReader.readLine()) != null) {
					result = result + read + "\r\n";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
		System.out.println("读取出来的文件内容是：" + "\r\n" + result);
		return result;
	}

	public static boolean writeTxtFile(String content, File fileName)
			throws Exception {
		RandomAccessFile mm = null;
		boolean flag = false;
		FileOutputStream o = null;
		try {
			o = new FileOutputStream(fileName);
			o.write(content.getBytes("UTF-8"));
			o.write("\r\n".getBytes());
			o.close();
			// mm=new RandomAccessFile(fileName,"rw");
			// mm.writeBytes(content);
			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
		return flag;
	}

	public static void contentToTxt(String filePath, String content) {
		String str = new String(); // 原有txt内容
		String s1 = new String();// 内容更新
		try {
			File f = new File(filePath);
			if (f.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				f.createNewFile();// 不存在则创建
			}
			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((str = input.readLine()) != null) {
				s1 += str + "\n";
			}
			System.out.println(s1);
			input.close();
			s1 += content;

			BufferedWriter output = new BufferedWriter(new FileWriter(f));
			output.write(s1);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	public static ArrayList<String> readFileByLine(File filepath){
		
		ArrayList<String> content=new ArrayList<>();
		try {
			//方法1
			
			FileInputStream file = new FileInputStream(filepath);
			InputStreamReader inputFileReader = new InputStreamReader(file,
					"utf-8");

			BufferedReader reader = new BufferedReader(inputFileReader);
			String tempString;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				content.add(tempString);
//				System.out.println(tempString);
			}
			//方法2
//			 FileInputStream out = new FileInputStream(file);  
//	         InputStreamReader isr = new InputStreamReader(out);  
//	            int ch = 0;  
//	            while ((ch = isr.read()) != -1) {  
//	                System.out.print((char) ch);  
//	            }  
	        //方法3
//			FileInputStream fis = new FileInputStream(file);
//			
//			 
//			int line;
//			while ((line = fis.read()) != -1) {
//				//osw.write("write something");
//				System.out.print((char)line);
//			}
			//方法4
//			BufferedReader reader=new BufferedReader(new FileReader(file));
//			String tmpstring;
//			while((tmpstring=reader.readLine())!=null){
//				String tmp=new String(tmpstring.getBytes(), "UTF-8");
//				System.out.println(tmp);
//				content.add(tmp); 
//			}
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;	
	}
	public static void main(String[] args){
		File tmp=new File("D:/luceneSpace/file/1.txt");
		FileOperation.readFileByLine(tmp);
		
	}
	
		
		
}


