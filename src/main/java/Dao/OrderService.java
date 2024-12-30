package Dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hibernate.Session;

import Bean.AdBean;
import Bean.OrderBean;
import IMPL.OrderBeanDaoImpl;
import dto.OrderDetailsResponseDTO;
import util.TimeForm;

public class OrderService {

	private Logger logger = Logger.getLogger(OrderService.class.getName());
	private OrderBeanDao orderBeanDao;

	public OrderService(Session session) {
		orderBeanDao = new OrderBeanDaoImpl(session);
	}
	
	public List<OrderDetailsResponseDTO> getAllOrders(){
		
		logger.info("get all orders");
		List<OrderBean> allOrders = orderBeanDao.getAllOrders();
		List<OrderDetailsResponseDTO> orderDetailsDTOs = setupOrderDetailDTOs(allOrders);
		
		logger.info("get all orders: " + orderDetailsDTOs.toString());
		
		return orderDetailsDTOs;
	}

	/**
	 * 取得篩選的訂單 filterParams = {"searchCondition":"","orderStatus":"","input":""}
	 * @param receivedData
	 * @return
	 */
	public List<OrderDetailsResponseDTO> filterOrders(Map<String, Object> filterParams) {
		
		logger.info("get filtered orders: " + filterParams.toString());

		String condition = (String) filterParams.get("searchCondition");
		String orderStatus = (String) filterParams.get("orderStatus");
		String input = (String) filterParams.get("input");
		
		List<OrderBean> filterOrders = orderBeanDao.filterOrders(condition, orderStatus, input);
		List<OrderDetailsResponseDTO> orderDetailDTOs = setupOrderDetailDTOs(filterOrders);
		
		return orderDetailDTOs;
	}

	
	public OrderDetailsResponseDTO checkOutOrderDetails(String orderId) {
		OrderBean order = orderBeanDao.getOrderBeanByTradNo(orderId);
		logger.info("訂單中的廣告資料: " + order.getAds().toString());
		return setupOrderDetailDTO(order);
	}
	
	/**
	 * 更新訂單狀態，變成取消 receivedData
	 * @param receivedData
	 * @return
	 */
	public OrderDetailsResponseDTO cancelOrder(String orderId) {
		
		logger.info("cancel order: " + orderId);
		
		OrderBean canceledOrderBean = orderBeanDao.cancelOrderStatusByTradNo(orderId);
		OrderDetailsResponseDTO responseDTO = setupOrderDetailDTO(canceledOrderBean);
		
		return responseDTO;
	}
	
	
	private OrderDetailsResponseDTO setupOrderDetailDTO(OrderBean order) {
		
		OrderDetailsResponseDTO detailDTO = new OrderDetailsResponseDTO();
		
		detailDTO.setUserId(order.getUserId());
		detailDTO.setUserName(order.getUser().getName());
		detailDTO.setOrderId(order.getMerchantTradNo());
		detailDTO.setPaidDate(TimeForm.convertZonedDateTimeToString(order.getMerchantTradDate()));
		detailDTO.setPaymentMethod(order.getChoosePayment());
		logger.info("選擇的付款方式: " + order.getChoosePayment());
		detailDTO.setOrderStatus(order.getOrderStatus());
		detailDTO.setTotalAmount(order.getTotalAmount());
		
		List<Long> houseIds = new ArrayList<>();
		List<String> houseTitles = new ArrayList<>();
		List<Long> adIds = new ArrayList<>();
		List<Long> adCouponApplied = new ArrayList<>();
		List<String> adNames = new ArrayList<>();
		List<Integer> adOriginalPrices = new ArrayList<>();
		List<Integer> adDiscountedPrices = new ArrayList<>();
		List<String> adPeriods = new ArrayList<>();
		
		List<AdBean> ads = order.getAds();
		for(AdBean ad : ads) {
			houseIds.add(ad.getHouse().getHouseId());
			houseTitles.add(ad.getHouse().getTitle());
			adIds.add(ad.getAdId());
			
			// 如果 coupno 為 1 = 有使用
			if(ad.getIsCouponUsed() == 1) {
				adCouponApplied.add(ad.getAdId()); // 將 ad id 記錄在 adCouponApplied 中
				BigDecimal discountedPrice = new BigDecimal(ad.getAdPrice().toString()).multiply(new BigDecimal("0.9"));
				discountedPrice = discountedPrice.setScale(0, RoundingMode.CEILING); // 無條件進位
				adDiscountedPrices.add(discountedPrice.intValueExact());
			}
			
			String adName = ad.getAdtype().getAdName();
			adNames.add(adName);
			
			adOriginalPrices.add(ad.getAdPrice());
			logger.info("產品價格: " + ad.getAdPrice());
			
			adPeriods.add(calculateAdPeriodStr(order.getMerchantTradDate(), adName));
		}
		
		detailDTO.setHouseIds(houseIds);
		detailDTO.setHouseTitles(houseTitles);
		detailDTO.setAdIds(adIds);
		detailDTO.setAdNames(adNames);
		detailDTO.setAdCouponApplied(adCouponApplied);
		detailDTO.setAdOriginalPrices(adOriginalPrices);
		detailDTO.setAdDisCountedPrices(adDiscountedPrices);
		detailDTO.setAdPeriods(adPeriods);

		return detailDTO;
	}
	
	private List<OrderDetailsResponseDTO> setupOrderDetailDTOs(List<OrderBean> orders) {
		
		List<OrderDetailsResponseDTO> responseDTOs = orders.stream()
				.map(this::setupOrderDetailDTO)
				.collect(Collectors.toList());
		
		return responseDTOs;
	}

	
	private String calculateAdPeriodStr(ZonedDateTime paidDate, String adName) {

		String numericPart = adName.replaceAll("\\D+","");
		
		int days = Integer.parseInt(numericPart);
		logger.info("要轉換的廣告天數 " + days);
		
		ZonedDateTime expiryDate = paidDate.plusDays(days);
		
		// 組成字串: XXXX-XX-XX ~ XXXX-XX-XX
		String paidDateStr = TimeForm.convertZonedDateTimeToString(paidDate);
		String expiryDateStr = TimeForm.convertZonedDateTimeToString(expiryDate);
		StringBuilder periodStr = new StringBuilder(paidDateStr.substring(0,10));
		periodStr.append(" ~ ");
		periodStr.append(expiryDateStr.substring(0,10));
		
		logger.info("起訖日: " + periodStr);
		
		return periodStr.toString();
	}
	
}
