

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*import java.util.List;
import java.sql.Date;
import java.util.ArrayList;*/



public class DBOperate {

	private static DBOperate operate = null;
	private DBOperate() {
		// ˽�еĹ��췽����Ϊ����ʵ�ֵ���ģʽ��׼��
	}
	public static DBOperate getInstance() {
		if (operate == null) {
			operate = new DBOperate();
		}
		return operate;
	}
	public String executeQuery_LuckyPoint(double lon0,double lat0)throws Exception
	{
		double x=0.001141;      //����(��������100��ʵ�ʶ�)
		double y=0.000899;      //γ��(�ϱ�����100��ʵ�ʶ�)
		double minLon=lon0-2*x;
		double maxLon=lon0+2*x;
		double minLat=lat0-2*y;
		double maxLat=lat0+2*y;
		String sql="select * from taxitrack where X>"+minLon+" and X<"+maxLon+" and Y>"+minLat+" and Y<"+maxLat;
		String CLA="";
		double lon;
		double lat;
		double minDist=Double.MAX_VALUE;
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null;
		try {
			con = ConnectionFactory.getConnection();  //jj:ִ��һ��sql���ʱ�����������ݿ�
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);  //jj:������ִ��һ��sql��䣬����һ����¼����װ��rs��
		} catch (SQLException e) {
			throw e;
		}
		while (rs!=null&&rs.next()) {
			//����ǰ��ѯ��������ŵ�������
			String nowCLA=rs.getString("CLA");
			lon=rs.getDouble("X");
			lat=rs.getDouble("Y");
			double nowDist=this.computeDist(lon0, lat0, lon, lat);
			if(minDist>nowDist)
			{
				minDist=nowDist;
				CLA=nowCLA;
			}
		}
		DBClose.closeQuery(con,stmt,rs);
		return CLA;
	}	
	public double computeDist(double longitude1, double latitude1, double longitude2, double latitude2) {
        double Lat1 = rad(latitude1); // γ��
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;//����γ��֮��
        double b = rad(longitude1) - rad(longitude2); //����֮��
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//�����������Ĺ�ʽ
        s = s * 6378137.0;//�����˵���뾶���뾶Ϊ�ף�
        s = Math.round(s * 10000d) / 10000d;//��ȷ�������ֵ
        return s;
 }
	private double rad(double d) {
        return d * Math.PI / 180.00; //�Ƕ�ת���ɻ���
 }
}
