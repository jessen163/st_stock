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

import org.jopenexchg.matcher.biz.BizAdaptor;
import org.jopenexchg.matcher.event.*;
import org.jopenexchg.pool.*;


public final class Matcher implements BizAdaptor
{
	private static int MAX_CALLAUCTION_PRICE_CNT = 2;
	
	private TradedInstList stockList = null;
	private RecyclablePool<PriceLeader> prcLdrPool = null;
	private AllocOnlyPool<Order> ordrPool = null;
	private EventHandler evtCbs = null;
	private BizAdaptor bizAdpt = null;
	private LinkedList<Long> delPrcLdrList = null;
	private LinkedList<Long> delPrcLdrList2 = null;
	
	// 集合竞价用的两个常量
	private PriceLeader lowestSellLdr = null;
	private PriceLeader highestBuyLdr = null;
	
	public Matcher(int prcLdrCnt, int orderCnt) 
		throws InstantiationException, IllegalAccessException
	{
		bizAdpt = this;
		stockList = new TradedInstList();
		prcLdrPool =  new RecyclablePool<PriceLeader>(PriceLeader.class, prcLdrCnt);
		ordrPool = new AllocOnlyPool<Order>(Order.class, orderCnt);
		delPrcLdrList = new LinkedList<Long>();
		delPrcLdrList2 = new LinkedList<Long>();
		
		lowestSellLdr = prcLdrPool.getObj();
		lowestSellLdr.price = TradedInst.LL_PRICE;
		lowestSellLdr.accumQty = 0;
		lowestSellLdr.ocQtySum = 0;
		lowestSellLdr.prior = Long.MIN_VALUE;
		
		highestBuyLdr = prcLdrPool.getObj();
		highestBuyLdr.price = TradedInst.UL_PRICE;
		highestBuyLdr.accumQty = 0;
		highestBuyLdr.ocQtySum = 0;
		highestBuyLdr.prior = Long.MIN_VALUE;
	}

	public final void setBizAdpt(BizAdaptor bizAdpt) 
	{
		if(bizAdpt != null)
		{
			this.bizAdpt = bizAdpt;
		}
	}	
	
	public final void setEvtCbs(EventHandler evtCbs)
	{
		this.evtCbs = evtCbs;
	}
	
	public final TradedInst addStock(int stockId, String stockName)
	{
		TradedInst stock = new TradedInst(stockId, stockName);
		
		stockList.addStock(stock);
		
		return stock;
	}
	
	public final Order allocOrder()
	{
		return ordrPool.getObj();
	}

	/**
	 *  用来根据slotId来撤单
	 *  
	 * @param id
	 * @return
	 */
	public final boolean delOrder(Order order)
	{
		if(order == null)
		{
			return false;
		}
		
		if(order.stock == null)
		{
			order.stock = stockList.getStock(order.stockid);
			if(order.stock == null)
			{
				return false;
			}
		}
		
		order.delflg = true;
		long prior = bizAdpt.calcPrior(order);

		TreeMap<Long, PriceLeader> prcList = order.stock.getPrcList(order.isbuy);
		PriceLeader prcLdr = prcList.get(prior);
		if(prcLdr == null)
		{
			return false;
		}
		
		prcLdr.accumQty -= order.remQty;
		if(prcLdr.accumQty <= 0)
		{
			prcList.remove(prcLdr.prior);
			prcLdrPool.putObj(prcLdr);
		}

		if(this.evtCbs != null)
		{
			evtCbs.leaveOrderBook(order);
		}	
		
		return true;
	}
	
	// 简单插入订单簿. 可能增加价格档位，增加已存在的价格档位的累积数量，订单簿增加订单
	private final boolean insertOrder(Order order)
	{
		if(order.remQty <= 0)
		{
			return false;
		}
		
		if(order.stock == null)
		{
			order.stock = stockList.getStock(order.stockid);
			if(order.stock == null)
			{
				return false;
			}			
		}
		
		long prior = bizAdpt.calcPrior(order);
		
		PriceLeader prcLdr = order.stock.getPrcList(order.isbuy).get(prior);
		
		if(prcLdr == null)
		{
			prcLdr = prcLdrPool.getObj();
			if(prcLdr == null)
			{
				return false;
			}
			
			prcLdr.prior = prior;
			prcLdr.price = order.price;
			prcLdr.ordPrc = order.ordPrc;
			
			order.stock.addtoPrcList(order.isbuy, prcLdr);
		}
		
		prcLdr.orderList.add(order);
		
		// 只有在加入单子的时候这个量才上升
		prcLdr.accumQty += order.remQty;
		
		if(this.evtCbs != null)
		{
			evtCbs.enterOrderBook(order);
		}		
		
		return true;
	}
	
