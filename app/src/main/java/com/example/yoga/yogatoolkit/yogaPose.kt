package com.example.yoga.yogatoolkit


import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.tasks.components.containers.*
import org.json.JSONObject


class yogaPose(private val filename: String,private val activity: AppCompatActivity) {
    private var _roi: MutableMap<String,Boolean> = mutableMapOf()
    private var _tips :String = ""
    private var _ang : Map<String,List<*>> = mapOf()
    private var _ang_dict : MutableMap<String,Any> = mutableMapOf()
    private var point3d: List<Landmark?>? = null
    private var sample_angle_dict:JSONObject? = null
    //private var _ang
    val angle_dict: Map<String,Any> get() =  _ang_dict
    val cur_roi : Map<String,Boolean> get() = _roi
    val cur_tips :String get() = _tips
    val cur_ang : Map<String,List<*>> get() = _ang
    val cur_sample_angle_dict : JSONObject? get() = sample_angle_dict
    fun init(){
        if(filename == "Tree Style")
        {
            _roi["LEFT_KNEE"]= false
            _roi["LEFT_HIP"]= false
            _roi["RIGHT_FOOT_INDEX"]= false
            _roi["RIGHT_KNEE"]= false
            _roi["RIGHT_HIP"]= false
            _roi["LEFT_SHOULDER"]= false
            _roi["RIGHT_SHOULDER"]= false
            _roi["LEFT_ELBOW"]= false
            _roi["RIGHT_ELBOW"]= false
            _roi["LEFT_INDEX"]= false
            _roi["RIGHT_INDEX"]= false
            setang(AngleNodeDef.TREE_ANGLE)
            setsample_angle_dict(readJsonFile(activity,"TreePose/sample.json"))
        }
        else if(filename == "Warrior2 Style") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "RIGHT_ANKLE" to false,
                "RIGHT_KNEE" to false,
                "LEFT_KNEE" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "NOSE" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false
            )
            setRoi(initroi)
            setang(AngleNodeDef.WARRIOR_II_ANGLE)
        }
        else if(filename == "Plank") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "NOSE" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "LEFT_KNEE" to false,
                "RIGHT_KNEE" to false,
                "LEFT_ANKLE" to false,
                "RIGHT_ANKLE" to false,
            )
            setRoi(initroi)
            setang(AngleNodeDef.PLANK_ANGLE)
        }
        else if(filename == "Reverse Plank") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "NOSE" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false,
                "LEFT_INDEX" to false,
                "RIGHT_INDEX" to false,
                "LEFT_WRIST" to false,
                "RIGHT_WRIST" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "LEFT_KNEE" to false,
                "RIGHT_KNEE" to false
            )
            setRoi(initroi)
            setang(AngleNodeDef.REVERSE_PLANK_ANGLE)
        }
        else if(filename == "Child's pose") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "NOSE" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false,
                "LEFT_WRIST" to false,
                "RIGHT_WRIST" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "RIGHT_KNEE" to false,
                "LEFT_KNEE" to false,
                "LEFT_ANKLE" to false,
                "RIGHT_ANKLE" to false,
            )
            setRoi(initroi)
            setang(AngleNodeDef.CHILDS_ANGLE)
        }
        else if(filename == "Seated Forward Bend") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "NOSE" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false,
                "LEFT_WRIST" to false,
                "RIGHT_WRIST" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "RIGHT_KNEE" to false,
                "LEFT_KNEE" to false,
                "RIGHT_ANKLE" to false,
                "LEFT_ANKLE" to false,
                "RIGHT_FOOT_INDEX" to false,
                "LEFT_FOOT_INDEX" to false,
            )
            setRoi(initroi)
            setang(AngleNodeDef.SEATEDFORWARDBEND_ANGLE)
        }
        else if(filename == "Low Lunge") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "NOSE" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false,
                "LEFT_WRIST" to false,
                "RIGHT_WRIST" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "RIGHT_KNEE" to false,
                "LEFT_KNEE" to false,
                "RIGHT_ANKLE" to false,
                "LEFT_ANKLE" to false,
            )
            setRoi(initroi)
            setang(AngleNodeDef.LOWLUNGE_ANGLE)
        }
        else if(filename == "Downward dog") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "NOSE" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false,
                "LEFT_WRIST" to false,
                "RIGHT_WRIST" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "RIGHT_KNEE" to false,
                "LEFT_KNEE" to false,
                "RIGHT_ANKLE" to false,
                "LEFT_ANKLE" to false,
                "LEFT_HEEL" to false,
                "RIGHT_HEEL" to false,
            )
            setRoi(initroi)
            setang(AngleNodeDef.DOWNWARDDOG_ANGLE)
        }
        else if(filename == "Pyramid pose") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "NOSE" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false,
                "LEFT_WRIST" to false,
                "RIGHT_WRIST" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "RIGHT_KNEE" to false,
                "LEFT_KNEE" to false,
                "RIGHT_ANKLE" to false,
                "LEFT_ANKLE" to false,
                "RIGHT_FOOT_INDEX" to false,
                "LEFT_FOOT_INDEX" to false,
                "LEG" to false,
            )
            setRoi(initroi)
            setang(AngleNodeDef.PYRAMID_ANGLE)
        }
        else if(filename == "Bridge pose") {
            val initroi :MutableMap<String,Boolean> = mutableMapOf(
                "NOSE" to false,
                "LEFT_SHOULDER" to false,
                "RIGHT_SHOULDER" to false,
                "LEFT_ELBOW" to false,
                "RIGHT_ELBOW" to false,
                "LEFT_WRIST" to false,
                "RIGHT_WRIST" to false,
                "LEFT_HIP" to false,
                "RIGHT_HIP" to false,
                "RIGHT_KNEE" to false,
                "LEFT_KNEE" to false,
                "RIGHT_ANKLE" to false,
                "LEFT_ANKLE" to false,
                "RIGHT_FOOT_INDEX" to false,
                "LEFT_FOOT_INDEX" to false,
            )
            setRoi(initroi)
            setang(AngleNodeDef.BRIDGE_ANGLE)
        }
        else{
            //error
        }
        initialAngleDict()
    }

    fun setRoi(roi:MutableMap<String,Boolean>) {
        _roi = roi
    }
    fun setTip(tip:String) {
        _tips = tip
    }
    fun setang(ang:Map<String,List<*>>){
        _ang = ang
    }
    fun setsample_angle_dict(sample_angle_dict:JSONObject?){
        this.sample_angle_dict = sample_angle_dict
    }

    fun initialAngleDict(){
        var index = 0
        var ang: MutableMap<String, Any> = mutableMapOf()
        for ((key,_) in cur_ang){
            ang[key] = 0
            index++
        }
        _ang_dict = ang
    }
    fun getMediapipeResult(poseLandmarkerResults: MutableList<Landmark>):String{
        point3d = poseLandmarkerResults
        for ((key,value) in cur_ang){
            var angle = computeAngle(getLandmarks(point3d?.get(value[0] as Int)),
                    getLandmarks(point3d?.get(value[1] as Int)),
                    getLandmarks(point3d?.get(value[2] as Int)))
            _ang_dict[key] = angle
        }
        if (filename == "Tree Style")
            _tips = treePoseRule(_roi, _tips, sample_angle_dict, _ang_dict, point3d!! /*, mat*/)
        else if(filename == "Warrior2 Style"){

        }
        return cur_tips
    }

}