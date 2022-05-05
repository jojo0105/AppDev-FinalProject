package com.example.discovery.ViewModels;

import com.example.discovery.Data.FireBaseCallBackVisit;
import com.example.discovery.Data.VisiteDao;
import com.example.discovery.Models.Visit;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;

public class VisiteViewModel {
    public static VisiteDao visiteDao = new VisiteDao();

    public static Task<Void> addToViste(Visit visit) {
        return visiteDao.addToViste(visit);
    }

    // need to order by
    public static void getAllVisit(FireBaseCallBackVisit callBack){
       visiteDao.getAllVisit(callBack);
    }

    public static Query removeViste(Visit visit) {
        return visiteDao.removeViste(visit);
    }

    public static Task<Void> updateVisit (Visit visit, String visit_id) {
        return visiteDao.updateVisit(visit,visit_id);
    }


}
