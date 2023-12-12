package xhtd_ab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import xhtd_ab.config.ConfigDB;
import xhtd_ab.model.CustomerIdempiere;

public class JDBCIdempiereConnect {
	private static final String DB_IDEMPIERE_URL = ConfigDB.dbIdempiereUrl;
	private static final String DB_IDEMPIERE_USERNAME = ConfigDB.dbIdempiereUserName;
	private static final String DB_IDEMPIERE_PASSWORD = ConfigDB.dbIdempierePassword;

	public static List<CustomerIdempiere> getCustomerProfileFromDBIdempiere() {
		Connection conn = null;
		PreparedStatement stm = null;
		CustomerIdempiere cus;
		List<CustomerIdempiere> customers = new ArrayList<>();
		try {
			System.out.println("getCustomerProfileFromDBIdempiere START.....");
			String sql = "SELECT * FROM TDAB_CUSTOMER";
			conn = DriverManager.getConnection(DB_IDEMPIERE_URL, DB_IDEMPIERE_USERNAME, DB_IDEMPIERE_PASSWORD);
			stm = conn.prepareStatement(sql);
			stm.setQueryTimeout(400);

			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				cus = new CustomerIdempiere();
				cus.setTDAB_CUSTOMER_ID(rs.getInt("TDAB_CUSTOMER_ID"));
				cus.setVALUE(rs.getString("VALUE"));
				cus.setTAXCODE(rs.getString("TAXCODE"));
				cus.setNUMBERBUSINESS(rs.getString("NUMBERBUSINESS"));
				cus.setNAME(rs.getString("NAME"));
				cus.setCOMPANYNAME(rs.getString("COMPANYNAME"));

				customers.add(cus);
			}
			System.out.println("customers.size--" + customers.size());
			System.out.println("getCustomerProfileFromDBIdempiere END....!");
		} catch (SQLException e) {
			System.err.println("getCustomerProfileFromDBIdempiere UPDATE ERROR : ");
			e.printStackTrace();
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (Exception e2) {
				System.err.println("getCustomerProfileFromDBIdempiere CLOSE CONNECTION : ");
				e2.printStackTrace();
			}
		}
		return customers;
	}
	public static List<CustomerIdempiere> getTDABCusT24(String param) {
		Connection conn = null;
		PreparedStatement stm = null;
		CustomerIdempiere cus;
		List<CustomerIdempiere> customers = new ArrayList<>();
		try {
			String parameter = "('".concat(param.toString()).concat("')");
	        String sql = "SELECT * FROM TDAB_CUS_T24 where taxcode =" + parameter;
			 
			conn = DriverManager.getConnection(DB_IDEMPIERE_URL, DB_IDEMPIERE_USERNAME, DB_IDEMPIERE_PASSWORD);
			stm = conn.prepareStatement(sql);
			stm.setQueryTimeout(400);

			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				cus = new CustomerIdempiere();
				cus.setVALUE(rs.getString("VALUE"));
				cus.setTAXCODE(rs.getString("TAXCODE"));
				cus.setNUMBERBUSINESS(rs.getString("NUMBER_BUSINESS"));
				cus.setNAME(rs.getString("NAME"));
				cus.setCOMPANYNAME(rs.getString("COMPANYNAME"));

				customers.add(cus);
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return customers;
	}

	public static void updateTDABCUSTOMER(List<CustomerIdempiere> customers) {
		Connection conn = null;
		Statement stm = null;
		PreparedStatement preparedStatement = null;
		String sql;
		try {
			System.out.println("updateTDABCUSTOMER START.....");
			conn = DriverManager.getConnection(DB_IDEMPIERE_URL, DB_IDEMPIERE_USERNAME, DB_IDEMPIERE_PASSWORD);
//			stm = conn.createStatement();
//			stm.setQueryTimeout(10000);
			
			conn.setAutoCommit(false);
System.out.println("customers update"+customers.size());

			for (CustomerIdempiere customerIdempiere : customers) {
//				System.out.println(customerIdempiere.getTAXCODE()+ "-"+customerIdempiere.getVALUE());
				sql = generateSqlUpdateTDABCustomer(customerIdempiere);
				if (null != sql) {
					//stm.addBatch(sql);
//					System.out.println("sql--"+sql);
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeUpdate();
				}
			}
//			stm.executeBatch();
			conn.commit();
			System.out.println("updateTDABCUSTOMER END....!");
		} catch (Exception e) {
			System.err.println("updateTDABCUSTOMER UPDATE ERROR : ");
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				//stm.close();
				conn.close();
			} catch (Exception e2) {
				System.err.println("updateTDABCUSTOMER CLOSE CONNECTION : ");
				e2.printStackTrace();
			}
		}
	
	}
	
	public static void updateTDABCUST24(CustomerIdempiere customers) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		String sql;
		
		
			try {
				conn = DriverManager.getConnection(DB_IDEMPIERE_URL, DB_IDEMPIERE_USERNAME, DB_IDEMPIERE_PASSWORD);	
				conn.setAutoCommit(false);

				sql = generateSqlUpdateTDABCusT24(customers);
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeUpdate();
//				stm.executeBatch();
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					preparedStatement.close();
					//stm.close();
					conn.close();
				} catch (Exception e2) {
					System.err.println("updateTDABCUS24 CLOSE CONNECTION : ");
					e2.printStackTrace();
				}
			}
		
	
	}
	public static void insertTDABCUST24(CustomerIdempiere customers) {
		Connection conn = null;

		PreparedStatement preparedStatement = null;
		String sql;

			try {
				conn = DriverManager.getConnection(DB_IDEMPIERE_URL, DB_IDEMPIERE_USERNAME, DB_IDEMPIERE_PASSWORD);	
				conn.setAutoCommit(false);

				sql = generateSqlInsertTDABCustomer(customers);
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeUpdate();
//				stm.executeBatch();
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					preparedStatement.close();
					//stm.close();
					conn.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		
		
	
	}

	private static String generateSqlUpdateTDABCustomer(CustomerIdempiere cus) {
		StringBuffer sql = new StringBuffer("UPDATE TDAB_CUSTOMER SET ");

		if (null != cus && !cus.getVALUE().isEmpty()) {
			sql.append("VALUE = '").append(cus.getVALUE()).append("', ");
		} else {
			sql.append("VALUE = NULL,");
		}

		if (null != cus && !cus.getCOMPANYNAME().isEmpty()) {
			sql.append("COMPANYNAME = '").append(cus.getCOMPANYNAME()).append("',");
		} else {
			sql.append("COMPANYNAME = NULL,");
		}

		if (null != cus && !cus.getNAME().isEmpty()) {
			sql.append("NAME = '").append(cus.getNAME()).append("',");
		} else {
			sql.append("NAME = NULL,");
		}

		if (null != cus && !cus.getTAXCODE().isEmpty()) {
			sql.append("TAXCODE = '").append(cus.getTAXCODE()).append("',");
		} else {
			sql.append("TAXCODE = NULL,");
		}

		if (null != cus && !cus.getNUMBERBUSINESS().isEmpty()) {
			sql.append("NUMBERBUSINESS = '").append(cus.getNUMBERBUSINESS()).append("',");
		} else {
			sql.append("NUMBERBUSINESS = NULL,");
		}

		sql.append("CHECK_T24 = 'Y' WHERE TAXCODE = '").append(cus.getTAXCODE()).append("'");
		return sql.toString();
	}
	
	private static String generateSqlInsertTDABCustomer(CustomerIdempiere cus) {
		StringBuffer sql = new StringBuffer("INSERT INTO TDAB_CUS_T24 (VALUE, COMPANYNAME, NAME, TAXCODE ,number_business, CHECK_T24) values ( ");

		if (null != cus && !cus.getVALUE().isEmpty()) {
			sql.append("'").append(cus.getVALUE()).append("', ");
		} else {
			sql.append("NULL,");
		}

		if (null != cus && !cus.getCOMPANYNAME().isEmpty()) {
			sql.append("'").append(cus.getCOMPANYNAME()).append("',");
		} else {
			sql.append("NULL,");
		}

		if (null != cus && !cus.getNAME().isEmpty()) {
			sql.append("'").append(cus.getNAME()).append("',");
		} else {
			sql.append("NULL,");
		}

		if (null != cus && !cus.getTAXCODE().isEmpty()) {
			sql.append("'").append(cus.getTAXCODE()).append("',");
		} else {
			sql.append("NULL,");
		}

		if (null != cus && !cus.getNUMBERBUSINESS().isEmpty()) {
			sql.append("'").append(cus.getNUMBERBUSINESS()).append("',");
		} else {
			sql.append("= NULL,");
		}

		sql.append("'Y')");
		return sql.toString();
	}
	private static String generateSqlUpdateTDABCusT24(CustomerIdempiere cus) {
		StringBuffer sql = new StringBuffer("UPDATE tdab_cus_t24 SET ");

		if (null != cus && !cus.getVALUE().isEmpty()) {
			sql.append("VALUE = '").append(cus.getVALUE()).append("', ");
		} else {
			sql.append("VALUE = NULL,");
		}

		if (null != cus && !cus.getCOMPANYNAME().isEmpty()) {
			sql.append("COMPANYNAME = '").append(cus.getCOMPANYNAME()).append("',");
		} else {
			sql.append("COMPANYNAME = NULL,");
		}

		if (null != cus && !cus.getNAME().isEmpty()) {
			sql.append("NAME = '").append(cus.getNAME()).append("',");
		} else {
			sql.append("NAME = NULL,");
		}

		if (null != cus && !cus.getTAXCODE().isEmpty()) {
			sql.append("TAXCODE = '").append(cus.getTAXCODE()).append("',");
		} else {
			sql.append("TAXCODE = NULL,");
		}

		if (null != cus && !cus.getNUMBERBUSINESS().isEmpty()) {
			sql.append("Number_Business = '").append(cus.getNUMBERBUSINESS()).append("',");
		} else {
			sql.append("Number_Business = NULL,");
		}

		sql.append("CHECK_T24 = 'Y' WHERE TAXCODE = '").append(cus.getTAXCODE()).append("'");
		return sql.toString();
	}

	public static void updateTDABCUSTOMERPROFILE(List<CustomerIdempiere> customers) {
		Connection conn = null;
		Statement stm = null;
		String sql;
		try {
			System.out.println("updateTDABCUSTOMERPROFILE START.....");
			conn = DriverManager.getConnection(DB_IDEMPIERE_URL, DB_IDEMPIERE_USERNAME, DB_IDEMPIERE_PASSWORD);
			stm = conn.createStatement();
			stm.setQueryTimeout(400);
			conn.setAutoCommit(false);
			for (CustomerIdempiere customerIdempiere : customers) {
				sql = generateSqlUpdateTDABCustomerProfile(customerIdempiere);
				if (null != sql)
					stm.addBatch(sql);
			}
			 stm.executeBatch();
			 conn.commit();
			System.out.println("updateTDABCUSTOMERPROFILE END....!");
		} catch (Exception e) {
			System.err.println("updateTDABCUSTOMERPROFILE UPDATE ERROR : ");
			e.printStackTrace();
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (Exception e2) {
				System.err.println("updateTDABCUSTOMERPROFILE CLOSE CONNECTION : ");
				e2.printStackTrace();
			}
		}
	}

	private static String generateSqlUpdateTDABCustomerProfile(CustomerIdempiere cus) {
		StringBuffer sql = new StringBuffer("UPDATE TDAB_CUSTOMER_PROFILE SET ");
		if (null != cus && !cus.getVALUE().isEmpty()) {
			sql.append("VALUE = '").append(cus.getVALUE()).append("', ");
		} else {
			sql.append("VALUE = NULL,");
		}

		if (null != cus && !cus.getCOMPANYNAME().isEmpty()) {
			sql.append("COMPANYNAME = '").append(cus.getCOMPANYNAME()).append("',");
		} else {
			sql.append("COMPANYNAME = NULL,");
		}

		if (null != cus && !cus.getNAME().isEmpty()) {
			sql.append("NAME = '").append(cus.getNAME()).append("',");
		} else {
			sql.append("NAME = NULL,");
		}

		if (null != cus && !cus.getTAXCODE().isEmpty()) {
			sql.append("TAXCODE = '").append(cus.getTAXCODE()).append("',");
		} else {
			sql.append("TAXCODE = NULL,");
		}

		if (null != cus && !cus.getNUMBERBUSINESS().isEmpty()) {
			sql.append("NUMBERBUSINESS = '").append(cus.getNUMBERBUSINESS()).append("',");
		} else {
			sql.append("NUMBERBUSINESS = NULL,");
		}

		sql.append("CIF = NULL WHERE TAXCODE = '").append(cus.getTAXCODE()).append("'");
		return sql.toString();

	}
}
