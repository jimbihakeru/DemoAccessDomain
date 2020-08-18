package testing.API;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.seleniumhq.jetty9.util.log.Log;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import CheckConnecttion.Actions;
import SwitchNetwork.switchnetwork;
import telegrambots.notifyBot;

public class GetRequestAutomation {

	public WebDriver driver;
	public static Properties prop;
	public ExtentReports extent;
	public static ExtentHtmlReporter htmlReporter;
	public ExtentTest test;
	Log log;

//	WebDriverWait wait=new WebDriverWait(driver, 10);
	notifyBot bot = new notifyBot();
	switchnetwork wifi = new switchnetwork();
	Actions action = new Actions();

	public String IDElement = "//*[@id='footer' or @id='header']";
	public String ClassElement = "//*[@class='header' or @class='header-custom' or @class='footer footer-custom']";

	@BeforeTest
	public void setExtent() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/ExtentReport.html");
		htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Hostname", "localhost");
		extent.setSystemInfo("OS", "Mac OS");
		extent.setSystemInfo("Tester Name", "QC_Jimbi");
		extent.setSystemInfo("Browser", "Chrome");
	}

	@AfterTest
	public void endReport() {
		extent.flush();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void ApiTesting() throws IOException, InterruptedException {
		String network[] = { "fpt", "vnpt", "viettel"};
		for (String x : network) {
			if (x.equals("fpt")) {
				System.out.println(x);
				wifi.switchToSpecificNetwork("FPT", "");
				try {
					Thread.sleep(15000);
					wifi.CheckConnectSuccess("FPT", "");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (x.equals("vnpt")) {
				System.out.println(x);
				wifi.switchToSpecificNetwork("Long Quang", "");
				try {
					Thread.sleep(15000);
					wifi.CheckConnectSuccess("Long Quang", "");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (x.equals("viettel")) {
				System.out.println(x);
				wifi.switchToSpecificNetwork("Viettel", "");
				try {
					Thread.sleep(15000);
					wifi.CheckConnectSuccess("Viettel", "");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 

			prop = new Properties();
			FileInputStream fis = new FileInputStream("src/main/java//configs/URL.properties");
			prop.load(fis);
			
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("mac")) {
				System.setProperty("webdriver.chrome.driver", "resource/chromedrivers");
			} else {
				System.setProperty("webdriver.chrome.driver", "resource/chromedriver.exe");
			}
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--incognito");
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, options);

			for (int i = 1; i <= prop.size(); i++) {
				driver = new ChromeDriver(cap);
				driver.manage().window().maximize();
//				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				String url = prop.getProperty("url" + i);
				driver.get(url);
				action.checkPageIsReady();
				test = extent.createTest(url);
				if (driver.findElements(By.xpath(IDElement)).size() != 0) {
					test.log(Status.PASS, url + "----- Access OK");
					System.out.println(driver.getCurrentUrl() + "    ------ Access Success on " + x.toUpperCase());
				} else if (driver.findElements(By.xpath(ClassElement)).size() != 0) {
					test.log(Status.PASS, url + "----- Access OK");
					System.out.println(driver.getCurrentUrl() + "    ------ Access Success on " + x.toUpperCase());
				} else {
					test.log(Status.FAIL, url + "------ Access failed on " + x.toUpperCase());
					System.out.println("Access Failed");
					bot.sendMsg(driver.getCurrentUrl() + "    ------ Access Failed on " + x.toUpperCase());
				}

				driver.close();
			}
			System.out.println("========================================================================\n");
			wifi.ClearCacheDNS();
		}

	}

}