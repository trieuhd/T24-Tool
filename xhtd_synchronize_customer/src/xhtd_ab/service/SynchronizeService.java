package xhtd_ab.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xhtd_ab.database.JDBCIdempiereConnect;
import xhtd_ab.database.JDBCT24Connect;
import xhtd_ab.model.CustomerIdempiere;
import xhtd_ab.model.CustomerProfile;

public class SynchronizeService {
	public static void doSynchronize() {
		List<String> taxCodes = new ArrayList<>();
		List<CustomerProfile> customerProfiles = new ArrayList<>();
		Map<String, CustomerIdempiere> MapCustomer = new HashMap<String, CustomerIdempiere>();
		CustomerIdempiere customer;
		try {
			// get thong tin khach hang tu db idempiere
			List<CustomerIdempiere> customerIdempieres = JDBCIdempiereConnect.getCustomerProfileFromDBIdempiere();
			if (customerIdempieres.isEmpty() || customerIdempieres.size() == 0) {
				System.out.println("HAVE NO CUSTOMER NEED UPDATE.....!");
				return;
			}
			for (CustomerIdempiere mCustomer : customerIdempieres) {
				taxCodes.add(mCustomer.getTAXCODE());
				MapCustomer.put(mCustomer.getTAXCODE(), mCustomer);
			}
			int count = 200;
			for (int i = 0; i <= taxCodes.size() / count; i++) {
				List<String> maSoThue = new ArrayList<>();
				for (int j = count * i + 1; j <= count * (i + 1); j++) {
//					System.out.println("i:"+i+" -j:"+j);
					if (j <= taxCodes.size()) {
						maSoThue.add(taxCodes.get(j - 1).toString());
					}
				}
				System.out.println("maSoThue.size()-"+maSoThue.size());
				customerProfiles = JDBCT24Connect.getCustomerProfileFromT24(maSoThue);

				// dang có 91 bản ghi trên T24 tìm từ tất cả mst trong hệ thống xhtd, trong đó
				// có các mst trùng nhau
				// dong bo
				if (customerProfiles.isEmpty() || customerProfiles.size() == 0) {
					System.out.println("HAVE NO CUSTOMER CAN UPDATE.....!");
					return;
				}
				System.out.println("customerProfiles.size()-"+customerProfiles.size());
				System.out.println("CONVERT DATA FROM T24 TO IDEMPIERE DB START.....");
				Map<String, CustomerIdempiere> map = new HashMap<>();
				for (CustomerProfile customerProfile : customerProfiles) {
					customer = new CustomerIdempiere();
					customer = MapCustomer.get(customerProfile.getMA_SO_THUE());

					customer.setCHECK_T24("Y");

					// ma khach hang/ID T24
					customer.setVALUE(customerProfile.getMA_KH());

					// Ten Doanh nhiep
					customer.setCOMPANYNAME(customerProfile.getTEN_KH());

					// ten giam doc doanh nghiep
					customer.setNAME(customerProfile.getTEN_NGUOI_DAI_DIEN());

					// so giay phep kinh doanh
					customer.setNUMBERBUSINESS(customerProfile.getGIAY_PHEP_KINH_DOANH());

					map.put(customerProfile.getMA_SO_THUE(), customer);
//					updateCustomers.add(customer);
				}
				List<CustomerIdempiere> updateCustomers = new ArrayList<CustomerIdempiere>(map.values());
				System.out.println("updateCustomers-"+updateCustomers.size());
				System.out.println("CONVERT DATA FROM T24 TO IDEMPIERE DB END....!");

				// save MCustomer
				JDBCIdempiereConnect.updateTDABCUSTOMER(updateCustomers);

				// save MCustomerProfile
				JDBCIdempiereConnect.updateTDABCUSTOMERPROFILE(updateCustomers);

			}

		} catch (Exception e) {
			System.err.println("SYNCHRONIZE ERROR : ");
			e.printStackTrace();
		}

	}
}
