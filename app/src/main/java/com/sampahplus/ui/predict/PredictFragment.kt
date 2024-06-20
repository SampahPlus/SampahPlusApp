package com.sampahplus.ui.predict

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.sampahplus.R
import com.sampahplus.data.remote.response.HistoryItem
import com.sampahplus.databinding.FragmentPredictBinding
import com.sampahplus.ui.ViewModelFactory
import com.sampahplus.ui.result.ResultActivity
import com.sampahplus.utils.DateFormatter
import com.sampahplus.utils.ResultState
import com.sampahplus.utils.getImageUri
import com.sampahplus.utils.uriToFile
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File

class PredictFragment : Fragment() {
    private lateinit var binding: FragmentPredictBinding
    private val viewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: PredictAdapter
    private lateinit var email: String
    private var currentImageUri: Uri? = null

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private val cropImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let {
                currentImageUri = it
                showImage()
                binding.btnPredict.isEnabled = true
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            cropError?.let {
                Log.e(ContentValues.TAG, "Crop error: ", it)
                showToast("Gagal memotong gambar")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPredictBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        auth = FirebaseAuth.getInstance()
        email = auth.currentUser?.email.toString()

        setupRecyclerView()
        setupHistory()
        setupButtonListener()

        binding.btnPredict.isEnabled = false
    }

    private fun setupButtonListener() {
        binding.apply {
            btnGaleri.setOnClickListener {
                startGaleri()
            }
            btnCamera.setOnClickListener {
                startCamera()
            }
            btnPredict.setOnClickListener {
                lifecycleScope.launch { predictImage() }
            }
        }
    }

    private suspend fun predictImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext())
            Log.d("Image File", "showImage: ${imageFile.path}")

            viewModel.predictImage(imageFile, email).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.pbLoadingHistory.visibility = View.VISIBLE
                    }

                    is ResultState.Success -> {
                        binding.pbLoadingHistory.visibility = View.GONE
                        AlertDialog.Builder(requireContext()).apply {
                            setTitle("Berhasil!!")
                            setMessage(result.data.message)
                            setPositiveButton("Ok") { _, _ ->
                                val resultActivity = Intent(requireContext(), ResultActivity::class.java)
                                resultActivity.putExtra("result", result.data.results)
                                binding.ivPreview.setImageResource(R.drawable.img_scan)
                                startActivity(resultActivity)
                            }
                            create()
                            show()
                        }
                    }

                    is ResultState.Error -> {
                        binding.pbLoadingHistory.visibility = View.GONE
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultState.Empty -> {
                        binding.pbLoadingHistory.visibility = View.GONE
                    }
                }
            }
        } ?: run {
            showToast(getString(R.string.img_empty_warning))
            binding.pbLoadingHistory.visibility = View.GONE
        }
        currentImageUri = null
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { uri ->
                showImage()

            }
        }
    }

    private fun startGaleri() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherGallery.launch(chooser)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                currentImageUri = uri
                uCrop(uri)
                showImage()
            } ?: showToast("Gagal mengambil gambar atau tidak ada gambar yang dipilih")
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            Log.d(ContentValues.TAG, "Menampilkan gambar: $uri")
            binding.ivPreview.setImageURI(uri)
            binding.btnPredict.isEnabled = true
        } ?: Log.d(ContentValues.TAG, "Tidak ada gambar untuk ditampilkan")
    }

    private fun uCrop(sourceUri: Uri) {
        val fileName = "preview_cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, fileName))
        val uCrop = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .getIntent(requireContext())

        cropImageLauncher.launch(uCrop)
    }

    private fun setupRecyclerView() {
        adapter = PredictAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(context)
        binding.rvHistory.adapter = adapter
    }

    private fun setupHistory() {
        viewModel.getHistories(email).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.tvEmpty.visibility = View.GONE
                    binding.pbLoadingHistory.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    binding.rvHistory.visibility = View.VISIBLE
                    binding.tvEmpty.visibility = View.GONE
                    binding.pbLoadingHistory.visibility = View.GONE
                    val sortedDataList = result.data.histories?.history?.sortedByDescending { historyItem ->
                        historyItem?.tanggal?.let { DateFormatter.parseDate(it) }
                    }
                    adapter.submitList(sortedDataList)
                }

                is ResultState.Error -> {
                    binding.pbLoadingHistory.visibility = View.GONE
                    if (result.error != "Not Found")
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }

                is ResultState.Empty -> {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.pbLoadingHistory.visibility = View.GONE
                    binding.rvHistory.visibility = View.GONE
                }
            }
        }

        adapter.setOnItemClickCallback(object : PredictAdapter.OnItemClickCallback {
            override fun onItemClicked(history: HistoryItem) {
                val result = Intent(requireContext(), ResultActivity::class.java)
                result.putExtra("history", history)
                Log.d("Kirim history di fragment ", "history")
                startActivity(result)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    override fun onResume() {
        super.onResume()
        // Muat ulang data setiap kali fragment ini kembali ke tampilan
        setupHistory()
    }
}
