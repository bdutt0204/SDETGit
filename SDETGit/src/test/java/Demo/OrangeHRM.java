package Demo;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

//import junit.framework.Assert;

public class OrangeHRM {

	public String baseUrl ="https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
	public WebDriver driver;

	@BeforeTest
	public void setup()
	{
		System.out.println("Before Test Executed");

		driver = new ChromeDriver();

		driver.manage().window().maximize();

		driver.get(baseUrl);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));

	}
	@Test(priority =2,enabled=false)
	public void LoginTestwithValidCredentials()
	{
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");
		driver.findElement(By.xpath(" //input[@placeholder='Password']")).sendKeys("admin123");
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		String pageTitle= driver.getTitle();

		/*if(pageTitle.equals("OrangeHRM")) {
			System.out.println("Login Successful");
		}else
		{
			System.out.println("Login Failed");

		}*/
		logOut();
		Assert.assertEquals("OrangeHRM", pageTitle);

	}

	@Test (priority=1,enabled=false)
	public void LoginTestwithInvalidCredentials() throws InterruptedException
	{
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");
		driver.findElement(By.xpath(" //input[@placeholder='Password']")).sendKeys("1234");
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		String message_expected= "Invalid credentials";

		String message_actual= driver.findElement(By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")).getText();

		Assert.assertEquals(message_expected, message_actual);
		Thread.sleep(1500);

	}

	public void login()
	{
		driver.findElement(By.xpath("//input[@placeholder='username']")).sendKeys("Admin");
		driver.findElement(By.xpath("//input[@placeholder='password']")).sendKeys("admin123");
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

	}

	public void logOut()  
	{
		driver.findElement(By.xpath("//p[@class='oxd-userdropdown-name']")).click();
		//driver.findElement(By.xpath("//a[normalize-space()='Logout']"));

		List <WebElement> elementList=driver.findElements(By.xpath("//a[@class='oxd-userdropdown-link']"));

		/*for (int i=0;i<elementList.size();i++)
		{

			Thread.sleep(1000);
			System.out.println(i+ ":" +elementList.get(i).getText());

		}*/
		elementList.get(3).click();

	}

	@Test (priority=3,enabled=false)
	public void addEmployee() throws InterruptedException 
	{
		login();


		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[text()='Add Employee']")).click();

		driver.findElement(By.xpath("//input[@placeholder='First Name']")).sendKeys("Radha");
		driver.findElement(By.xpath("//input[@placeholder='Last Name']")).sendKeys("Gupta");

		Thread.sleep(2000);

		driver.findElement(By.xpath("//button[@class='oxd-icon-button oxd-icon-button--solid-main employee-image-action']")).click();

		driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();


		String confirmation_message=driver.findElement(By.xpath("//h6[normalize-space()='Personal Details']")).getText();

		if (confirmation_message.contains("Personal Details")) {
			System.out.println("Employee added succesfully");
		}
		else {
			System.out.println("Failed to add Employee");
		}
		logOut();
		Assert.assertEquals("Personal Details", confirmation_message);	

	}

	@Test(priority =4,enabled=false)
	public  void searchEmployeeByName() throws InterruptedException
	{
		login();

		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		driver.findElement(By.xpath("//a[contains(normalize-space(),'Employee List')]")).click();
		driver.findElements(By.tagName("input")).get(1).sendKeys("Radha gupta");


		driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();

		Thread.sleep(5000);

		List<WebElement> element =driver.findElements(By.xpath("//span[@class='oxd-text oxd-text--span']"));

		String expected_message ="Records Found";
		String actual_message= element.get(0).getText();
		System.out.println(actual_message);

		/*	for (int i=0;i<element.size();i++)
		{
			System.out.println("At index:" + i+ "Text is" + element.get(i).getText());
		}*/
		logOut();
		//Assert.assertEquals(expected_message, actual_message);
		Assert.assertTrue(actual_message.contains(expected_message));
	}
	@Test(priority=5,enabled =false)
	public void searchEmployeeById() throws InterruptedException
	{
		String empid="00392";
		String message_actual="00392";
		login();

		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[contains(normalize-space(),'Employee List')]")).click();
		driver.findElements(By.tagName("input")).get(2).sendKeys(empid);

		Thread.sleep(5000);
		driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();

		Thread.sleep(5000);

		JavascriptExecutor  executor=(JavascriptExecutor) driver;
		executor.executeScript("window.scrollBy(0," + 500 +  ")");

		Thread.sleep(5000);

		List<WebElement> rows=driver.findElements(By.xpath("//div[@role='row']"));

		if(rows.size()>1) {

			message_actual=driver.findElement(By.xpath("((//div[@role='row'])[2]/div[@role='cell'])[2]")).getText();
		}
		logOut();
		Assert.assertEquals(empid, message_actual);
	}

	@Test(priority=6,enabled =false)
	public void fileUpload() throws IOException, InterruptedException
	{
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		driver.findElement(By.xpath("//span[@class='oxd-topbar-body-nav-tab-item']")).click();
		driver.findElement(By.partialLinkText("Data ")).click();
		driver.findElement(By.xpath("//div[@class='oxd-file-button']")).click();

		Runtime.getRuntime().exec("C://Bharat_SeleniumDemos//SeleniumPractice//UploadFileSeleniumHRMPractice.exe");
		Thread.sleep(5000);
		driver.findElement(By.xpath("//button[normalize-space()='Upload']")).submit();

		logOut();
	}

	@Test(priority=7,enabled =false)
	public void deleteEmployee() throws InterruptedException
	{
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[contains(normalize-space(),'Employee List')]")).click();
		driver.findElements(By.tagName("input")).get(1).sendKeys("Sharath V");

		driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();

		Thread.sleep(5000);

		driver.findElement(By.xpath("//i[@class='oxd-icon bi-trash']")).click();

		Thread.sleep(3000);

		driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--label-danger orangehrm-button-margin']")).click();

		//String mesg =driver.findElement(By.xpath("//span[norm]alize-space()='No Records Found'")).getText();
		String mesg =driver.findElement(By.xpath("[@class='oxd-text oxd-text--span'][1]")).getText();

		Assert.assertEquals(mesg, "No Records Found");

		Thread.sleep(3000);

		logOut();

	}

	@Test(priority=8,enabled=true)
	public void ListEmployee() throws InterruptedException
	{
		login();
		driver.findElement(By.xpath("//span[text()='PIM']")).click();
		driver.findElement(By.xpath("//a[contains(normalize-space(),'Employee List')]")).click();

		Thread.sleep(3000);

		List<WebElement> total_linkelements = driver.findElements(By.xpath("//ul[@class='oxd-pagination__ul']/li"));

		int totallinks= total_linkelements.size();

		for (int i=0;i<totallinks;i++)
		{
			String currentlinktext= total_linkelements.get(i).getText();

			try 
			{
				int page= Integer.parseInt(currentlinktext);

				System.out.println("Page :" +page);
				total_linkelements.get(i).click();
				List<WebElement> emp_list =driver.findElements(By.xpath("(//div[@class='oxd-table-card'])/div/div[4]"));
				for(int j=0;j<emp_list.size();j++) {

					//print last name of each row
					String lastname =emp_list.get(j).getText();
					System.out.println(lastname);
				}
			}
			catch(Exception e)
			{
				System.out.println("Not a Number");

			}

		}
		Thread.sleep(5000);
		logOut();
	}

	@AfterTest
	public void teardown() throws InterruptedException
	{
		//logOut();
		Thread.sleep(7000);
		driver.close();
		driver.quit();

	}

}
