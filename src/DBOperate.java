

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
		// 私有的构造方法，为下面实现单利模式做准备
	}
	public static DBOperate getInstance() {
		if (operate == null) {
			operate = new DBOperate();
		}
		return operate;
	}
	public String executeQuery_LuckyPoint(double lon0,double lat0)throws Exception
	{
		double x=0.001141;      //经度(东西方向100米实际度)
		double y=0.000899;      //纬度(南北方向100米实际度)
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
			con = ConnectionFactory.getConnection();  //jj:执行一条sql语句时，先连接数据库
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);  //jj:真正的执行一条sql语句，返回一条记录，封装在rs中
		} catch (SQLException e) {
			throw e;
		}
		while (rs!=null&&rs.next()) {
			//将当前查询到的相册存放到集合中
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
        double Lat1 = rad(latitude1); // 纬度
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;//两点纬度之差
        double b = rad(longitude1) - rad(longitude2); //经度之差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//计算两点距离的公式
        s = s * 6378137.0;//弧长乘地球半径（半径为米）
        s = Math.round(s * 10000d) / 10000d;//精确距离的数值
        return s;
 }
	private double rad(double d) {
        return d * Math.PI / 180.00; //角度转换成弧度
 }
}
