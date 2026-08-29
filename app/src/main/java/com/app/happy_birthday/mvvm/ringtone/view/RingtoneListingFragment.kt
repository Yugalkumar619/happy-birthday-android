package com.app.happy_birthday.mvvm.ringtone.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.FragmentRingtoneListingBinding
import com.app.happy_birthday.helper.BaseFragment
import com.app.happy_birthday.helper.Constants
import com.app.happy_birthday.helper.LocaleHelper.getAllRingtone
import com.app.happy_birthday.helper.RingtoneHelper
import com.app.happy_birthday.helper.interfaces.CommonInterfaceClickEvent
import com.app.happy_birthday.mvvm.home.view.HomeActivity
import com.app.happy_birthday.mvvm.ringtone.view_model.RingtoneViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class RingtoneListingFragment : BaseFragment() {

    private lateinit var mActivity: HomeActivity
    private lateinit var binding: FragmentRingtoneListingBinding
    private lateinit var viewModel: RingtoneViewModel
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as HomeActivity
        viewModel = ViewModelProvider(this)[RingtoneViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRingtoneListingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
        initOnClickListener()

    }


    private fun initializeFields(){

        binding.root.post {
            AdsManager.loadBanner(requireActivity(), R.id.banner_container)
        }

        binding.ilToolbar.root.backgroundTintList = ContextCompat.getColorStateList(mActivity,R.color.color_pink)

        setUpToolbar(binding.ilToolbar,
            title = getString(R.string.label_ringtone),
            isBackArrow = true,
            toolbarClickListener = object : CommonInterfaceClickEvent {
                override fun onToolBarListener(type: String) {
                    if (type == Constants.TOOLBAR_ICON_ONE){

                    }
                    if (type == Constants.TOOLBAR_ICON_TWO){

                    }
                }
            }
        )


        binding.rvRingtone.adapter = viewModel.adapterRingtone
        viewModel.adapterRingtone
        viewModel.arrListRingtoneData.clear()
        viewModel.arrListRingtoneData.addAll(getAllRingtone())
        viewModel.adapterRingtone.onClickEvent = onItemClickListener
        viewModel.updateRingtoneListAdapter()
    }

    private val onItemClickListener = object :  CommonInterfaceClickEvent{
        override fun onItemClick(type: String, position: Int) {

            var data = viewModel.arrListRingtoneData.get(position)
            when(type){
                "play" -> {

                    if(isPlaying){
                        stopRamRingtone()
                    }else{
                        playRamRingtone(data?.ringtone?:0)
                    }
                    isPlaying = !isPlaying

                    viewModel.arrListRingtoneData.forEach {
                        it?.isPlaying = false
                    }
                    viewModel.arrListRingtoneData.get(position)?.isPlaying = isPlaying
                    viewModel.updateRingtoneListAdapter()
                }

                "share" -> {
                    lifecycleScope.launch {
                        shareRingtone(data?.ringtone ?: 0, "ram_ringtone_${position+1}.mp3")
                    }
                }
                else -> {

                    showRingtonePopup(mActivity,data?.ringtone?:0,data?.title?:"")
                }
            }
        }
    }
    private fun initOnClickListener(){

    }


    // Add this anywhere in your Activity (or make it reusable)
    private var mediaPlayer: MediaPlayer? = null

    private fun playRamRingtone(tune: Int) {
        // Release previous player if still playing
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(mActivity, tune) // Change filename here
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()

        Toast.makeText(mActivity, "Playing Shri Ram Dhun", Toast.LENGTH_SHORT).show()
    }

    private fun stopRamRingtone() {
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
            it.release()
            mediaPlayer = null
        }
    }

    override fun onDestroy() {
        stopRamRingtone()  // Prevents memory leak
        super.onDestroy()
    }


    fun showRingtonePopup(context: Context, rawId: Int, ringtoneName: String) {
        // Stop any playing audio first
        mediaPlayer?.release()
        mediaPlayer = null

        val dialog =
            Dialog(context) // Optional: create style for rounded corners
        dialog.setContentView(R.layout.dialog_ringtone) // We'll create this layout next
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Views
        val tvTitle = dialog.findViewById<TextView>(R.id.tvRingtoneName)
        val btnPlay = dialog.findViewById<ImageButton>(R.id.btnPlay)
        val btnStop = dialog.findViewById<ImageButton>(R.id.btnStop)
        val btnSetRingtone = dialog.findViewById<TextView>(R.id.btnSetRingtone)
        val btnSetAlarm = dialog.findViewById<TextView>(R.id.btnSetAlarm)
        val btnSetNotification = dialog.findViewById<TextView>(R.id.btnSetNotification)

        tvTitle.text = ringtoneName

        // Create MediaPlayer from raw
        mediaPlayer = MediaPlayer.create(context, rawId)
        mediaPlayer?.setOnCompletionListener {
            btnPlay.visibility = View.VISIBLE
            btnStop.visibility = View.GONE
        }

        btnPlay.setOnClickListener {
            mediaPlayer?.start()
            btnPlay.visibility = View.GONE
            btnStop.visibility = View.VISIBLE
        }

        btnStop.setOnClickListener {
            mediaPlayer?.pause()
            btnPlay.visibility = View.VISIBLE
            btnStop.visibility = View.GONE
        }

        // Reuse your save & set logic
        val ringtoneHelper = RingtoneHelper(context) // From our earlier code

        btnSetRingtone.setOnClickListener {
            lifecycleScope.launch {
                ringtoneHelper.saveRawToRingtones(rawId, ringtoneName, RingtoneManager.TYPE_RINGTONE,
                    onPermissionNeeded = {
                        // Open settings screen
                        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                            data = Uri.parse("package:${mActivity.packageName}")
                        }
                        startActivity(intent)
                    },
                    onSuccess = {
                        AdsManager.showInterstitial(mActivity,{})
                        dialog.dismiss()
                    })
            }
        }

        btnSetAlarm.setOnClickListener {
            lifecycleScope.launch {
                ringtoneHelper.saveRawToRingtones(rawId, ringtoneName, RingtoneManager.TYPE_ALARM,
                    onPermissionNeeded = {
                        // Open settings screen
                        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                            data = Uri.parse("package:${mActivity.packageName}")
                        }
                        startActivity(intent)
                    },
                    onSuccess = {
                        AdsManager.showInterstitial(mActivity,{})
                        dialog.dismiss()
                    })
            }
        }

        btnSetNotification.setOnClickListener {
            lifecycleScope.launch {
                ringtoneHelper.saveRawToRingtones(rawId, ringtoneName, RingtoneManager.TYPE_NOTIFICATION,
                    onPermissionNeeded = {
                        // Open settings screen
                        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                            data = Uri.parse("package:${mActivity.packageName}")
                        }
                        startActivity(intent)
                    },
                    onSuccess = {
                        AdsManager.showInterstitial(mActivity,{})
                        dialog.dismiss()
                    })

            }
        }

        dialog.setOnDismissListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }

        dialog.show()
    }


    /**
     * Share any ringtone from res/raw as .mp3 file
     * Users can send to friends, save to device, upload, etc.
     */
    suspend fun shareRingtone(rawId: Int, fileName: String = "Jai_Shri_Ram_Dhun.mp3") = withContext(Dispatchers.IO) {
        try {
            // Step 1: Copy raw → cache folder as real .mp3 file
            val cacheFile = File(context?.cacheDir, fileName)
            context?.resources?.openRawResource(rawId).use { input ->
                cacheFile.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }

            // Step 2: Create share intent with proper URI (FileProvider)
            val uri = androidx.core.content.FileProvider.getUriForFile(
                mActivity,
                "${context?.packageName}.provider",  // See step below
                cacheFile
            )

            withContext(Dispatchers.Main) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "audio/mpeg"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_TEXT, "Jai Shri Ram! Listen to this beautiful Ram Bhajan\nShared from Ram Wallpaper App")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                context?.startActivity(
                    Intent.createChooser(shareIntent, "Share Shri Ram Dhun")
                )
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Share failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}