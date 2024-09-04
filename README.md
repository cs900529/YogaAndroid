
# YogaAndroid
## 安裝說明
* 需自行導入.gitignore、app\build.gradle檔案
* 修改build.gradle如下
    - 修改buildPython 和pip 
    ```gradle = 
    python {

        buildPython "你的python可執行檔路徑"
        version "3.8"
        pip {
            // A requirement specifier, with or without a version number:
            install "numpy"
            install "opencv-python"
            install "Pillow"
        }
    }
    ```
    - 修改SDK版本
    ```gradle = 
    defaultConfig {
        applicationId "com.example.yoga"
        minSdk 26
        targetSdk 33
        versionCode 1
        versionName "1.0"
    ```
    - 修改mediaPipe版本 = 0.10.9
    ```gradle =
    // MediaPipe Library
    implementation 'com.google.mediapipe:tasks-vision:0.10.9'
    ```
    - 加上dependency
    ```gradle =
    //delay
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0'
    ```
## UI部分
\app\src\main\java\com\example\yoga\ *.xml為靜態佈局文件

\app\src\main\res\layout\  * .kt、* .java編寫核心程式邏輯、動態佈局

每個view分別有一個靜態佈局和一個動態佈局文件

以下為目前有的頁面

### 首頁

activity_main.xml、View\MainActivity.kt

![image](https://hackmd.io/_uploads/Sk7ybkOBA.png)

### 藍芽連線頁面

activity_main_bluetooth.xml、discover\BluetoothActivity.java

![image](https://hackmd.io/_uploads/B1_L-kurA.png)

### 校正頁面

activity_calibration_stage.xml、View\CalibrationStage.kt

![image](https://hackmd.io/_uploads/Bka_ZJ_H0.png)

### 選擇菜單頁面
activity_choose_menu.xml、View\ChooseMenu.kt
![Screenshot_20240703-210759606](https://hackmd.io/_uploads/BynhMmFOA.jpg)

### 全部動作頁面
activity_all_pose_menu.xml、View\AllPoseMenu.kt

![image](https://hackmd.io/_uploads/rJMcZkOSA.png)

### 訓練菜單頁面

activity_training_menu.xml、View\TrainingMenu.kt
![Screenshot_20240703-210805696](https://hackmd.io/_uploads/By2hM7tOC.jpg)


### 影片示範頁面

activity_video_guide.xml、View\VideoGuide.kt

![image](https://hackmd.io/_uploads/rkOjZ1OHA.png)

### 瑜珈主程式

activity_yoga_main.xml、View\YogaMain.kt

![image](https://hackmd.io/_uploads/BypLQ1OB0.png)

### 計分頁面

activity_yoga_result.xml、View\YogaResult.kt

![image](https://hackmd.io/_uploads/HkmtXJ_HC.png)

### 各頁面尚未解決的問題
#### 1. 瑜珈菜單部分 尚未完成
把訓練菜單(TrainingMenu.kt)、中場休息(RestInterval.kt)、結算畫面(YogaResult.kt)串起來，現在只有完成單獨頁面
預計流程: 動作1=>中場休息=>動作2=>中場休息=>....=>動作N=>結算畫面
#### 2. 音樂播放bug
### 未來展望
1. 美工
    
## 硬體部份
https://hackmd.io/@erictang/B1fItiqI0

https://github.com/cs900529/YogaMat-with-RaspberryPi


## Mediapipe部分
## 其他
### 實用工具
[Scrcpy](https://github.com/Genymobile/scrcpy/releases)
可以將手機畫面投到電腦上，並可由滑鼠鍵盤操作
![image](https://hackmd.io/_uploads/S19CR9A8C.png)
### MVVM架構
https://medium.com/ken-do-everything/mvvm-%E6%9E%B6%E6%A7%8B%E7%AF%87-%E6%9B%B8%E8%AE%80%E5%BE%97%E5%A4%9A-%E4%BA%BA%E8%87%AA%E7%84%B6%E5%B0%B1%E5%A5%BD%E7%9C%8B%E8%B5%B7%E4%BE%86-4fd595581e7f