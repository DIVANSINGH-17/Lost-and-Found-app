package com.shruti.lofo.ui.DashBoard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.shruti.lofo.R;
import com.shruti.lofo.databinding.FragmentDashboardBinding;
import com.shruti.lofo.ui.Found.FoundDetails;
import com.shruti.lofo.ui.Lost.LostDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DashBoardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private ArrayList<DashBoardViewModel> arr_recent_lofo;
    private RecyclerRecentLoFoAdapter adapter;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // The unstable ImageSlider has been completely removed to prevent the app from crashing.

        RecyclerView recentLostFoundList = binding.recentLostFoundList;
        arr_recent_lofo = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false);
        recentLostFoundList.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerRecentLoFoAdapter(requireContext(), arr_recent_lofo);
        recentLostFoundList.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Query for items
        fetchAndDisplayItems();

        adapter.setOnItemClickListener(item -> {
            String selectedItemName = item.getItemName();
            Intent intent;
            if(item.getTag().equalsIgnoreCase("lost")) {
                intent = new Intent(requireContext(), LostDetails.class);
            } else {
                intent = new Intent(requireContext(), FoundDetails.class);
            }
            intent.putExtra("itemId", selectedItemName);
            startActivity(intent);
        });

        // Get and display user name
        displayUserData();

        return root;
    }

    private void fetchAndDisplayItems() {
        db.collection("lostItems").get().addOnSuccessListener(lostItemsSnapshot -> {
            db.collection("foundItems").get().addOnSuccessListener(foundItemsSnapshot -> {
                if (binding == null) return; // Check if view is still valid

                List<DocumentSnapshot> mergedItems = new ArrayList<>();
                mergedItems.addAll(lostItemsSnapshot.getDocuments());
                mergedItems.addAll(foundItemsSnapshot.getDocuments());

                Collections.sort(mergedItems, (o1, o2) -> {
                    String dateString1 = o1.contains("dateLost") ? o1.getString("dateLost") : o1.getString("dateFound");
                    String dateString2 = o2.contains("dateLost") ? o2.getString("dateLost") : o2.getString("dateFound");

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date1 = null;
                    Date date2 = null;

                    try {
                        if (dateString1 != null) date1 = dateFormat.parse(dateString1);
                        if (dateString2 != null) date2 = dateFormat.parse(dateString2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date1 != null && date2 != null) {
                        return date2.compareTo(date1);
                    }
                    return 0;
                });

                List<DocumentSnapshot> recentItems = mergedItems.size() > 10 ? mergedItems.subList(0, 10) : mergedItems;

                arr_recent_lofo.clear();
                for (DocumentSnapshot item : recentItems) {
                    DashBoardViewModel lofo = item.toObject(DashBoardViewModel.class);
                    if (lofo != null) {
                        arr_recent_lofo.add(lofo);
                    }
                }
                adapter.notifyDataSetChanged();
            });
        });
    }

    private void displayUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email == null) return;

            db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
                // IMPORTANT: Check if the view is still alive before updating the UI
                if (!task.isSuccessful() || binding == null) {
                    Log.d("FirebaseDebug", "Task failed or view is destroyed.");
                    return;
                }

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    if (name != null) {
                        binding.userName.setText(name);
                    }
                }
            });
        } else {
            Log.d("FirebaseDebug", "No user currently logged in.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // This is crucial for preventing memory leaks
    }
}
