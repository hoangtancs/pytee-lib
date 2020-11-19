package vn.pytee.connection.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import com.mysql.cj.jdbc.MysqlDataSource;

public class ConnectionPool {
	private static List<Properties> CONFIGs;

	private static int POOL_SIZE_DEFAULT = 1;
	private static int POOL_REFRESH_TIME = 5;

	private static Map<String, BasePool<Connection>> POOLs;
	private static Map<Connection, String> BORROWSER_CONN;

	static {
		CONFIGs = new ArrayList<Properties>();
		POOLs = new ConcurrentHashMap<String, BasePool<Connection>>();
		BORROWSER_CONN = new ConcurrentHashMap<Connection, String>();

		try {
			File folder = new File("resources/");
			if (!folder.exists()) {
				folder.mkdir();
			}

			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				if (!file.isFile()) {
					continue;
				}

				// load for properties file
				if (getExtension(file).equalsIgnoreCase("properties")) {
					FileInputStream stream = new FileInputStream(file);
					Properties properties = new Properties();
					properties.load(stream);
					CONFIGs.add(properties);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initConnection() throws SQLException {
		for (Properties properties : CONFIGs) {
			verify(properties);
		}
	}

	private static boolean verify(Properties properties) throws SQLException {
		boolean passed = true;
		MysqlDataSource dataSource = new MysqlDataSource();

		if (properties.getProperty("mysql.host") == null) {
			passed = false;
		} else {
			dataSource.setServerName(properties.getProperty("mysql.host"));
		}

		if (properties.getProperty("mysql.port") == null) {
			passed = false;
		} else {
			dataSource.setPortNumber(Integer.parseInt(properties.getProperty("mysql.port")));
		}

		if (properties.getProperty("mysql.database") == null) {
			passed = false;
		} else {
			dataSource.setDatabaseName(properties.getProperty("mysql.database"));
		}

		// check username and password
		if (properties.getProperty("mysql.password") == null) {
			passed = false;
		} else {
			dataSource.setPassword(properties.getProperty("mysql.password"));
		}

		if (properties.getProperty("mysql.username") == null) {
			passed = false;
		} else {
			dataSource.setUser(properties.getProperty("mysql.username"));
		}

		dataSource.setCharacterEncoding(properties.getProperty("mysql.character_encoding", "utf-8"));
		dataSource.setAutoReconnectForPools(Boolean.getBoolean(properties.getProperty("mysql.auto_reconnect", "true")));

		boolean useUnicode = Boolean.getBoolean(properties.getProperty("mysql.unicode", "true"));
		if (useUnicode) {
			dataSource.setCharacterEncoding("utf-8");
		}

		dataSource.setCharacterSetResults(properties.getProperty("mysql.character_set_results", "utf-8"));

		Connection con = dataSource.getConnection();
		con.close();

		int initPoolSize = Integer.parseInt(properties.getProperty("mysql.init_pool", POOL_SIZE_DEFAULT + ""));
		int maxPoolSize = Integer.parseInt(properties.getProperty("mysql.max_pool", POOL_SIZE_DEFAULT + ""));
		int refreshTime = Integer.parseInt(properties.getProperty("mysql.refresh_time", POOL_REFRESH_TIME + ""));

		BasePool<Connection> pool = new Pool(dataSource, initPoolSize, maxPoolSize, refreshTime);
		POOLs.put(properties.getProperty("mysql.database"), pool);

		return passed;
	}

	public static Connection getConnection(String connectionName) {
		if (!POOLs.containsKey(connectionName))
			return null;

		Connection conn = POOLs.get(connectionName).borrowObject();
		if (conn == null)
			return null;

		BORROWSER_CONN.put(conn, connectionName);

		return conn;
	}

	public static void releaseConnection(Connection conn) {
		if (!BORROWSER_CONN.containsKey(conn))
			return;

		String connectionName = BORROWSER_CONN.get(conn);
		if (!POOLs.containsKey(connectionName))
			return;

		BORROWSER_CONN.remove(conn);
		POOLs.get(connectionName).returnObject(conn);
	}

	public static void releaseResource(PreparedStatement ps, ResultSet rs) {
		try {
			if (ps != null) {
				ps.clearBatch();
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {

		}
	}

	public static void clearPool() {
		try {
			for (Map.Entry<String, BasePool<Connection>> entry : POOLs.entrySet()) {
				Iterator<Connection> inter = entry.getValue().iterator();
				while (inter.hasNext()) {
					inter.next().close();
				}

				entry.getValue().shutdown();
			}

			POOLs.clear();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static String getExtension(File file) {
		String filename = file.getPath();
		if (filename == null) {
			return null;
		}
		int extensionPos = filename.lastIndexOf('.');
		int lastUnixPos = filename.lastIndexOf('/');
		int lastWindowsPos = filename.lastIndexOf('\\');
		int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

		int index = lastSeparator > extensionPos ? -1 : extensionPos;
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index + 1);
		}
	}

	public static void main(String[] args) {
		try {
			ConnectionPool.initConnection();

			ConnectionPool.clearPool();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
