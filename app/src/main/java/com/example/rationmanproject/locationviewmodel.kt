package com.example.rationmanproject

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class locationviewmodel @Inject constructor(private val repository: Repository, private val authinstance : FirebaseAuth) : ViewModel() {

    private val _nearestshops = MutableStateFlow<List<Pair<apiresponse, Float>>>(emptyList())
    val nearestshops : StateFlow<List<Pair<apiresponse, Float>>> = _nearestshops

    private var _allshops = MutableStateFlow<List<Pair<apiresponse, Float>>>(emptyList())
    val allshops : StateFlow<List<Pair<apiresponse, Float>>> = _allshops

    private val _address = MutableStateFlow<String?>(null)
    val address : StateFlow<String?> = _address

    val useremail = authinstance.currentUser?.email?:"Anonymous"


    fun lessdistanceshops(userloc: userlocation) {

        viewModelScope.launch {
            val result = repository.getallshop()
                .map { shop ->
                    val floatArray = FloatArray(1)
                    val distance = distancebetween(
                        shop.latitude, shop.longitude,
                        userloc.latitude, userloc.longitude,
                        floatArray
                    )
                    shop to distance
                }
                .sortedBy { it.second }
                .take(5)

            _nearestshops.value = result
        }
    }

    fun getallshops(userloc: userlocation) {
        viewModelScope.launch {
            val shops = repository.getallshop()
                .map { shop ->
                    val floatArray = FloatArray(1)
                    val distance = distancebetween(
                        shop.latitude, shop.longitude,
                        userloc.latitude, userloc.longitude,
                        floatArray
                    )
                    shop to distance
                }
                .sortedBy { it.second }


            _allshops.value = shops
        }

    }

    fun readableaddress(userloc: userlocation, context: Context){

        viewModelScope.launch(Dispatchers.IO){
            try {
                val geocoder = Geocoder(context , Locale.getDefault())
                val addresses = geocoder.getFromLocation(userloc.latitude , userloc.longitude,1)
                if(!addresses.isNullOrEmpty())
                {
                    _address.value = addresses[0].getAddressLine(0)
                }
                else {
                    _address.value = "No address found"
                }

            }
            catch (e : Exception)
            {
                _address.value = "Error: ${e.message}"

            }
        }


    }

    fun filtershops(userloc: userlocation, query : String, filterby : String){

        viewModelScope.launch {
            val shops = repository.getallshop()

            val filtershops = shops.filter { shop ->
                query.isEmpty() || shop.name.contains(query , ignoreCase = true)

            }
                .filter { shop->
                    when(filterby){
                        "ALL" -> true
                        "RICE" -> shop.stock.rice
                        "WHEAT" -> shop.stock.wheat
                        "SUGAR" -> shop.stock.sugar
                        "DAL" -> shop.stock.dal
                        else -> true
                    }


                }.map { shop ->
                    val floatArray = FloatArray(1)
                    val distance = distancebetween(
                        shop.latitude, shop.longitude,
                        userloc.latitude, userloc.longitude,
                        floatArray
                    )
                    shop to distance
                }
                .sortedBy { it.second }

            _allshops.value = filtershops


        }

    }

    fun filtershopsreportscreen(userloc: userlocation, query : String){

        viewModelScope.launch {
            val shops = repository.getallshop()

            val filtershops = shops.filter { shop ->
                query.isEmpty() || shop.name.contains(query , ignoreCase = true)

            }
                .map { shop ->
                    val floatArray = FloatArray(1)
                    val distance = distancebetween(
                        shop.latitude, shop.longitude,
                        userloc.latitude, userloc.longitude,
                        floatArray
                    )
                    shop to distance
                }
                .sortedBy { it.second }

            if(query.isBlank()){
                getallshops(userloc)
            }else{
                _allshops.value = filtershops
            }


        }

    }

    private val _uiEvents = MutableSharedFlow<String>()
    val uievents : SharedFlow<String> = _uiEvents

    fun addreport(shopid : String , report : Report){
        viewModelScope.launch {
            try {
                repository.addReport(shopid, report)
                _uiEvents.emit("Report added successfully ✅")
            } catch (e: Exception) {
                _uiEvents.emit("Failed to add report: ${e.message}")
            }
        }
    }

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports

    fun getReports(shopid: String) {
        viewModelScope.launch {
            try {
                repository.getreports(shopid).collect { reportsList ->
                    _reports.value = reportsList
                }
            } catch (e: Exception) {
                _uiEvents.emit("Failed to load reports: ${e.message}")
            }
        }
    }


    private val _subsidyResponse = MutableStateFlow<subsidyapiresponse?>(null)
    val subsidyResponse: StateFlow<subsidyapiresponse?> = _subsidyResponse

    fun checkSubsidy(aadharNumber: String) {
        viewModelScope.launch {
            val result = repository.getSubsidyByAadhaar(aadharNumber)
            _subsidyResponse.value = result
        }
    }

    fun distancebetween(lat1 : Double , long1 : Double , userlat : Double , userlong : Double , floatArray: FloatArray) : Float{

        Location.distanceBetween(lat1,long1,userlat,userlong , floatArray)
        return floatArray[0]


    }
    private var _afterloggingscreen = MutableStateFlow<Screen?>(null)
    var afterloggingscreen : StateFlow<Screen?> = _afterloggingscreen

    fun loggingstatus(){
        viewModelScope.launch {

            val currentuser = authinstance.currentUser
            if(currentuser != null) _afterloggingscreen.value = Screen.homescreen
            else _afterloggingscreen.value = Screen.Signup
        }
    }










}



//class viewmodelfactory(val repository : Repository) : ViewModelProvider.Factory{
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return locationviewmodel(repository) as T
//    }
//
//
//}


//🔹 Why SharedFlow is not recommended for lists/state
//
//No default value
//
//SharedFlow doesn’t hold a current value.
//
//When your UI subscribes, it won’t know what the “latest list of shops” is unless it was just emitted.
//
//That means your UI could start with nothing until the next emission.
//
//Possible data loss
//
//If the UI subscribes late (e.g. after ViewModel already emitted), the data could be missed.
//
//But for state (shops, reports, location), the UI should always be able to render the latest snapshot, even if nothing new happens.
//
//State is not an “event”
//
//Shops list, user location, and reports are continuous state.
//
//They should persist and be replayed whenever a new collector comes (like after screen rotation).
//
//That’s exactly what StateFlow is designed for.