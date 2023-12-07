package xhtd_ab.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xhtd_ab.database.JDBCIdempiereConnect;
import xhtd_ab.database.JDBCT24Connect;
import xhtd_ab.model.CustomerIdempiere;
import xhtd_ab.model.CustomerProfile;

public class SynchronizeServiceT24 {
	public static void doSynchronize() {

		List<CustomerProfile> customerProfiles = new ArrayList<>();
		List<CustomerIdempiere> customerIdem = new ArrayList<>();
		
		CustomerIdempiere customer;
		
		List<CustomerProfile> cusIdsUpdate = new ArrayList<>();
		List<CustomerProfile> cusIdsInsert = new ArrayList<>();
		
		List<CustomerIdempiere> cusIdsUpdateIdem = new ArrayList<>();
		List<CustomerIdempiere> cusIdsInsertIdem = new ArrayList<>();
		try {
			// lay du lieu full t24

			customerProfiles = JDBCT24Connect.getAllCusFromT24();
			System.out.println("customerProfiles.size()-"+customerProfiles.size());
			if (customerProfiles.isEmpty() || customerProfiles.size() == 0) {
				System.out.println("HAVE NO CUSTOMER CAN UPDATE.....!");
				return;
			}

			System.out.println("CONVERT DATA FROM T24 TO tdab_cus_T24 DB START.....");
			
			
			int count = 200;
			for (int i = 0; i <= customerProfiles.size() / count; i++) {
				List<CustomerProfile> maKh = new ArrayList<>();
				for (int j = count * i + 1; j <= count * (i + 1); j++) {
//					System.out.println("i:"+i+" -j:"+j);
					if (j <= customerProfiles.size()) {
						maKh.add(customerProfiles.get(j - 1));
					}
				}
				System.out.println("maSoThue.size()-"+maKh.size());
//				check 200 cái cus của T24 đã có trong tdab chưa
				for(i=0;i< maKh.size(); i++) {
					customerIdem = JDBCIdempiereConnect.getCustomerProfileFromTDAB1(maKh.get(i).MA_KH);	
					if (customerIdem.size()>0) {
						cusIdsUpdate.add(maKh.get(i));
					}
					else {
						cusIdsInsert.add(maKh.get(i));
					}
				}
				System.out.println("cusIdsUpdate=="+cusIdsUpdate.size());
				System.out.println("cusIdsInsert=="+cusIdsInsert.size());
						
			}
			
			for (CustomerProfile cusId : cusIdsUpdate) {
				customer = new CustomerIdempiere();


				// ma khach hang/ID T24
				customer.setVALUE(cusId.getMA_KH());

				// Ten Doanh nhiep
				customer.setCOMPANYNAME(cusId.getTEN_KH());

				// ten giam doc doanh nghiep
				customer.setNAME(cusId.getTEN_NGUOI_DAI_DIEN());

				// so giay phep kinh doanh
				customer.setNUMBERBUSINESS(cusId.getGIAY_PHEP_KINH_DOANH());
				
				customer.setTAXCODE(cusId.getMA_SO_THUE());
				
				cusIdsUpdateIdem.add(customer);

			}
			for (CustomerProfile cusId : cusIdsInsert) {
				customer = new CustomerIdempiere();


				// ma khach hang/ID T24
				customer.setVALUE(cusId.getMA_KH());

				// Ten Doanh nhiep
				customer.setCOMPANYNAME(cusId.getTEN_KH());

				// ten giam doc doanh nghiep
				customer.setNAME(cusId.getTEN_NGUOI_DAI_DIEN());

				// so giay phep kinh doanh
				customer.setNUMBERBUSINESS(cusId.getGIAY_PHEP_KINH_DOANH());
				
				customer.setTAXCODE(cusId.getMA_SO_THUE());
				
				cusIdsInsertIdem.add(customer);

			}
			JDBCIdempiereConnect.updateTDABCUST24(cusIdsUpdateIdem);
			JDBCIdempiereConnect.insertTDABCUST24(cusIdsInsertIdem);

			System.out.println("CONVERT DATA FROM T24 TO IDEMPIERE DB END....!");




		} catch (Exception e) {
			System.err.println("SYNCHRONIZE ERROR : ");
			e.printStackTrace();
		}

	}
}
