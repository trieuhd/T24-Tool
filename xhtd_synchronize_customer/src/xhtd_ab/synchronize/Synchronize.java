package xhtd_ab.synchronize;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import xhtd_ab.database.JDBCIdempiereConnect;
import xhtd_ab.database.JDBCT24Connect;
import xhtd_ab.model.CustomerIdempiere;
import xhtd_ab.model.CustomerProfile;

public class Synchronize extends TimerTask{
	public Synchronize(){
	}
	
	public void doSynchronize() {
		List<String> taxCodes = new ArrayList<>();
		List<CustomerProfile> customerProfiles = new ArrayList<>();
		Map<String, CustomerIdempiere> MapCustomer = new HashMap<String, CustomerIdempiere>();
		CustomerIdempiere customer;
		try {
			//get thong tin khach hang tu db idempiere
			List<CustomerIdempiere> customerIdempieres = JDBCIdempiereConnect.getCustomerProfileFromDBIdempiere();
			if(customerIdempieres.isEmpty() || customerIdempieres.size() == 0) {
				System.out.println("HAVE NO CUSTOMER NEED UPDATE.....!");
				return;
			}
			for (CustomerIdempiere mCustomer : customerIdempieres) {
				taxCodes.add(mCustomer.getTAXCODE());
				MapCustomer.put(mCustomer.getTAXCODE(), mCustomer);
			}
			
			//get thong tin khach hang tu T24
			customerProfiles = JDBCT24Connect.getCustomerProfileFromT24(taxCodes);
			
			System.out.println("taxCodes-"+taxCodes);
			//dong bo
			if(customerProfiles.isEmpty() || customerProfiles.size() == 0) {
				System.out.println("HAVE NO CUSTOMER CAN UPDATE.....!");
				return;
			}
			
			System.out.println("CONVERT DATA FROM T24 TO IDEMPIERE DB START.....");
			Map<String, CustomerIdempiere> map = new HashMap<>();
			for (CustomerProfile customerProfile : customerProfiles) {
				customer = new CustomerIdempiere();
				customer = MapCustomer.get(customerProfile.getMA_SO_THUE());
				
				customer.setCHECK_T24("Y");
				
				//ma khach hang/ID T24
				customer.setVALUE(customerProfile.getMA_KH());
				
				//Ten Doanh nhiep
				customer.setCOMPANYNAME(customerProfile.getTEN_KH());
				
				//ten giam doc doanh nghiep
				customer.setNAME(customerProfile.getTEN_NGUOI_DAI_DIEN());
				
				//so giay phep kinh doanh
				customer.setNUMBERBUSINESS(customerProfile.getGIAY_PHEP_KINH_DOANH());
				
				map.put(customerProfile.getMA_SO_THUE(), customer);
//				updateCustomers.add(customer);
			}
			List<CustomerIdempiere> updateCustomers = new ArrayList<CustomerIdempiere>(map.values());
			System.out.println("CONVERT DATA FROM T24 TO IDEMPIERE DB END....!");
			
			//save MCustomer
			JDBCIdempiereConnect.updateTDABCUSTOMER(updateCustomers);
			
			//save MCustomerProfile
			JDBCIdempiereConnect.updateTDABCUSTOMERPROFILE(updateCustomers);
		} catch (Exception e) {
			System.err.println("SYNCHRONIZE ERROR : ");
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		System.out.println("SYNCHRONIZE START AT : "+new Date(System.currentTimeMillis()));
		doSynchronize();
		System.out.println("SYNCHRONIZE END AT : "+new Date(System.currentTimeMillis()));
	}
}
