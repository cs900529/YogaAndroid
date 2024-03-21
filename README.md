
# YogaAndroid
安裝說明:
1. 需自行導入.gitignore、app\build.gradle檔案
2. 修改build.gradle如下
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