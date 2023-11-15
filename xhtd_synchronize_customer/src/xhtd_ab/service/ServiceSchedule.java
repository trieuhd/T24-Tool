package xhtd_ab.service;

import xhtd_ab.config.ConfigDB;

import java.util.Date;
import java.util.TimerTask;

public class ServiceSchedule extends TimerTask{

	public ServiceSchedule() {
	}
	@Override
	public void run() {
		System.out.println("SCHEDULE START AT : "+new Date(System.currentTimeMillis()));
		ConfigDB.loadConfig();
		SynchronizeService.doSynchronize();
//		RestartIdempiereService.restart();
		System.out.println("SCHEDULE END AT : "+new Date(System.currentTimeMillis()));
	}

}
