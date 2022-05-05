package com.example.discovery.Data;

import com.example.discovery.Models.Visit;

import java.util.List;

public interface FireBaseCallBackVisit {
    void onVisitResponse(List<Visit> visits);
}
