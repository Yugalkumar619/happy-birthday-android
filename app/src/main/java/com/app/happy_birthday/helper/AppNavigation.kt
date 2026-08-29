import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.happy_birthday.R
import com.app.happy_birthday.mvvm.wishes.model.WishesDataModel
import com.app.happy_birthday.mvvm.category.view_model.CategoryListingObj
import com.app.happy_birthday.mvvm.home.view.HomeActivity
import com.app.happy_birthday.mvvm.intro.view.IntroActivity
import com.app.happy_birthday.mvvm.login.view.LoginActivity
import com.app.happy_birthday.mvvm.saved.view.SavedImageActivity
import com.app.happy_birthday.mvvm.wallpaper.view.ImageViewerActivity
import com.app.happy_birthday.mvvm.wallpaper.view.ImageViewerObj
import com.app.happy_birthday.mvvm.wallpaper.view_model.WallpaperListingObj
import com.app.happy_birthday.mvvm.wishes.view_model.WishesObj


object AppNavigation {
    fun Context.navigateToHome(msg: String = "", block: () -> Unit) {
        val intent = Intent(this, HomeActivity::class.java)
        if (msg.isNotEmpty()) intent.putExtra("msg", msg)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        block()
    }

    fun Context.navigateToLogin(clearBackStack: Boolean = false) {
        val intent = Intent(this, LoginActivity::class.java)
        if (clearBackStack) intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun Fragment.navigateToWallpaperListing(wallpaperListingObj: WallpaperListingObj = WallpaperListingObj()){
        val bundle = Bundle()
        bundle.putSerializable("WallpaperListingObj", wallpaperListingObj)
        findNavController().navigate(R.id.action_navigation_wallpaper_listing,bundle)
    }

    fun Fragment.navigateToCategoryListing(categoryListingObj: CategoryListingObj = CategoryListingObj()){
        val bundle = Bundle()
        bundle.putSerializable("CategoryListingObj", categoryListingObj)
        findNavController().navigate(R.id.action_navigation_category_listing,bundle)
    }
    fun Fragment.navigateToSavedListing(){
        findNavController().navigate(R.id.action_navigation_saved_listing)
    }
   fun Fragment.navigateToWishesListing(wishesObj: WishesObj = WishesObj()){
       val bundle = Bundle()
       bundle.putSerializable("WishesObj", wishesObj)
        findNavController().navigate(R.id.navigation_wishes_listing,bundle)
    }

    fun Fragment.navigateToRingtoneListing(){
        findNavController().navigate(R.id.action_navigation_ringtone_listing)
    }

    fun Fragment.navigateToBhajanDetails(wishesDataModel: WishesDataModel?){
        val bundle = Bundle()
        bundle.putSerializable("BhajanDataModel", wishesDataModel)
        findNavController().navigate(R.id.action_navigation_bhajan_details,bundle)
    }


    fun Context.navigateToIntro() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
    }


    fun Activity.backStackWithIntent(intent: Intent , resultCode:Int = Activity.RESULT_OK) {
        setResult(resultCode, intent)
        finish()
    }

    fun Activity.navigateToImageViewer(imageViewerObj: ImageViewerObj){
        val intent = Intent(this, ImageViewerActivity::class.java)
        intent.putExtra("ImageViewerObj",imageViewerObj)
        startActivity(intent)
    }

    fun Activity.navigateToSavedImageViewer(imageViewerObj: ImageViewerObj){
        val intent = Intent(this, SavedImageActivity::class.java)
        intent.putExtra("ImageViewerObj",imageViewerObj)
        startActivity(intent)
    }

}