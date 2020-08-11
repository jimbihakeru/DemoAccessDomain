package SwitchNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class switchnetwork {
	public void switchToSpecificNetwork(String networkName, String networkPass) throws InterruptedException {

		String osName = System.getProperty("os.name").toLowerCase();
		String cmd;
		if (osName.contains("mac")) {
			cmd = "networksetup -setairportnetwork en0" + " " + networkName + " " + networkPass;
		} else {
			cmd = "netsh wlan connect ssid=\"" + networkName + "\"" + " name=\"" + networkName + "\"";
		}
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void ClearCacheDNS() {

		String osName = System.getProperty("os.name").toLowerCase();
		String cmd;
		if (osName.contains("mac")) {
			cmd = "sudo killall -HUP mDNSResponder";
			cmd = "12345679";
		} else {
			cmd = "ipconfig /flushdns";
		}
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void CheckConnectSuccess(String networkName, String networkPass) throws IOException, InterruptedException {
		String ssids = null;
	    ProcessBuilder builder = new ProcessBuilder(
	            "cmd.exe", "/c", "netsh wlan show interfaces");
	    builder.redirectErrorStream(true);
	    Process p = builder.start();
	    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String line;
	    line = r.readLine();
	    while (r.read()!=-1) {
	        line = r.readLine();
	        if (line.contains("SSID")){
	            if(!line.contains("BSSID"))
	                if(line.contains("SSID")&&!line.contains("name")&&!line.contains("SSIDs"))
	                {
	                    line=line.substring(8);
	                    ssids = line;
	                }

	        }

	    }
	    System.out.println("SSID name == "+ssids);
	    if(ssids.contains(networkName)) {
	    	System.out.println("successful connection");
	    }
	    else {
	    	System.out.println("Access connection Failed");
	    	switchToSpecificNetwork(networkName, networkPass);
	    }
	}

}
