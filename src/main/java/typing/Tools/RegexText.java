package typing.Tools;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexText {
	private int gang = 0;
	private int kong = 0;
	private int i,j,k,length,sign1=0,sign2=0;
	private char [] a;
	private String duan;
	public static int duan1;
	public String[] CarryCom(String str){
		String comarti[] = new String[3];
		String title = "";
		a = str.toCharArray();
		length = str.length();
		for(j=length-1;j>0;j--){
			if(a[j]=='第'){
				for(k=j-1;k>=j-5;k--){
					if(a[k]=='-'){gang++;}
				}
				if(gang>=5){
					sign1 = k;
					kong=0;
//					duan = String.copyValueOf(a, j+1,5);
					duan = "";
					while(a[j]!='段'){
						duan = duan+a[j];j++;
					}
				}
				gang=0;
			}
			if(sign1!=0)
				break;

		}
		for(j=sign1;j>0;j--){
			if(a[j]=='\n')
			{
				kong++;
				a[j]='#';
				if(kong>=2){
					sign2  = j;
					while(a[j]!='\n'&&j>0)
						j--;
					if(j>0)
						a[j]='#';
					else
						j=-1;
					for(j=j+1;j<sign2;j++)
						title+=String.valueOf(a[j]);
					kong = 0;
					break;}
			}
		}
		for(i=0;i<sign2;i++)
			a[i]='#';
		for(i=sign1;i<length;i++)
			a[i]='#';
		
		sign2=0;
		sign1=0;
		str = String.valueOf(a);
		String regex = "[^0123456789]+";
		str = str.replaceAll("#","");
//		str = qukong(str);
//		str = huanfu(str);
		duan = duan.replaceAll(regex,"");
		duan1 = Integer.valueOf(duan);
		comarti[0] = title;
		comarti[1] = str;
		comarti[2] = String.valueOf(duan1);
//		System.out.println("标题:"+title+"\n"+str);
		return comarti;
	}
	public static Long getGroupID(String event){
		int begin = event.indexOf("groupId=");
		int end = event.indexOf(",",begin);
		Long GroupId = Long.valueOf(event.substring(begin+8,end));
		return  GroupId;
	}
	public static double[] getGrade(String event){
		double [] Grade = new double[3];
		int SpeedSign = event.indexOf("速度");
		int SpeedEnd = event.indexOf(" ",SpeedSign);
		int temp = event.indexOf("/",SpeedSign);
		if(temp<SpeedEnd&&temp!=-1)SpeedEnd=temp;

		int keylengthSign = event.indexOf("码长");
		int keylengthEnd = event.indexOf(" ",keylengthSign);
		int keySpeedSign = event.indexOf("击键");
		int keySpeedEnd = event.indexOf(" ",keySpeedSign);

		Grade[0] = Double.parseDouble(event.substring(SpeedSign+2,SpeedEnd));

		if(keylengthSign==-1)
			Grade[1] = 0;
		else
			Grade[1] = Double.parseDouble(event.substring(keySpeedSign+2,keySpeedEnd));
		if(keySpeedSign==-1)
			Grade[2] = 0;
		else
			Grade[2] = Double.parseDouble(event.substring(keylengthSign+2,keylengthEnd));
		return Grade;
	}
	public static String AddZero(String str){
		String time[] = str.split("-");
		if(time[1].length()==1){time[1]="0"+time[1];}
		if(time[2].length()==1){time[2]="0"+time[2];}
		return  time[0]+"-"+time[1]+"-"+time[2];
	}
	public static  double FourOutFiveIn(double d){
		return  Double.parseDouble(String.format("%.2f", d));
	}
	public static String qukong(String str){return str.replaceAll("\\s*", "");}
	public static String instead(String str){
		str = str.replaceAll(",","，");
		str = str.replaceAll("\\.","。");
		str = str.replaceAll(";","；");
		str = str.replaceAll(":","：");
		return  str;
	}
	public static Matcher isAt(String str ){
		String regex = "\\[CQ:at,qq=(.*?)\\]";//正则匹配出<p>与</p>之间
		Pattern pattern = Pattern.compile(regex);//匹配模式
		Matcher m = pattern.matcher(str);//判断是否符合匹配
		return  m;
	}
}
