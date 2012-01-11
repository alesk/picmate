package org.picmate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.marakana.R;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.TableRow;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
import android.graphics.BitmapFactory.Options;

import android.os.Bundle;
//import android.os.PowerManager;
//import android.os.PowerManager.WakeLock;
//import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;
import android.view.WindowManager;
//import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity {
	private static final String TAG = "CameraDemo";
	private SimulationView mSimulationView;
	// private SensorManagerSimulator mSensorManager; // commented out and
	// replaced by HW andrid sensor below
	private SensorManager mSensorManager;

	private Context context;
	private int photoNumber = 1;
	private int sensorXNumber = 1;

	private WindowManager mWindowManager;
	private Display mDisplay;
	//private UDPSender udpSender = new UDPSender("178.172.27.207", 9876);
	private UDPSender udpSender = new UDPSender("192.168.0.108", 9876);

	// private Button mStartButton;
	// private Button mCancelButton;
	// private Button mTakePhotoButton;

	private TextView mCoordinates;
	private TextView mAlert;

	Preview preview; // <1>
	Button buttonClick; // <2>

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Get an instance of the SensorManager
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Get an instance of the WindowManager
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();

		// ui controls

		Button buttonStart = (Button) findViewById(R.id.button2);
		buttonStart.setOnClickListener(startListener);

		Button buttonStop = (Button) findViewById(R.id.button1);
		buttonStop.setOnClickListener(stopListener);
		context = this;

		mCoordinates = (TextView) findViewById(R.id.textView3);

		mAlert = (TextView) findViewById(R.id.textView2);

		// instantiate our simulation view and set it as the activity's content
		mSimulationView = new SimulationView(this);
		mSimulationView.init();
		preview = new Preview(this); // <3>
		((FrameLayout) findViewById(R.id.preview)).addView(preview); // <4>

		buttonClick = (Button) findViewById(R.id.button3);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) { // <5>
				preview.camera.takePicture(shutterCallback, rawCallback,
						jpegCallback);
				mSimulationView.sendAndRoll();
			}
		});

		mSimulationView.startSimulation();

		Log.d(TAG, "onCreate'd");
	}

	private OnClickListener startListener = new OnClickListener() {
		public void onClick(View v) {

			Toast.makeText(CameraActivity.this,
					"The Start button was clicked.", Toast.LENGTH_LONG).show();
			mSimulationView.cumDistanceX = 0;
			mSimulationView.cumDistanceY = 0;
			mSimulationView.cumDistanceZ = 0;

		}

	};

	// Create an anonymous implementation of OnClickListener
	private OnClickListener stopListener = new OnClickListener() {
		public void onClick(View v) {

			Toast.makeText(CameraActivity.this, "The Stop button was clicked.",
					Toast.LENGTH_LONG).show();

			// finish();
			CameraActivity.this.mCoordinates.setText("");

		}
	};

	// Called when shutter is opened
	ShutterCallback shutterCallback = new ShutterCallback() { // <6>
		public void onShutter() {
			Log.d(TAG, "onShutter'd");
		}
	};

	// Handles data for raw picture
	PictureCallback rawCallback = new PictureCallback() { // <7>
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	// Handles data for jpeg picture
	PictureCallback jpegCallback = new PictureCallback() { // <8>
		public void onPictureTaken(byte[] data, Camera camera) {

			Toast.makeText(CameraActivity.this, "Voila.", Toast.LENGTH_LONG)
					.show();
			int id = getResources().getIdentifier("imageView" + photoNumber,
					"id", context.getPackageName());
			photoNumber++;
			ImageView image = (ImageView) findViewById(id);
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			TableRow layout = (TableRow) findViewById(R.id.tableRow1);
			image.setImageBitmap(bitmap);

			FileOutputStream outStream = null;
			try {
				// Write to SD Card
				outStream = new FileOutputStream(String.format(
						"/sdcard/%d.jpg", System.currentTimeMillis())); // <9>
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} catch (FileNotFoundException e) { // <10>
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
			camera.startPreview();
		}
	};

	@Override
	protected void onStart() {// activity is started and visible to the user

		super.onStart();

	}

	@Override
	protected void onResume() {// activity was resumed and is visible again

		super.onResume();

	}

	@Override
	protected void onPause() { // device goes to sleep or another activity
								// appears

		super.onPause();

	}

	@Override
	protected void onStop() { // the activity is not visible anymore

		super.onStop();

	}

	@Override
	protected void onDestroy() {// android has killed this activity

		super.onDestroy();
	}

	/*
	 * @Override protected void onResume() { super.onResume(); /* when the
	 * activity is resumed, we acquire a wake-lock so that the screen stays on,
	 * since the user will likely not be fiddling with the screen or buttons.
	 * 
	 * mWakeLock.acquire();
	 * 
	 * // Start the simulation mSimulationView.startSimulation(); }
	 * 
	 * @Override protected void onPause() { super.onPause(); /* When the
	 * activity is paused, we make sure to stop the simulation, release our
	 * sensor resources and wake locks
	 * 
	 * 
	 * // Stop the simulation mSimulationView.stopSimulation();
	 * 
	 * // and release our wake-lock mWakeLock.release(); }
	 */

	public class Point {
		public double x;
		public double y;
		public double z;

		Point(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	class SimulationView extends View implements SensorEventListener {

		private Sensor mAccelerometer;
		/*
		 * private long mLastT; private float mLastDeltaT;
		 * 
		 * private float mXDpi; private float mZDpi; private float
		 * mMetersToPixelsX; private float mMetersToPixelsZ; private Bitmap
		 * mBitmap;
		 * 
		 * private float mXOrigin; private float mZOrigin;
		 */

		private long mSensorTimeStamp;
		private long mCpuTimeStamp;
		private long lastTime = 0;
		public double cumDistanceX = 0;
		public double cumDistanceY = 0;
		public double cumDistanceZ = 0;
		private double X;
		private double Y;
		private double Z;
		public double cumRadius;
		public int cumIndex;
		public Vector<Point> cumVector;
		float x = 1f;

		float gravity[] = new float[3];
		/*
		 * private float mHorizontalBound; private float mVerticalBound;
		 */

		private float mWishPosY;
		private float mWishPosZ;
		private float mWishPosX;

		// private float mStepsPosY = 0;
		// private float mOneMinusFriction;

		public void init() {

			cumDistanceZ = 0d;
			cumDistanceX = 0d;
			cumDistanceY = 0d;
			cumIndex = 0;
			cumVector = new Vector<Point>();
			cumRadius = 0d;
		}

		public void setCurrX(int newValue) {
			cumDistanceX = newValue;
		}

		public float radiusFromPoints(double CumDistanceX1,
				double CumDistanceY1, double CumDistanceX2,
				double CumDistanceY2, double CumDistanceX3, double CumDistanceY3) {

			float r = -1;
			float eps = 0.001f; // Tolerance to singular points

			// Compute center
			double c_ax = (CumDistanceX1 + CumDistanceX2) / 2;
			double c_ay = (CumDistanceY1 + CumDistanceY2) / 2;
			double c_bx = (CumDistanceX2 + CumDistanceX3) / 2;
			double c_by = (CumDistanceY2 + CumDistanceY3) / 2;
			double c_x = c_bx - c_ax;
			double c_y = c_by - c_ay;

			// Compute directions
			double nr_a = Math.sqrt((Math.pow((CumDistanceX2 - CumDistanceX1),
					2) + Math.pow((CumDistanceY2 - CumDistanceY1), 2)));
			double nr_b = Math.sqrt((Math.pow((CumDistanceX3 - CumDistanceX2),
					2) + Math.pow((CumDistanceY3 - CumDistanceY2), 2)));

			double s_ax = (CumDistanceY1 - CumDistanceY2) / nr_a;
			double s_ay = (CumDistanceX2 - CumDistanceX1) / nr_a;
			double s_bx = (CumDistanceY2 - CumDistanceY3) / nr_b;
			double s_by = (CumDistanceX3 - CumDistanceX2) / nr_b;

			// Compute intersection and radius
			double u, v;
			double detSys = s_ax * s_by - s_ay * s_bx; // System determinant
			if (Math.abs(detSys) > eps) {
				// Compute line shifts
				u = (c_x * s_by - c_y * s_bx) / detSys;
				v = (c_x * s_ay - c_y * s_ax) / detSys;

				// Compute intersection point and radius
				if ((u > 0 && v > 0) || (u < 0 && v < 0)) {
					double cp_x = c_ax + u * s_ax;
					double cp_y = c_ay + u * s_ay;
					double R = Math.sqrt(Math.pow((cp_x - CumDistanceX2), 2)
							+ Math.pow((cp_y - CumDistanceY2), 2));
					float f = (float) R;
				}
			}

			// Return radius. If it does not exist, it returns -1;
			return r;
		}

		public void setCurrZ(int newValue) {
			cumDistanceZ = newValue;
		}

		public SimulationView(Context context) {
			super(context);
			mAccelerometer = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

			Options opts = new Options();
			opts.inDither = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			CameraActivity.this.mAlert.setText("");
			CameraActivity.this.mCoordinates.setText("");

		}

		public void computePhysics(float SensorX, float SensorY, float SensorZ,
				long timestamp) {

			String msg = String.format("%f, %f, %f, %d", SensorX, SensorY,
					SensorZ, timestamp);
			udpSender.send(msg);

			float timeDiffNanoSeconds;
			if (lastTime == 0)
				timeDiffNanoSeconds = 0;
			else
				timeDiffNanoSeconds = timestamp - lastTime;

			/*
			 * Gravity compensation section
			 */
			final float alpha = 0.8f;
			float linear_acceleration[] = new float[3];
			gravity[0] = alpha * gravity[0] + (1 - alpha) * SensorX;
			gravity[1] = alpha * gravity[1] + (1 - alpha) * SensorY;
			gravity[2] = alpha * gravity[2] + (1 - alpha) * SensorZ;
			linear_acceleration[0] = SensorX - gravity[0];
			linear_acceleration[1] = SensorY - gravity[1];
			linear_acceleration[2] = SensorZ - gravity[2];

			final float velocityX = linear_acceleration[0]
					* timeDiffNanoSeconds / 1000000000;
			final float velocityY = linear_acceleration[1]
					* timeDiffNanoSeconds / 1000000000;
			final float velocityZ = linear_acceleration[2]
					* timeDiffNanoSeconds / 1000000000;

			final double distanceX = velocityX
					* timeDiffNanoSeconds
					/ 1000000000
					+ (linear_acceleration[0]
							* Math.pow(Double
									.valueOf(timeDiffNanoSeconds / 1000000000),
									Double.valueOf(2)) / 2);
			final double distanceY = velocityY
					* timeDiffNanoSeconds
					/ 1000000000
					+ (linear_acceleration[1]
							* Math.pow(Double
									.valueOf(timeDiffNanoSeconds / 1000000000),
									Double.valueOf(2)) / 2);
			final double distanceZ = velocityZ
					* timeDiffNanoSeconds
					/ 1000000000
					+ (linear_acceleration[2]
							* Math.pow(Double
									.valueOf(timeDiffNanoSeconds / 1000000000),
									Double.valueOf(2)) / 2);

			cumDistanceX = cumDistanceX + distanceX;
			cumDistanceY = cumDistanceY + distanceY;
			cumDistanceZ = cumDistanceZ + distanceZ;

			X = cumDistanceX;
			Y = cumDistanceY;
			Z = cumDistanceZ;

			StringBuilder strStatus = new StringBuilder("");

			CameraActivity.this.mCoordinates.setText(strStatus);

			// strStatus.append("\nPosX = " + linear_acceleration[0]);
			// strStatus.append("\nPosY = " + linear_acceleration[1]);
			// strStatus.append("\nPosZ = " + linear_acceleration[2]);
			// strStatus.append("\nTimediff = " + timeDiffNanoSeconds);
			strStatus.append("\nCumDistX = " + cumDistanceX);
			strStatus.append("\nCumDistY = " + cumDistanceY);

			strStatus.append("\nCumDistZ = " + cumDistanceZ);

			/*
			 * CameraActivity.this.mAlert.setText(""); if (mWishPosX -
			 * cumDistanceX > 0.05f ) { //Log.d("X pos",
			 * "Move a little to the right");
			 * CameraActivity.this.mAlert.setText(
			 * "Move a little to the right");}
			 * 
			 * CameraActivity.this.mAlert.setText(""); if (mWishPosX -
			 * cumDistanceX > -0.05f ) { //Log.d("X pos",
			 * "Move a little to the left");
			 * CameraActivity.this.mAlert.setText("Move a little to the left");}
			 * 
			 * CameraActivity.this.mAlert.setText(""); if (mWishPosZ -
			 * cumDistanceZ > 0.02f ) { //Log.d("Z pos",
			 * "Move a little further");
			 * CameraActivity.this.mAlert.setText("Move a little further");}
			 * 
			 * CameraActivity.this.mAlert.setText(""); if (mWishPosZ -
			 * cumDistanceZ > -0.02f ) { //Log.d("Z pos",
			 * "Move a little closer");
			 * CameraActivity.this.mAlert.setText("Move a little closer");} else
			 * { CameraActivity.this.mAlert.setText("OK"); }
			 */

			CameraActivity.this.mCoordinates.setText(strStatus);
			lastTime = timestamp;
		}

		public void sendAndRoll() {
			String msg = String.format("NEW_PICTURE");
			udpSender.send(msg);

			return;
			
			/*Point p = new Point(cumDistanceX, cumDistanceY, cumDistanceZ);
			cumVector.add(p);
			cumIndex += 1;
			if (cumIndex > 2) {
				Point p0 = cumVector.elementAt(cumIndex - 3);
				Point p1 = cumVector.elementAt(cumIndex - 2);
				Point p2 = cumVector.elementAt(cumIndex - 1);
				cumRadius = radiusFromPoints(p0.x, p0.z, p1.x, p1.z, p2.x, p2.z);
			}
			
			 String msg = String.format("%d, %f, %f, %f, %f", cumIndex-1,
			 cumDistanceX, cumDistanceY, cumDistanceZ, cumRadius);
			 udpSender.send(msg);
			 */
		}

		public void startSimulation() {
			/*
			 * It is not necessary to get accelerometer events at a very high
			 * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
			 * automatic low-pass filter, which "extracts" the gravity component
			 * of the acceleration. As an added benefit, we use less power and
			 * CPU resources.
			 */

			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_FASTEST);
		}

		public void stopSimulation() {
			mSensorManager.unregisterListener(this);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			// compute the origin of the screen relative to the origin of
			// the bitmap
			/*
			 * mXOrigin = (w - mBitmap.getWidth()) * 0.5f; mZOrigin = (h -
			 * mBitmap.getHeight()) * 0.5f; mHorizontalBound = ((w /
			 * mMetersToPixelsX) * 0.5f); mVerticalBound = ((h /
			 * mMetersToPixelsZ) * 0.5f);
			 */
		}

		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
				return;
			this.computePhysics(event.values[0], event.values[1],
					event.values[2], event.timestamp);
		}

		// @Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

	}

}
/*
 * class Point { int x, y; Point() { System.out.println("default"); } Point(int
 * x, int y) { SimulationView.CurrX = x; SimulationView.CurrY = y; } // A Point
 * instance is explicitly created at class initialization time: static Point
 * origin = new Point(0,0); // A String can be implicitly created by a +
 * operator: public String toString() { return "(" + x + "," + y + ")"; }}
 */