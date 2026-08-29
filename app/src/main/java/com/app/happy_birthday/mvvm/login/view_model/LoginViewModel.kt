package com.app.happy_birthday.mvvm.login.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.happy_birthday.data_source.api_manager.ApiRepository
import com.app.happy_birthday.helper.Validation
import com.app.happy_birthday.mvvm.login.model.LoginRequestModel
import com.app.happy_birthday.helper.helper_model.UserResponseModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class LoginObj(
    var strEmail: String = "",
    var strPassword: String = ""
)
class LoginViewModel : ViewModel() {
    var objLogin = LoginObj()
    val mutLoginResponse : MutableLiveData<UserResponseModel?> = MutableLiveData()
    fun loginUser(model : LoginRequestModel){
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            mutLoginResponse.postValue(UserResponseModel(status = 0 , message = throwable.localizedMessage))
        }){
            try {
                ApiRepository
                    .apiPost<LoginRequestModel,UserResponseModel>("" , model)
                    .collectLatest {
                        mutLoginResponse.postValue(it)
                }
            }catch (e:Exception){
                mutLoginResponse.postValue(UserResponseModel(status = 0 , message = e.localizedMessage))
            }
        }
    }

    fun checkValidation(block: (value: Int) -> Unit) {
        when {
            objLogin.strEmail.isEmpty() -> block(1)
            !Validation.isValidEmail(objLogin.strEmail) -> block(2)
            objLogin.strPassword.isEmpty() -> block(3)
            objLogin.strPassword.length < 6 -> block(4)
            else -> block(0)
        }
    }

}