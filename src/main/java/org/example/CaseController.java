package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cases")
public class CaseController {
    @Autowired
    private ArangoCaseService arangoCaseService;

    @GetMapping
    public List<Case> getAllCases() {
        return arangoCaseService.getAllCases();
    }
}
