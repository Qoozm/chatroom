package util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public final class ConnectionManager {
	
	private static ConnectionManager instance;
	private static ComboPooledDataSource dataSource;
	
	private ConnectionManager() {
		
	}
	
	static {
		
		ResourceBundle resourceBundle = ResourceBundle.getBundle("c3p0");
		dataSource = new ComboPooledDataSource();
		
		try {
			dataSource.setDriverClass(resourceBundle.getString("driver"));
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		dataSource.setJdbcUrl(resourceBundle.getString("url"));
		dataSource.setUser(resourceBundle.getString("username"));
		dataSource.setPassword(resourceBundle.getString("password"));
		
	}
	
	public synchronized static final ConnectionManager getInstance() {
		
		if (instance == null) {
			try {
				instance = new ConnectionManager();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	public synchronized final Connection getConnection() {
		
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void close(ResultSet rs, PreparedStatement pStatement, Connection connection) {
		
		try {
			if (rs != null) {
				rs.close();
			}
			if (pStatement != null) {
				pStatement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	protected void finalize() throws Throwable
    {
        // 关闭datasource
        DataSources.destroy(dataSource);
        super.finalize();
    }
	
}
