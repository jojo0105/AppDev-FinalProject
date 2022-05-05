package com.example.discovery.Data;

import androidx.annotation.NonNull;

import com.example.discovery.Models.Visit;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VisiteDao {
    private DatabaseReference visitModel;

    public VisiteDao() {
        this.visitModel = DB.selectCollection("visit");
    }

    public Task<Void> addToViste(Visit visit) {
        String id = visitModel.push().getKey();
        visit.setId(id);
        return visitModel.child(id).setValue(visit);
    }

    // need to order by
    public  void getAllVisit(FireBaseCallBackVisit callBack){
        List<Visit> visits = new ArrayList<>();
        visitModel.orderByChild("userId").equalTo(Session.getInstance().getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                visits.clear();
                for (DataSnapshot value : snapshot.getChildren()){
                    Visit visit = value.getValue(Visit.class);
                    visits.add(visit);
                }
                callBack.onVisitResponse(visits);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Query removeViste(Visit visit) {
        return visitModel.orderByChild("userId").equalTo(Session.getInstance().getUserId());


//                whereEqualTo("userId", Session.getInstance().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(QueryDocumentSnapshot value : queryDocumentSnapshots){
//                    Park park = value.get("park", Park.class);
//                    if(park.getId().equals(visit.getPark().getId())){
//                        visitModel.document(value.getId()).delete();
//                    }
//                }
//            }
//        });
    }

    public  Task<Void> updateVisit (Visit visit, String visit_id) {
        return visitModel.child(visit_id).setValue(visit);
    }

}


