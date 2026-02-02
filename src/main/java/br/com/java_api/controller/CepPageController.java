package br.com.java_api.controller;

import br.com.java_api.dto.CepResponseDTO;
import br.com.java_api.service.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CepPageController {

    @Autowired
    private CepService cepService;

    @GetMapping("/cep")
    public String showForm() {
        return "cep";
    }

    @PostMapping("/cep")
    public String buscarEndereco(@RequestParam String cep, Model model) {
        try {
            CepResponseDTO endereco = cepService.buscarEndereco(cep);
            model.addAttribute("endereco", endereco);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao buscar o endereço: " + e.getMessage());
        }

        return "cep";
    }
}