	// 在这个里面会减少对手方  prcLdr.accumQty
	private final boolean matchOnePrcLvl(Order newOrd, PriceLeader prcLdr, TreeMap<Long, PriceLeader> peerPrcLdrTree, TradedInst stock)
	{
		Order oldOrd = null;
		long matchQty = 0;
		
		while(newOrd.remQty > 0)
		{
			oldOrd = prcLdr.orderList.peek();
			if(oldOrd == null)
			{
				return true;
			}
			
			if(oldOrd.delflg == true)
			{
				// delayed deleting of deleted orders from list
				prcLdr.orderList.remove();	
			}
			else
			{
				if(oldOrd.remQty <= newOrd.remQty)
				{
					matchQty = oldOrd.remQty;
					newOrd.remQty -= matchQty;
					oldOrd.remQty = 0;
					
					prcLdr.accumQty -= matchQty;
					prcLdr.orderList.remove();
					
					if(this.evtCbs != null)
					{
						evtCbs.match(newOrd, oldOrd, matchQty, prcLdr.price);
						
						evtCbs.leaveOrderBook(oldOrd);
					}						
				}
				else
				{
					matchQty = newOrd.remQty;
					oldOrd.remQty -= matchQty;
					newOrd.remQty = 0;
					
					prcLdr.accumQty -= matchQty;
					
					if(this.evtCbs != null)
					{
						evtCbs.match(newOrd, oldOrd, matchQty, prcLdr.price);
					}						
				}
			}

		}
		
		return true;
	}
	
