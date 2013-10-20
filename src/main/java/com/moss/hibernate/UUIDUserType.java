/**
 * Copyright (C) 2013, Moss Computing Inc.
 *
 * This file is part of hibernate-toolkit.
 *
 * hibernate-toolkit is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * hibernate-toolkit is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with hibernate-toolkit; see the file COPYING.  If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so.  If you do not wish to do so, delete this
 * exception statement from your version.
 */
package com.moss.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class UUIDUserType implements UserType {
	public static final String CLASS_NAME="com.moss.hibernate.UUIDUserType";
	public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
		return UUID.fromString((String) arg0);
	}

	public Object deepCopy(Object arg0) throws HibernateException {
		return arg0;
	}

	public Serializable disassemble(Object arg0) throws HibernateException {
		return ((UUID)arg0).toString();
	}

	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		if(arg0 ==null && arg1==null) return true; // both are null
		else if(arg0==null || arg1==null) return false; // just one of them is null
		else return arg0.equals(arg1); // neither of them are null
	}

	public int hashCode(Object arg0) throws HibernateException {
		return arg0.hashCode();
	}

	public boolean isMutable() {
		return false;
	}

	public Object nullSafeGet(ResultSet resultSet, String[] columnNames, Object owner) throws HibernateException, SQLException {
		String textForm = resultSet.getString(columnNames[0]);
		if(textForm==null) return null;
		else return UUID.fromString(textForm);
	}

	public void nullSafeSet(PreparedStatement statement, Object value, int columnPosition) throws HibernateException, SQLException {
		if(value==null){
			statement.setNull(columnPosition, java.sql.Types.VARCHAR);
		}else{
			statement.setString(columnPosition, ((UUID)value).toString());
		}
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	public Class returnedClass() {
		return UUID.class;
	}

	public int[] sqlTypes() {
		return new int[]{java.sql.Types.VARCHAR};
	}
	
}
