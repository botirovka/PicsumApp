package com.botirovka.picsumapp.ui.imageViewer

import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.botirovka.picsumapp.databinding.FragmentImageBinding
import com.botirovka.picsumapp.utils.Extenstions.shareImageLink
import com.botirovka.picsumapp.utils.ScreenUtils

class ImageFragment : Fragment() {
    private lateinit var binding: FragmentImageBinding
    private val viewModel: ImageViewModel by viewModels()
    private val args: ImageFragmentArgs by navArgs()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var matrix: Matrix
    private val matrixValues = FloatArray(9)
    private var scale = 1.0F
    private val scaleMin = 0.5F
    private val scaleMax = 3F


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        matrix = Matrix()
        binding.ivImage.imageMatrix = matrix
        gestureDetector =
            GestureDetector(requireContext(), GestureListener(matrix, binding.ivImage))
        scaleGestureDetector =
            ScaleGestureDetector(requireContext(), ScaleListener(matrix, binding.ivImage))

        setupUI()
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.ivImage.setOnLongClickListener {
            Log.d("mydebug", "Reset")
            binding.ivImage.imageMatrix.reset()
            true
        }
        binding.ivImage.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event).also {
                matrix.getValues(matrixValues)
                updateSeekBarScale(matrixValues[Matrix.MSCALE_X])
            }
            scaleGestureDetector.onTouchEvent(event).also {
                matrix.getValues(matrixValues)
                updateSeekBarScale(matrixValues[Matrix.MSCALE_X])
            }
        }
        binding.ivImage.imageMatrix
        binding.SeekBarScaleFactor.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            var isFromTouch = false
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("mydebug", "onProgressChanged: ")
                if (isFromTouch) {
                    scale = (progress / 100.0).toFloat()
                    val imageCenter = ScreenUtils.getScreenWidthPixels() / 2.0F
                    matrix.setScale(scale, scale, imageCenter, imageCenter)
                    binding.ivImage.imageMatrix = matrix
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isFromTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isFromTouch = false
            }
        })

        binding.toolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.ivShareImageLink.setOnClickListener {
            viewModel.imageBitmap.value?.let { shareImageLink(it) }
        }
    }

    private fun setupUI() {
        viewModel.loadImageById(args.imageId)
        binding.SeekBarScaleFactor.max = (scaleMax * 100).toInt()
        binding.SeekBarScaleFactor.min = (scaleMin * 100).toInt()
        binding.SeekBarScaleFactor.progress = 100

    }

    private fun setupObservers() {
        viewModel.imageBitmap.observe(viewLifecycleOwner) { imageBitmap ->
            binding.ivImage.setImageBitmap(imageBitmap.bitmap)
            binding.toolbar.tvToolbarHeading.text = "ID: ${imageBitmap.id}"
        }
    }

    private fun updateSeekBarScale(newScale: Float) {
        scale = newScale
        binding.SeekBarScaleFactor.progress = (scale * 100).toInt()
    }
}