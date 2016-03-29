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

public final class TradedInst
{
	static final int UL_PRICE = Integer.MAX_VALUE;
	static final int LL_PRICE = 0;
	static final int NO_PRICE = 0;
	
	// ֤ȯ����
	public int stockid = -1;
	
	// ֤ȯ���
	public byte stockname[] = null;
	
	// ����ļ۸����. Long �����ȼ�
	public TreeMap<Long, PriceLeader> buyPrcList = null;
	
	// �����ļ۸����
	public TreeMap<Long, PriceLeader> sellPrcList = null;
	
	// �����������. ���Ǹ��ݶ����� ordPrice �ֶζ��� price �ֶ�����
	public long prevClsPrc = NO_PRICE;
	public long openPrc = NO_PRICE;
	public long highPrc = LL_PRICE;
	public long lowPrc = UL_PRICE;
	
	// �ܳɽ����ͳɽ����
	public long totalValue = 0;
	public long totalAmount = 0;
	
	public TradedInst(int stockId, String stockName)
	{
		this.stockid = stockId;
		this.stockname = stockName.getBytes();
		
		buyPrcList = new TreeMap<Long, PriceLeader>();
		sellPrcList = new TreeMap<Long, PriceLeader>();
	}
	
	
	public final TreeMap<Long, PriceLeader> getPrcList(boolean isBuy)
	{
		if(isBuy)
		{
			return buyPrcList;
		}
		else
		{
			return sellPrcList;
		}
	}
	
	public final TreeMap<Long, PriceLeader> getPeerPrcTree(boolean iAmBuy)
	{
		if(!iAmBuy)
		{
			return buyPrcList;
		}
		else
		{
			return sellPrcList;
		}
	}	
	
	
	public final void addtoPrcList(boolean isBuy, PriceLeader prcLdr)
	{
		if(isBuy)
		{
			buyPrcList.put(prcLdr.prior, prcLdr);
		}
		else
		{
			sellPrcList.put(prcLdr.prior, prcLdr);
		}
	}
	

	/**
	 * 
	 * @param iAmBuy: �����ǲ�����
	 * @return null when does not exist such a peer prcldr
	 */
	public final Map.Entry<Long, PriceLeader> getBestPeerPrcLdr(boolean iAmBuy)
	{
		Map.Entry<Long, PriceLeader> bestPrcLdr = null;
		TreeMap<Long, PriceLeader> prcList = getPeerPrcTree(iAmBuy);
		
		bestPrcLdr = prcList.firstEntry();
		
		return bestPrcLdr;
	}
	
}
