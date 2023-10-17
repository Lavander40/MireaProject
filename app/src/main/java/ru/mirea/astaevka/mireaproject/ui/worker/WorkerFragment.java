package ru.mirea.astaevka.mireaproject.ui.worker;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.astaevka.mireaproject.databinding.FragmentSlideshowBinding;
import ru.mirea.astaevka.mireaproject.databinding.FragmentWorkerBinding;
import ru.mirea.astaevka.mireaproject.ui.slideshow.SlideshowViewModel;

public class WorkerFragment extends Fragment {

    private FragmentWorkerBinding binding;
    private int PermissionCode = 200;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkerViewModel slideshowViewModel =
                new ViewModelProvider(this).get(WorkerViewModel.class);

        binding = FragmentWorkerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (ContextCompat.checkSelfPermission(inflater.getContext(), POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            //Log.d(MainActivity.class.getSimpleName().toString(), "Разрешения получены");
        } else {
            //Log.d(MainActivity.class.getSimpleName().toString(), "Нет разрешений!");

            ActivityCompat.requestPermissions(getActivity(), new String[]{POST_NOTIFICATIONS, FOREGROUND_SERVICE}, PermissionCode);

        }

        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(inflater.getContext(), PlayerService.class);
                ContextCompat.startForegroundService(inflater.getContext(), serviceIntent);
            }
        });

        binding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().stopService(
                        new Intent(inflater.getContext(), PlayerService.class));
            }
        });
//        WorkRequest uploadWorkRequest =
//                new OneTimeWorkRequest.Builder(UploadWorker.class)
//                        .build();
//        WorkManager
//                .getInstance(this.getContext())
//                .enqueue(uploadWorkRequest);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}