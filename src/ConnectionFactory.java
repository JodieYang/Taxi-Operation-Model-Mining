

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
	private static ConnectionFactory factory = null; //jj:factory�Ǿ�̬�ģ��������

	/**
	 * constructor
	 * 
	 * @throws Exception
	 */
	private ConnectionFactory() throws Exception {
		Properties prop = new Properties();
//jj:��jdbc.properties�ļ���is��һ��������
		// ������
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("jdbc.properties");
		try {
			// ���������е����ݼ��سɼ�ֵ��
			prop.load(is);  //jj:���������ݣ�is�����ص�һ��Properties������
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("���������ļ�����");
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
	
	//jj:��Ҳ��һ����������
	public static Connection getConnection() {
		Connection conn = null;
		if (factory == null) {   //��Щ��������ʾ���죬ֻ���ù����������
			try {
				factory = new ConnectionFactory();  //jj��ֻ����һ������
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				return null;
			}
		}

		try { //jj:java�ķ������
			Class.forName(factory.getDriver());  //jj:��һ����ɶ��˼������
			
			//jj:������ô�࣬��һ�����������������ӣ����ݿ�URL�����ݿ⡣��һ���Ǻ��ģ�����
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