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
package org.jopenexchg.matcher.biz;

import org.jopenexchg.matcher.*;

/**
 * 
 * ���ӿ�������װ�������Ҫ���á�Ȼ����ҵ����صĺ���, ����ʹ�����߼�����
 * 
 * ���������ڣ����㶩�����ȼ�������ɽ�����
 *
 * ���ȼ������÷��棺����ԽС��ζ��Խ�ȱ�����
 * 	 ���ȼ�����˼·�ǣ�
 *     ������Ȩ���̣�ͬһ�۸���ƽ�����ȿ���������������Ϊ����
 *     ������Ȩ���̣�ͬһ�۸���ƽ�����ȿ��������������Ϊ����
 *     
 *     ����������������� adjust. ��ͨ����0����Ҫ�������ȼ��ģ�adjust > 0
 *     �������̣�
 *     		prior = (price << N - adjust)
 *     �������̣�
 *          prior = -(price << N + adjust)
 *     
 *     ������adjust �ĺϷ�������� [0, 2^N -1] ��
 */

public interface BizAdaptor
{
	/**
	 * ���㶩�����ȼ�
	 * @return
	 */
	public long calcPrior(Order order);

	
	/**
	 * 
	 * ����˼�λ�µ�������ȼ���ֵ
	 * @return
	 */	
	public long calcMaxPrior(boolean isbuy, long price);
	
	/**
	 * ����order�����ԭʼ���ۼ����ڲ�ʹ�õ�price
	 */
	public long ordPrc2Price(long ordPrc);

	public long price2OrdPrc(long price);
	
}
