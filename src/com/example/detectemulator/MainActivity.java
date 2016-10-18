package com.example.detectemulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		Log.e("density", "density=====================: " + dm.density);
		Log.e("density", "width=====================: " + dm.widthPixels);
		Log.e("density", "height=====================: " + dm.heightPixels);

		txt = (TextView) findViewById(R.id.txt);

		String breakLine = "\r\n\r\n";

		String str = "EmulatorDetector: isEmulator = " + (EmulatorDetector.isEmulator() ? "TRUE" : "FALSE") + breakLine;

		str += "EmulatorDetector: " + EmulatorDetector.getDeviceListing() + breakLine;

		str += "DeviceUtil: " + (DeviceUtil.isEmulator() ? "TRUE" : "FALSE") + breakLine;

		str += "FINGERPRINT contains: " + (Build.FINGERPRINT.contains("generic") ? "TRUE" : "FALSE") + breakLine;

		str += "Build.PRODUCT google_sdk: " + (Build.PRODUCT.equals("google_sdk") ? "TRUE" : "FALSE") + breakLine;

		str += "Build.HARDWARE goldfish: " + (Build.HARDWARE.equals("goldfish") ? "TRUE" : "FALSE") + breakLine;

		str += "Build.MODEL google_sdk: " + (Build.MODEL.equals("google_sdk") ? "TRUE" : "FALSE") + breakLine;

		str += "isAndroidEmulator: " + (isAndroidEmulator() ? "TRUE" : "FALSE") + breakLine;

		str += "Build.PRODUCT matches: " + (Build.PRODUCT.contains("sdk") ? "TRUE" : "FALSE") + breakLine;

		str += "isvm: " + (isvm() ? "TRUE" : "FALSE") + breakLine;

		str += "runsInEmulator: " + (runsInEmulator() ? "TRUE" : "FALSE") + breakLine;

		str += "useSensorManager: " + (useSensorManager() ? "TRUE" : "FALSE") + breakLine;

		/*try {
			InetAddress inet;

			inet = InetAddress.getByName("10.0.2.2");

			str += "InetAddress: " + (inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable")
					+ breakLine;
		} catch (Exception e) {
		}

		str += "ping: " + ping("10.0.2.2");*/

		txt.setText(str);
	}

	public boolean isAndroidEmulator() {
		String product = Build.PRODUCT;

		boolean isEmulator = false;
		if (product != null)
			isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_");
		return isEmulator;
	}

	public boolean isvm() {
		StringBuilder deviceInfo = new StringBuilder();
		deviceInfo.append("Build.PRODUCT " + Build.PRODUCT + "\n");
		deviceInfo.append("Build.FINGERPRINT " + Build.FINGERPRINT + "\n");
		deviceInfo.append("Build.MANUFACTURER " + Build.MANUFACTURER + "\n");
		deviceInfo.append("Build.MODEL " + Build.MODEL + "\n");
		deviceInfo.append("Build.BRAND " + Build.BRAND + "\n");
		deviceInfo.append("Build.DEVICE " + Build.DEVICE + "\n");

		Boolean isvm = false;
		if ("google_sdk".equals(Build.PRODUCT) || "sdk_google_phone_x86".equals(Build.PRODUCT)
				|| "sdk".equals(Build.PRODUCT) || "sdk_x86".equals(Build.PRODUCT) || "vbox86p".equals(Build.PRODUCT)
				|| Build.FINGERPRINT.contains("generic") || Build.MANUFACTURER.contains("Genymotion")
				|| Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86")) {
			isvm = true;
		}

		if (Build.BRAND.contains("generic") && Build.DEVICE.contains("generic")) {
			isvm = true;
		}

		return isvm;
	}

	public boolean runsInEmulator() {
		if (Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator")
				|| Build.MODEL.contains("Android SDK"))
			return true;
		return false;
	}

	public boolean useSensorManager() {
		SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (manager.getSensorList(Sensor.TYPE_ALL).isEmpty())
			return true;
		return false;
	}

	public String ping(String url) {
		String str = "";
		try {
			Process process = Runtime.getRuntime().exec("ping -c 5 " + url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int i;
			char[] buffer = new char[4096];
			StringBuffer output = new StringBuffer();
			while ((i = reader.read(buffer)) > 0)
				output.append(buffer, 0, i);
			reader.close();
			str += output.toString();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			str += "Exception: " + sw.toString();
		}
		return str;
	}

}
