package com.example.photochangerecord

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseIntArray
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.ViewModelProvider
import com.example.photochangerecord.databinding.ActivityCameraBinding
import com.example.photochangerecord.viewmodel.CameraBackGroundViewModel
import kotlinx.android.synthetic.main.activity_camera.*
import splitties.toast.toast
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


// Camera2 Document: https://developer.android.com/reference/android/hardware/camera2/package-summary
// Reference: https://webnautes.tistory.com/822
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding // Build->Rebuild Project 할때 생성된다고 함!(어쩐지)
    private lateinit var viewModel: CameraBackGroundViewModel

    private lateinit var mContext: Context

    private lateinit var mSurfaceViewHolder: SurfaceHolder
    private lateinit var mImageReader: ImageReader
    private lateinit var mCameraDevice: CameraDevice
    private lateinit var mPreviewBuilder: CaptureRequest.Builder
    private lateinit var mSession: CameraCaptureSession

    private var mHandler: Handler? = null

    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private lateinit var mSensorManager: SensorManager

    private val deviceOrientation: DeviceOrientation by lazy { DeviceOrientation() }
    private var mHeight: Int = 0
    private var mWidth: Int = 0

    private var realHeight: Int = 0
    private var realWidth: Int = 0

    var mCameraId = CAMERA_BACK

    companion object {
        private const val TAG = "CameraActivity"

        const val CAMERA_BACK = "0"
        const val CAMERA_FRONT = "1"

        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(ExifInterface.ORIENTATION_NORMAL, 0)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_90, 90)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_180, 180)
            ORIENTATIONS.append(ExifInterface.ORIENTATION_ROTATE_270, 270)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        val intent = intent
        var folderName: String? = intent.getStringExtra("folderName")

        Log.d(TAG, "onCreate: $folderName")

        // 테스트
        if (folderName != null) {
            toast(folderName)
        }

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.hide()

        // 상태바 숨기기
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // 화면 켜짐 유지
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )


        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera)

        // 뷰모델 인스턴스를 가져온다.
        viewModel = ViewModelProvider(this).get(CameraBackGroundViewModel::class.java)

        // 원래 this로 액티비티를 연결했지만 뷰모델을 여기서 연결한다!
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initSensor()
        initView()


        // 의문: binding.alphaBackgroundImageSlider vs alphaBackgroundImageSlider 무슨 차이가 있는거지?
        // 해결: findViewById로 별도의 선언 없이 사용 가능하다.
        // 뷰 바인딩의 장점 - 널 안정성 && 타입 안정성

        // 새로운 의문: 코틀린에서는 원래 안해도 됐잖아?
        // 해결: 맞다. 근데 코틀린 안드로이드 익스텐션이 2021년에 종료된다고 한다.
        binding.alphaBackgroundImageSlider.positionListener = {
            Log.d(TAG, "onCreate: $it")
            viewModel.updateValue(it)
        }

        binding.btnHome.setOnClickListener {
            this.onBackPressed()
        }

        binding.cameraBtn.setOnClickListener {
            // 사진 확인 화면 -> 해당 폴더에 저장하기
            getBitMapFromSurfaceView(binding.surfaceView) { bitmap ->
                persistImage(
                    bitmap!!,
                    folderName!!
                )
            }

        }
    }

    fun viewToBitmap(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        return if (view is SurfaceView) {
            view.setZOrderOnTop(true)
            view.draw(canvas)
            view.setZOrderOnTop(false)
            bitmap
        } else {
            //For ViewGroup & View
            view.draw(canvas)
            bitmap
        }
    }


    /**
     * Pixel copy to copy SurfaceView/VideoView into BitMap
     * Work with Surface View, Video View
     * Won't work on Normal View
     */
    private fun getBitMapFromSurfaceView(videoView: SurfaceView, callback: (Bitmap?) -> Unit) {
        val bitmap: Bitmap = Bitmap.createBitmap(
            videoView.width,
            videoView.height,
            Bitmap.Config.ARGB_8888
        );
        try {
            // Create a handler thread to offload the processing of the image.
            val handlerThread = HandlerThread("PixelCopier");
            handlerThread.start();
            PixelCopy.request(
                videoView, bitmap,
                PixelCopy.OnPixelCopyFinishedListener { copyResult ->
                    Log.d(TAG, "getBitMapFromSurfaceView: $copyResult")
                    if (copyResult == PixelCopy.SUCCESS) {
                        callback(bitmap)
                    }
                    handlerThread.quitSafely();
                },
                Handler(handlerThread.looper)
            )
        } catch (e: IllegalArgumentException) {
            callback(null)
            // PixelCopy may throw IllegalArgumentException, make sure to handle it
            e.printStackTrace()
        }
    }


    private fun persistImage(bitmap: Bitmap, folderName: String) {
        try {
//            val dir = File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/CertificateMarker")
//            val dir = File(
//                Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DCIM
//                ).toString() + "/PhotoChangeRecord"
//            )

            val dir = File(
                getExternalFilesDir(
                    Environment.DIRECTORY_DCIM
                ).toString() + "/$folderName"
            )

            if (!dir!!.exists()) {
                dir.mkdirs()
            }
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
            var time = dateFormat.format(Date())
            val newFile = File(dir, "${time}.jpg")

//            val out = openFileOutput(newFile.toString(), MODE_APPEND)
            val out = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()

            Log.d(TAG, "persistImage: Success")
            Log.d(TAG, "persistImage: ${newFile.path}")

        } catch (e: Exception) {
            Log.d(TAG, "persistImage: Fail")
            Log.d(TAG, "persistImage: $e")
        }
    }

    private fun initSensor() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    private fun initView() {
        with(DisplayMetrics()) {
            windowManager.defaultDisplay.getMetrics(this)
            mHeight = heightPixels
            mWidth = widthPixels
        }

        mSurfaceViewHolder = surfaceView.holder
        mSurfaceViewHolder.addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(holder: SurfaceHolder) {
                initCameraAndPreview()

//                alpha_camera_image.invalidate()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                mCameraDevice.close()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder, format: Int,
                width: Int, height: Int
            ) {

            }
        })

        btn_convert.setOnClickListener { switchCamera() }
    }

    private fun switchCamera() {
        when (mCameraId) {
            CAMERA_BACK -> {
                mCameraId = CAMERA_FRONT
                mCameraDevice.close()
                openCamera()
            }
            else -> {
                mCameraId = CAMERA_BACK
                mCameraDevice.close()
                openCamera()
            }
        }
    }


    fun initCameraAndPreview() {
        val handlerThread = HandlerThread("CAMERA2")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)

        openCamera()
    }

    private fun openCamera() {
        try {
            val mCameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val characteristics = mCameraManager.getCameraCharacteristics(mCameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            val largestPreviewSize = map!!.getOutputSizes(ImageFormat.JPEG)[0]
            setAspectRatioTextureView(largestPreviewSize.height, largestPreviewSize.width)

            mImageReader = ImageReader.newInstance(
                largestPreviewSize.width,
                largestPreviewSize.height,
                ImageFormat.JPEG,
                7
            )
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) return

            mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler)
        } catch (e: CameraAccessException) {
            toast("카메라를 열지 못했습니다.")
        }
    }

    private val deviceStateCallback = object : CameraDevice.StateCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            try {
                takePreview()
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onDisconnected(camera: CameraDevice) {
            mCameraDevice.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            toast("카메라를 열지 못했습니다.")
        }
    }

    @Throws(CameraAccessException::class)
    fun takePreview() {
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        mPreviewBuilder.addTarget(mSurfaceViewHolder.surface)
        mCameraDevice.createCaptureSession(
            listOf(mSurfaceViewHolder.surface, mImageReader.surface),
            mSessionPreviewStateCallback,
            mHandler
        )
    }

    private val mSessionPreviewStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            mSession = session
            try {
                // Key-Value 구조로 설정
                // 오토포커싱이 계속 동작
                mPreviewBuilder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                )
                //필요할 경우 플래시가 자동으로 켜짐
                mPreviewBuilder.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                )
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }

        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            toast("카메라 구성 실패")
        }
    }

    override fun onResume() {
        super.onResume()

        mSensorManager.registerListener(
            deviceOrientation.eventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI
        )
        mSensorManager.registerListener(
            deviceOrientation.eventListener, mMagnetometer, SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(deviceOrientation.eventListener)
    }

    private fun setAspectRatioTextureView(ResolutionWidth: Int, ResolutionHeight: Int) {
        if (ResolutionWidth > ResolutionHeight) {
            val newWidth = mWidth
            val newHeight = mWidth * ResolutionWidth / ResolutionHeight

            realWidth = newWidth
            realHeight = newHeight

            updateTextureViewSize(newWidth, newHeight)

        } else {
            val newWidth = mWidth
            val newHeight = mWidth * ResolutionHeight / ResolutionWidth

            realWidth = newWidth
            realHeight = newHeight

            updateTextureViewSize(newWidth, newHeight)
        }

        val layoutParams: ViewGroup.LayoutParams = alpha_background_image.layoutParams
        layoutParams.width = realWidth
        layoutParams.height = realHeight
        Log.d(TAG, "setAspectRatioTextureView: $realHeight $realWidth\"")
        alpha_background_image.layoutParams = layoutParams

    }

    private fun updateTextureViewSize(viewWidth: Int, viewHeight: Int) {
        Log.d("ViewSize", "TextureView Width : $viewWidth TextureView Height : $viewHeight")
        surfaceView.layoutParams = FrameLayout.LayoutParams(viewWidth, viewHeight)
    }
}