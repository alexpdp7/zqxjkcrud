package net.pdp7.zqxjkcrud.mvc;

import static java.util.Map.entry;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import net.pdp7.zqxjkcrud.dao.Dao;
import net.pdp7.zqxjkcrud.dao.Row;
import net.pdp7.zqxjkcrud.dao.Table;
import net.pdp7.zqxjkcrud.dao.Update;

@Controller
public class ZqxjkCrudController {

	@Autowired
	protected Dao dao;

	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index",
				Map.ofEntries(
						entry("tables", dao.getTables().values()),
						entry("principal", SecurityContextHolder.getContext().getAuthentication().getPrincipal())));
	}

	@RequestMapping("/table/{name}")
	public ModelAndView table(@PathVariable String name) {
		Table table = dao.getTables().get(name);
		return new ModelAndView("table",
				Map.ofEntries(
						entry("table", table),
						entry("rows", table.getRows())));
	}

	@RequestMapping("/table/{name}/new")
	public ModelAndView newView(@PathVariable String name) {
		Table table = dao.getTables().get(name);
		return new ModelAndView("row",
				Map.ofEntries(
						entry("table", table),
						entry("row", new Row.TransientRow()),
						entry("action", Update.TableAction.INSERT)));
	}

	@RequestMapping("/table/{name}/row/{id}")
	public ModelAndView rowView(@PathVariable String name, @PathVariable String id) {
		Table table = dao.getTables().get(name);
		Row row = table.getRow(id);
		return new ModelAndView("row",
				Map.ofEntries(
						entry("table", table),
						entry("row", row),
						entry("action", Update.TableAction.UPDATE)));
	}

	@PostMapping("/update")
	public String update(String next, WebRequest request) {
		Update update = new Update(dao, request.getParameterMap());
		dao.update(update);
		return "redirect:" + next;
	}
}
