package jdragon.club;

import Tool.RegexText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RamdomLoad {
	public static String getRamdomWenben(){
			String str = getUrlConStr();//�����վstr
			String regex = "<p>(.*?)</p>";//����ƥ���<p>��</p>֮��
			Pattern pattern = Pattern.compile(regex);//ƥ��ģʽ
			Matcher m = pattern.matcher(str);//�ж��Ƿ����ƥ��
			String str1 = "";
			while(m.find()){
				int i =1;
				str1+=m.group(i);
				i++;
			}
//			System.out.println(str1);
			return RegexText.instead(RegexText.qukong(str1));
	}
	static String getUrlConStr(){
		try{
			URL url = new URL("https://meiriyiwen.com/random");
			URLConnection urlcon = url.openConnection(); //ģ���������������
//			urlcon.setRequestProperty("User-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
			urlcon.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0");
			InputStreamReader in = new InputStreamReader(urlcon.getInputStream(),"utf-8");
			BufferedReader bufferRead = new BufferedReader(in);
			String str = "";
			String temp = null;
			while((temp = bufferRead.readLine())!=null)
				str+=temp;
//			System.out.println(str);
			return str;
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
}
