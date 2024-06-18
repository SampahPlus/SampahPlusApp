package com.sampahplus.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.sampahplus.R
import com.sampahplus.data.remote.response.ResultsItem
import com.sampahplus.databinding.FragmentHomeBinding
import com.sampahplus.ui.ViewModelFactory
import com.sampahplus.ui.knowledge.KnowledgeActivity
import com.sampahplus.utils.ResultState

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: HomeAdapter
    private lateinit var auth: FirebaseAuth

//    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false,)
        return binding.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
            auth = FirebaseAuth.getInstance()
//        db = FirebaseFirestore.getInstance()

        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupKnowledge()

//        setupUsername()
        setupButtonListener()

//        binding.layoutHeader.ivProfile.setOnClickListener(){
//            findNavController().navigate(R.id.action_homeFragment_to_predictFragment)
//        }
    }

    //Komentar ini gambarnya belum ke set karena aku gk tau ngambil resourcenya dari mana nanti kalo udah ada kamu set gambar nya pake binding + username nya
    // cara manggil binding username binding.layoutHeader.tvName
    //Terus kodingan dionView created kamu hapus slash nya biar gak jadi komentar dan bisa diklik ke profile
    private fun setupButtonListener() {
        binding.layoutBanner.btnPagePredict.setOnClickListener() {
            findNavController().navigate(R.id.action_homeFragment_to_predictFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter()
        binding.rvKnowledge.layoutManager = GridLayoutManager(context, 2)
        binding.rvKnowledge.adapter = adapter
    }
    private fun setupKnowledge() {
        viewModel.getKnowledge().observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.pbLoadingKnowledge.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    binding.pbLoadingKnowledge.visibility = View.GONE
                    adapter.submitList(result.data.knowledgeResponse)
                }

                is ResultState.Error -> {
                    binding.pbLoadingKnowledge.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                is ResultState.Empty  ->{
                    binding.pbLoadingKnowledge.visibility = View.GONE
                    binding.rvKnowledge.visibility = View.GONE
                }

            }
        }

        // Example of setting an item click listener
        adapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(knowledgeItem: ResultsItem) {
                val knowledgeDetail = Intent(requireContext(), KnowledgeActivity::class.java)
                knowledgeDetail.putExtra("knowledge", knowledgeItem)
                startActivity(knowledgeDetail)
            }
        })
    }


//    private fun setupUsername() {
//        val userId = auth.currentUser?.uid
//        if (userId != null) {
//            val docRef = db.collection("users").document(userId)
//            docRef.get()
//                .addOnSuccessListener { document ->
//                    if (document != null && document.exists()) {
//                        val name = document.getString("name")
//                          binding.layoutHeader.tvName.text = getString(name)
//                    } else {
//                        Log.d("HomeActivity", "No such document")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d("HomeActivity", "get failed with ", exception)
//                }
//        }
}