/*
 * Copyright 2013 QAPROSOFT (http://qaprosoft.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tengmoney.foundation.report;

import com.tengmoney.foundation.log.TestLogCollector;
import com.tmoney.foundation.utils.Configuration;
import com.tmoney.foundation.utils.Configuration.Parameter;
import com.tmoney.foundation.utils.FileManager;
import com.tmoney.foundation.utils.R;
import com.tmoney.foundation.utils.ZipManager;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
public class HtmlReportGenerator
{
	private static final String REPORT_NAME = "/report.html";
	private static final String GALLERY_ZIP = "gallery-lib.zip";
	private static final String TITLE = "Test steps demo";
	
	private static final String PERF_REPORT = R.REPORT.get("perf_report");
	private static final String TIME_GRAPH_DATA = "${time_graph_data}";
	private static final String SUCCESS_GRAPH_DATA = "${success_rate_data}";
	private static final String PERF_TABLE_DATA = "${perf_table_data}";
	
	private static final String PERF_TABLE = R.REPORT.get("perf_table");
	private static final String PERF_TITLE = "${title}";
	private static final String PERF_ENV = "${env}";
	private static final String PERF_FINISH_DATE = "${finish_date}";
	private static final String REPORT_URL = "${report_url}";
	private static final String PERF_DATA = "${result_rows}";
	private static final String PERF_TITLE_ROW = R.REPORT.get("perf_title_row");
	private static final String PERF_DATA_ROW = R.REPORT.get("perf_data_row");

	public static void generate(String rootDir)
	{
		copyGalleryLib();
		List<File> testFolders = FileManager.getFilesInDir(new File(rootDir));
		for (File testFolder : testFolders)
		{
			List<File> images = FileManager.getFilesInDir(testFolder);
			createReportAsHTML(testFolder, images);
		}
	}

	private static void createReportAsHTML(File testFolder, List<File> images)
	{
		try
		{
			List<String> imgNames = new ArrayList<String>();
			for (File image : images)
			{
				imgNames.add(image.getName());
			}
			imgNames.remove("thumbnails");
			imgNames.remove("test.log");
			if(imgNames.size() == 0) {
				return;
			}
			
			Collections.sort(imgNames);

			StringBuilder report = new StringBuilder();
			for (int i = 0; i < imgNames.size(); i++)
			{
				String image = R.REPORT.get("image");
				image = image.replace("${image}", imgNames.get(i));
				image = image.replace("${thumbnail}", imgNames.get(i));
				image = image.replace("${alt}", imgNames.get(i));
				image = image.replace("${title}", TestLogCollector.getScreenshotComment(imgNames.get(i)));
				report.append(image);
			}
			String wholeReport = R.REPORT.get("container").replace("${images}", report.toString());
			wholeReport = wholeReport.replace("${title}", TITLE);
			FileManager.createFileWithContent(testFolder.getAbsolutePath() + REPORT_NAME, wholeReport);
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			log.error(e.getStackTrace().toString());
		}
	}

	private static void copyGalleryLib()
	{
		File reportsRootDir = new File(System.getProperty("user.dir") + "/" + Configuration.get(Parameter.ROOT_REPORT_DIRECTORY));
		if (!new File(reportsRootDir.getAbsolutePath() + "/gallery-lib").exists())
		{
			try
			{
				InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(GALLERY_ZIP);
				ZipManager.copyInputStream(is, new BufferedOutputStream(new FileOutputStream(reportsRootDir.getAbsolutePath() + "/"
						+ GALLERY_ZIP)));
				ZipManager.unzip(reportsRootDir.getAbsolutePath() + "/" + GALLERY_ZIP, reportsRootDir.getAbsolutePath());
				File zip = new File(reportsRootDir.getAbsolutePath() + "/" + GALLERY_ZIP);
				zip.delete();
			}
			catch (Exception e)
			{
				log.error(e.getMessage());
			}
		}
	}
}
