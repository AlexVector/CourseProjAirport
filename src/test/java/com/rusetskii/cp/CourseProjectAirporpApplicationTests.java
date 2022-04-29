package com.rusetskii.cp;

import com.rusetskii.cp.controller.AdminController;
import com.rusetskii.cp.controller.MainController;
import com.rusetskii.cp.form.PlaneForm;
import com.rusetskii.cp.form.TicketForm;
import com.rusetskii.cp.model.CartInfo;
import com.rusetskii.cp.model.CustomerInfo;
import com.rusetskii.cp.model.OrderDetailInfo;
import com.rusetskii.cp.model.PlaneInfo;
import com.rusetskii.cp.pangination.PaginationResult;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
class CourseProjectAirporpApplicationTests {

	@Autowired
	AdminController adc;

	@Autowired
	MainController mc;


	@Test
	void ticketCreation(){
		TicketForm tf = new TicketForm();
		tf.setTicket_id("3");
		tf.setPlane_id("2");
		tf.setName("Тестовый");
		tf.setPrice(123);
		adc.hardticketSave(tf);
	}

	@Test
	void planeCreation(){
		PlaneForm pf = new PlaneForm();
		pf.setPlane_id("3");
		pf.setName("Тестовый");
		pf.setInfo("Информация");
		adc.hardplaneSave(pf);
	}

	@Test
	void showOrder(){
		List<OrderDetailInfo> list = adc.hardOrderView("1");
		System.out.println(list.get(0).getId());
		System.out.println(list.get(0).getTicketCode());
		System.out.println(list.get(0).getTicketName());
		System.out.println(list.get(0).getPrice());
		System.out.println(list.get(0).getQuanity());
		System.out.println(list.get(0).getAmount());
	}

	@Test
	void getPlanePagList(){
		PaginationResult<PlaneInfo> result = mc.hardListPlaneHandler("SuperJet", 1, 100, 10);
		System.out.println(result.getList().get(0).getPlane_id());
		System.out.println(result.getList().get(0).getName());
		System.out.println(result.getList().get(0).getInfo());
	}

	@Test
	void dataChartCollector(){
		Map<String, Integer> result = mc.hardOrderChartHandler();
		for (Map.Entry<String, Integer> res: result.entrySet()){
			System.out.println("Дата: "+res.getKey()+";  	Продано билетов: "+res.getValue());
		}
	}

	@Test
	void orderSaveChecker(){
		CartInfo cartInfo = new CartInfo();
		CustomerInfo customerInfo = new CustomerInfo();
		cartInfo.setOrderNum(4);
		customerInfo.setName("Имя");
		customerInfo.setValid(true);
		customerInfo.setAddress("Адрес");
		customerInfo.setEmail("Email");
		customerInfo.setPhone("123-45-67");
		cartInfo.setCustomerInfo(customerInfo);
		mc.hardShoppingCartConfirmation(cartInfo);
	}


}
