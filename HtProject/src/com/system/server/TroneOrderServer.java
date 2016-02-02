package com.system.server;

import java.util.List;
import java.util.Map;

import com.system.dao.TroneOrderDao;
import com.system.model.TroneOrderModel;

public class TroneOrderServer
{
	public List<TroneOrderModel> loadTroneOrderListQiBa()
	{
		return new TroneOrderDao().loadTroneOrderListQiBa();
	}
	
	public List<TroneOrderModel> loadTroneOrderList()
	{
		return new TroneOrderDao().loadTroneOrderList();
	}
	
	public Map<String, Object> loadTroneOrder(int spId,int spTroneId,int cpId, int status,int pageIndex)
	{
		return new TroneOrderDao().loadTroneOrder(spId, spTroneId, cpId, status ,pageIndex);
	}
	
	public boolean addTroneOrder(TroneOrderModel model)
	{
		return new TroneOrderDao().addTroneOrder(model);
	}
	
	public boolean updateTroneOrder(TroneOrderModel model)
	{
		return new TroneOrderDao().updateTroneOrder(model);
	}
	
	public TroneOrderModel getTroneOrderById(int id)
	{
		return new TroneOrderDao().getTroneOrderById(id);
	}
	
	public List<TroneOrderModel> loadTroneOrderListBySpTroneId(int spTroneId)
	{
		return new TroneOrderDao().loadTroneOrderListBySpTroneId(spTroneId);				
	}
	
	public List<TroneOrderModel> loadTroneOrderListByTroneId(int troneId)
	{
		return new TroneOrderDao().loadTroneOrderListByTroneId(troneId);				
	}
	
	public List<TroneOrderModel> loadTroneOrderListByCpSpTroneId(int cpId,int spTroneId,int status)
	{
		return new TroneOrderDao().loadTroneOrderListByCpSpTroneId(cpId, spTroneId, status);
	}
}
