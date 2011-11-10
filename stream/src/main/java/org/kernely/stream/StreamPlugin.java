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
package org.kernely.stream;

import org.kernely.core.plugin.AbstractPlugin;
import org.kernely.stream.model.Message;
import org.kernely.stream.model.Stream;
import org.kernely.stream.resources.StreamResource;
import org.kernely.stream.service.StreamService;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
/**
 * The user plugin
 *
 */
public class StreamPlugin  extends AbstractPlugin {

	@Inject
	EventBus eventBus;
	
	/**
	 * Default constructor
	 */
	public StreamPlugin() {
		super("Stream", "/streams");
		registerController(StreamResource.class);
<<<<<<< HEAD
		registerModel(Message.class);
		registerModel(Stream.class);
	}
	
	@Override
	public void start(){
		eventBus.register(UserEventHandler.class);
=======
		registerModel(StreamMessage.class);

>>>>>>> b32140e8f60de95b42a4067311d87bbc122ecaa3
	}
	
	@Override
	protected void configure() {
		bind(StreamService.class);
		bind(UserEventHandler.class);
	}
}
