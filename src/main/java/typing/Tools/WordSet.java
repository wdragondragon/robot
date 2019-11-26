package typing.Tools;


import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static java.io.File.separator;


public class WordSet {
	private static Set<Character> wordSet = new HashSet();
	static {
		String str;
		File more = new File("编码文件"+separator+"单字编码"+separator+"单字字集.txt");
		try {
			Fuhao();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			FileInputStream fis = new FileInputStream(more);
			InputStreamReader read = new InputStreamReader(fis, "UTF-8");
			BufferedReader  bufferRead = new BufferedReader(read);
			while((str=bufferRead.readLine())!=null) {
				String[] splited = str.split("\\s+");
				String ch = splited[0];
				String bm = splited[1];
				wordSet.add(ch.toCharArray()[0]);
			}
			bufferRead.close();
			read.close();
			fis.close();
		}catch(Exception e){
			System.out.println("打开失败2");
			e.printStackTrace();
		}
	}
	public static void Fuhao() throws IOException{
		String str;
		File FuhaoFile = new File("编码文件"+separator+"符号文件"+separator+"符号文件.txt");
		FileInputStream fis = new FileInputStream(FuhaoFile); 
        InputStreamReader read = new InputStreamReader(fis, "UTF-8");
		BufferedReader  bufferRead = new BufferedReader(read);
		while((str=bufferRead.readLine())!=null){
			String[] splited = str.split("\\s+");
			String ch = splited[0];
//			String bm = splited[1];
			wordSet.add(ch.toCharArray()[0]);
		}
		bufferRead.close();
		read.close();
		fis.close();
	}
	public static Set<Character>  get(){
		return wordSet;
	}
}
