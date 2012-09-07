package org.openmrs.module.ccsp.fragment.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.annotation.FragmentParam;

public class EncountersTodayFragmentController {

    private Date defaultStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date defaultEndDate(Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);
        return cal.getTime();
    }

    public void controller(FragmentModel model,
                           @FragmentParam(value="start", required=false) Date startDate,
                           @FragmentParam(value="end", required=false) Date endDate) {

        if (startDate == null) {
            startDate = defaultStartDate();
        }
        if (endDate == null) {
            endDate = defaultEndDate(startDate);
        }
        
        model.addAttribute("stuff", "Test Attribute!");
        model.addAttribute("encounters", Context.getEncounterService().getEncounters(null, null, startDate, endDate, null, null, null, false));
    }

}
