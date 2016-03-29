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

import org.jopenexchg.hldg.*;

/**
*   �������ڲ���ͨ��Order �� ��������ɵġ�������ʱ������ٶ���O(1)
*   	������ʱ��ֻ�ڶ�������һ����ǲ��Ƽ���ӦPrcLdr���ۻ��������������ϴӶ�����ɾ��
*   	������ΪĿǰ����������һ��˫��������JAVA���õ����ݽṹ�����ܸ��ݶ���ֱ�Ӻܿ���
*   	˫�������ж�λ��λ�á������������DELAY��δ���Ե�λ��ʱ������ɡ�
*   	
*   	����һ���ӿ��ٶȵķ�ʽ��PrcLdr�ϵ��ۻ���������0��ʱ��Ϳ��Լ�ɾ������۸�λ
**/

public final class Order
{
	/**
	 * ���������ǺͶ������յ���ʱ����ȫһ��
	 */
	public short pbu = 0;
	public int reff = 0;

	public byte acctType = 'A';
	public int accNo = 0;
	public boolean isbuy = true;
	public int stockid = 0;
	public long ordQty = 0;
	public long ordPrc = 0;		// �����ʾ�ļ۸����ο����ֵ
	
	/**
	 * ���µ����ݲ������ύʱ�Ķ���������ȫһ�����ɱ�
	 */
	public long price = 0;			// �ڲ���֯���кʹ���õļ۸�. ������Щ�����ծȯ��Ʒ�����ۿ����������ʵ�
	public long remQty = 0;
	public boolean delflg = false;

	/**
	 * CONTEXT ���򣬲��� Lazy Loading ��ʽ
	 * 
	 * lazy loading, ��֤һ���������ֻ��ѯһ�γֲ�
	 * 		
	 * 		�ֲֿ�Ĳ�ѯ�͸����ٶ��Ǻܿ��: ���ݻ�׼���ԣ�������335W/s, ��ѯ��709W/s
	 * 
	 * 		����֤ȯ����������Ӧ����ǰ�˼���ʱ��ͼ�����ͬʱ���ô��ֶ�
	 * 		����֤ȯ�����뷽�������ڷ������ƥ���ʱ�����Ҫ��ѯ��ͬʱ���ô��ֶ�
	 * 
	 */
	public TradedInst stock = null;
	public Hldg hldg = null;	
	
	public String toString()
	{
		StringBuffer temp = new StringBuffer(256);
		
		temp.append("isBuy = ");
		temp.append(isbuy);	
		
		temp.append("; stockid = ");
		temp.append(stockid);
		
		temp.append("; ordPrice = ");
		temp.append(ordPrc);
		
		temp.append("; ordQty = ");
		temp.append(ordQty);
		
		temp.append("; remQty = ");
		temp.append(remQty);
		
		return temp.toString();
	}
	
}
