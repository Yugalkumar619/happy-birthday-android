package com.app.happy_birthday.helper


import android.content.Context
import android.content.Intent
import android.net.Uri
import com.app.happy_birthday.helper.Extensions.printLog
import com.app.happy_birthday.helper.PrefUtils.getDeepLink
import com.app.happy_birthday.helper.PrefUtils.saveDeepLink
import com.app.happy_birthday.helper.helper_model.DeepLinkModel
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.BranchError
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

object ShareDetails {

    /**
     *
     * @see [performDeepLinkNavigation] function to navigate deeplink use this function from where you want to perform navigation
     * @author this function will navigate to respective target destination as per defined in when block
     * */
    fun Context.performDeepLinkNavigation() {
        if (getDeepLink() != null){
            val deeplinkModel = getDeepLink()
            saveDeepLink(null)
            when(deeplinkModel?.PB_target){
                /** define all targets here add use this from object for both pushwoosh & branch*/
                DeepLinkTargets.exampleDetails -> { /** add respective navigation  here*/ }
                DeepLinkTargets.exampleListing -> { /** add respective navigation  here*/ }
                DeepLinkTargets.exampleExternalWeb -> { /** add respective navigation  here*/ }
                /** remove or modify this cases this is only for reference*/
            }
        }
    }

    /**
     * @param referringParams in this jsonObject will come all the deeplink parameters
     * @see [performDeepLinkNavigation] function to navigate deeplink use this function from where you want to perform navigation
     * @author this function will save triggered deeplink in App preference
     * */
    fun Context.initSaveDeepLink(referringParams: JSONObject?, error: BranchError?) {
        referringParams.toString().printLog("branch IO :")
        if (error == null) {
            if (referringParams != null
                && referringParams.has(Constants.PB_target_id)
                && !referringParams.getString(Constants.PB_target_id).isNullOrEmpty()
            ) {
                saveDeepLink(
                    DeepLinkModel(
                        PB_target = referringParams.optString(Constants.PB_target, ""),
                        PB_target_id = referringParams.optString(Constants.PB_target_id, ""),
                        PB_secondary_id = referringParams.optString(Constants.PB_secondary_id, ""),
                        PB_title_en = referringParams.optString(Constants.PB_title_en, ""),
                        PB_title_ar =referringParams.optString(Constants.PB_title_ar, "")
                    )
                )
            }
        }
    }
    fun Context.shareDetails(
        setTarget: String,
        strTitle: String,
        strDesc: String,
        strImage: String,
        strID: String,
        strSecondaryId: String = "",
        loading: ((loading: Boolean) -> Unit)? = null
    ) {
        setBranchIo(setTarget, strTitle, strDesc, strImage, strID, strSecondaryId) {
            loading?.invoke(it)
        }
    }


    private fun Context.setBranchIo(
        setTarget: String,
        strTitle: String,
        strDesc: String,
        strImage: String,
        strID: String,
        strSecondaryId: String,
        loading: (loading: Boolean) -> Unit
    ) {
        var branchIoLink = ""
        val contentData = ContentMetadata()
            .addCustomMetadata(Constants.PB_target, setTarget)
            .addCustomMetadata(Constants.PB_target_id, strID)
            .addCustomMetadata(Constants.PB_secondary_id, strSecondaryId)
            .addCustomMetadata(Constants.PB_title_en, strTitle)
        //Branch io
        val branchUniversalObject =
            BranchUniversalObject().setCanonicalIdentifier(strID)
                .setTitle(strTitle)
                .setContentDescription(strDesc)
                .setContentImageUrl(strImage)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(contentData)

        val linkProperties = LinkProperties()
            .setFeature("sharing")

        branchUniversalObject.generateShortUrl(this, linkProperties) { url, error ->
            if (error == null) {
                branchIoLink = url
                branchIoLink.printLog("Branch IO")
                if (strImage != "") {
                    if (NetworkUtil.getConnectivityStatus(this)) {
                        downloadFile(strImage, strTitle, strDesc, branchIoLink, loading)
                    } else {
                        shareIntent(strTitle = strTitle, strDesc = strDesc, branchIoLink = branchIoLink)
                    }
                } else {
                    shareIntent(strTitle = strTitle, strDesc = strDesc, branchIoLink = branchIoLink)
                }
            } else {
                if (strImage != "") {
                    if (NetworkUtil.getConnectivityStatus(this)) {
                        downloadFile(strImage, strTitle, strDesc, branchIoLink, loading)
                    } else {
                        shareIntent(strTitle = strTitle, strDesc = strDesc, branchIoLink = branchIoLink)
                    }
                } else {
                    shareIntent(strTitle = strTitle, strDesc = strDesc, branchIoLink = branchIoLink)
                }
            }
        }
    }

    fun Context.downloadFile(
        fileUrl: String,
        strTitle: String,
        strDesc: String,
        branchIoLink: String,
        loading: (loading: Boolean) -> Unit
    ) {
        loading(true)
        val fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1)
        runBlocking {
            FileHelper.saveImage(this@downloadFile, fileUrl, fileName, object : OnFileDownloadListener {
                override fun onDownloadCompleted(filePath: Uri?) {
                    loading(false)
                    shareIntent(
                        result = filePath,
                        strTitle = strTitle,
                        strDesc = strDesc,
                        branchIoLink = branchIoLink
                    )
                    ("$branchIoLink \n $filePath").printLog("Branch IO onDownloadCompleted")
                }

                override fun onDownloadError() {
                    loading(false)
                    shareIntent(strTitle = strTitle, strDesc = strDesc, branchIoLink = branchIoLink)
                    (branchIoLink).printLog("Branch IO onDownloadError")
                }
            })
        }

    }

    private fun Context.shareIntent(
        result: Uri? = null,
        strTitle: String,
        strDesc: String,
        branchIoLink: String
    ) {
        try {
            if (result != null) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_SUBJECT, strTitle)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    strTitle + "\n" + strDesc + "\n" + branchIoLink
                )
                intent.putExtra(Intent.EXTRA_STREAM, result)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.type = "image/*"
                startActivity(Intent.createChooser(intent, "Share Data"))
            } else {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, strTitle)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    strTitle + "\n" + strDesc + "\n" + branchIoLink
                )
                startActivity(Intent.createChooser(intent, "Share Data"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, strTitle)
            intent.putExtra(
                Intent.EXTRA_TEXT,
                strTitle + "\n" + strDesc + "\n" + branchIoLink
            )
            startActivity(Intent.createChooser(intent, "Share Data"))
        }
    }
}