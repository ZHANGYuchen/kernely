/*
Copyright 2011 Prometil SARL

This file is part of Kernely.

Kernely is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

Kernely is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public
License along with Kernely.
If not, see <http://www.gnu.org/licenses/>.
*/
package org.kernely.core.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kernely.core.hibernate.AbstractModel;
import org.kernely.core.resources.AbstractController;
import org.quartz.Job;
import org.quartz.Trigger;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public abstract class AbstractPlugin extends AbstractModule {
	
	
	//the controller list
	private List<Class<? extends AbstractController>> controllers;
	
	//the model list
	private List<Class<? extends AbstractModel>> models;
	
	//the job map
	private Map<Class<? extends Job>, Trigger> jobs;
	
	//the name of the abstract plugin
	private String name;
	
	//the path of the plugin
	private String path;
	
	//the configuration path
	private String configurationFilepath;
	
	
	public AbstractPlugin(String pName, String pPath){
		name = pName;
		path = pPath;
		controllers = new  ArrayList<Class<? extends AbstractController>>();
		models = new ArrayList<Class<? extends AbstractModel>>();
		jobs = new HashMap<Class<? extends Job>, Trigger>();
	}
	
	/**
	 * The plugin is injected just before this method.
	 */
	public void start(){

	}
	
	protected void registerModel(Class<? extends AbstractModel> model){
		models.add(model);
		
	}
	
	protected void registerController(Class<? extends AbstractController> controller){
		controllers.add(controller);
	}
	
	protected void registerConfigurationPath(String pFilepath){
		configurationFilepath = pFilepath;
	}
	public Module getModule(){
		return this;
	}
	
	
	/**
	 * Returns the name of the plugin
	 * @return the name of the plugin
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Return the plugin path
	 * @return the plugin path
	 */
	public String getPath(){
		return path;
	}
	/**
	 * Add a job to the list of jobs
	 * @param job the job to add
	 * @param trigger the trigger
	 */
	protected void registerJob(Class <? extends Job> job, Trigger trigger){
		jobs.put(job, trigger);
	}
	
	/**
	 * Return the configuration filepath
	 * @return the configuration file path
	 */
	public String getConfigurationFilepath(){
		return configurationFilepath;
	}
	
	/**
	 * Returns the controller list
	 * @return the resources list 
	 */
	public List<Class<? extends AbstractController>> getControllers(){
		return controllers;
	}
	
	/**
	 * The methods returns the models
	 * @return the method returns the model
	 */
	public List<Class<? extends AbstractModel>> getModels(){
		return models;
	}
	
	/**
	 * The method returns the list of job with there associated trigger
	 * @return the map of job and trigger
	 */
	public Map<Class<? extends Job>, Trigger> getJobs() {
		return jobs;
	}


	@Override
	protected void configure() {
		//do nothing
	}
	
	
	
	
}
