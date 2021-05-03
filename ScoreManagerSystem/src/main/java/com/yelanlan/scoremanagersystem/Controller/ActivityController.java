package com.yelanlan.scoremanagersystem.Controller;

import com.yelanlan.scoremanagersystem.ServiceIface.IActivityService;
import com.yelanlan.scoremanagersystem.ServiceIface.IParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IParticipateService participateService;
}
