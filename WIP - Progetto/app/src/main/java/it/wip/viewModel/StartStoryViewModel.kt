package it.wip.viewModel

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.widget.Switch
import androidx.lifecycle.*
import it.wip.database.WIPDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StartStoryViewModel(application: Application) : AndroidViewModel(application) {

    private val _storyName = MutableLiveData("")
    val storyName : LiveData<String>
        get() = _storyName

    private val _studyTime = MutableLiveData(10F)
    val studyTime : LiveData<Float>
        get() = _studyTime

    private val _maxStudyTime = MutableLiveData(60F)
    val maxStudyTime : LiveData<Float>
        get() = _maxStudyTime

    private val _maxStudyTimeGraphic = MutableLiveData(50F)
    val maxStudyTimeGraphic : LiveData<Float>
        get() = _maxStudyTimeGraphic

    private val _breakTime = MutableLiveData(50F)
    val breakTime : LiveData<Float>
        get() = _breakTime

    private val _switchState = MutableLiveData(true)
    private val _hSwitchState = MutableLiveData(true)

    //val avatarsName = mutableListOf<String>()
    private val allShoppedElements = mutableListOf<String>()
    private val shopElements = mutableListOf<String>()


    val avatarShoppedElements = mutableListOf<String>()

    init {

        val userIdPreference = application.applicationContext.getSharedPreferences("userId", Context.MODE_PRIVATE)
        val userId = userIdPreference.getInt("userId", Context.MODE_PRIVATE)
        val wipDb = WIPDatabase.getInstance(application.applicationContext)

        viewModelScope.launch {
            runBlocking {
                val currentShoppedElements = wipDb.shoppedDao().getAllByUser(userId)
                val allElements = wipDb.shopElementDao().getAll()

                for(i in currentShoppedElements){
                    //avatarsName.add(avatar.shopElement)
                    allShoppedElements.add(i.shopElement)
                }
                for (i in allElements){
                    if(i.type == "avatar"){
                        shopElements.add(i.elementName)
                    }
                }

                for(i in allShoppedElements){
                    for(j in shopElements){
                        if(i==j){
                            avatarShoppedElements.add(i)
                        }
                    }
                }

            }

            val user = wipDb.userDao().getUserById(userId)
            _studyTime.value = user.studyTime
            _maxStudyTime.value = user.maxStudyTime
            _maxStudyTimeGraphic.value = _maxStudyTime.value!! - 10F
            _breakTime.value = _maxStudyTime.value!! - 10F

        }
    }

    fun setStoryName(storyName: String) {
        this._storyName.value = storyName
    }

    fun setStudyBreakTime(studyTime: Float) {
        this._studyTime.value = studyTime
        this._breakTime.value = this._maxStudyTime.value?.minus(this._studyTime.value!!)
    }




    //              SWITCH SILENT MODE - NORMAL MODE
    fun silenceNormal(context: Context){
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if(_switchState.value!!){
            audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            _switchState.value = false
        }else{
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            _switchState.value = true
        }
    }




    //              SWITCH SILENT MODE - HARDCORE MODE
    fun hardcoreMode(switchSilentMode: Switch, context: Context){

        if (_switchState.value!! && _hSwitchState.value!!) {
            switchSilentMode.isChecked = true
            silenceNormal(context)
            _switchState.value = false
            _hSwitchState.value = false
            switchSilentMode.isClickable = false
        } else if (!_switchState.value!! && !_hSwitchState.value!!) {
            switchSilentMode.isChecked = false
            silenceNormal(context)
            _switchState.value = true
            _hSwitchState.value = true
            switchSilentMode.isClickable = true
        } else if (!_switchState.value!! && _hSwitchState.value!!) {
            _switchState.value = false
            _hSwitchState.value = false
            switchSilentMode.isClickable = false
        }
    }


    //              RETURN SELECTED MODE
    fun selectedMode(): Int {
        return if (_hSwitchState.value!!) { 0 } else { 1 }
    }
}