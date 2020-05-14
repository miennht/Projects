package com.example.aiintegration;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DigitClassifier {
    private static final int FLOAT_TYPE_SIZE = 4;
    private static final int PIXEL_SIZE = 1;
    private static final int OUTPUT_CLASSES_COUNT = 10;
    private Context mContext;
    private Interpreter interpreter = null;
    Boolean isInitialized = false;
    /** Executor to run inference task in the background. */
    ExecutorService executorService= Executors.newCachedThreadPool();
    private int inputImageWidth = 0; // will be inferred from TF Lite model.
    private int inputImageHeight = 0; // will be inferred from TF Lite model.
    private int modelInputSize = 0; // will be inferred from TF Lite model.
    public DigitClassifier(Context context){
        mContext = context;
    }
    void initialize() {

        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Perform some computation
                initializeInterpreter();
                return null;
            }
        };
        executorService.submit(callable);
        /*
        InitializeAsync initializeAsync = new InitializeAsync();
        initializeAsync.execute();
        */
    }
    private class InitializeAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                initializeInterpreter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Finish loading model");
            return null;
        }


    }
    private void initializeInterpreter() throws IOException {
        AssetManager assetManager = mContext.getAssets();
        ByteBuffer model = loadModelFile(assetManager, "mnist.tflite");
        System.out.println("model: " + model);
        Interpreter.Options options = new Interpreter.Options();
        options.setUseNNAPI(true);
        interpreter = new Interpreter(model, options);

        // TODO: Read the model input shape from model file.
        int[] inputShape = interpreter.getInputTensor(0).shape();
        System.out.println("inputShape: " + inputShape);
        inputImageWidth = inputShape[1];
        inputImageHeight = inputShape[2];
        modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE;
        // Finish interpreter initialization.
        this.interpreter = interpreter;

        isInitialized = true;
        System.out.println("Initialized TFLite interpreter.");

    }
    //Memory-map the model file in assets
    private ByteBuffer loadModelFile(AssetManager assetManager, String fileName) throws IOException {
        //Open the model using FileInputStream
        System.out.println("assetManager: " + assetManager);
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(fileName);//Remember to add aaptOptions { noCompress "your-file-name}
        System.out.println("assetFileDescriptor: " + assetFileDescriptor);
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        System.out.println("fileInputStream: " + fileInputStream);
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    String classifyAsync(Bitmap bitmap) throws InterruptedException, ExecutionException {
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                // Perform some computation
                String result = classify(bitmap);
                return result;
            }
        };
        Future<String> future = executorService.submit(callable);
        while(!future.isDone()) {
            System.out.println("Task is still not done...");
            Thread.sleep(200);
        }
        System.out.println("Task completed! Retrieving the result");
        String result = future.get();
        System.out.println(result);
        return result;
    }

    void close() {
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Perform some computation
                interpreter.close();
                return null;
            }
        };
        executorService.submit(callable);
    }

    String classify(Bitmap bitmap){
        String resultString = null;
        System.out.println("inputImageWidth: " + inputImageWidth);
        System.out.println("inputImageHeight: " + inputImageHeight);
        Bitmap resizedImage = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight,true);
        System.out.println("resizedImage: " + resizedImage);
        ByteBuffer byteBuffer = convertBitmapToByteBuffer(resizedImage);
        // Define an array to store the model output.
        float[] output = new float[OUTPUT_CLASSES_COUNT];

        // Run inference with the input data.
        interpreter.run(byteBuffer, output);

        // Post-processing: find the digit that has the highest probability
        // and return it a human-readable string.
        float result = output[0];
        //int maxIndex = result.indices.maxBy { result[it] } ?: -1;
        int maxIndex = 10;
        resultString = "Prediction Result: " + maxIndex + "\nConfidence: %2f" + result;
        return resultString;
    }



    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(modelInputSize);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] pixels = new int[inputImageWidth * inputImageHeight];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int pixelValue: pixels) {
            float r = pixelValue >> 16 & 0xFF;
            float g = pixelValue >> 8 & 0xFF;
            float b = pixelValue & 0xFF;
            // Convert RGB to grayscale and normalize pixel value to [0..1].
            float normalizedPixelValue = (r + g + b) / 3.0f / 255.0f;
            byteBuffer.putFloat(normalizedPixelValue);
        }
        return byteBuffer;
    }

}
