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
package com.moss.hibernate.enumeration;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;


public abstract class AbstractEnumerationUserType implements EnhancedUserType, Serializable {
	private static final int[] SQL_TYPES = {Types.VARCHAR};
	
	protected abstract Class getReturnedClass();
	protected abstract Object getInstance(String name);
	
	public int[] sqlTypes() { return SQL_TYPES; }
	public Class returnedClass() { return getReturnedClass(); }
	public boolean equals(Object x, Object y) { return x == y; }
	public Object deepCopy(Object value) { return value; }
	public boolean isMutable() { return false; }
	
	public Object nullSafeGet(ResultSet resultSet,
			String[] names,
			Object owner)
	throws HibernateException, SQLException {
		
		String name = resultSet.getString(names[0]);
		return resultSet.wasNull() ? null : getInstance(name);
	}
	
	public void nullSafeSet(PreparedStatement statement,
			Object value,
			int index)
	throws HibernateException, SQLException {
		
		if (value == null) {
			statement.setNull(index, Types.VARCHAR);
		}
		else if (value instanceof SimpleEnumeration) {
			statement.setString(index, ((SimpleEnumeration)value).name());
		}
		else {
			statement.setString(index, objectToSQLString(value));
		}
		
	}
	
	public Serializable disassemble(Object value) throws HibernateException {
		if (value instanceof SimpleEnumeration) return ((SimpleEnumeration)value).name();
		else return objectToSQLString(value);
	}
	
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return getInstance((String)cached);
	}
	
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		// return original since this persister is going to be immutable
		return original;
	}
	
	public int hashCode(Object x) throws HibernateException {
		if (x instanceof SimpleEnumeration) return ((SimpleEnumeration)x).name().hashCode();
		else return x.toString().hashCode();
	}
	
	public Object fromXMLString(String arg0) {
		return getInstance(arg0);
	}
	
	public String objectToSQLString(Object arg0) {
		if(arg0==null) return "NULL";
		else return arg0.toString();
	}
	
	public String toXMLString(Object arg0) {
		if(arg0 == null) return "";
		return arg0.toString();
	}
	
}

