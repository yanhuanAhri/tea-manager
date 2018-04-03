package com.tea.mservice.manager.area.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.tea.mservice.manager.area.service.AreaService;

import java.util.List;
import java.util.Map;

/**
 * Created by liuyihao on 2017/10/12.
 *
 * 区域
 */
@Controller
@RequestMapping("/area")
public class AreaController {

    private static final Logger LOG = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    @GetMapping
    @ResponseBody
    public List<Map> list(@RequestParam Long parentId) throws Exception {
        return areaService.list(parentId);
    }
}
