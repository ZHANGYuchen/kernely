/**
 * Copyright 2011 Prometil SARL
 *
 * This file is part of Kernely.
 *
 * Kernely is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Kernely is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with Kernely.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package org.kernely.core.migrations;

import java.util.ArrayList;
import java.util.List;

import org.kernely.core.migrations.migrator.Command;
import org.kernely.core.migrations.migrator.CreateTable;
import org.kernely.core.migrations.migrator.DataBaseConstants;
import org.kernely.core.migrations.migrator.Insert;
import org.kernely.core.migrations.migrator.Migration;
import org.kernely.core.migrations.migrator.RawSql;

/**
 * Core plugin migration script for version 0.1
 */
public class Migration01 extends Migration {

	/**
	 * Constructor
	 */
	public Migration01() {
		super("0.1");
	}

	/**
	 * Script
	 * @see org.kernely.core.migrations.migrator.Migration#getList()
	 */
	@Override
	public List<Command> getList() {
		ArrayList<Command> commands = new ArrayList<Command>();
		// the table kernely _ user
		CreateTable user = CreateTable.name("kernely_user");
		user.column(DataBaseConstants.ID_COLUMN, DataBaseConstants.LONG_PK);
		user.column("username", DataBaseConstants.VARCHAR_30);
		user.column("password", DataBaseConstants.VARCHAR_300);
		user.column("locked", DataBaseConstants.BOOLEAN_DEFAULT_FALSE);

		Insert insertBoby = Insert.into("kernely_user");
		insertBoby.set(DataBaseConstants.ID_COLUMN, "1");
		insertBoby.set("username", "bobby");
		insertBoby.set("password", "$shiro1$SHA-256$500000$u/+Rmhoh59DGbMEqz7/8Wg==$V3LD3zWJXwB9ws5FKbxC2NTVEsdDv/+Xwd/5E2UawM0=");
		insertBoby.set("locked", "false");

		Insert insertJohn = Insert.into("kernely_user");
		insertJohn.set(DataBaseConstants.ID_COLUMN, "2");
		insertJohn.set("username", "john");
		insertJohn.set("password", "$shiro1$SHA-256$500000$u/+Rmhoh59DGbMEqz7/8Wg==$V3LD3zWJXwB9ws5FKbxC2NTVEsdDv/+Xwd/5E2UawM0=");
		insertJohn.set("locked", "false");

		commands.add(user);
		commands.add(insertBoby);
		commands.add(insertJohn);
		
		CreateTable manager = CreateTable.name("kernely_user_managers");
		manager.column("manager_id", DataBaseConstants.LONG_NOT_NULL);
		manager.column("user_id", DataBaseConstants.LONG_NOT_NULL);
		RawSql  managedUserForeignKey= new RawSql("ALTER TABLE kernely_user_managers ADD CONSTRAINT fk_managed_id FOREIGN KEY (manager_id) REFERENCES kernely_user (id)");
		RawSql  managerUserForeignKey= new RawSql("ALTER TABLE kernely_user_managers ADD CONSTRAINT fk_manager_id FOREIGN KEY (user_id) REFERENCES kernely_user (id)");

		commands.add(manager);
		commands.add(managedUserForeignKey);
		commands.add(managerUserForeignKey);
		
		//the table kernely group
		CreateTable group = CreateTable.name("kernely_group");
		group.column(DataBaseConstants.ID_COLUMN, DataBaseConstants.LONG_PK);
		group.column("name", DataBaseConstants.VARCHAR_30);
		
		commands.add(group);
		
		// the table kernely permission
		CreateTable permission = CreateTable.name("kernely_permission");
		permission.column(DataBaseConstants.ID_COLUMN, DataBaseConstants.LONG_PK);
		permission.column("name", DataBaseConstants.VARCHAR_30);

		commands.add(permission);
		
		//the table kernely_userDetails
		CreateTable userDetails = CreateTable.name("kernely_user_details");
		userDetails.column(DataBaseConstants.ID_COLUMN, DataBaseConstants.LONG_PK);
		userDetails.column("name", DataBaseConstants.VARCHAR_50);
		userDetails.column("firstname", DataBaseConstants.VARCHAR_50);
		userDetails.column("mail", DataBaseConstants.VARCHAR_50);
		userDetails.column("image",DataBaseConstants.VARCHAR_100);
		userDetails.column("user_id", DataBaseConstants.LONG);
		userDetails.column("address", DataBaseConstants.VARCHAR_100);
		userDetails.column("zip", DataBaseConstants.VARCHAR_5);
		userDetails.column("city", DataBaseConstants.VARCHAR_30);
		userDetails.column("nationality", DataBaseConstants.VARCHAR_30);
		userDetails.column("homephone", DataBaseConstants.VARCHAR_20);
		userDetails.column("mobilephone",DataBaseConstants.VARCHAR_20);
		userDetails.column("businessphone",DataBaseConstants.VARCHAR_20);
		userDetails.column("ssn", DataBaseConstants.VARCHAR_20);
		userDetails.column("civility", DataBaseConstants.INT);
		userDetails.column("birth", DataBaseConstants.DATE);
		userDetails.column("hire", DataBaseConstants.DATE);
		RawSql  userDetailsForeignKey= new RawSql("ALTER TABLE kernely_user_details ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES kernely_user (id)");
	
		Insert insertJohnDetails = Insert.into("kernely_user_details");
		insertJohnDetails.set(DataBaseConstants.ID_COLUMN, "5");
		insertJohnDetails.set("name", "doe");
		insertJohnDetails.set("firstname", "john");
		insertJohnDetails.set("mail", "john.doe@prometil.com");
		insertJohnDetails.set("user_id", "2");
		insertJohnDetails.set("address", "frayssnet le gelat");
		insertJohnDetails.set("zip","46250");
		insertJohnDetails.set("city","toulouse");
		insertJohnDetails.set("nationality","french");
		insertJohnDetails.set("homephone","0123456789");
		insertJohnDetails.set("mobilephone","0632154789");
		insertJohnDetails.set("businessphone","0897654321");
		insertJohnDetails.set("ssn", "123565552221111");
		insertJohnDetails.set("civility","1");
		insertJohnDetails.set("birth", "01/12/1990");
		
		Insert insertBobbyDetails = Insert.into("kernely_user_details");
		insertBobbyDetails.set(DataBaseConstants.ID_COLUMN, "6");
		insertBobbyDetails.set("name", "robert");
		insertBobbyDetails.set("firstname", "bobby");
		insertBobbyDetails.set("mail", "robert.bobby@prometil.com");
		insertBobbyDetails.set("user_id", "1");
		insertBobbyDetails.set("address", "frayssnet le gelat");
		insertBobbyDetails.set("zip","46250");
		insertBobbyDetails.set("city","toulouse");
		insertBobbyDetails.set("nationality","english");
		insertBobbyDetails.set("homephone","0123456789");
		insertBobbyDetails.set("mobilephone","0632154789");
		insertBobbyDetails.set("businessphone","0897654321");
		insertBobbyDetails.set("ssn", "123565552221111");
		insertBobbyDetails.set("civility","1");
		insertBobbyDetails.set("birth", "01/12/1990");
		
		
		commands.add(userDetails);
		commands.add(userDetailsForeignKey);
		commands.add(insertJohnDetails);
		commands.add(insertBobbyDetails);
		
		//the table role
		CreateTable role = CreateTable.name("kernely_role");
		role.column(DataBaseConstants.ID_COLUMN,DataBaseConstants.LONG_PK);
		role.column("name", DataBaseConstants.VARCHAR_30);
		
		commands.add(role);
		
		Insert userRole = Insert.into("kernely_role");
		userRole.set(DataBaseConstants.ID_COLUMN,"3");
		userRole.set("name","User");
		
		Insert adminRole = Insert.into("kernely_role");
		adminRole.set(DataBaseConstants.ID_COLUMN,"4");
		adminRole.set("name","Administrator");
		
		Insert rhRole = Insert.into("kernely_role");
		rhRole.set(DataBaseConstants.ID_COLUMN, "5");
		rhRole.set("name", "Human resource");
		
		Insert projectManagerRole = Insert.into("kernely_role");
		projectManagerRole.set(DataBaseConstants.ID_COLUMN, "6");
		projectManagerRole.set("name", "Project manager");
		
		Insert clientRole = Insert.into("kernely_role");
		clientRole.set(DataBaseConstants.ID_COLUMN, "7");
		clientRole.set("name", "Client");
		
		Insert bookKeeperRole = Insert.into("kernely_role");
		bookKeeperRole.set(DataBaseConstants.ID_COLUMN, "8");
		bookKeeperRole.set("name", "Book keeper");
				
		commands.add(userRole);
		commands.add(adminRole);
		commands.add(rhRole);
		commands.add(projectManagerRole);
		commands.add(clientRole);
		commands.add(bookKeeperRole);
		
		//the table group permision
		CreateTable groupPermission = CreateTable.name("kernely_group_permissions");
		groupPermission.column("group_id", DataBaseConstants.LONG_NOT_NULL);
		groupPermission.column("permission_id", DataBaseConstants.LONG_NOT_NULL);
		
		RawSql groupPermissionGroup = new RawSql("ALTER TABLE kernely_group_permissions ADD CONSTRAINT fk_group_id FOREIGN KEY (group_id) REFERENCES kernely_group (id)");
		RawSql groupPermissionPermission = new RawSql("ALTER TABLE kernely_group_permissions ADD CONSTRAINT fk_permission_id FOREIGN KEY (permission_id) REFERENCES kernely_permission (id)");
		RawSql groupPermissionPrimaryKey = new RawSql("ALTER TABLE kernely_group_permissions ADD PRIMARY KEY (group_id,permission_id)");
		
		commands.add(groupPermission);
		commands.add(groupPermissionPermission);
		commands.add(groupPermissionGroup);
		commands.add(groupPermissionPrimaryKey);
		
		//the table group roles
		CreateTable groupRole = CreateTable.name("kernely_group_roles");
		groupRole.column("group_id", DataBaseConstants.LONG_NOT_NULL);
		groupRole.column("role_id", DataBaseConstants.LONG_NOT_NULL);
		
		RawSql groupRoleGroup = new RawSql("ALTER TABLE kernely_group_roles ADD CONSTRAINT fk_group_id FOREIGN KEY (group_id) REFERENCES kernely_group (id)");
		RawSql groupRoleRole = new RawSql("ALTER TABLE kernely_group_roles ADD CONSTRAINT fk_roles_id FOREIGN KEY (role_id) REFERENCES kernely_role (id)");
		RawSql groupRolePrimaryKey = new RawSql("ALTER TABLE kernely_group_roles ADD PRIMARY KEY (group_id, role_id)");
		
		commands.add(groupRole);
		commands.add(groupRoleGroup);
		commands.add(groupRoleRole);
		commands.add(groupRolePrimaryKey);
		
		//  the table user_group 
		CreateTable userGroup = CreateTable.name("kernely_user_group"); 
		userGroup.column("user_id", DataBaseConstants.LONG_NOT_NULL);
		userGroup.column("group_id", DataBaseConstants.LONG_NOT_NULL);
		
		RawSql userGroupGroup = new RawSql("ALTER TABLE kernely_user_group ADD CONSTRAINT fk_group_id FOREIGN KEY ( group_id) REFERENCES kernely_group (id)");
		RawSql userGroupUser = new RawSql("ALTER TABLE kernely_user_group ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES kernely_user (id)");
		RawSql userGroupPrimaryKey = new RawSql("ALTER TABLE kernely_user_group ADD PRIMARY KEY (user_id, group_id)");
		
		commands.add(userGroup);
		commands.add(userGroupUser);
		commands.add(userGroupGroup);
		commands.add(userGroupPrimaryKey);
				
		
		// the table user_permission
		CreateTable userPermission = CreateTable.name("kernely_user_permissions"); 
		userPermission.column("user_id", DataBaseConstants.LONG_NOT_NULL);
		userPermission.column("permission_id", DataBaseConstants.LONG_NOT_NULL);
		
		RawSql userPermissionUser = new RawSql("ALTER TABLE kernely_user_permissions ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES kernely_user (id)");  
		RawSql userPermissionPermission = new RawSql("ALTER TABLE kernely_user_permissions ADD CONSTRAINT fk_permission_id FOREIGN KEY (permission_id) REFERENCES kernely_permission (id)"); 
		RawSql userPermissionPrimaryKey = new RawSql("ALTER TABLE kernely_user_permissions ADD PRIMARY KEY (user_id, permission_id)"); 
		
		commands.add(userPermission);
		commands.add(userPermissionPermission);
		commands.add(userPermissionUser);
		commands.add(userPermissionPrimaryKey);
		
		// the table user_roles
		CreateTable userRoles= CreateTable.name("kernely_user_roles"); 
		userRoles.column("user_id", DataBaseConstants.LONG_NOT_NULL);
		userRoles.column("role_id", DataBaseConstants.LONG_NOT_NULL);
		
		RawSql userRoleUser = new RawSql("ALTER TABLE kernely_user_roles ADD CONSTRAINT fk_user_id  FOREIGN KEY (user_id) REFERENCES kernely_user (id)");  
		RawSql userRoleRole = new RawSql("ALTER TABLE kernely_user_roles ADD CONSTRAINT fk_role_id FOREIGN KEY (role_id ) REFERENCES kernely_role (id)");
		RawSql userRolePrimaryKey = new RawSql("ALTER TABLE kernely_user_roles ADD PRIMARY KEY (user_id, role_id)");
		
		commands.add(userRoles);
		commands.add(userRoleRole);
		commands.add(userRoleUser);
		commands.add(userRolePrimaryKey);	
		
		Insert insertUserRole1 = Insert.into("kernely_user_roles");
		insertUserRole1.set("user_id", "2");
		insertUserRole1.set("role_id", "4");
		
		commands.add(insertUserRole1);
		
		Insert insertUserRole2 = Insert.into("kernely_user_roles");
		insertUserRole2.set("user_id", "1");
		insertUserRole2.set("role_id", "3");
		
		commands.add(insertUserRole2);
		
		CreateTable mail = CreateTable.name("kernely_mail");
		mail.column(DataBaseConstants.ID_COLUMN, DataBaseConstants.LONG_PK);
		mail.column("subject", DataBaseConstants.TEXT);
		mail.column("content", DataBaseConstants.TEXT);
		mail.column("recipients", DataBaseConstants.TEXT);
		mail.column("cc", DataBaseConstants.TEXT);
		mail.column("status",DataBaseConstants.INT);
		
		commands.add(mail);
		
		//sequence 
		RawSql hibernateSequence = new RawSql("CREATE SEQUENCE hibernate_sequence START 10");
		commands.add(hibernateSequence);
		
		return commands;
	}

}
