package com.zhangmagle.loader.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import com.zhangmagle.loader.ResourceLoader;
import com.zhangmagle.main.GroupManagement;
import com.zhangmagle.model.Event;

public class FileResourceLoaderImpl implements ResourceLoader{

	private final Logger LOGGER = Logger.getLogger("FileResourceLoaderImpl");
	
	private final String SEPARATOR = System.getProperty("file.separator");
	private final String WORK_DIR = System.getProperty("user.dir");
	private final String REGEX = "\\|";
	private final String EVENT_SEPARATOR = "|";
	
	private String fileName;
	private GroupManagement groupM;
	
	public FileResourceLoaderImpl(String fileName) {
		this.fileName = fileName;
		groupM = new GroupManagement();
	}
	
	public void loadResource(){
		File resourceFile = new File(WORK_DIR + SEPARATOR + fileName);
		FileInputStream fileInputStream = null;
		BufferedReader bufferReader = null;
		try {
			fileInputStream = new FileInputStream(resourceFile);
			bufferReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = "";
			while((line = bufferReader.readLine()) != null) {
				if (!line.contains(EVENT_SEPARATOR)) {
					LOGGER.severe("***event input format error*** " +  line);
					continue;
				}
				String[] eventStrs = line.trim().split(REGEX);
				if (eventStrs.length != 2 || !eventStrs[1].trim().matches("[-0-9]+")) {
					LOGGER.severe("***event input format error*** " +  line);
					continue;
				}
				int duration = Integer.parseInt(eventStrs[1].trim());
				Event event = new Event(eventStrs[0].trim(), duration);
				groupM.groupEvent(event);
			}
			bufferReader.close();
			fileInputStream.close();
		}catch (IOException e) {
			LOGGER.severe(e.getMessage());
			try {
				if (null != bufferReader) {
					bufferReader.close();
				}
				if (null != fileInputStream) {
					fileInputStream.close();
				}
			} catch (IOException subE) {
				LOGGER.severe(subE.getMessage());
			}
		}
	}
	
	
	
	public GroupManagement resultSet() {
		return groupM;
	}
}
