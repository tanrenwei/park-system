package com.imust.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.imust.entity.Park;
import com.imust.entity.Order;
import com.imust.entity.Users;
import com.imust.service.ParkService;
import com.imust.service.UserService;
import com.imust.service.OrderService;

@Controller
@RequestMapping("/car")
public class ParkController {
	@Autowired
	private ParkService parkService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/car-select")
	public String getCarByKey(Model model) {
		List<Park> carList = parkService.getAll();
		model.addAttribute("carList",carList);
		model.addAttribute("carNum",carList.size());
		return "list";
	}
	
	@RequestMapping("/findCar")
	public String findCar(@RequestParam("status") int status,Model model) {
		model.addAttribute("status",status);
		if(status==-1) {
			List<Park> carList = parkService.getAll();
			model.addAttribute("carList",carList);
		}else {
			List<Park> carList = parkService.getAllByKey(status);
			model.addAttribute("carList",carList);
			model.addAttribute("carNum",carList.size());
		}
		return "list";
	}
	@RequestMapping("/detail")
	public String editCar(@RequestParam("id") int id,Model model){
		Park car = parkService.getById(id);
		model.addAttribute("car",car);
		return "detail";
	}
	
	@RequestMapping("/buy")
	public String buy(HttpSession session,@RequestParam("id") int id){
		Park car = parkService.getById(id);
		Users user = (Users)session.getAttribute("LogUser");
		int p = user.getPoint();
		car.setStatus(1);
		if(parkService.updateCarStatus(car)) {
			Order order =new Order();
			order.setUser_id(user.getId());
			order.setPark_id(id);
			if(p>=100&&p<300) {
				order.setTotal(car.getPrice()*0.9);
			}else if(p>=300&&p<500) {
				order.setTotal(car.getPrice()*0.8);
			}else if(p>=500) {
				order.setTotal(car.getPrice()*0.7);
			}
			if(orderService.addOrder(order)) {
				user.setPoint(p+10);
				userService.updatePoint(user);
			}
		}
		return "redirect:/order/showOrder";
	}
}
