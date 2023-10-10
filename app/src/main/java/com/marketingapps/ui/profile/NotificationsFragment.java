package com.marketingapps.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.marketingapps.MainActivity;
import com.marketingapps.R;
import com.marketingapps.databinding.FragmentProfileBinding;
import com.marketingapps.singleton.LoginSingleton;

public class NotificationsFragment extends Fragment {
    View root;
    Button loginWithId;
    Button btn_logout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (!LoginSingleton.getInstance().isLogin) {
            root = inflater.inflate(R.layout.fragment_login, container, false);
            loginWithId = root.findViewById(R.id.btn_loginwithid);
            loginWithId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginSingleton.getInstance().setLogin(true);
                    recreateFragment();
                }
            });
        } else {
            root = inflater.inflate(R.layout.fragment_profile, container, false);
            btn_logout = root.findViewById(R.id.btn_logout);
            btn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginSingleton.getInstance().setLogin(false);
                    recreateFragment();
                }
            });
        }

        return root;
    }

    public void recreateFragment() {
        MainActivity.navigateToProfile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}