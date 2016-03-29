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

public final class HldgKey
{
	/**
	 *  FIVE ELEMENTS IN ALL
	 */
	public byte accType;
	public int accNo;
	
	public int stockid;
	
	public short pbu;
	public short hldgType;
	
	/**
	 *  �ڲ��Դ����У�
	 *  ��������ֲ֣�
	 *  	ԭ������  1000 * 1000 ����������ֲ�ʹ�� 110ms
	 *  	��ԭ������ 1000 * 1000 ����������ֲ�ʹ�� 140ms
	 *  hashCode �������ٶȣ�
	 *		��ԭ������hashCode���: 142ms - 140ms = 2ms, �ɼ�������Ҫ���������������
	 *		���ʹ��ԭ�����Ͳ�������λ�Ȳ���long���ٷ���hashCode, ��ʹ��142ms - 110ms = 42ms
	 *		���ʹ��ԭ�����Ͳ�ֱ������ֶ�ֵ�󷵻�int���ܺ�ʱ�� (110ms - 110ms), ��������ʱ
	 *  �ܵ�hash�� �����ٶ�(���������hashCode�����ٶ�)��
	 *  	ԭ���������ͣ���λ������long�󷵻���hashCode: 267ms - 110ms = 157ms
	 *      ԭ�����ͣ�ֱ���ֶ���ӷ��ط�: 235ms - 110ms = 125 ms
	 *  ���ۣ�
	 *  	HldgKey����ʹ��ԭ������, hashֵ���Բ�����򵥵ķ�ʽ������, ��������Ҳ����             
	 */	
	public final int hashCode()
	{
		return accType + accNo + stockid + pbu + hldgType;
	}
	
	/**
	 * ��Ҫ�Լ�׫д equals() �Ը���ȱʡʵ�֣� this == obj 
	 * 
	 * @param obj
	 * @return
	 */
	public final boolean equals(HldgKey obj)
	{
		if(obj == null)
		{
			return false;
		}
		
		if (this == obj)
		{
			return true;
		}
		
		if(this.accType != obj.accType)
		{
			return false;
		}
		
		if(this.accNo != obj.accNo)
		{
			return false;
		}
		
		if(this.stockid != obj.stockid)
		{
			return false;
		}
		
		if(this.pbu != obj.pbu)
		{
			return false;
		}
		
		if(this.hldgType != obj.hldgType)
		{
			return false;
		}
		
		return true;
	}
	
}