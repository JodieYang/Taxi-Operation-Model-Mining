

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class ConnectionFactory {
	/** database driver class name */
	private String driver = "";

	/** database URL associated with the URL */
	private String dbURL = "";

	/** user name of the database */
	private String user = "";

	/** password for the current user */
	private String password = "";

	/** factory instance */
	private static ConnectionFactory factory = null; //jj:factory是静态的，属于类的

	/**
	 * constructor
	 * 
	 * @throws Exception
	 */
	private ConnectionFactory() throws Exception {
		Properties prop = new Properties();
//jj:读jdbc.properties文件，is是一个输入流
		// 方法链
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("jdbc.properties");
		try {
			// 从输入流中的数据加载成键值对
			prop.load(is);  //jj:将所读内容，is，加载到一个Properties对象中
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("加载配置文件出错");
		}
		
		this.driver = (String) prop.get("driver");
		this.dbURL = (String) prop.get("url");
		this.user = (String) prop.get("user");
		this.password = (String) prop.get("password");
	}

	/**
	 * @return database dbURL
	 */
	public String getDbURL() {
		return dbURL;
	}

	/**
	 * @return database driver class name
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @return password of the current user
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the database user
	 */
	public String getUser() {
		return user;
	}
	
	//jj:这也是一个工厂方法
	public static Connection getConnection() {
		Connection conn = null;
		if (factory == null) {   //这些都不能显示构造，只能用工厂方法获得
			try {
				factory = new ConnectionFactory();  //jj：只能有一个连接
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				return null;
			}
		}

		try { //jj:java的反射机制
			Class.forName(factory.getDriver());  //jj:这一步是啥意思？？？
			
			//jj:做了这么多，这一步可能是真正的连接，数据库URL，数据库。这一步是核心！！！
			conn = DriverManager.getConnection(factory.getDbURL(), factory
					.getUser(), factory.getPassword());
		} catch (ClassNotFoundException e) {
			System.out.println(" No class " + factory.getDriver()
					+ " found error");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Failed to get connection :" + e.getMessage());
			e.printStackTrace();
		}

		return conn;
	}
}