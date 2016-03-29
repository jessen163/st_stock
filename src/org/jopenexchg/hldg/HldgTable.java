/* Java Open Exchange(jOpenExchg) Project
 *
 * Copyright (C) 2013  Alex Song
 *
 * This file is part of jOpenExchg.  
 *
 * jOpenExchg is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * jOpenExchg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jopenexchg.hldg;

import java.util.*;

import org.jopenexchg.pool.*;

/**
 * 
 * �ֱֲ�����Ͳ������.
 *
 */
public final class HldgTable
{
	static final int DEF_HLDG_TABLE_SIZE = 1000;
	
	private AllocOnlyPool<Hldg> hldgPool = null;
	private HashMap<HldgKey, Hldg> hldgTbl = null;
	
	public HldgTable(int maxSize) 
		throws InstantiationException, IllegalAccessException
	{
		if(maxSize <= 0)
		{
			maxSize = DEF_HLDG_TABLE_SIZE;
		}
	
		hldgPool = new AllocOnlyPool<Hldg>(Hldg.class, maxSize);
		hldgTbl = new HashMap<HldgKey, Hldg>(maxSize);
	}
	
	/**
	 * 
	 * @param key
	 * @return ���������û�ж�Ӧ��¼���᷵�� null�����ᴴ����¼
	 */
	public final Hldg findHldg(HldgKey key)
	{
		if(key == null)
		{
			return null;
		}
		
		return hldgTbl.get(key);
	}
	
	public final void putHldg(Hldg hldg)
	{
		if(hldg != null && hldg.key != null)
		{
			hldgTbl.put(hldg.key, hldg);
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return ���ǻ᷵��һ����¼�����û�оͻ�����һ����ӦKey�Ŀռ�¼
	 */
	public final Hldg getHldg(HldgKey key)
	{
		if(key == null)
		{
			return null;
		}
		
		Hldg hldg = hldgTbl.get(key);
		if(hldg == null)
		{
			hldg = hldgPool.getObj();
			if(hldg == null)
			{
				return null;
			}
			hldg.key = key;
			return hldg;
		}
		else
		{
			return hldg;
		}
	}
	
}
