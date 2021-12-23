
package com.tengmoney.autoframework;


import com.tengmoney.foundation.report.ReportContext;
import com.tmoney.foundation.utils.Configuration;
import com.tmoney.foundation.utils.Configuration.Parameter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.openqa.selenium.remote.BrowserType.HTMLUNIT;

/**
 * Screenshot manager for operation with screenshot capturing, resizing and
 * removing of old screenshot folders.
 * 
 * @author zhzh.yin
 */
@Slf4j
public class Screenshot
{

	public static String capture(WebDriver driver)
	{
		//todo 为什么不能直接用Parameter?
		return capture(driver, Configuration.getBoolean(Parameter.AUTO_SCREENSHOT) && !Configuration.getBoolean(Parameter.TAKE_ONLY_FAIL_SCREENSHOT));
	}
	
	/**
	 * Captures web-browser screenshot, creates thumbnail and copies both images
	 * to specified sceenshots location.
	 * 
	 * @param driver
	 *            instance used for capturing.
	 * @return screenshot name.
	 */
	public static String capture(WebDriver driver, boolean isTakeScreenshot)
	{
		String screenName = "";
		
		if (isTakeScreenshot && !HTMLUNIT.equalsIgnoreCase(Configuration.get(Parameter.BROWSER)))
		{
			try
			{
				// Define test screenshot root
				String test = DriverPool.getTestNameByDriver(driver);
				if (StringUtils.isEmpty(test)){
					return null;}
				File testScreenRootDir = ReportContext.getTestDir(test);

				// Capture full page screenshot and resize
				String fileID = test.replaceAll(" ", "_") + "-" + System.currentTimeMillis();
				screenName = fileID + ".png";
				String fullScreenPath = testScreenRootDir.getAbsolutePath() + "/" + screenName;
				File fullScreen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				resizeImg(fullScreen, Configuration.getInt(Parameter.BIG_SCREEN_WIDTH), Configuration.getInt(Parameter.BIG_SCREEN_HEIGHT));
				FileUtils.copyFile(fullScreen, new File(fullScreenPath));

				// Create screenshot thumbnail
				String thumbScreenPath = fullScreenPath.replace(screenName, "/thumbnails/" + screenName);
				File thumbScreen = new File(thumbScreenPath);
				FileUtils.copyFile(fullScreen, thumbScreen);
				resizeImg(thumbScreen, Configuration.getInt(Parameter.SMALL_SCREEN_WIDTH),
						Configuration.getInt(Parameter.SMALL_SCREEN_HEIGHT));
			}
			catch ( IOException e)
			{
				log.error("Problems with screenshot capturing!");
			}
		}
		return screenName;
	}

	/**
	 * Resizes image according to specified dimensions.
	 * 
	 * @param imageFile
	 *            - image to resize.
	 * @param width
	 *            - new image width.
	 * @param height
	 *            - new image height.
	 */
	public static void resizeImg(File imageFile, int width, int height)
	{
		try
		{
			BufferedImage bufImage = ImageIO.read(imageFile);
			bufImage = Scalr.resize(bufImage, Scalr.Method.BALANCED, Scalr.Mode.FIT_TO_WIDTH, width, height, Scalr.OP_ANTIALIAS);
			if (bufImage.getHeight() > height)
			{
				bufImage = Scalr.crop(bufImage, bufImage.getWidth(), height);
			}
			ImageIO.write(bufImage, "png", imageFile);
		}
		catch (Exception e)
		{
			log.error("Image scaling problem!");
		}
	}
}
