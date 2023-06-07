package ru.mirea.astaevka.mireaproject.ui.compass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompassViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public CompassViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is compass fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
