package com.poly.controller;

import com.poly.dao.ProductDAO;
import com.poly.dao.RevenueStatDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class ReportController {

	@Autowired
	private ProductDAO proDao;

	@Autowired
	private RevenueStatDAO revenueStatDAO;

	/* ---- Báo cáo tồn kho cũ (nếu vẫn dùng) ---- */
	@GetMapping("/report")
	public String report(Model model) {
		model.addAttribute("items", proDao.getInventoryByCategory());
		return "report";
	}

	/* ---- Dashboard doanh thu ---- */
	@GetMapping("/admin/revenue")
	public String revenueDashboard(
			@RequestParam(name = "from", required = false)
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date from,

			@RequestParam(name = "to", required = false)
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date to,

			Model model) {

		model.addAttribute("from", from);
		model.addAttribute("to",   to);

		/* 1. Theo ngày / tháng / năm */
		model.addAttribute("byDay",   revenueStatDAO.sumByDay(from, to));
		model.addAttribute("byMonth", revenueStatDAO.sumByMonth(from, to));
		model.addAttribute("byYear",  revenueStatDAO.sumByYear(from,  to));

		/* 2 → 5. Theo SP / Danh mục / Payment / User */
		model.addAttribute("byProduct",  revenueStatDAO.sumByProduct());
		model.addAttribute("byCategory", revenueStatDAO.sumByCategory());
		model.addAttribute("byPayment",  revenueStatDAO.sumByPayment());
		model.addAttribute("byUser",     revenueStatDAO.sumByUser());

		return "admin/revenue-dashboard";   // file HTML trong canvas
	}
}
