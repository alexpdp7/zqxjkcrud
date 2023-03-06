package net.pdp7.zqxjkcrud.mvc;

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.Map;
import net.pdp7.zqxjkcrud.dao.Dao;
import net.pdp7.zqxjkcrud.dao.Row;
import net.pdp7.zqxjkcrud.dao.Table;
import net.pdp7.zqxjkcrud.dao.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ZqxjkCrudController {

  @Autowired protected Dao dao;

  @SafeVarargs
  protected final ModelAndView model(
      String viewName, Map.Entry<? extends String, ? extends Object>... entries) {
    Map<String, Object> model = new HashMap<String, Object>(Map.<String, Object>ofEntries(entries));
    model.put("principal", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return new ModelAndView(viewName, model);
  }

  @RequestMapping("/")
  public ModelAndView index() {
    return model("index", entry("tables", dao.getTables().values()));
  }

  @RequestMapping("/table/{name}/")
  public ModelAndView table(@PathVariable String name) {
    Table table = dao.getTables().get(name);
    return model("table", entry("table", table), entry("rows", table.getRows()));
  }

  @RequestMapping("/table/{name}/new/")
  public ModelAndView newView(@PathVariable String name) {
    Table table = dao.getTables().get(name);
    return model(
        "row",
        entry("table", table),
        entry("row", new Row.TransientRow()),
        entry("action", Update.TableAction.INSERT));
  }

  @RequestMapping("/table/{name}/row/{id}")
  public ModelAndView rowView(@PathVariable String name, @PathVariable String id) {
    Table table = dao.getTables().get(name);
    Row row = table.getRow(id);
    return model(
        "row",
        entry("table", table),
        entry("row", row),
        entry("action", Update.TableAction.UPDATE));
  }

  @PostMapping("/update")
  public String update(String next, WebRequest request) {
    Update update = new Update(dao, request.getParameterMap());
    dao.update(update);
    return "redirect:" + next;
  }
}
