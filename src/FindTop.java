import java.util.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
public class FindTop {

	//filePath:"D:\\mystudy\\oneday.txt"
	//all:all
	//topNum:7000
	public static Set<DriverEntry> findTop(String filePath,int all,int topNum) {
		// TODO Auto-generated method stub
		Set<DriverEntry>topdrivers=new HashSet<>();
		BufferedReader br=null;
		try {
			int count=1;   //��1��
			br=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String str=br.readLine();
			String[] strArray=null;
			String[] lastCar=null;
			if(str!=null)
			{
				strArray=str.split(",");
				lastCar=strArray;
			}
			//��ʱallû�����ã���Ϊ���꣬����topNum�����õ�
			while(str!=null)
			{
				DriverEntry driver=new DriverEntry();
				driver.setCarNumber(lastCar[0]);
				boolean up=false;
				double upLon=0;
				double upLat=0;
				while(lastCar[0].equals(strArray[0]))
				{
				//	System.out.println("��while��");
					if(strArray[1].equals("1")&&strArray[2].equals("1")&&strArray[8].equals("1"))
					{
						up=true;
						upLon=Double.parseDouble(strArray[4]);
						upLat=Double.parseDouble(strArray[5]);
					}
					else if(up==true&&strArray[1].equals("0")&&strArray[2].equals("0")&&strArray[8].equals("1"))
					{
				//		System.out.println("��else if��");
						List<List<Double>>upPoints=driver.getUpPoints();
						List<List<Double>>downPoints=driver.getDownPoints();
						List<Double> upPoint=new ArrayList<>();
						List<Double> downPoint=new ArrayList<>();
						upPoint.add(upLon);
						upPoint.add(upLat);
						upPoints.add(upPoint);
						downPoint.add(Double.parseDouble(strArray[4]));
						downPoint.add(Double.parseDouble(strArray[5]));
						downPoints.add(downPoint);
						double dist=computeDist(upLon,upLat,Double.parseDouble(strArray[4]),Double.parseDouble(strArray[5]));
						driver.setNetIncome(driver.getNetIncome()+computeMoney(dist));
						up=false;
					}
					else;
					
					str=br.readLine();
					if(str!=null)
						strArray=str.split(",");
					else break;
				}
				if(str!=null)
				{
					lastCar=strArray;
					count++;
				}
				if(driver.getNetIncome()>0)
				{	
				//	System.out.println("�������0");
					if(topdrivers.size()<topNum)
					{
						topdrivers.add(driver);
					}
					else 
					{	TreeSet<DriverEntry>t=new TreeSet<>(topdrivers);
						List<DriverEntry> temp=new ArrayList<>(t);
						//�Ե�����˾�����з���
						if(driver.getNetIncome()<temp.get(0).getNetIncome())
						{
							DriverEntry d=temp.get(0);
							topdrivers.remove(d);
							topdrivers.add(driver);
						}
					 	/*
						//�Ը�����˾�����з��������޸ģ�����
						if(driver.getNetIncome()>temp.get(temp.size()-1).getNetIncome())
						{
							DriverEntry d=temp.get(temp.size()-1);
							topdrivers.remove(d);
							topdrivers.add(driver);
						}
						*/
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return topdrivers;
	}
	
	private static double computeDist(double lon1,double lat1,double lon2,double lat2) {
    	double EARTH_RADIUS=6378137;    //��
        // ��γ�ȣ��Ƕȣ�ת���ȡ����������������Ե���Math.cos��Math.sin
        double radiansAX = Math.toRadians(lon1); // A������
        double radiansAY = Math.toRadians(lat1); // Aγ����
        double radiansBX = Math.toRadians(lon2); // B������
        double radiansBY = Math.toRadians(lat2); // Bγ����
        // ��ʽ�С�cos��1cos��2cos����1-��2��+sin��1sin��2���Ĳ��֣��õ���AOB��cosֵ
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
        double acos = Math.acos(cos); // ������ֵ
        return EARTH_RADIUS * acos; // ���ս��
    }
	private static double computeMoney(double dist)
	{
		double money=0;
		if(dist<3000)
		{
			money+=10;
			return money;
		}
		else {
			money+=10;
			if(dist<15000)
				money+=((dist-3000)/1000)*2;
			else 
			{
				money+=(12*2+((dist-15000)/1000)*3);
			}
		}
		return money;
	}
}
