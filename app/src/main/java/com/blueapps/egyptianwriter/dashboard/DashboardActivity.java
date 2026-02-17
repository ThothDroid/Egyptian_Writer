package com.blueapps.egyptianwriter.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.blueapps.egyptianwriter.R;
import com.blueapps.egyptianwriter.dashboard.documents.DocumentFragment;
import com.blueapps.egyptianwriter.databinding.DashboardBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

@SuppressWarnings("rawtypes")
public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DashboardBinding binding;
    private static final String TAG = "DashboardActivity";
    private boolean hasStarted = false;

    private FragmentManager fragmentManager;
    private MainViewModel viewModel;

    // Views
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private FragmentContainerView fragmentContainerView;
    private NavigationView navigationView;

    // Constants
    public static final String KEY_INTENT = "Intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create non fullscreen layout
        binding = DashboardBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set names for Views
        drawerLayout = binding.drawerLayout;
        menuButton = binding.buttonMenu;
        fragmentContainerView = binding.contentContainerView;
        navigationView = binding.navigationView;

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        fragmentManager = getSupportFragmentManager();

        menuButton.setOnClickListener(view -> {
            drawerLayout.open();
        });

        viewModel.getUiState().observe(this, uiData -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(fragmentContainerView.getId(), uiData.getSelectedFragment(), new Bundle());
            transaction.commit();
        });

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_item_documents);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Handle intents
        if (!hasStarted) {
            if (Objects.equals(getIntent().getAction(), Intent.ACTION_VIEW) ||
                    Objects.equals(getIntent().getAction(), Intent.ACTION_SEND)) {

                Bundle bundle = new Bundle();
                bundle.putParcelable(KEY_INTENT, getIntent());
                DocumentFragment documentFragment = new DocumentFragment();
                documentFragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(fragmentContainerView.getId(), documentFragment);
                transaction.commit();
            }
            hasStarted = true;
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Class destination = viewModel.onNavigationSelectionChange(menuItem, navigationView,
                drawerLayout);

        if (destination != null){
            startActivity(new Intent(DashboardActivity.this, destination));
        }

        return false;
    }
}