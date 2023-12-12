package xhtd_ab.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.util.resources.cldr.ext.CurrencyNames_sw_UG;
import xhtd_ab.database.JDBCIdempiereConnect;
import xhtd_ab.database.JDBCT24Connect;
import xhtd_ab.model.CustomerIdempiere;
import xhtd_ab.model.CustomerProfile;

public class SynchronizeServiceT24 {
	public static void doSynchronize() {

		List<CustomerProfile> customerProfiles = new ArrayList<>();
		CustomerIdempiere customer;
		try {
			// lay du lieu t24

			customerProfiles = JDBCT24Connect.getAllCusFromT24();
			System.out.println("customerProfiles.size()-"+customerProfiles.size());
			if (customerProfiles.isEmpty() || customerProfiles.size() == 0) {
				System.out.println("HAVE NO CUSTOMER CAN UPDATE.....!");
				return;
			}
			
			System.out.println("CONVERT DATA FROM T24 TO tdab_cus_t24 DB START.....");

			for (CustomerProfile customerProfile : customerProfiles) {
				customer = new CustomerIdempiere();

				customer.setVALUE(customerProfile.getMA_KH());
				customer.setCOMPANYNAME(customerProfile.getTEN_KH());
				customer.setNAME(customerProfile.getTEN_NGUOI_DAI_DIEN());
				customer.setNUMBERBUSINESS(customerProfile.getGIAY_PHEP_KINH_DOANH());
				customer.setTAXCODE(customerProfile.getMA_SO_THUE());
				
				if(JDBCIdempiereConnect.getTDABCusT24(customer.getVALUE()).size() > 0) {
					JDBCIdempiereConnect.updateTDABCUST24(customer);	
				}
				else {
					JDBCIdempiereConnect.insertTDABCUST24(customer);	
				}

			}

			System.out.println("CONVERT DATA FROM T24 TO IDEMPIERE DB END....!");




		} catch (Exception e) {
			System.err.println("SYNCHRONIZE ERROR : ");
			e.printStackTrace();
		}

	}
}
