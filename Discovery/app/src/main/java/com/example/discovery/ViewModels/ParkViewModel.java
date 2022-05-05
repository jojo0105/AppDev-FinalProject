package com.example.discovery.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.discovery.Models.Park;

import java.util.List;

public class ParkViewModel extends ViewModel {
    private static ParkViewModel instance;

    public static ParkViewModel getInstance(ViewModelStoreOwner activity){
        if(instance == null){
            instance = new ViewModelProvider(activity)
                    .get(ParkViewModel.class);
        }
        return instance;
    }
    private final MutableLiveData<Park> selectedPark = new MutableLiveData<>();
    private final MutableLiveData<String> code = new MutableLiveData<>();
    private final MutableLiveData<List<Park>> selectedParks = new MutableLiveData<>();

    public LiveData<String> getCode() {
        return code;
    }

    public void selectCode(String c) {
        code.setValue(c);
    }

    public LiveData<Park> getSelectedPark() {
        return selectedPark;
    }

    public void setSelectedPark(Park park) {
        selectedPark.setValue(park);
    }

    public LiveData<List<Park>> getParks() {
        return  selectedParks;
    }

    public void setSelectedParks(List<Park> parks) {
         selectedParks.setValue(parks);
    }


}
