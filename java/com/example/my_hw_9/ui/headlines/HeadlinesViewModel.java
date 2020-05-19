package com.example.my_hw_9.ui.headlines;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HeadlinesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HeadlinesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is headlines fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}