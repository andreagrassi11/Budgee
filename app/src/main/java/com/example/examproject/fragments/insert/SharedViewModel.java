package com.example.examproject.fragments.insert;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<String>> recognizedTextList = new MutableLiveData<>();

    // Method to update the recognized text list
    public void setRecognizedTextList(List<String> textList) {
        recognizedTextList.setValue(textList); // Use postValue() if updating from a background thread
    }

    // Method to retrieve the recognized text list
    public LiveData<List<String>> getRecognizedTextList() {
        return recognizedTextList;
    }
}
