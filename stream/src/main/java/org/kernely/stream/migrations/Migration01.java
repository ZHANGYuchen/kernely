package org.kernely.stream.migrations;

import java.util.ArrayList;
import java.util.List;

import org.kernely.core.migrations.migrator.Command;
import org.kernely.core.migrations.migrator.CreateTable;
import org.kernely.core.migrations.migrator.Migration;
import org.kernely.core.migrations.migrator.RawSql;

/**
 * The migration script
 */
public class Migration01 extends Migration {

	/**
	 * The constructor
	 */
	public Migration01() {
		super("0.1");
	}

	/**
	 * The script
	 * @return list of commands
	 */
	@Override
	public List<Command> getList() {
		ArrayList<Command> commands = new ArrayList<Command>();
		CreateTable stream = CreateTable.name("kernely_stream");
		stream.column("id", "bigint primary key");
		stream.column("category", "varchar(50)");
		stream.column("title", "varchar(50)");
		stream.column("locked", "boolean DEFAULT false");
		stream.column("user_id", "bigint");
		RawSql streamForeignKey= new RawSql("ALTER TABLE kernely_stream ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES kernely_user (id)");
		
		commands.add(stream);
		commands.add(streamForeignKey);
		
		CreateTable message = CreateTable.name("kernely_message");
		message.column("id", "bigint primary key");
		message.column("content", "text");
		message.column("message_parent", "bigint");
		message.column("stream_id", "bigint");
		message.column("date", "timestamp");
		message.column("commentable", "boolean not null");
		message.column("user_id", "bigint");
		
		RawSql messageForeignKeyUser= new RawSql("ALTER TABLE kernely_message ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES kernely_user (id)");
		RawSql messageForeignKeyParent= new RawSql("ALTER TABLE kernely_message ADD CONSTRAINT fk_parent_id FOREIGN KEY (message_parent) REFERENCES kernely_message (id)");
		RawSql messageForeignKeyStream= new RawSql("ALTER TABLE kernely_message ADD CONSTRAINT fk_stream_id FOREIGN KEY (stream_id) REFERENCES kernely_stream (id)");
		
		commands.add(message);
		commands.add(messageForeignKeyParent);
		commands.add(messageForeignKeyStream);
		commands.add(messageForeignKeyUser);
		
		return commands; 
	}


}
