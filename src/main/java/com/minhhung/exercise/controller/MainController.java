package com.minhhung.exercise.controller;

import com.minhhung.exercise.model.API;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class MainController {

    @PostMapping("/analyse")
    public Map<String, Map<String, Object>> demo(@RequestBody List<String> apis) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        List<API> apiList = new ArrayList<>();
        for (String api : apis) {
            String[] data = api.split("/");
            apiList.add(new API(data[0], data[1], data[2]));
        }

        Map<String, List<API>> groupByProject = apiList
                .stream()
                .collect(Collectors.groupingBy(API::getProject));

        for (Map.Entry<String, List<API>> entryByProject : groupByProject.entrySet()) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("_count", entryByProject.getValue().size());

            Map<String, List<API>> groupByTheme = entryByProject.getValue()
                    .stream()
                    .collect(Collectors.groupingBy(API::getTheme));

            for (Map.Entry<String, List<API>> entryByTheme : groupByTheme.entrySet()) {
                Map<String, Integer> map2 = new HashMap<>();
                map2.put("_count", entryByTheme.getValue().size());


                Map<String, List<API>> groupByMethod = entryByTheme.getValue()
                        .stream()
                        .collect(Collectors.groupingBy(API::getMethod));

                for (Map.Entry<String, List<API>> entryByMethod : groupByMethod.entrySet()) {
                    map2.put(entryByMethod.getKey(), entryByMethod.getValue().size());
                }

                map1.put(entryByTheme.getKey(), map2);

            }
            result.put(entryByProject.getKey(), map1);
        }
        return result;
    }
}
