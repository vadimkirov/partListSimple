package com.example.partlist.controller;


import com.example.partlist.model.PartModel;
import com.example.partlist.repository.MainRepository;
import com.example.partlist.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final PartService service;

    private final MainRepository repository;

    @Autowired
    public MainController(MainRepository repository, PartService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/")
    public String hi(){
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       Model model,
                       @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC)
                               Pageable pageable){
        getMainPage(filter, model, pageable);
        return "index";
    }

    private void getMainPage(@RequestParam(required = false, defaultValue = "") String filter,
                             Model model, @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC)
                                     Pageable pageable) {
        Page<PartModel> page = getPartList(filter, pageable) ;
        model.addAttribute("sumComps",service.computersForAssembly());
        model.addAttribute("url", "/main");
        model.addAttribute("page", page);
        model.addAttribute("filter",filter);
    }

    @PostMapping
    public String add(@RequestParam(value = "partTitle", defaultValue = "?*?") String partTitle,
                      @RequestParam(value = "partQuantity", required = false, defaultValue = "0") Integer partQuantity,
                      @RequestParam(value = "partRequared", required = false, defaultValue = "false") Boolean partRequired,
                      @RequestParam(required = false, defaultValue = "") String filter,
                      @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC)
                              Pageable pageable,
                      Model model){

        if(!partTitle.equals("?*?") && partQuantity>=0) {
            PartModel newDetail = new PartModel(partTitle,partQuantity,partRequired);
            repository.save(newDetail);}
        getMainPage(filter, model, pageable);
        return "redirect:/main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("part", repository.getOne(id));
        return "editOldPart";
    }

    @PostMapping("/update")
    public String update(@RequestParam("partId") PartModel updated,
                         @RequestParam("partTitle") String title,
                         @RequestParam("partQuantity") Integer quantity,
                         @RequestParam(value = "partRequired", defaultValue = "") String required){
        Boolean i_required = required.equals("on");
        updated.setTitle(title);
        updated.setQuantity(quantity);
        updated.setRequired(i_required);
        repository.save(updated);
        return "redirect:/main";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        repository.deleteById(id);
        return "redirect:/main";
    }

    @GetMapping("/requared")
    public String getRequared(@RequestParam(required = false, defaultValue = "") String filter,
                              Model model,
                              @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC)
                                      Pageable pageable){

        Page<PartModel> page = repository.findByRequired(true,pageable);
        model.addAttribute("sumComps",service.computersForAssembly());
        model.addAttribute("url","/requared");
        model.addAttribute("page", page);
        model.addAttribute("filter",filter);
        return "index";
    }


    @GetMapping("/norequared")
    public String getNoRequared(@RequestParam(required = false, defaultValue = "") String filter,
                                Model model,
                                @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC)
                                        Pageable pageable){

        Page<PartModel> page = repository.findByRequired(false,pageable);
        model.addAttribute("sumComps",service.computersForAssembly());
        model.addAttribute("url","/norequared");
        model.addAttribute("page", page);
        model.addAttribute("filter",filter);
        return "index";
    }


    private Page<PartModel> getPartList(@RequestParam(required = false, defaultValue = "") String filter,
                                        @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC)
                                                Pageable pageable) {
        Page<PartModel> parts ;
        if(filter != null && !filter.isEmpty()) {
            parts = repository.findAllByTitleIsContainingIgnoreCase(filter, pageable);
        }else {
            parts = repository.findAll(pageable);
        }
        return parts;
    }

}
