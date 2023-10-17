package ru.mirea.astaevka.mireaproject.ui.gallery;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ru.mirea.astaevka.mireaproject.R;
import ru.mirea.astaevka.mireaproject.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//get a list of installed apps.
        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;
        PackageManager pm = this.getContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(flags);

        for (ApplicationInfo pack : packages) {
            TextView valueTV;
            if((pack.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0) {
                valueTV = new TextView(this.getContext());
            } else {
                valueTV = new TextView(this.getContext(), null, 0, R.style.TextAppearance);
            }
            valueTV.setText(pack.packageName);
            binding.layout.addView(valueTV);
        }

//        final WebView webView = binding.webview;
//        webView.loadUrl("https://www.mirea.ru/");

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}