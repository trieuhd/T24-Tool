package xhtd_ab.service;

import java.io.IOException;

public class RestartIdempiereService {
//	private static final String CMD = "systemctl restart backendserver";
	private static final String CMD = "nohup sh idempiere-server.sh >> idempiere-server.log 2>&1 &";
	private static Process runtime;
	
	public static void restart() {
		try
		{
//			Process runtime = Runtime.getRuntime().exec("cmd /c start notepad++.exe");
			System.out.println("RESTART IDEMPIERE SERVICE START.....");
			setRuntime(Runtime.getRuntime().exec(CMD));
			System.out.println("RESTART IDEMPIERE SERVICE END....!");
		} catch (IOException e)
		{
			System.err.println("RESTART IDEMPIERE SERVICE ERROR");
		    e.printStackTrace();
		}
	}

	public static Process getRuntime() {
		return runtime;
	}

	public static void setRuntime(Process runtime) {
		RestartIdempiereService.runtime = runtime;
	}
}