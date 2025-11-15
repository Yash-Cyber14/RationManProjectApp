package com.example.rationmanproject

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SignOutState(
    val errorMessage: String? = null
)

data class SignUP( var errormessage:String?=null , var confirm: Boolean =false , var loading:Boolean= false )

class AuthViewModel : ViewModel(){

    private val _signOutState = mutableStateOf(SignOutState())
    val signOutState: State<SignOutState> = _signOutState

    private val _signupstate = mutableStateOf(SignUP())
    val signupstate : State<SignUP> = _signupstate

    var _profileemail = mutableStateOf<String>("")
    var profileemail : State<String> = _profileemail


    val auth = FirebaseAuth.getInstance()

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            try {
                _signupstate.value = _signupstate.value.copy(loading = true)

                auth.createUserWithEmailAndPassword(email, password).await() //  suspend until complete

                _signupstate.value = _signupstate.value.copy(
                    loading = false,
                    confirm = true,
                    errormessage = null
                )
                _profileemail.value = email
            } catch (e: Exception) {
                _signupstate.value = _signupstate.value.copy(
                    loading = false,
                    confirm = false,
                    errormessage = e.message
                )
            }
        }
    }

    fun signin(email: String , password: String){

        viewModelScope.launch {

            try {
                auth.signInWithEmailAndPassword(email , password).await()

                _signupstate.value = _signupstate.value.copy(loading = false , confirm = true )



            }
            catch (e:Exception){

                _signupstate.value = _signupstate.value.copy(loading = false , confirm = false , errormessage = e.message)


            }

        }

    }

    fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            _signOutState.value = _signOutState.value.copy(errorMessage = e.message)
        }
    }

}