	// 集合竞价期间插入订单簿，不做MATCH
	public final boolean ocallInsOrder(Order order)
	{
		if(order.stock == null)
		{
			order.stock = stockList.getStock(order.stockid);
			if(order.stock == null)
			{
				System.out.println("getStock() failed");
				return false;
			}
		}	
		order.remQty = order.ordQty;
		order.price = bizAdpt.ordPrc2Price(order.ordPrc);
		
		if(evtCbs != null)
		{
			evtCbs.incomingOrder(order);
		}		
		return insertOrder(order);
	}
	
	
	// 这个是连续竞价时候使用的方式，先尝试匹配再插入订单簿
	public final boolean matchInsOrder(Order order)
	{
		order.remQty = order.ordQty;
		order.price = bizAdpt.ordPrc2Price(order.ordPrc);

		if(order.stock == null)
		{
			order.stock = stockList.getStock(order.stockid);
			if(order.stock == null)
			{
				System.out.println("getStock() failed");
				return false;
			}
		}
		
		if(evtCbs != null)
		{
			evtCbs.incomingOrder(order);
		}
		
		long maxPeerPrior = bizAdpt.calcMaxPrior(!(order.isbuy), order.price);

		TreeMap<Long, PriceLeader> peerPrcLdrTree = order.stock.getPeerPrcTree(order.isbuy);
		Set<Map.Entry<Long, PriceLeader> > peerPrcLdrSet = peerPrcLdrTree.entrySet();
		
		long priceLevelCnt = 0;
		long prevPrice = TradedInst.NO_PRICE;
		
		Map.Entry<Long, PriceLeader> peerEntry = null;
		PriceLeader prcLdr = null;
		
		delPrcLdrList.clear();
		
		Iterator<Map.Entry<Long, PriceLeader>> its =  peerPrcLdrSet.iterator();
		while(its.hasNext() && (order.remQty > 0))
		{
			peerEntry = its.next();
			prcLdr = peerEntry.getValue();
			
			if(prcLdr.prior <= maxPeerPrior)
			{
				if(prevPrice != prcLdr.price)
				{
					priceLevelCnt++;
					prevPrice = prcLdr.price;
					
					// 在这里以后可以利用priceLevelCnt来控制市价订单吃多少档位
					if(priceLevelCnt >= 5)
					{
					}
				}
				
				// 针对此档位上的订单列表进行匹配
				if(false == matchOnePrcLvl(order, prcLdr, peerPrcLdrTree, order.stock))
				{
					System.out.println("matchOnePrcLvl() failed");
					return false;
				}
				
				// 放入待回收价格档位列表
				if(prcLdr.accumQty <= 0 )
				{
					delPrcLdrList.add(prcLdr.prior);
				}
			}
			else
			{
				break;
			}
		}

		// 删除用完的对手方价格档位并回收到池中
		Iterator<Long> myIter = delPrcLdrList.iterator();
		while(myIter.hasNext())
		{
			PriceLeader rmvLdr = peerPrcLdrTree.remove(myIter.next());
			prcLdrPool.putObj(rmvLdr);
		}
		
		if(this.evtCbs != null)
		{
			evtCbs.noMoreMatch(order);
		}			
		
		if(order.remQty > 0)
		{
			if(insertOrder(order))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}


	/**
	 *  这里是缺省的业务处理实现
	 */
	
	protected final long calcBasePrior(boolean isbuy, long price)
	{
		long basePrior = 0;
		
		if(isbuy)
		{
			basePrior = -price;
		}
		else
		{
			basePrior = price;
		}
		
		return basePrior;		
	}
	
	@Override
	public final long calcPrior(Order order) 
	{
		return calcBasePrior(order.isbuy, order.price);
	}

	@Override
	public final long calcMaxPrior(boolean isbuy, long price) 
	{
		return calcBasePrior(isbuy, price);
	}

	@Override
	public final long ordPrc2Price(long ordPrc) 
	{
		return ordPrc;
	}

	@Override
	public final long price2OrdPrc(long price) 
	{
		return price;
	}
	
	private final void sumPrcLdrQtyFromHead(NavigableMap<Long, PriceLeader> prcLdrMap)
	{
		Long key = null;
		PriceLeader prcLdr = null;
		Map.Entry<Long, PriceLeader> entry = null;
			
		long qtySum = 0;
		entry = prcLdrMap.firstEntry();
		while(entry != null)
		{
			key = entry.getKey();
			prcLdr = entry.getValue();
			
			qtySum += prcLdr.accumQty;
			prcLdr.ocQtySum = qtySum;
			
			entry = prcLdrMap.higherEntry(key);
		}		
	}
	
	private final long min(long a, long b)
	{
		if(a < b)
		{
			return a;
		}
		else
		{
			return b;
		}
	}
	
	public final boolean calcCallAuction(TradedInst stock, CallAuctionResult result)
	{
		long maxBuyPrior = 0;
		long maxSellPrior = 0;
		Long buyKey = null;
		Long sellKey = null;
		PriceLeader buyLdr = null;
		PriceLeader sellLdr = null;
		Map.Entry<Long, PriceLeader> buyEntry = null;
		Map.Entry<Long, PriceLeader> sellEntry = null;
		
		if(stock == null || result == null)
		{
			return false;
		}
		
		sellEntry = stock.sellPrcList.firstEntry();
		if(sellEntry == null)
		{
			return false;
		}
		sellLdr = sellEntry.getValue();
		
		buyEntry = stock.buyPrcList.firstEntry();
		if(buyEntry == null)
		{
			return false;
		}		
		buyLdr = buyEntry.getValue();
		
		if(sellLdr.price > buyLdr.price)
		{
			// 订单簿完全没有交叉
			return false;
		}

		maxBuyPrior = bizAdpt.calcMaxPrior(true, sellLdr.price);
		maxSellPrior = bizAdpt.calcMaxPrior(false, buyLdr.price);
		
		NavigableMap<Long, PriceLeader> sellLdrSet = stock.sellPrcList.headMap(maxSellPrior, true);
		NavigableMap<Long, PriceLeader> buyLdrSet = stock.buyPrcList.headMap(maxBuyPrior, true);
		
		// 累积买集合竞价的量. 卖集合竞价的量的累积放在下面顺便做，减少一次遍历
		sumPrcLdrQtyFromHead(buyLdrSet);
		
		// 开始计算集合竞价出清价
		buyEntry = buyLdrSet.lastEntry();

		// 中间结果
		long qtyHigh = 0;	
		PriceLeader bestPrcLdr0 = null;
		PriceLeader bestPrcLdr1 = null;
		long bestPrcCnt = 0;

		// 比较用的窗口
		PriceLeader currSellLdr = null;
		PriceLeader lastSellLdr = null;
		
		PriceLeader nextBuyLdr = null;
		PriceLeader currBuyLdr = null;
		
		// 这个是落在最低卖价下面的一个KEY。模拟预读
		currSellLdr = lowestSellLdr;
		sellKey = lowestSellLdr.prior;
		
		// 买方真正预先读取价格最低的那一档. 这个一定存在
		buyKey = buyEntry.getKey();
		buyLdr = buyEntry.getValue();
		nextBuyLdr = buyLdr;
		
		boolean sellPrcUp = true;
		boolean buyPrcUp = true;
		
		while(true)
		{
			// SELL 一方按需读取当前档位
			if(sellKey != null &&  sellPrcUp == true)
			{
				sellEntry = sellLdrSet.higherEntry(sellKey);
				if(sellEntry != null)
				{
					lastSellLdr = currSellLdr;
					
					sellKey = sellEntry.getKey();				
					sellLdr = sellEntry.getValue();
					
					currSellLdr = sellLdr;
					currSellLdr.ocQtySum = lastSellLdr.ocQtySum + currSellLdr.accumQty;
				}
				else
				{
					sellKey = null;
				}
			}
			
			// 买入方按需预读下一档 
			if(buyKey != null && buyPrcUp == true)
			{
				buyEntry = buyLdrSet.lowerEntry(buyKey);
				if(buyEntry != null)
				{
					currBuyLdr = nextBuyLdr;

					buyKey = buyEntry.getKey();				
					buyLdr = buyEntry.getValue();
					
					nextBuyLdr = buyLdr;
				}
				else
				{
					currBuyLdr = nextBuyLdr;					
					nextBuyLdr = highestBuyLdr;
					
					buyKey = null;
				}
			}			
			
			long minQty = 0;
			
			// 此时可以做判断
			if(currSellLdr.price <= currBuyLdr.price)
			{
				minQty = min(currSellLdr.ocQtySum, currBuyLdr.ocQtySum);
				if(minQty > qtyHigh)
				{
					// 可以出清较低的卖盘 和 较高的买盘
					if(minQty >= lastSellLdr.ocQtySum && minQty >= nextBuyLdr.ocQtySum)
					{
						bestPrcCnt = 1;
						bestPrcLdr0 = currSellLdr;
						qtyHigh = minQty;
					}
				}
				else if(minQty == qtyHigh)
				{
					// 可以出清较低的卖盘 和 较高的买盘
					if(minQty >= lastSellLdr.ocQtySum && minQty >= nextBuyLdr.ocQtySum)
					{
						bestPrcCnt++;
						if(bestPrcCnt > MAX_CALLAUCTION_PRICE_CNT)
						{
							System.out.println("Strange error in ocall");
						}
						bestPrcLdr1 = currSellLdr;
					}					
				}
				else if(minQty < qtyHigh)
				{
					break;
				}
				
				sellPrcUp = true;
				if(currSellLdr.price == currBuyLdr.price)
				{
					buyPrcUp = true;
				}
				else
				{
					buyPrcUp = false;
				}
			}
			else
			{
				sellPrcUp = false;
				buyPrcUp = true;
			}
					
			if( (sellKey == null) && (buyKey == null))
			{
				break;
			}
		}
		
		// now fill the result
		if(bestPrcCnt == MAX_CALLAUCTION_PRICE_CNT)
		{
			result.ordPrc = (bestPrcLdr0.ordPrc + bestPrcLdr1.ordPrc) / MAX_CALLAUCTION_PRICE_CNT;
		}
		else
		{
			result.ordPrc = bestPrcLdr0.ordPrc;
		}
		result.price = bizAdpt.ordPrc2Price(result.ordPrc);
		result.volume = qtyHigh;
		
		return true;
	}

	
	public final boolean doCallAuction(TradedInst stock, CallAuctionResult result)
	{
		PriceLeader buyLdr = null;
		PriceLeader sellLdr = null;
		Order buyOrd = null;
		Order sellOrd = null;
		
		if(stock == null || result == null)
		{
			return false;
		}

		if(result.volume <= 0)
		{
			// 订单簿完全没有交叉
			return true;
		}
		
		long remainQty = result.volume;
		
		Iterator<Map.Entry<Long, PriceLeader>> itsB =  stock.buyPrcList.entrySet().iterator();
		Iterator<Map.Entry<Long, PriceLeader>> itsS =  stock.sellPrcList.entrySet().iterator();
		
		if(!itsB.hasNext())
		{
			return false;
		}
		else
		{
			buyLdr = itsB.next().getValue();
		}		

		if(!itsS.hasNext())
		{
			return false;
		}		
		else
		{
			sellLdr = itsS.next().getValue();
		}
		
		delPrcLdrList.clear();
		delPrcLdrList2.clear();
		
		boolean nextBuyOrd = true;
		boolean nextSellOrd = true;
		long qty = 0;
		
		while(remainQty > 0)
		{
			// Get an buy order when needed
			while(nextBuyOrd == true)
			{
				buyOrd = buyLdr.orderList.poll();
				if(buyOrd == null)
				{
					if(!itsB.hasNext())
					{
						return false;
					}
					else
					{
						buyLdr = itsB.next().getValue();
						continue;
					}
				}
				else
				{
					if(buyOrd.delflg == true)
					{
						continue;
					}
					else
					{
						break;
					}
				}
			}
			
			// Get a sell order when needed
			while(nextSellOrd == true)
			{
				sellOrd = sellLdr.orderList.poll();
				if(sellOrd == null)
				{
					if(!itsS.hasNext())
					{
						return false;
					}
					else
					{
						sellLdr = itsS.next().getValue();	
						continue;
					}
				}
				else
				{
					if(sellOrd.delflg == true)
					{
						continue;
					}
					else
					{
						break;
					}
				}
			}
			
			// match their quantity
			qty = min(buyOrd.remQty, sellOrd.remQty);
			qty = min(qty, remainQty);
			
			buyOrd.remQty -= qty;
			buyLdr.accumQty -= qty;
			
			if(buyLdr.accumQty <= 0)
			{
				delPrcLdrList.add(buyLdr.prior);
			}
			
			sellOrd.remQty -= qty;
			sellLdr.accumQty -= qty;

			if(sellLdr.accumQty <= 0)
			{
				delPrcLdrList2.add(sellLdr.prior);
			}			
			
			remainQty -= qty;
			
			if(this.evtCbs != null)
			{
				evtCbs.callAuctionMatch(buyOrd, sellOrd, qty, result.price);
				
			}
			
			if(buyOrd.remQty <= 0)
			{
				if(this.evtCbs != null)
				{
					evtCbs.leaveOrderBook(buyOrd);
				}	
				
				nextBuyOrd = true;
			}
			else
			{
				nextBuyOrd = false;
			}
			
			if(sellOrd.remQty <= 0)
			{
				if(this.evtCbs != null)
				{
					evtCbs.leaveOrderBook(sellOrd);
				}	
				
				nextSellOrd = true;
			}
			else
			{
				nextSellOrd = false;
			}
		}
		
		// Delete and recycle PRICE LEADERS
		Iterator<Long> myIter = delPrcLdrList.iterator();
		while(myIter.hasNext())
		{
			PriceLeader rmvLdr = stock.buyPrcList.remove(myIter.next());
			prcLdrPool.putObj(rmvLdr);
		}		
		
		myIter = delPrcLdrList2.iterator();
		while(myIter.hasNext())
		{
			PriceLeader rmvLdr = stock.sellPrcList.remove(myIter.next());
			prcLdrPool.putObj(rmvLdr);
		}		
		
		return true;
	}
	
	
}
