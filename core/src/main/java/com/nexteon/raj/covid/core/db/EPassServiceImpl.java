package com.nexteon.raj.covid.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.commons.datasource.poolservice.DataSourcePool;
import com.nexteon.raj.covid.core.entity.EPass;
import com.nexteon.raj.covid.core.util.ResourceUtility;

@Component(service = EPassService.class, immediate = true)
public class EPassServiceImpl implements EPassService {

	@Reference
	private DataSourcePool source;

	@Override
	public boolean saveEPassData(EPass epass, String table) {
		Connection connection = null;
		try{
			connection = getConnection();
			String saveQuery = "insert into " + table + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(saveQuery);
			ResourceUtility.setValuesInPS(pstmt,epass);
			int updated = pstmt.executeUpdate();
			if(updated > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	

	@Override
	public EPass getEPassData(long id, String table) {
		Connection connection = null;
		try{
			connection = getConnection();
			String saveQuery = "select * from " + table + " where id=?";
			PreparedStatement pstmt = connection.prepareStatement(saveQuery);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			EPass epass = ResourceUtility.setEPassFromResultSet(rs);
			if(epass!=null) {
				return epass;
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private Connection getConnection() {
		DataSource dataSource = null;
		Connection con = null;
		try {
			dataSource = (DataSource) source.getDataSource("COVID");
			con = dataSource.getConnection();
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
