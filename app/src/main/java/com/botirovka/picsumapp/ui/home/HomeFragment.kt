package com.botirovka.picsumapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.botirovka.picsumapp.databinding.FragmentHomeBinding
import com.botirovka.picsumapp.domain.models.ImageBitmap

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ImageAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
        adapter = ImageAdapter(::onItemImageClick, ::onShareClick)
        binding.rvImages.adapter = adapter
    }

    private fun setupListeners() {
        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                when {
                    dy < -10 && lastVisibleItemPosition > 5 -> {
                        binding.ivScrollUp.visibility = View.VISIBLE
                    }

                    dy > 0 -> binding.ivScrollUp.visibility = View.GONE
                    else -> {
                        if (lastVisibleItemPosition <= 2) {
                            binding.ivScrollUp.visibility = View.GONE
                        }
                    }
                }
                if (lastVisibleItemPosition != RecyclerView.NO_POSITION &&
                    lastVisibleItemPosition >= totalItemCount - 1 &&
                    viewModel.loadingLiveData.value == false
                ) {
                    Log.d("mydebug", "loading")
                    viewModel.loadImages()
                }
            }
        })

        binding.ivScrollUp.setOnClickListener {
            if (adapter.itemCount >= 5) {
                binding.rvImages.scrollToPosition(5)
            }
            binding.rvImages.smoothScrollToPosition(0)
            binding.ivScrollUp.visibility = View.GONE
        }
    }

    private fun setupObservers() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            Log.d("mydebug", "setupObservers: $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        }
        viewModel.imagesBitmap.observe(viewLifecycleOwner) { bitmaps ->
            Log.d("mydebug", "submit")
            adapter.submitList(bitmaps)
        }
    }

    private fun onItemImageClick(imageBitmap: ImageBitmap) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToImageFragment(
                imageBitmap.id
            )
        )
    }

    private fun onShareClick(imageBitmap: ImageBitmap) {
        onShareClick(imageBitmap)
    }
}