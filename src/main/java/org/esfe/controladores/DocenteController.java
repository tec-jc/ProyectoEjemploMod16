package org.esfe.controladores;

import org.esfe.modelos.Docente;
import org.esfe.servicios.interfaces.IDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/docentes")
public class DocenteController {
    @Autowired
    private IDocenteService docenteService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; // si no está seteado se asigna 0
        int pageSize = size.orElse(5); // tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Docente> docentes = docenteService.buscarTodosPaginados(pageable);
        model.addAttribute("docentes", docentes);

        int totalPages = docentes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "docente/index";
    }

    @GetMapping("/create")
    public String create(Docente docente){
        return "docente/create";
    }

    @PostMapping("/save")
    public String save(Docente docente, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute(docente);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "docente/create";
        }

        docenteService.crearOEditar(docente);
        attributes.addFlashAttribute("msg", "Docente creado correctamente");
        return "redirect:/docentes";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteService.buscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteService.buscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/edit";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Docente docente = docenteService.buscarPorId(id).get();
        model.addAttribute("docente", docente);
        return "docente/delete";
    }

    @PostMapping("/delete")
    public String delete(Docente docente, RedirectAttributes attributes){
        docenteService.eliminarPorId(docente.getId());
        attributes.addFlashAttribute("msg", "Docente eliminado correctamente");
        return "redirect:/docentes";
    }
}
