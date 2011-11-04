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
package org.kernely.core.test;

import groovy.text.SimpleTemplateEngine;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.BaseConfiguration;
import org.kernely.core.hibernate.EntityManagerProvider;
import org.kernely.core.plugin.PluginsLoader;
import org.kernely.core.service.mail.Mailer;
import org.kernely.core.service.mail.builder.MailBuilder;
import org.kernely.core.template.TemplateRenderer;
import org.mockito.Mockito;

import com.google.common.eventbus.EventBus;
import com.google.guiceberry.GuiceBerryModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * @author g.breton
 *
 */
public class StreamTestModule extends AbstractModule {

	/**
	 * 
	 */
	@Override
	protected void configure() {
		install(new GuiceBerryModule());
		bind(PluginsLoader.class);
		bind(TemplateRenderer.class);
		Mailer mailerMock = Mockito.mock(Mailer.class);
		MailBuilder mailBuilderMock = Mockito.mock(MailBuilder.class);
		Mockito.when(mailBuilderMock.cc(Mockito.anyString())).thenReturn(mailBuilderMock);
		Mockito.when(mailBuilderMock.to(Mockito.anyString())).thenReturn(mailBuilderMock);
		Mockito.when(mailBuilderMock.subject(Mockito.anyString())).thenReturn(mailBuilderMock);
		Mockito.when(mailerMock.create(Mockito.anyString())).thenReturn(mailBuilderMock);
		bind(Mailer.class).toInstance(mailerMock);
		bind(EntityManagerProvider.class).to(HibernateTestUtil.class);
		bind(SimpleTemplateEngine.class);
		bind(EventBus.class);

	}
	
	@Provides
	protected AbstractConfiguration getAbstractConfiguration(){
		return new BaseConfiguration();
	}

}
