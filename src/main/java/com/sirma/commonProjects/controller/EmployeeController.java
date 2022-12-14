package com.sirma.commonProjects.controller;

import com.sirma.commonProjects.model.ColleaguesView;
import com.sirma.commonProjects.model.Employee;
import com.sirma.commonProjects.service.EmployeeService;
import com.sirma.commonProjects.service.FileObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.apache.logging.log4j.util.Strings.isBlank;

/**
 * Controller class for processing input data in form of a CSV file
 * and redirecting to a datagrid view of the manipulated data
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class EmployeeController {
    private final FileObjectMapper<Employee> fileObjectMapper;

    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    @PostMapping("/datagrid")
    public String datagrid(
            @RequestParam MultipartFile file,
            @RequestParam String dateFormat,
            Model model) throws IOException {

        validateInputParameters(file, dateFormat);

        List<Employee> employees = fileObjectMapper.convertFileContentToModel(file, dateFormat);
        List<ColleaguesView> colleaguesViews = EmployeeService.extractEmployeesWorkingOnSameProject(employees);

        model.addAttribute("colleaguesViews", colleaguesViews);
         return "datagrid";
    }

    private void validateInputParameters(MultipartFile file, String dateFormat) {
        if (file == null || file.isEmpty() || isBlank(dateFormat)) {
            throw new IllegalArgumentException("csv file and date format pattern must be provided");
        }
    }
}
