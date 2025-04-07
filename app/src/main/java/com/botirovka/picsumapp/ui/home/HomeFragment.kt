package com.botirovka.picsumapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.botirovka.picsumapp.R
import com.botirovka.picsumapp.databinding.FragmentHomeBinding
import com.botirovka.picsumapp.domain.models.ImageBitmap
import com.botirovka.picsumapp.domain.models.ImageResult
import com.botirovka.picsumapp.domain.models.PossibleError
import com.botirovka.picsumapp.utils.Extenstions.shareImageLink
import com.botirovka.picsumapp.utils.NetworkUtils

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ImageAdapter
    private var IsNoInternetDialogShowed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState != null){
            showNoInternetDialog()
            viewModel.clearBitmaps()
        }
        adapter = ImageAdapter(
            requireContext(),
            viewModel.imageList,
            ::onItemImageClick,
            ::onShareClick
        )

        setupObservers()
        setupListeners()

        binding.ivScrollUp.visibility = View.VISIBLE
        binding.lvImages.adapter = adapter
    }

    private fun showNoInternetDialog() {
        if(IsNoInternetDialogShowed.not()){
            if(adapter.isEmpty){
                binding.tvError.text = getString(R.string.error_no_internet_connection_initial)
            }
            else{
                binding.tvError.text = getString(R.string.error_no_internet_connection)
            }
            binding.tvError.visibility = View.VISIBLE

        }
        IsNoInternetDialogShowed = true
    }

    private fun setupListeners() {
        binding.lvImages.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val lastItemPos = binding.lvImages.lastVisiblePosition

            if(lastItemPos >= viewModel.imageList.size - 1 &&
                viewModel.loadingLiveData.value == false &&
                viewModel.imageList.size > 0) {
                if(isInternetAvailable()){
                    viewModel.loadImages()
                }
                else{
                    showNoInternetDialog()
                }
            }
            Log.d("mydebug", "onViewCreated: $lastItemPos")
            if(lastItemPos >= 5){
                binding.ivScrollUp.visibility = View.VISIBLE
            }
            else{
                binding.ivScrollUp.visibility = View.GONE
            }
        }

        binding.ivScrollUp.setOnClickListener {
            binding.lvImages.smoothScrollToPositionFromTop(0, 0)
            binding.ivScrollUp.visibility = View.GONE
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if(isInternetAvailable()){
                viewModel.loadImages()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupObservers() {
        if(isInternetAvailable().not()){
            showNoInternetDialog()
        }
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            Log.d("mydebug", "setupObservers: $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        }
        viewModel.imagesBitmap.observe(viewLifecycleOwner) { imageResult ->
            Log.d("mydebug", "imageResult: $imageResult")
            when(imageResult){
                is ImageResult.Data -> {
                    viewModel.imageList.addAll(imageResult.data)
                    adapter.notifyDataSetChanged()
                }
                is ImageResult.Error -> {
                    binding.tvError.visibility = View.VISIBLE
                    when(imageResult.error){
                        PossibleError.EmptyImageListError -> {binding.tvError.text = "Error: Empty image list"}
                        is PossibleError.GeneralError -> {binding.tvError.text = "Error: Something went wrong"}
                        PossibleError.NoInternetConectionError -> {binding.tvError.text = getString(R.string.error_no_internet_connection)}
                    }
                }
                is ImageResult.None -> {}
            }

        }
    }

    private fun onItemImageClick(imageBitmap: ImageBitmap) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToImageFragment(
                imageBitmap.id
            )
        )
        viewModel.clearBitmaps()
        IsNoInternetDialogShowed = false
    }

    private fun onShareClick(imageBitmap: ImageBitmap) {
        shareImageLink(imageBitmap)
    }

    private fun isInternetAvailable() : Boolean{
        return NetworkUtils.isInternetAvailable(requireContext()).also {
            if(it == true) {
                IsNoInternetDialogShowed = false
                binding.tvError.visibility = View.GONE
            }
        }
    }
}