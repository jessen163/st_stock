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
package org.jopenexchg.matcher;

import java.util.*;

import org.jopenexchg.pool.WithId;


public final class PriceLeader implements WithId
{
	// 
	private int id = 0;
	
	// ���ȼ�������Խ��Խ���ں���. ����Ӧ�ü۸�Խ�����ȼ�Խ��; ��Ӧ�ü۸�Խ�����ȼ�Խ��
	public long prior = 0;
	
	public long ordPrc = 0;		// �Ͷ��������ֵ��Ӧ
	public long price = 0;		// ���Ŷ���ʱ���ֵ��Ӧ
	public long accumQty = 0;
	
	public long ocQtySum = 0;	// ���Ͼ��ۼ�������ʱ��ʹ��
	
	public LinkedList<Order> orderList = new LinkedList<Order>();

	@Override
	public final int getId() 
	{
		return id;
	}

	@Override
	public final void setId(int id) 
	{
		this.id = id;
	}

}