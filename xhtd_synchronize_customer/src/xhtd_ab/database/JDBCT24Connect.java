package xhtd_ab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import xhtd_ab.config.ConfigDB;
import xhtd_ab.model.CustomerProfile;

public class JDBCT24Connect {
	private static final String DB_T24_URL = ConfigDB.dbT24Url;
	private static final String DB_T24_USERNAME = ConfigDB.dbT24UserName;
	private static final String DB_T24_PASSWORD = ConfigDB.dbT24Password;
	
	public static List<CustomerProfile> getCustomerProfileFromT24(List<String> dsmasothue) {
		Connection conn = null;
		PreparedStatement stm = null;
		CustomerProfile cusProfile;
		List<CustomerProfile> cusProfiles = new ArrayList<>();
		try {
			System.out.println("getCustomerProfileFromT24 START.....");
			String sql = generateSql(dsmasothue);
			conn = DriverManager.getConnection(DB_T24_URL,DB_T24_USERNAME,DB_T24_PASSWORD);
			stm = conn.prepareStatement(sql);
			stm.setQueryTimeout(2000);
			
			ResultSet rs = stm.executeQuery();
			while ( rs.next() ) {
				cusProfile = new CustomerProfile();
				cusProfile.setMA_SO_THUE(rs.getString("MA_SO_THUE"));
				cusProfile.setMA_KH(rs.getString("MA_KH"));
				cusProfile.setTEN_KH(rs.getString("TEN_KH"));
				cusProfile.setTEN_NGUOI_DAI_DIEN(rs.getString("TEN_NGUOI_DAI_DIEN"));
				cusProfile.setCHUC_DANH_NGUOI_DAI_DIEN(rs.getString("CHUC_DANH_NGUOI_DAI_DIEN"));
				cusProfile.setGIAY_PHEP_KINH_DOANH(rs.getString("GIAY_PHEP_KINH_DOANH"));
				cusProfile.setNAT_ID_TYPE(rs.getString("NAT_ID_TYPE"));
				cusProfile.setPKKH(rs.getString("PKKH"));
//				cusProfile.setD_D_CUST_DATE_OF_BIRTH(rs.getString("D_D_CUST_DATE_OF_BIRTH"));
//				cusProfile.setD_D_CUST_START_DATE(rs.getString("D_D_CUST_START_DATE"));
//				cusProfile.setLAST_INFO_CHANGE_DATE(rs.getString("LAST_INFO_CHANGE_DATE"));
				cusProfile.setCOT_DUTRU_01(rs.getString("COT_DUTRU_01"));
				cusProfile.setCOT_DUTRU_02(rs.getString("COT_DUTRU_01"));
				cusProfile.setCOT_DUTRU_03(rs.getString("COT_DUTRU_01"));
				cusProfile.setCOT_DUTRU_04(rs.getString("COT_DUTRU_01"));
				cusProfile.setCOT_DUTRU_05(rs.getString("COT_DUTRU_01"));
//				cusProfile.setD_GET_DATE(rs.getString("D_GET_DATE"));
				
				cusProfiles.add(cusProfile);
            }
			System.out.println("getCustomerProfileFromT24 END.....");
		} catch (SQLException e) {
			System.err.println("getCustomerProfileFromT24 UPDATE ERROR : ");
			e.printStackTrace();
		}finally {
			try {
				stm.close();
				conn.close();
			} catch (Exception e2) {
				System.err.println("getCustomerProfileFromT24 CLOSE CONNECTION : ");
				e2.printStackTrace();
			}
		}
		return cusProfiles;
	}
	private static String generateSql(List<String> dsmasothue) {
//		String parameter = dsmasothue.stream().map(String::toString).collect(Collectors.joining("','"));
		StringBuffer param = new StringBuffer();
		for (String str : dsmasothue) {
			param.append(str).append("','");
		}
        String parameter = "('".concat(param.toString()).concat("')");
//        String sql = "SELECT * FROM ofsaatm.abb_sme_cust_for_scoring_v where ma_so_thue in";
//        sql = sql.concat(parameter);
        String sql = "select * from (SELECT row_number() OVER(PARTITION BY MA_SO_THUE ORDER BY D_LAST_INFO_CHANGE_DATE DESC) row_num, a.* FROM ofsaatm.abb_sme_cust_for_scoring_v a where MA_SO_THUE in ";
        sql = sql.concat(parameter).concat(" ) where row_num = 1");
        return sql;
	}
}
